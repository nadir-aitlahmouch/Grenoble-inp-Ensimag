#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <math.h>
#include "../include/jpeg_reader.h"
#include "../include/bitstream.h"
#include "../include/huffman.h"
#include "../include/treatment.h"
#include "../include/pgm_ppm.h"
#include "../include/informations.h"
#include "../include/rgb.h"
#include "../include/loeffler.h"




/*################################DECODER_MAGNITUDE POUR DC ET AC####################################*/

/*Fonction qui retourne la valeur sachant sa magnitude et son codage*/
int16_t decoder_magnitude(uint8_t magnitude, uint32_t indice){
     if (magnitude == 0) return 0;
     int16_t result;
     /*SI le résultat est négatif*/
     if (indice < (1<<(magnitude -1))){
       int value_min = -((1 <<magnitude) -1);
       result = value_min + indice;
       return result;
     }
     /*Si le résultat est positif*/
     result = indice;
     return result;
}



/*#################################DECODERUR DC POUR UN BLOC##########################################*/

/* Decoder la valeur d'un DC à partir du stream et sauvegarde sa valeur */
void decoder_DC(struct jpeg_desc *jdesc, struct bitstream *stream,
                 int16_t *prec_dc, int16_t *decode_DC_AC, enum component comp){

  // on cherhche l'indice de la table_huff
  uint8_t indice_tale_huff = get_scan_component_huffman_index(jdesc, DC, comp);
  // on recupere la valeur de magnitude_DC
  uint8_t magnitude_DC = next_huffman_value(get_huffman_table(jdesc, DC, indice_tale_huff), stream);
  // indice
  uint32_t indice = 0;
  uint8_t nb_bits = read_bitstream(stream, magnitude_DC, &indice, true);
  assert(nb_bits == magnitude_DC && "ERROR in extraction.c: le nombre de bits lus n'est pas égal à la magnitude.");
  int16_t resultat;
  resultat = prec_dc[comp] + decoder_magnitude(magnitude_DC, indice);

  prec_dc[comp] = resultat;
  *decode_DC_AC = resultat;
}


/*################################PARTIE DECODEUR_AC POUR UN BLOC######################################*/


/* Récuperation des bits de poids fort */
uint8_t get_upper_bits(uint8_t byte){
  uint8_t up_b;
  up_b = byte >> 4;
  return up_b;
}




/* Récuperation des bits du  poids faible */
uint8_t get_lower_bits(uint8_t byte){
  uint8_t low_b;
  byte = byte << 4;
  low_b = byte >> 4;
  return low_b;
}




/* Retourne les 63 valeurs AC décodées à partir du stream et les rajoute dans un tableau */
void decoder_AC(struct jpeg_desc *jdesc, struct bitstream *stream,
                  int16_t *decode_DC_AC, enum component comp){

  uint8_t indice_tale_huff = get_scan_component_huffman_index(jdesc, AC, comp);
  /* On intitialise indice_tab à 1 pour laisser la 1ère case à DC. */
  uint8_t indice_tab = 1;
  /* Parcours avec distinction des cas */
  while(indice_tab < 64){
    uint8_t byte = next_huffman_value(get_huffman_table(jdesc, AC, indice_tale_huff), stream);
    uint8_t up = get_upper_bits(byte);
    uint8_t lw = get_lower_bits(byte);
    if (lw==0){
        /*On effectue un assert pour éliminer le cas 0x?0 */
        assert(up == 0xf || up==0 && "ERROR in extraction.c : Code RLE invalide.");
        if (byte == 0){
         for (uint8_t i=indice_tab; i< 64; i++){
           *(decode_DC_AC + i) = 0;
         }indice_tab=64;
       }
      else if (byte == 0xf0){
         for(uint8_t i = indice_tab; i<indice_tab+16; i++){
            *(decode_DC_AC + i) = 0;
         }
      indice_tab += 16;
  }
  }
   else{
     for(uint8_t i= indice_tab; i<up + indice_tab; i++){
      *(decode_DC_AC +i ) = 0;
 }
     /* Dans les cas 0xAB on décode la magnitude et son indice pour en
      ressortir la valeur finale AC */
     indice_tab += up;
     uint32_t indice = 0;
     uint8_t  nb_bits =  read_bitstream(stream, lw, &indice, true);
     assert(nb_bits == lw && "ERROR in extraction.c: Nombre de bits lus invalide.");
     int16_t res = decoder_magnitude(lw, indice);
     *(decode_DC_AC + indice_tab) = res;
     indice_tab ++;
     }
}
}





/*####################################DECODEUR_DC_AC pour un bloc#####################################*/


/* Fonction globale qui mixe les résultats des deux focntions précédentes et
retourne le résultat du bloc dans un tableau. */
int16_t *decoder_DC_AC(struct jpeg_desc *jdesc,struct bitstream *stream, int16_t *prec_dc,enum component comp){
  int16_t *decode_DC_AC = malloc(64*sizeof(int16_t));
  decoder_DC(jdesc, stream, prec_dc, decode_DC_AC,comp);
  decoder_AC(jdesc, stream, decode_DC_AC, comp);
  return decode_DC_AC;
}


/*############################################DECODEUR MC##############################################*/


/* Fonction pour libérer la structure mcu */
uint8_t free_mcu(struct mcu *my_mcu){
  for (uint8_t i = 0; i < my_mcu->taille_comp[0]; i++) {
          free(my_mcu->blocks_Y[i]);
  }
  free(my_mcu->blocks_Y);
  for (uint8_t i = 0; i < my_mcu->taille_comp[1]; i++) {
          free(my_mcu->blocks_Cb[i]);
  }
  free(my_mcu->blocks_Cb);
  for (uint8_t i = 0; i < my_mcu->taille_comp[2]; i++) {
          free(my_mcu->blocks_Cr[i]);
  }
  free(my_mcu->blocks_Cr);
  free(my_mcu->taille_comp);
  return 0;
}



/* Focntion qui retourne un mcu en remplissant ses champs adéquoitement.
(pour voir la structure mcu rdv extraction.h) */
struct mcu decoder_mcu(struct jpeg_desc *jdesc, struct bitstream *stream, int16_t *prec_dc, float *cos_calc, float *sin_calc){
  uint16_t *blocs_cop = blocs_par_components(jdesc);
  /*ordre de traitement des composantes*/
  uint32_t *ordre_component = ordre_components(jdesc);
  /*allocation des elts de mcu(Y Cb Cr)*/
  int16_t **blocks_Y = malloc(blocs_cop[0]*sizeof(int16_t *));
  int16_t **blocks_Cb = malloc(blocs_cop[1]*sizeof(int16_t *));
  int16_t **blocks_Cr = malloc(blocs_cop[2]*sizeof(int16_t *));
  for(int i =0; i< 3; i++){
    switch (ordre_component[i]){
      case 0:
            /*ordre de Y*/
            for (int indice_y = 0; indice_y<blocs_cop[0]; indice_y++){
              int16_t *bloc = decoder_DC_AC(jdesc, stream, prec_dc, 0);
              quantification_inverse(jdesc, bloc, 0);
              zigzag_inverse(bloc);
              loeffler(bloc,cos_calc,sin_calc);
              *(blocks_Y + indice_y) = bloc;
            }
            break;
      case 1:
            /*ordre de Cb*/
            for (int indice_Cb = 0; indice_Cb<blocs_cop[1]; indice_Cb++){
              int16_t *bloc = decoder_DC_AC(jdesc, stream, prec_dc, 1);
              quantification_inverse(jdesc, bloc, 1);
              zigzag_inverse(bloc);
              loeffler(bloc,cos_calc,sin_calc);
              *(blocks_Cb + indice_Cb) = bloc;
            }
            break;
      case 2:
            /*ordre de Cr*/
            for (int indice_Cr = 0; indice_Cr<blocs_cop[2]; indice_Cr++){
              int16_t *bloc = decoder_DC_AC(jdesc, stream, prec_dc, 2);
              quantification_inverse(jdesc, bloc, 1);
              zigzag_inverse(bloc);
              loeffler(bloc,cos_calc,sin_calc);
              *(blocks_Cr + indice_Cr) = bloc;
            }
            break;
      default:
            break;
  }
}
 /*mcu à retourner apres decodage*/
  struct mcu my_mcu = {blocs_cop, blocks_Y, blocks_Cb, blocks_Cr};
  free(ordre_component);
  return my_mcu;
}



/*############################################DECODEUR_IMAGE PGM ##########################################*/



/*Retourne un tableau de pixels apres le decodage,
     on utilise cette fonction pour les images blanc et noir*/

uint32_t *genere_pgm(struct jpeg_desc *jdesc, struct bitstream *stream){

  float *cos_calc = tab_cos();
  float *sin_calc = tab_sin();

  struct mcu mcu_boucle;
  uint16_t *bloc_comp = blocs_par_components(jdesc);
  uint32_t nombre_bloc_mcu=0;
  /*on calcule le nombre de total des blocs par mcu*/
  for (int i=0; i<3; i++) nombre_bloc_mcu += bloc_comp[i];
  /*initialiser les valeurs de DC (pour Y Cb Cr) à 0*/
  int16_t prec_dc[3] = {0,0,0};
  /*nombre de blocs dans toute l'image*/
  uint32_t nombre_blocs_total = nb_bloc_tot(jdesc);
  /*nombre_mcus*/
  uint32_t nombre_mcu = nb_mcu(jdesc);
  /*allocation du tableau des pixels*/
  uint32_t *image = malloc(64*nombre_blocs_total*sizeof(uint32_t));
  /*decodage des mcus*/
  for (uint32_t i = 0; i < nombre_mcu; i++){
    mcu_boucle = decoder_mcu(jdesc, stream, prec_dc, cos_calc, sin_calc);
    /*la mise à jour du tableau image apres le decodage d'une
      nouvelle mcu*/
    for (uint16_t j = 0; j < mcu_boucle.taille_comp[0]; j++){
      for (uint16_t k = 0; k < 64; k++){
        image[i*64*nombre_bloc_mcu +j*64 + k] = mcu_boucle.blocks_Y[j][k];
      }
    }
    /*on libere mcu alloue dans decode_mcu*/
    free_mcu(&mcu_boucle);
  }

    free(bloc_comp);
    /*on ordonne les pixels*/
    uint32_t *ordonne = ordonner_pixel(jdesc,image);
    /*libere le tableau initial des pixels*/
    free(image);
    /*idem pour troncature*/
    uint32_t *tronquee = tronquer(jdesc, ordonne);
    free(ordonne);
    free(cos_calc);
    free(sin_calc);
    return tronquee;
}
