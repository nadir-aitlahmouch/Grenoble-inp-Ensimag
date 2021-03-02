#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <assert.h>
#include <stdbool.h>


/*###########################################STRUCTURE_BITSTREAM##############################################*/

struct bitstream{
  /*nom_fichier*/
  FILE *fichier;
  /*dernier octet lu*/
  uint8_t byte_prec;
  /*octet courant à chaque fois on lit le bit du poids fort et on le met a jour*/
  uint8_t byte_courant;
  /*octet suivant (apres le courant) NB:c'est possible d'avoir un octet imcomplet*/
  uint8_t byte_suivant;
  /*nombre delts de l'octet suivant*/
  uint8_t nb_elt_suiv;
  /*fin*/
  bool end_of_file;
  /*modulo de l'indice du bit courant (pour le byte stuffing)*/
  uint8_t indice_bit;
};

/*###############################################################################################################*/



/*Initialiser le bitstream*/
struct bitstream *create_bitstream(const char *filename){
  FILE *fichier = fopen(filename, "r");
  struct bitstream *stream = malloc(sizeof(struct bitstream));
  stream->fichier = fichier;
  stream->byte_prec = 0;
  uint8_t nb_byte = fread(&(stream->byte_courant), 1, 1, stream->fichier);
  assert(nb_byte == 1 && "ERROR in bitstream.c : fread n'a pas lu un octet.");
  nb_byte = fread(&(stream->byte_suivant), 1, 1, stream->fichier);
  if (nb_byte != 1){
    stream->end_of_file = true;
    stream->nb_elt_suiv =  8;
  }
  else{
  stream->end_of_file = false;
  }
  stream->nb_elt_suiv = 8;
  stream->indice_bit = 0;
  return stream;
}


/*mise a jour  byte_courant*/
uint8_t get_upper_load_next(uint8_t *byte, uint8_t nb_bits){
  uint8_t lire_bits = *byte >> (8 - nb_bits);
  *byte = *byte << nb_bits;
  return lire_bits;
}


/*fonction pour sauter byte_stuffing*/
void byte_stuffing(struct bitstream *stream){
  if((stream->byte_prec == 0xff) && (stream->byte_courant == 0x00)){
    stream->byte_courant = stream->byte_suivant;
    uint8_t bit_restant = 8 - stream->nb_elt_suiv;
    uint8_t nb_byte = fread(&(stream->byte_suivant), 1, 1, stream->fichier);
    if(nb_byte != 1){
      stream->end_of_file = true;
    }
    else{
      uint8_t add_courant = get_upper_load_next(&(stream->byte_suivant), bit_restant);
      stream->byte_courant += add_courant;
      stream->nb_elt_suiv = 8 - bit_restant;
    }
  }
}


/*lecture d'un bit à la fin du fichier 'dernier octet'*/
uint8_t read_bit_end_file(struct bitstream *stream, uint32_t *dest, uint8_t bit){
  if(stream->nb_elt_suiv == 0) return 0;
  bit = stream->byte_courant >> 7;
  stream->byte_courant = stream->byte_courant << 1;
  stream->nb_elt_suiv -= 1;
  stream->byte_prec = stream->byte_prec << 1;
  stream->byte_prec += bit;
  *dest = *dest << 1;
  *dest += bit;
  stream->indice_bit = (stream->indice_bit + 1) % 8;
  return 1;
}


/*lecture d'un bit, cas suivant non vide*/
uint8_t read_bit_not_empty(struct bitstream *stream, uint32_t *dest, uint8_t bit, uint8_t bit_suiv){
  /*recuperation du bit courant*/
  bit = stream->byte_courant >> 7;
  /*mise a jour de prec*/
  stream->byte_prec = stream->byte_prec << 1;
  stream->byte_prec += bit;
  /*mise a jour de courant*/
  bit_suiv = stream->byte_suivant >> 7;
  stream->byte_suivant = stream->byte_suivant << 1;
  stream->nb_elt_suiv -=1;
  stream->byte_courant = stream->byte_courant << 1;
  stream->byte_courant += bit_suiv;
  /*on ajoute a dest le nouveau bit*/
  *dest = *dest << 1;
  *dest += bit;
  stream->indice_bit = (stream->indice_bit + 1) % 8;
  return 1;
}



/*Lecture d'un bit, cas suivant vide*/
uint8_t read_bit_empty(struct bitstream *stream, uint32_t *dest, uint8_t bit, uint8_t bit_suiv){
  /*recharge le suivant*/
  uint8_t nb_byte = fread(&(stream->byte_suivant), 1, 1, stream->fichier);
  if (nb_byte==1){
    stream->nb_elt_suiv = 8;
    bit = stream->byte_courant >> 7;
    stream->byte_prec = stream->byte_prec << 1;
    stream->byte_prec += bit;
    bit_suiv = stream->byte_suivant >> 7;
    stream->byte_suivant = stream->byte_suivant << 1;
    stream->nb_elt_suiv -=1;
    stream->byte_courant = stream->byte_courant << 1;
    stream->byte_courant += bit_suiv;
    *dest = *dest << 1;
    *dest += bit;
    stream->indice_bit = (stream->indice_bit + 1) % 8;
    return 1;
  }
  /*fin fichier*/
  else{
    stream->end_of_file = true;
    stream->nb_elt_suiv = 8;
    bit = stream->byte_courant >> 7;
    stream->byte_courant = stream->byte_courant << 1;
    stream->nb_elt_suiv -= 1;
    stream->byte_prec = stream->byte_prec << 1;
    stream->byte_prec += bit;
    *dest = *dest << 1;
    *dest += bit;
    stream->indice_bit = (stream->indice_bit + 1) % 8;
    return 1;
  }
}



/*lecture d'un seul bit*/
uint8_t read_bit(struct bitstream *stream, uint32_t *dest, bool discard_byte_stuffing){
  /*bit courant*/
  uint8_t bit = 0;
  /*premier bit de suivant*/
  uint8_t bit_suiv = 0;
  /*condition pour byte_stuffing*/
  uint8_t res;
  if (discard_byte_stuffing && (stream->indice_bit == 0)) byte_stuffing(stream);
  /*cas: fin fichier*/
  if (stream->end_of_file) res = read_bit_end_file(stream, dest, bit);

  /*cas: not fin fichier*/
  else{
      /* cas :le suivant n'est pas vide*/
      if (stream->nb_elt_suiv!=0) res = read_bit_not_empty(stream, dest, bit, bit_suiv);
      /*cas suivant est vide*/
      else res = read_bit_empty(stream, dest, bit, bit_suiv);
  }
  return res;
}


/*###########################################READ-BITSTREAM##############################################*/

uint8_t read_bitstream(struct bitstream* stream, uint8_t bits, uint32_t *dest, bool discard_byte_stuffing){
 uint8_t bit = 0;
 *dest = 0;
for(uint8_t bit_rest = 0; bit_rest < bits; bit_rest++){
       bit += read_bit(stream, dest, discard_byte_stuffing);
}
   return bit;
}

/*###########################################RESTE-DES-FONCTIONS##############################################*/

bool end_of_bitstream(struct bitstream *stream){
  if(stream->end_of_file && (stream->nb_elt_suiv==0)){
    return true;
  }
  return false;
}


void close_bitstream(struct bitstream *stream){
    fclose(stream->fichier);
    free(stream);
}
