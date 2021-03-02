#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <math.h>
#include "../include/jpeg_reader.h"
#include "../include/bitstream.h"
#include "../include/huffman.h"
#include "../include/treatment.h"
#include "../include/pgm_ppm.h"
#include "../include/extraction.h"
#include "../include/informations.h"
#include "../include/rgb.h"



/*Principe d'upsampling: après l'etape d'upsampling les components de chaque mcu
   sont bien organisés comme indiqué dans le sujet: les blocs  gauche->droite et de haut->bas
    donc avant de generer rgb on ordonne les mcus */




/*#########################################UPSAMPLING ONE BLOCK#################################*/


int16_t **upsampling_h(int16_t *tableau, uint8_t fact_h){
  int16_t **blocs_upsampling = malloc(fact_h*sizeof(int16_t *));
  // int16_t *bloc = malloc(64*sizeof(int16_t));
  for(uint8_t i = 0; i< fact_h; i++) blocs_upsampling[i] = malloc(64*sizeof(int16_t));
  int16_t tableau_blocs[64*fact_h];
  for(uint8_t i = 0; i < 64; i++){
    for(uint8_t j = 0; j < fact_h; j++){
      tableau_blocs[i*fact_h + j] = tableau[i];
    }
  }
  for(uint8_t indice_ligne = 0; indice_ligne < 8; indice_ligne++){
    for(uint8_t indice_bloc = 0; indice_bloc < fact_h; indice_bloc++){
      for(uint8_t indice_elt = 0; indice_elt < 8; indice_elt ++){
        blocs_upsampling[indice_bloc][indice_ligne*8+indice_elt] =
        tableau_blocs[indice_ligne*fact_h*8+8*indice_bloc+indice_elt];
      }
      }
    }
    return blocs_upsampling;
}

int16_t **upsampling_v(int16_t *tableau, uint8_t fact_v){
 int16_t **blocs_upsampling = malloc(fact_v*sizeof(int16_t *));
 int16_t tableau_temp[64*fact_v];
 for(uint8_t indice_ligne = 0; indice_ligne < 8; indice_ligne++){
   for(uint8_t i = 0; i < fact_v; i++){
     for(uint8_t indice_elt = 0; indice_elt < 8; indice_elt++){
       tableau_temp[indice_ligne*fact_v*8 + i*8 + indice_elt] = tableau[indice_ligne*8 + indice_elt];
     }
   }
 }
 for(uint8_t i = 0; i < fact_v; i++){
   blocs_upsampling[i] = malloc(64*sizeof(int16_t));
   for(uint8_t indice_elt = 0; indice_elt < 64; indice_elt ++){
     blocs_upsampling[i][indice_elt] = tableau_temp[i*64+indice_elt];
   }
 }
 return blocs_upsampling;
}


/*############################################ TYPE ################################################*/



/*retourne tableau de tableaux, chacun de taille 2 contenant le facteur
qu'on va utiliser dans upsampling ci dessus, un tableau pour Cb et le deuxieme
pour Cr*/
uint8_t **type_echantillonage(struct jpeg_desc *jdesc){
  uint8_t **types = malloc(2*sizeof(uint8_t *));
  types[0] = malloc(2*sizeof(uint8_t));
  types[1] = malloc(2*sizeof(uint8_t));
  uint16_t factor_h_y  = get_frame_component_sampling_factor(jdesc, 0, 0);
  uint16_t factor_l_y  = get_frame_component_sampling_factor(jdesc, 1, 0);
  uint16_t factor_h_cb = get_frame_component_sampling_factor(jdesc, 0, 1);
  uint16_t factor_l_cb = get_frame_component_sampling_factor(jdesc, 1, 1);
  uint16_t factor_h_cr = get_frame_component_sampling_factor(jdesc, 0, 2);
  uint16_t factor_l_cr = get_frame_component_sampling_factor(jdesc, 1, 2);
  assert(factor_h_y%factor_h_cb==0 && "ERROR in upsampling.c : Facteur d'échantillonage erroné.");
  assert(factor_l_y%factor_l_cb==0  && "ERROR in upsampling.c : Facteur d'échantillonage erroné.");
  assert(factor_h_y%factor_h_cr==0  && "ERROR in upsampling.c : Facteur d'échantillonage erroné.");
  assert(factor_l_y%factor_l_cr==0  && "ERROR in upsampling.c : Facteur d'échantillonage erroné.");
  assert(factor_h_y*factor_l_y <=10  && "ERROR in upsampling.c : Facteur d'échantillonage erroné.");
  assert(factor_h_cb*factor_l_cb <=10  && "ERROR in upsampling.c : Facteur d'échantillonage erroné.");
  assert(factor_h_cr*factor_l_cr <=10  && "ERROR in upsampling.c : Facteur d'échantillonage erroné.");
  types[0][0] = factor_h_y/factor_h_cb;
  types[0][1] = factor_l_y/factor_l_cb;
  types[1][0] = factor_h_y/factor_h_cr;
  types[1][1] = factor_l_y/factor_l_cr;
  return types;
}




/*#################################### UPSAPLING MCU #############################################*/


/*Fonction qui retourne un mcu dont les blocs Cb et Cr sont suréchantillonnés après
leur échantillonage horizontal dans l'encodage*/
void upsampling_horizontal(struct mcu *my_mcu, uint8_t **type){
  int16_t **nouveau_blocks_Cb = malloc(my_mcu->taille_comp[0]*sizeof(int16_t *));
  int16_t **nouveau_blocks_Cr = malloc(my_mcu->taille_comp[0]*sizeof(int16_t *));
  for (uint16_t i = 0; i < my_mcu->taille_comp[1]; i++){
    int16_t **blocs_apres_upsampling = upsampling_h(my_mcu->blocks_Cb[i], type[0][0]);
    free(my_mcu->blocks_Cb[i]);
    for (uint8_t j = 0; j < type[0][0]; j++){
      nouveau_blocks_Cb[i*type[0][0] + j] = blocs_apres_upsampling[j];
    }
  free(blocs_apres_upsampling);
  }
  free(my_mcu->blocks_Cb);
  for (uint16_t i = 0; i < my_mcu->taille_comp[2]; i++){
    int16_t **blocs_apres_upsampling = upsampling_h(my_mcu->blocks_Cr[i], type[1][0]);
    free(my_mcu->blocks_Cr[i]);
    for (uint8_t j = 0; j < type[1][0]; j++){
        nouveau_blocks_Cr[i*type[1][0] + j] = blocs_apres_upsampling[j];
    }
    free(blocs_apres_upsampling);
  }
  free(my_mcu->blocks_Cr);
  my_mcu->taille_comp[1] = my_mcu->taille_comp[1]*type[0][0];
  my_mcu->taille_comp[2] = my_mcu->taille_comp[2]*type[1][0];
  my_mcu->blocks_Cb = nouveau_blocks_Cb;
  my_mcu->blocks_Cr = nouveau_blocks_Cr;
}


/*Fonction qui retourne un mcu dont les blocs Cb et Cr sont suréchantillonnés après
leur échantillonage vertical dans l'encodage*/
void upsampling_vertical(struct jpeg_desc *jdesc, struct mcu *my_mcu, uint8_t **type){
  int16_t **nouveau_blocks_Cb = malloc(my_mcu->taille_comp[0]*sizeof(uint16_t *));
  int16_t **nouveau_blocks_Cr = malloc(my_mcu->taille_comp[0]*sizeof(uint16_t *));
  uint16_t nb_bloc_h = get_frame_component_sampling_factor(jdesc, 0, 0);

  for (uint16_t  indice_col = 0; indice_col < my_mcu->taille_comp[1]/nb_bloc_h; indice_col++){
    for(uint16_t indice_ligne = 0; indice_ligne < nb_bloc_h; indice_ligne++){
      int16_t **blocs_apres_upsampling = upsampling_v(my_mcu->blocks_Cb[indice_col*nb_bloc_h+indice_ligne], type[0][1]);
      free(my_mcu->blocks_Cb[indice_col*nb_bloc_h+indice_ligne]);
      for (uint8_t j = 0; j < type[0][1]; j++){
        nouveau_blocks_Cb[nb_bloc_h*(indice_col)*type[0][1]+nb_bloc_h*j+indice_ligne] = blocs_apres_upsampling[j];
      }
      free(blocs_apres_upsampling);
    }
  }
  free(my_mcu->blocks_Cb);
  for (uint16_t  indice_col = 0; indice_col < my_mcu->taille_comp[2]/nb_bloc_h; indice_col++){
    for(uint16_t indice_ligne = 0; indice_ligne < nb_bloc_h; indice_ligne++){
      int16_t **blocs_apres_upsampling = upsampling_v(my_mcu->blocks_Cr[indice_col*nb_bloc_h+indice_ligne], type[1][1]);
      free(my_mcu->blocks_Cr[indice_col*nb_bloc_h+indice_ligne]);
      for (uint8_t j = 0; j < type[1][1]; j++){
        nouveau_blocks_Cr[nb_bloc_h*(indice_col)*type[1][1]+nb_bloc_h*j+indice_ligne] = blocs_apres_upsampling[j];
      }
      free(blocs_apres_upsampling);
    }
  }
  free(my_mcu->blocks_Cr);
  my_mcu->taille_comp[1] = my_mcu->taille_comp[1]*type[0][1];
  my_mcu->taille_comp[2] = my_mcu->taille_comp[2]*type[1][1];
  my_mcu->blocks_Cb = nouveau_blocks_Cb;
  my_mcu->blocks_Cr = nouveau_blocks_Cr;
}




/*Fonction qui retourne un mcu dont les blocs Cb et Cr sont suréchantillonnés après
leur échantillonage horizontal et vertical dans l'encodage*/

void upsampling_vertical_horizontal(struct jpeg_desc *jdesc, struct mcu *my_mcu, uint8_t **type){
  int16_t **nouveau_blocks_Cb = malloc(my_mcu->taille_comp[1]*type[0][0]*sizeof(uint16_t *));
  int16_t **nouveau_blocks_Cr = malloc(my_mcu->taille_comp[2]*type[1][0]*sizeof(uint16_t *));
  for (uint16_t i = 0; i < my_mcu->taille_comp[1]; i++){
    int16_t **blocs_apres_upsampling = upsampling_h(my_mcu->blocks_Cb[i], type[0][0]);
    for (uint8_t j = 0; j < type[0][0]; j++){
      nouveau_blocks_Cb[i*type[0][0] + j] = blocs_apres_upsampling[j];
    }
    free(blocs_apres_upsampling);
    free(my_mcu->blocks_Cb[i]);
  }
  free(my_mcu->blocks_Cb);
  for (uint16_t i = 0; i < my_mcu->taille_comp[2]; i++){
    int16_t **blocs_apres_upsampling = upsampling_h(my_mcu->blocks_Cr[i], type[1][0]);
    for (uint8_t j = 0; j < type[1][0]; j++){
      nouveau_blocks_Cr[i*type[1][0] + j] = blocs_apres_upsampling[j];
    }
    free(blocs_apres_upsampling);
    free(my_mcu->blocks_Cr[i]);
  }
  free(my_mcu->blocks_Cr);
  my_mcu->taille_comp[1] = my_mcu->taille_comp[1]*type[0][0];
  my_mcu->taille_comp[2] = my_mcu->taille_comp[2]*type[1][0];
  my_mcu->blocks_Cb = nouveau_blocks_Cb;
  my_mcu->blocks_Cr = nouveau_blocks_Cr;
  upsampling_vertical(jdesc, my_mcu, type);
}



/* Effectue le suréchantillonnage pour un mcu donc les blocs Cb et Cr sont échantillonnés
(traite tous les cas) */

void upsampling(struct jpeg_desc *jdesc, struct mcu *my_mcu){
  uint8_t **type = type_echantillonage(jdesc);
  if (!((type[0][0] == 1) && (type[0][1] == 1))){
    if (((type[0][0] != 1) || (type[1][0] != 1)) && ((type[0][1] == 1) && (type[1][1] == 1))){
       upsampling_horizontal(my_mcu, type);
    }
    else if (((type[0][1] != 1) || (type[1][1] != 1)) && ((type[0][0] == 1) && (type[1][0] == 1))){
       upsampling_vertical(jdesc, my_mcu, type);
    }
    else {
    upsampling_vertical_horizontal(jdesc, my_mcu, type);
    }
  }
  free(type[0]);
  free(type[1]);
  free(type);
}
