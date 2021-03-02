#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <assert.h>
#include <math.h>
#include "../include/jpeg_reader.h"
#include "../include/bitstream.h"
#include "../include/huffman.h"
#include "../include/treatment.h"
#include "../include/pgm_ppm.h"
#include "../include/extraction.h"
#include "../include/rgb.h"




/* Fonction qui retourne un tableau trois cases informant sur le nombre de blocs
 par composante (Y, Cb, Cr dans 1 mcu), sert à remplir une structure mcu*/
uint16_t *blocs_par_components(struct jpeg_desc *jdesc){
      uint16_t *tableau = calloc(3,sizeof(uint16_t));
      uint8_t nombre_comp = get_nb_components(jdesc);
      for (int i = 0; i < nombre_comp; i++){
        uint8_t samp_h = get_frame_component_sampling_factor(jdesc, DIR_H, i);
        uint8_t samp_v = get_frame_component_sampling_factor(jdesc, DIR_V, i);
        tableau[i] = samp_h*samp_v;
      }
      return tableau;
}




/* Retourne la taille réelle de l'image où tous les mcu seraient complets.
   c'est à dire avant troncature*/
uint16_t get_size_reelle(struct jpeg_desc *jdesc,enum direction dir){
  uint16_t size = get_image_size(jdesc, dir);
  uint8_t fact_hv = get_frame_component_sampling_factor(jdesc, dir, 0);
  if (size%(8*fact_hv) != 0){
    size = size + (8*fact_hv) - (size%(8*fact_hv));
  }
  return size;
}





/* Retoune le nombre de mcu dans une image*/
uint32_t nb_mcu(struct jpeg_desc *jdesc){
  uint16_t hauteur = get_size_reelle(jdesc, DIR_H);
  uint16_t largeur = get_size_reelle(jdesc,  DIR_V);
  uint32_t nb_blocs = (hauteur*largeur)/64;
  uint8_t fact_h = get_frame_component_sampling_factor(jdesc, DIR_H, 0);
  uint8_t fact_v = get_frame_component_sampling_factor(jdesc, DIR_V, 0);
  return nb_blocs/(fact_h*fact_v);
}





/*Retourne le nombre total de blocs dans une image*/
uint32_t nb_bloc_tot(struct jpeg_desc *jdesc){
  uint16_t *bloc_comp = blocs_par_components(jdesc);
  uint32_t res=0;
  uint32_t nbs_mcu = nb_mcu(jdesc);
  for (int i=0; i<3; i++) res += bloc_comp[i];
  free(bloc_comp);
  return res*nbs_mcu;
}





/*Fonction qui retourne l'ordre des composants selon leur ordre lecture dans le stream*/
uint32_t *ordre_components(struct jpeg_desc *jdesc){
  uint32_t *ordre = malloc(3*sizeof(uint32_t));
  /*dans le cas pgm on retourne l'ordre suivant, (pour eviter les cas apres)*/
  ordre[0] = 0; ordre[1] = 1; ordre[2] = 2;
  if (get_nb_components(jdesc) != 1){
    uint8_t Y_id = get_frame_component_id(jdesc, 0);
    uint8_t Cb_id = get_frame_component_id(jdesc, 1);
    uint8_t premiere_id = get_scan_component_id(jdesc, 0);
    uint8_t deuxieme_id = get_scan_component_id(jdesc, 1);
    if (Y_id == premiere_id){
      if (Cb_id == deuxieme_id){
        ordre[0] = 0;ordre[1]=1;ordre[2]=2;
      } else {
        ordre[0] = 0;ordre[1]=2;ordre[2]=1;
      }
    } else if (Cb_id == premiere_id){
      if (Y_id == deuxieme_id){
        ordre[0] = 1;ordre[1]=0;ordre[2]=2;
      } else {
        ordre[0] = 2;ordre[1]=0;ordre[2]=1;
      }
    }
        else {
          if (Y_id == deuxieme_id){
            ordre[0] = 1;ordre[1]=2;ordre[2]=0;
          }
          else {
            ordre[0] = 2;ordre[1]=1;ordre[2]=0;
          }
        }
    }
  return ordre;
}
