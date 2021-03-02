#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <time.h>
#include <math.h>
#include <stdint.h>
#include "../include/treatment.h"
#define PI 3.141592653589
#define SQRT12 0.7071067811865 //sqrt(0.5)
#define SQRT8  2.8284271247461 //sqrt(8)


/*#############################################Outils Déboggage#######################################*/

void print_ligne(float *tab, uint8_t longueur){
  for (uint8_t i = 0; i< longueur; i++){
    printf("%f ", tab[i]);
  }
  printf("\n");
}

void print_matrice_carree(float **tab, uint8_t longueur){
  for (uint8_t i = 0 ; i<longueur; i++){
    print_ligne(tab[i], longueur);
  }
  printf("\n");
}


/*#############################################LOEFFLER 1D#######################################*/

/*Permute les élements de tab selon le vecteur permutation*/
void permuter(float *tab, uint8_t longueur, uint8_t *permutation){
  float copie[longueur];
  for (uint8_t i = 0; i<longueur; i++){
    copie[i] = tab[i];
  }
  for (uint8_t i = 0; i<longueur; i++){
    tab[i] = copie[permutation[i]];
  }
}

/*Butterfly Unit*/
void butterfly(float *e0, float *e1){
  float tmp0 = *e0;
  float tmp1 = *e1;
  *e0 = (tmp0+tmp1)/2;
  *e1 = (tmp0-tmp1)/2;
}

/*Tableau cosinus : 8 valeurs précalculés de cos(n*pi/16)*/
float *tab_cos(){
  float *tab = malloc(8*sizeof(float));
  for (uint8_t i = 0; i<8; i++){
    tab[i] = cos(i*PI/16);
  }
  return tab;
}
/*De même pour sinus*/
float *tab_sin(){
  float *tab = malloc(8*sizeof(float));
  for (uint8_t i = 0; i<8; i++){
    tab[i] = sin(i*PI/16);
  }
  return tab;
}

/* Rotator Unit*/
void rotator(float k, uint8_t n, float *tab_cos, float *tab_sin, float *e0, float *e1){
  float tmp0 = *e0;
  float tmp1 = *e1;
  *e0 = tmp0*k*tab_cos[n] - tmp1*k*tab_sin[n];
  *e1 = tmp1*k*tab_cos[n] + tmp0*k*tab_sin[n];
}

/* Loeffler exactement comme décris dans le papier:
 https://www.ijaiem.org/volume3issue5/IJAIEM-2014-05-31-117.pdf */

void loeffler8(float *tab, float *cos_calc, float *sin_calc){
  /*stage 0 :*/
  for (uint8_t i = 0; i<8; i++){
    tab[i]= tab[i]*SQRT8;
  }
  /*stage 1 : easy peasy*/
  tab[3] = tab[3]*SQRT12;
  tab[5] = tab[5]*SQRT12;
  butterfly(&tab[1], &tab[7]);
  /*stage 2*/
  butterfly(&tab[0], &tab[4]);
  rotator(SQRT12, 6, cos_calc, sin_calc, &tab[2], &tab[6]);
  butterfly(&tab[7], &tab[5]);
  butterfly(&tab[1], &tab[3]);
  /*stage 3*/
  butterfly(&tab[0], &tab[6]);
  butterfly(&tab[4], &tab[2]);
  rotator(1, 3, cos_calc, sin_calc, &tab[7], &tab[1]);
  rotator(1, 1, cos_calc, sin_calc, &tab[3], &tab[5]);
  /*stage 4 : jolie figure*/
  butterfly(&tab[6], &tab[7]);
  butterfly(&tab[2], &tab[3]);
  butterfly(&tab[4], &tab[5]);
  butterfly(&tab[0], &tab[1]);
  /*réorganisation des "fils":*/
  uint8_t permutation[8] = {0,4,2,6,7,3,5,1};
  permuter(tab, 8, permutation);
}

/*#############################################Loeffler#######################################*/
/*Calcule la transposée d'une matrice*/
void transposee(float **matrice){// M=transposee(M)
  float copie[8][8];
  for (uint8_t i = 0; i<8; i++){
    for (uint8_t j = 0; j<8; j++){
      copie[i][j] = matrice[j][i];
    }
  }
  for (uint8_t i = 0; i<8; i++){
    for (uint8_t j = 0; j<8; j++){
      matrice[i][j] = copie[i][j];
    }
  }
}


void loeffler(int16_t *tab, float *cos_calc, float *sin_calc){
  /* un peu d'organisation.. */
  float **lignes = malloc(8*sizeof(float *));
  for (uint8_t i = 0; i<8; i++){
    float *ligne = malloc(8*sizeof(float));
    for (uint8_t j = 0; j<8; j++){
      ligne[j] = tab[8*i+j];
    }
    lignes[i] = ligne;
  }

  /* Et maintenant loeffler en 2D, expliqué joliment dans wiki :
   For example, a two-dimensional DCT-II of an image or a matrix is simply the
  one-dimensional DCT-II, from above, performed along the rows and then along
  the columns (or vice versa). The inverse of a multi-dimensional DCT is just a
  separable product of the inverse(s) of the corresponding one-dimensional
  DCT(s) (see above), e.g. the one-dimensional inverses applied along one
  dimension at a time in a row-column algorithm.
  SOURCE : https://en.wikipedia.org/wiki/Discrete_cosine_transform   */

  /* on applique loeffler8 pour chaque ligne */
  for (uint8_t i = 0; i<8; i++){
    loeffler8(lignes[i],cos_calc,sin_calc);
  }

  /* calcul de la trasposée pour appliquer par la suite loeffler8 sur les lignes
   de la transposée */
  transposee(lignes);
  for (uint8_t i = 0; i<8; i++){
    loeffler8(lignes[i],cos_calc,sin_calc);
  }

  /* réorganisation en tableau de longueur 64 */
  for (uint8_t i = 0; i<8; i++){
    for (uint8_t j = 0; j<8; j++){;
      tab[8*i+j] = saturee(lignes[j][i] + 128);
    }
  }
  /* valgrind, baby */
  for (uint8_t i = 0; i < 8; i++){
    free(lignes[i]);
  }
  free(lignes);
}
