#ifndef __TREATMENT_H__
#define __TREATMENT_H__

#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include "../include/jpeg_reader.h"
#include "../include/bitstream.h"
#include "../include/huffman.h"
#include <math.h>
#include "../include/extraction.h"


extern void quantification_inverse(struct jpeg_desc *jdesc, int16_t *tab_frequences, uint8_t i);
extern void zigzag_inverse(int16_t *tab_frequences);
extern void idct(int16_t *tab_frequences);
extern uint8_t saturee(float valeur);
extern uint32_t *ordonner_pixel(struct jpeg_desc *jdesc, uint32_t *tableau_image);
extern uint32_t *tronquer(struct jpeg_desc *jdesc, uint32_t *tableau);
extern int16_t **ordonner_comp(struct jpeg_desc *jdesc, int16_t **tableau);
extern void mcu_ordonne(struct jpeg_desc *jdesc, struct mcu *my_mcu);
extern uint32_t *ordonner_rgb(struct jpeg_desc *jdesc, uint32_t *image);

#endif
