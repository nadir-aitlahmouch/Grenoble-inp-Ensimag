#ifndef __BITSTREAM_H__
#define __BITSTREAM_H__

#include <stdint.h>
#include <stdbool.h>



struct bitstream{
  FILE *fichier;
  uint8_t byte_prec;
  uint8_t byte_courant;
  uint8_t byte_suivant;
  uint8_t nb_elt_suiv;
  bool end_of_file;
  uint8_t indice_bit;
};

extern struct bitstream *create_bitstream(const char *filename);

extern void close_bitstream(struct bitstream *stream);

extern uint8_t read_bitstream(struct bitstream *stream,
                              uint8_t nb_bits,
                              uint32_t *dest,
                              bool discard_byte_stuffing);
//
extern bool end_of_bitstream(struct bitstream *stream);

#endif
