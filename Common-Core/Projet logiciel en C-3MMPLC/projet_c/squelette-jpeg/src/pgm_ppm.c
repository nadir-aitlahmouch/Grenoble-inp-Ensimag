
#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include "../include/jpeg_reader.h"
#include "../include/bitstream.h"
#include "../include/huffman.h"
#include <math.h>
#include <string.h>
#include "../include/informations.h"
#include "../include/rgb.h"


void generer_ppm(FILE *fichier, uint32_t *tableau, uint16_t largeur, uint16_t hauteur){
    fprintf(fichier, "P6\n");
    fprintf(fichier, "%hu %hu\n", largeur, hauteur);
    fprintf(fichier, "%i\n", 255);

    for (int i = 0; i < largeur*hauteur; i++){
      uint8_t Red = tableau[i] >> 16;
      uint8_t Green = (tableau[i] >> 8) & 0xFF;
      uint8_t Blue = tableau[i] & 0xFF;

      uint8_t colors[] = {Red, Green, Blue};
      fwrite(colors, sizeof(uint8_t), 3, fichier);
    }
}



void generer_pgm(FILE *fichier, uint32_t *tableau, uint16_t largeur, uint16_t hauteur){
    fprintf(fichier, "P5\n");
    fprintf(fichier, "%hu %hu\n", largeur, hauteur);
    fprintf(fichier, "%i\n", 255);

    for (int i = 0; i < largeur*hauteur; i++){
      fwrite(&tableau[i], sizeof(uint8_t), 1, fichier);
    }
}


char *extension_modif(char *filename, char *extension)
{
    int len = strlen(filename) - 1;
    char *nouveau_nom = malloc((len + strlen(extension) + 1)*sizeof(char));
    while (filename[len] != 46) {
        len --;
    }
    len ++;
    int a = 0;
    while (a != len){
           nouveau_nom[a] = filename[a];
           a++;
    }
    for (uint8_t i = 0; i < 3; i++) {
        nouveau_nom[len + i] = extension[i];
    }
    nouveau_nom[len + 3] = '\0';
    return nouveau_nom;
}

void generateur_images_ppm_pgm(uint32_t *tableau, struct jpeg_desc *jpeg){
  uint8_t nombre_cpnt = get_nb_components(jpeg);
  char *filename = get_filename(jpeg);
  uint16_t hauteur = get_image_size(jpeg, 1);
  uint16_t largeur = get_image_size(jpeg, 0);
  if (nombre_cpnt == 1){
    char *nouveau_nom = extension_modif(filename, "pgm");
    FILE *fichier = fopen(nouveau_nom, "w");
    generer_pgm(fichier, tableau, largeur, hauteur);
    fclose(fichier);
    free(nouveau_nom);
  } else {
    char *nouveau_nom = extension_modif(filename, "ppm");
    FILE *fichier = fopen(nouveau_nom, "w");
    generer_ppm(fichier, tableau, largeur, hauteur);
    fclose(fichier);
    free(nouveau_nom);
  }
}
