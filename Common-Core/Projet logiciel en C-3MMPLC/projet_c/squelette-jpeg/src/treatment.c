#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <math.h>
#include "../include/jpeg_reader.h"
#include "../include/bitstream.h"
#include "../include/huffman.h"
#include "../include/informations.h"
#include "../include/rgb.h"
#define PI 3.141592653589


/*######################################IQUANT pour un bloc########################################*/



/* Effectue la quantification inverse pour un bloc */
void quantification_inverse(struct jpeg_desc *jdesc, int16_t *tab_frequences,
                             uint8_t i){

  uint8_t quant_index_table = get_frame_component_quant_index(jdesc, i);
  uint8_t *quant_table = get_quantization_table(jdesc, quant_index_table);
    for (int i = 0; i < 64; i++) {
        tab_frequences[i] = tab_frequences[i]*quant_table[i];
    }
}




/*####################################ZZinverse pour un bloc#######################################*/



/* Effectue l'étape du zig zag inverse pour un bloc */
void zigzag_inverse(int16_t *tab_frequences){

    int index = 0;
    /*On initialise un tableau dont les valeurs sont conforme à l'ordre requis
    par cette étape dans l'implémentation du nouveau tableau */
    int tab[] = {0,   1,  8, 16,  9,  2, 3, 10,
                17, 24, 32, 25, 18, 11,  4, 5,
                12, 19, 26, 33, 40, 48, 41, 34,
                27, 20, 13,  6,  7, 14, 21, 28,
                35, 42, 49, 56, 57, 50, 43, 36,
                29, 22, 15, 23, 30, 37, 44, 51,
                58, 59, 52, 45, 38, 31, 39, 46,
                53, 60, 61, 54, 47, 55, 62, 63
                           };
    int16_t temp[64] = {0};
    for (int i = 0; i < 64; i++) {
        temp[i] = tab_frequences[i];
    }
    for (int i=0; i < 64; i++) {
        index = tab[i];
        tab_frequences[index] = temp[i];
    }
}



/*######################################IDCT pour un bloc###########################################*/




/* Traite le coeff dans la somme */
float C_coeff(int i)
{
    if (i == 0) {
        return 1/sqrt(2);
    } else {
        return 1;
    }
}

/* Traite le flottant selon sa valeur et le met dans un octet */
uint8_t saturee(float valeur){
  uint8_t val  = valeur;
  if (valeur < 0){
    return 0;
  } else if (valeur > 255){
    return 255;
  } else {
    return val;
  }
}


/* Effectue l'étape de l'idct (pas optimal) */
void idct(int16_t *tab_frequences){
  int16_t copie[64];
  for (int i = 0; i<64; i++) {
      copie[i] = tab_frequences[i];
  }
    for (int y = 0; y < 8; y++) {
        for (int x = 0; x < 8; x++) {
            float temp = 0;
            for (int j = 0; j < 8; j++) {
                for (int i = 0; i < 8; i++) {
                    temp += C_coeff(j)*C_coeff(i)*
                            (cos((2*x+1)*i*(PI)/16)*cos((2*y+1)*j*(PI)/16)*copie[8*j + i]);
                }
            }
            tab_frequences[8*y + x] = saturee(temp/4 + 128);
        }
    }
}


/*#########################################ordonner_pixel PGM#########################################*/




/* Fonction qui ordonne les pixels selon ce qui est voulu avnt d'appeller generer_pgm. */
uint32_t *ordonner_pixel(struct jpeg_desc *jdesc, uint32_t *tableau_image){
    uint8_t nb_components = get_nb_components(jdesc);
    uint8_t diviser=3;
    if (nb_components == 1) diviser = 1;
    uint32_t nombre_blocs_total = nb_bloc_tot(jdesc)/diviser;
    uint32_t nb_bloc_ligne_h =  get_size_reelle(jdesc, DIR_H)/8;
    uint32_t nb_bloc_ligne_v =  get_size_reelle(jdesc, DIR_V)/8;
    /*indice de la ligne du bloc */
    uint32_t indice_bloc_ligne;
    /*indice de la colonne du bloc*/
    uint32_t indice_bloc_col;
    /*pour sauter une ligne on ajoute*/
    uint32_t decalage = nb_bloc_ligne_h*8;
    /*initialiser le tableau des pixels orodnnes*/
    uint32_t *tableau = malloc(64*nombre_blocs_total*sizeof(uint32_t));
    /*coordonnee d'un bloc*/
    uint32_t coordonnee;
    /*on parcourt les blocs*/
    for (uint32_t indice_bloc = 0; indice_bloc<nombre_blocs_total; indice_bloc++){
      /*calculer l'indice_bloc_ligne*/
      indice_bloc_ligne = indice_bloc % nb_bloc_ligne_h;
      /*idem pour indice_bloc_col*/
      indice_bloc_col = (indice_bloc/nb_bloc_ligne_h)%nb_bloc_ligne_v;
      /*coordonnee*/
      coordonnee =(nb_bloc_ligne_h*64*indice_bloc_col) + indice_bloc_ligne*8;
      /*parcourir les octets de chaque bloc*/
      for (int indice_byte = 0; indice_byte < 8; indice_byte++){
        /*les elts de chaque octets cette fois*/
        for (int indice_elt =0; indice_elt < 8; indice_elt ++){
          tableau[coordonnee +decalage*indice_byte + indice_elt] = tableau_image[64*indice_bloc + 8*indice_byte + indice_elt];
        }
      }
    }
    return tableau;
}




/*################################ Ordonner mcu apres upsampling RGB ################################*/




/*ordonner les components apres upsampling (pour un mcu)*/
/*on applique cett fonction pour chaque components*/
int16_t **ordonner_comp(struct jpeg_desc *jdesc, int16_t **tableau){
  uint8_t samp_h = get_frame_component_sampling_factor(jdesc, DIR_H, 0);
  uint8_t samp_v = get_frame_component_sampling_factor(jdesc, DIR_V, 0);
  int16_t **nouvelle_comp = malloc(samp_h*samp_v*sizeof(int16_t *));
  for(uint8_t i = 0;i< samp_h*samp_v; i++){
    nouvelle_comp[i] = malloc(64*sizeof(int16_t));
  }
  /*meme methode de la fonction precedente mais pour 1 mcu*/
  uint32_t nombre_blocs_total = samp_h*samp_v;
  uint32_t nb_bloc_ligne_h =  samp_h;
  uint32_t nb_bloc_ligne_v =  samp_v;
  uint32_t indice_bloc_ligne;
  uint32_t indice_bloc_col;
  uint32_t decalage = nb_bloc_ligne_h*8;
  int16_t *tableau_temp2 = malloc(64*nombre_blocs_total*sizeof(int16_t));
  uint32_t coordonnee;
  for (uint32_t indice_bloc = 0; indice_bloc<nombre_blocs_total; indice_bloc++){
    indice_bloc_ligne = indice_bloc % nb_bloc_ligne_h;
    indice_bloc_col = (indice_bloc/nb_bloc_ligne_h)%nb_bloc_ligne_v;
    coordonnee =(nb_bloc_ligne_h*64*indice_bloc_col) + indice_bloc_ligne*8;
    for (int indice_byte = 0; indice_byte < 8; indice_byte++){
      for (int indice_elt =0; indice_elt < 8; indice_elt ++){
        // tableau_temp2[coordonnee +decalage*indice_byte + indice_elt] = tableau_temp[64*indice_bloc + 8*indice_byte + indice_elt];
          tableau_temp2[coordonnee +decalage*indice_byte + indice_elt] = tableau[indice_bloc][8*indice_byte + indice_elt];
           // nouvelle_comp[indice_bloc][8*indice_byte + indice_elt] = tableau[indice_bloc][8*indice_byte + indice_elt];
      }
    }
  }
  /*on retourne un double pointeur*/
  for (uint16_t i = 0 ; i < samp_h*samp_v ; i++){
    for (uint8_t j = 0;j < 64; j++)
    nouvelle_comp[i][j] = tableau_temp2[i*64 + j];
  }
  /*on libere le tableau d'entree*/
  for (uint16_t i = 0; i < samp_h*samp_v;i++){
    free(tableau[i]);
  }
  free(tableau);
  free(tableau_temp2);
  return nouvelle_comp;
}



/*on ordonne 1 mcu en appliquant la fonction precedente pour chaque components*/
void mcu_ordonne(struct jpeg_desc *jdesc, struct mcu *my_mcu){
  /*ordonner chaque component d'abord*/
  int16_t **new_blocks_Y = ordonner_comp(jdesc, my_mcu->blocks_Y);
  int16_t **new_blocks_Cb = ordonner_comp(jdesc,my_mcu->blocks_Cb);
  int16_t **new_blocks_Cr = ordonner_comp(jdesc, my_mcu->blocks_Cr);
  my_mcu->blocks_Y = new_blocks_Y;
  my_mcu->blocks_Cb = new_blocks_Cb;
  my_mcu->blocks_Cr = new_blocks_Cr;
}




/*####################################### On ordonne image (RGB)####################################*/



/*Fonction qui ordonne les pixels rgb avant la génération du ppm*/
/*tjrs le meme principe mais cette fois les mcus jouent le role des blocs*/
uint32_t *ordonner_rgb(struct jpeg_desc *jdesc, uint32_t *image){
  uint32_t hauteur_reelle = get_size_reelle(jdesc, DIR_H);
  uint32_t largeur_reelle = get_size_reelle(jdesc, DIR_V);
  uint8_t samp_h = get_frame_component_sampling_factor(jdesc, DIR_H, 0);
  uint8_t samp_v = get_frame_component_sampling_factor(jdesc, DIR_V, 0);


  uint32_t nombre_mcu_total = nb_mcu(jdesc);
  uint32_t nb_mcu_ligne_h =  hauteur_reelle/(8*samp_h);
  uint32_t nb_mcu_ligne_v =  largeur_reelle/(8*samp_v);
  uint32_t indice_mcu_ligne;
  uint32_t indice_mcu_col;
  uint32_t decalage = nb_mcu_ligne_h*8*samp_h;
  uint32_t *tableau_temp2 = malloc(64*nombre_mcu_total*samp_h*samp_v*sizeof(uint32_t));
  uint32_t coordonnee;
  for (uint32_t indice_mcu = 0; indice_mcu<nombre_mcu_total; indice_mcu++){
    indice_mcu_ligne = indice_mcu % nb_mcu_ligne_h;
    indice_mcu_col = (indice_mcu/nb_mcu_ligne_h)%nb_mcu_ligne_v;
    coordonnee =(nb_mcu_ligne_h*8*samp_h*8*samp_v*indice_mcu_col) + indice_mcu_ligne*8*samp_h;
    for (int indice_ligne = 0; indice_ligne < 8*samp_v; indice_ligne++){
      for (int indice_elt =0; indice_elt < 8*samp_h; indice_elt ++){
        tableau_temp2[coordonnee +decalage*indice_ligne + indice_elt] = image[64*samp_h*samp_v*indice_mcu + 8*samp_h*indice_ligne + indice_elt];
      }
    }
  }
 return tableau_temp2;
}




/*######################################## Troncature #################################################*/



/* Fonction qui traite la troncature pour les images dont les mcus débordent de son cadre*/
uint32_t *tronquer(struct jpeg_desc *jdesc, uint32_t *tableau){
  uint32_t hauteur_reelle = get_size_reelle(jdesc, DIR_H);
  /*reelle = avant troncature*/
  uint16_t largeur = get_image_size(jdesc, DIR_V);
  uint16_t hauteur = get_image_size(jdesc, DIR_H);
  uint32_t  *copy = malloc(largeur*hauteur*sizeof(uint32_t));
  /*on parcourt les lignes de l'image apres tronca*/
  for (uint16_t ligne = 0; ligne < largeur; ligne++){
    /*on parcourt les colo de l'image apres tronca*/
    for(uint32_t colo = 0; colo < hauteur; colo++){
       copy[ligne*hauteur + colo] = tableau[ligne*hauteur_reelle + colo];
    }
  }
  return copy;
}
