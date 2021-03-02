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
#include "../include/upsampling.h"
#include "../include/loeffler.h"



/*############################################RGB pour one MCU#######################################*/




/* Convertit un mcu d'une structure YCbCr en RGB (à appeler après l'upsampling)*/
void *convert_mcu_to_rgb(struct jpeg_desc *jdesc,struct mcu *my_mcu, uint32_t *image, uint32_t j){
  /*apres l upsampling le nbr de blocs par component est le suivant:*/
  uint8_t nbr_blocs_component = get_frame_component_sampling_factor(jdesc, 0, 0)
                                *get_frame_component_sampling_factor(jdesc, 1, 0);
  uint16_t nb_comp = my_mcu->taille_comp[0];
  uint32_t var = 0;
  /*float pour caluler rgb*/
  float red,green,blue;
  /*Nouvelles variables pour convertir du float à l'int */
  uint32_t r,g,b;
  /*On parcourt les blocs pour appliquer les formules de r g b*/
  for(uint16_t indice_bloc = 0; indice_bloc<nb_comp; indice_bloc++){
    for(uint8_t i=0; i<64; i++){
      red = my_mcu->blocks_Y[indice_bloc][i] + 1.402*(my_mcu->blocks_Cr[indice_bloc][i] - 128);
      green = my_mcu->blocks_Y[indice_bloc][i]- 0.34414*(my_mcu->blocks_Cb[indice_bloc][i] - 128)
              - 0.71414*(my_mcu->blocks_Cr[indice_bloc][i] - 128);
      blue = my_mcu->blocks_Y[indice_bloc][i] + 1.772*(my_mcu->blocks_Cb[indice_bloc][i] - 128);
      r = (uint32_t) saturee(red);
      g = (uint32_t) saturee(green);
      b = (uint32_t) saturee(blue);
      /*Sotcke en octet 0x00RGB */
      var = (r<<16) + (g<<8) + b;
      /*on stocke les pixels dans image*/
      image[j*(64*nbr_blocs_component) +indice_bloc*64 + i] = var;
    }
  }
  return EXIT_SUCCESS;
}





/*#######################################RGB pour image############################################*/



/*Genere une image rgb, c'est l'equivalent de genere_pgm()
   mais cette fois pour ppm*/
uint32_t *genere_rgb(struct jpeg_desc *jdesc, struct bitstream *stream){

  float *cos_calc = tab_cos();
  float *sin_calc = tab_sin();

  /*On intisialise un tableau de trois cases prec_dc pour sauvegarder
    les différentes DC des différents composants*/
  int16_t prec_dc[3] = {0,0,0};
  /*idem que la fonction precedente*/
  uint8_t nbr_blocs_component = get_frame_component_sampling_factor(jdesc, 0, 0)
                                *get_frame_component_sampling_factor(jdesc, 1, 0);
  /*calcul: nbr de mcus*/
  uint32_t nombre_mcu = nb_mcu(jdesc);
  /*initialiser mcu à decoder et convertir to rgb*/
  struct mcu mcu_decode;
  /*initialisation du tableau des pixels apres decodage*/
  uint32_t *image = malloc(nombre_mcu*64*sizeof(uint32_t)*nbr_blocs_component);
  /*parcourir les mcus*/
for (uint32_t i = 0; i < nombre_mcu; i++){
    mcu_decode = decoder_mcu(jdesc, stream, prec_dc, cos_calc, sin_calc);
    /*apres decodage de mcu on passe upsampling */
    upsampling(jdesc, &mcu_decode);
    /*une assert pour verifier que les components ont la meme taille*/
    uint16_t nb_comp = mcu_decode.taille_comp[0];
    uint16_t taille1 = mcu_decode.taille_comp[1];
    uint16_t taille2 = mcu_decode.taille_comp[2];
    assert(nb_comp==taille1 && taille2==nb_comp && "ERROR in rgb.c: Les composantes n'ont pas la même taille.");
    /*ordonner mcu ligne par ligne*/
    mcu_ordonne(jdesc, &mcu_decode);
    /*derniere etape la convertion to rgb et on stocke le resultat dans
     le tableau pixel*/
    convert_mcu_to_rgb(jdesc, &mcu_decode, image, i);
    /*libere la mcu*/
    free_mcu(&mcu_decode);
}
  /*on ordonne le tableau image*/
  uint32_t *ordonne = ordonner_rgb(jdesc, image);
  free(image);
  /*tronquer*/
  uint32_t *tronquee = tronquer(jdesc, ordonne);
  free(ordonne);
  free(cos_calc);
  free(sin_calc);
  /*image finale*/
  return tronquee;
}
