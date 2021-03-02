#ifndef __PGM_PPM_H__
#define __PGM_PPM_H__


#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include "../include/jpeg_reader.h"
#include "../include/bitstream.h"
#include "../include/huffman.h"
#include <math.h>

extern void generer_ppm(FILE *fichier, uint32_t *tableau, uint16_t largeur, uint16_t hauteur);

extern void generer_pgm(FILE *fichier, uint32_t *tableau, uint16_t largeur, uint16_t hauteur);

extern char *extension_modif(char *filename, char *extension);

extern void generateur_images_ppm_pgm(uint32_t *tableau, struct jpeg_desc *jpeg);

// extern void generer_ppm(uint32_t *image, struct jpeg_desc *jdesc);

#endif
