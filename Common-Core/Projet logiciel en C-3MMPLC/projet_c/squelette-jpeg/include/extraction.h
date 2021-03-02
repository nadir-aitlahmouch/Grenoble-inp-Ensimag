#ifndef __EXTRACTION_H__
#define __EXTRACTION_H__


#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include "../include/jpeg_reader.h"
#include "../include/bitstream.h"
#include "../include/huffman.h"
#include <math.h>
#include <string.h>

/*structure mcu*/
struct mcu{
  /* ieme elt :nbr de bloc par ieme component*/
  uint16_t *taille_comp;
  /*blocs concat (Y,cb,cr)*/
  // omar kayn
  int16_t **blocks_Y;
  int16_t **blocks_Cb;
  int16_t **blocks_Cr;
};




extern int16_t *decoder_DC_AC(struct jpeg_desc *jdesc,struct bitstream *stream, int16_t *prec_dc, enum component comp);

extern uint16_t *blocs_par_components(struct jpeg_desc *jdesc);

extern struct mcu decoder_mcu(struct jpeg_desc *jdesc, struct bitstream *stream, int16_t *prec_dc, float *cos_calc, float *sin_calc);

extern uint32_t *genere_pgm(struct jpeg_desc *jdesc, struct bitstream *stream);

extern uint8_t free_mcu(struct mcu *my_mcu);

#endif
