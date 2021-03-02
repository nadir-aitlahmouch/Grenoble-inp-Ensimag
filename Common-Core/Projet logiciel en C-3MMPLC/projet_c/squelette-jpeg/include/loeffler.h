#ifndef __LOEFFLER_H__
#define __LOEFFLER_H__


#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include "../include/jpeg_reader.h"
#include "../include/bitstream.h"
#include "../include/huffman.h"
#include <math.h>
#include <string.h>

extern void loeffler(int16_t *tab, float *cos_calc, float *sin_calc);
extern float *tab_cos();
extern float *tab_sin();


#endif
