#ifndef __RGB_H__
#define __RGB_H__
#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include "../include/jpeg_reader.h"
#include "../include/bitstream.h"
#include "../include/huffman.h"
#include <math.h>
#include "../include/treatment.h"
#include "../include/pgm_ppm.h"
#include "../include/extraction.h"

extern void *convert_mcu_to_rgb(struct jpeg_desc *jdesc, struct mcu *my_mcu, uint32_t *image, uint32_t j);

extern uint32_t *genere_rgb(struct jpeg_desc *jdesc, struct bitstream *stream);
extern uint8_t free_mcu_2(struct mcu *my_mcu);

#endif
