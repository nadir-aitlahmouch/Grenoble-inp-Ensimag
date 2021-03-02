#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <stdint.h>
#include <netinet/in.h>
#include <stdbool.h>
#include "../include/bitstream.h"
#include "../include/huffman.h"
#include "../include/jpeg_reader.h"

/*#################################################STRUCTURE-JPEG#########################################*/

struct jpeg_desc{

    char *filename;
    struct bitstream* stream;
    bool fin;


    uint8_t nb_q_tab;
    uint8_t **tables_quantifications;
    uint16_t hauteur;
    uint16_t largeur;
    uint8_t nombre_comp;

    /*information Y*/
    uint8_t id_Y;
    uint8_t sampv_y;
    uint8_t samph_y;
    uint8_t iQ_y;

    /*information Cb*/
    uint8_t id_Cb;
    uint8_t sampv_Cb;
    uint8_t samph_Cb;
    uint8_t iQ_Cb;

    /*information Cb*/
    uint8_t id_Cr;
    uint8_t sampv_Cr;
    uint8_t samph_Cr;
    uint8_t iQ_Cr;

    /*Huffman*/
    uint8_t nombre_huff_DC;
    uint8_t nombre_huff_AC;
    struct huff_table **huffman_tables_DC;
    struct huff_table **huffman_tables_AC;

    /* SOS */
    uint8_t scan_i_Y;
    uint8_t scan_i_Cb;
    uint8_t scan_i_Cr;
    uint8_t i_DC_Y;
    uint8_t i_AC_Y;
    uint8_t i_DC_Cb;
    uint8_t i_AC_Cb;
    uint8_t i_DC_Cr;
    uint8_t i_AC_Cr;

    /*Progressif*/

    uint8_t indice_debut;
    uint8_t indice_fin;
    uint8_t A_high;
    uint8_t A_low;
};


/*############################################################################*/


/*Fonction sui lit le marqueur de chaque section*/
uint32_t lire_marqueur(struct bitstream *stream){
  uint32_t marqueur = 0;
  read_bitstream(stream, 16, &marqueur, true);
  return marqueur;
}

/*Test du marqueur du début de fichier*/
void test_debut_fichier(struct bitstream *stream){
  assert(lire_marqueur(stream)==0xffd8 && "ERROR in jpeg_reader.c: Marqueur de début de fichier invalide.");
}



/*Traitement de la section APP*/
void section_APP(struct bitstream *stream){
  assert(lire_marqueur(stream) == 0xffe0 && "ERROR in jpeg_reader.c: Marqueur de début de la section APP invalide.");
  uint32_t longueur =0;
  read_bitstream(stream, 16, &longueur, true);
  uint32_t JFIF1 = 0;
  uint32_t JFIF2 = 0;
  read_bitstream(stream, 4*8, &JFIF1, true);
  read_bitstream(stream, 8, &JFIF2, true);
  /*la valeur de JFIF en hexa*/
  assert(JFIF1 ==0x4a464946 && "ERROR in jpeg_reader.c: Lecture de 'JFIF/0' erronée.");
  assert(JFIF2 ==0x00 && "ERROR in jpeg_reader.c: Lecture de 'JFIF/0' erronée.");
  longueur -= 7;
  uint32_t reste = 0;
  for(uint16_t i = 0; i < longueur; i ++){
    read_bitstream(stream, 8, &reste, true);
  }
}




/*Traitement de la section COM*/
/*Cette fonction retourne le marqueur de DQT*/
uint16_t section_COM(struct bitstream *stream){
  uint32_t marqueur = lire_marqueur(stream);
  if(marqueur == 0xfffe){
    uint32_t longueur = 0;
    read_bitstream(stream, 16, &longueur, true);
    longueur -= 2;
    uint32_t reste = 0;
    for(uint16_t i = 0; i < longueur; i ++){
      read_bitstream(stream, 8, &reste, true);
    }
    return lire_marqueur(stream);
  }
  return marqueur;
}


/*Traitement de la section DQT*/
void section_DQT(struct jpeg_desc* jdesc, struct bitstream *stream){
   uint32_t longueur = 0;
   read_bitstream(stream, 16, &longueur, true);
   uint8_t nbr_tables = (longueur - 2) / 65;
   assert((longueur - 2) % 65 == 0 && "ERROR in jpeg_reader.c: Erreur dans la lecture de la longueur de la section DQT.");
   uint32_t precision = 0;
   uint32_t iQ = 0;
   for(uint8_t i = 0; i < nbr_tables; i++){
     read_bitstream(stream, 4, &precision, true);
     uint8_t *qtable = malloc(64*sizeof(uint8_t));
     read_bitstream(stream, 4, &iQ, true);
     for (uint8_t i=0; i<64; i++) {
       uint32_t valeur_dans_qtable;
       read_bitstream(stream, 8, &valeur_dans_qtable, true);
       qtable[i] = valeur_dans_qtable;
     }
     jdesc->tables_quantifications[iQ] = qtable;
     jdesc->nb_q_tab ++;
   }
 }


 /*Traitement de la section SOF*/
void section_SOF(struct jpeg_desc *jdesc, struct bitstream* stream){
  uint32_t longueur = 0;
  read_bitstream(stream, 16, &longueur, true);
  uint32_t precision;
  read_bitstream(stream, 8, &precision, true);
  assert(precision == 8 && "ERROR in jpeg_reader.c: Précision erronée.");

  uint32_t valeur = 0;
  read_bitstream(stream, 16, &valeur, true);
  jdesc->hauteur = (uint16_t)valeur;
  read_bitstream(stream, 16, &valeur, true);
  jdesc->largeur = (uint16_t)valeur;
  read_bitstream(stream, 8, &valeur, true);
  jdesc->nombre_comp = (uint8_t)valeur;

  /*On remplit avec les informations de Y*/
  uint32_t valeur_y = 0;
  read_bitstream(stream, 8, &valeur_y, true);
  jdesc->id_Y = (uint8_t) valeur_y;
  read_bitstream(stream, 4, &valeur_y, true);
  jdesc->samph_y = (uint8_t) valeur_y;
  read_bitstream(stream, 4, &valeur_y, true);
  jdesc->sampv_y = (uint8_t)valeur_y;
  read_bitstream(stream, 8, &valeur_y, true);
  jdesc->iQ_y = (uint8_t) valeur_y;
  if (jdesc->nombre_comp == 3){
    /*On remplit avec les informations de Cb*/
    uint32_t valeur_Cb = 0;
    read_bitstream(stream, 8, &valeur_Cb, true);
    jdesc->id_Cb = (uint8_t) valeur_Cb;
    read_bitstream(stream, 4, &valeur_Cb, true);
    jdesc->samph_Cb = (uint8_t) valeur_Cb;
    read_bitstream(stream, 4, &valeur_Cb, true);
    jdesc->sampv_Cb = (uint8_t)valeur_Cb;
    read_bitstream(stream, 8, &valeur_Cb, true);
    jdesc->iQ_Cb = (uint8_t) valeur_Cb;

    /*On remplit avec les informations de Cr*/
    uint32_t valeur_Cr = 0;
    read_bitstream(stream, 8, &valeur_Cr, true);
    jdesc->id_Cr = (uint8_t) valeur_Cr;
    read_bitstream(stream, 4, &valeur_Cr, true);
    jdesc->samph_Cr = (uint8_t) valeur_Cr;
    read_bitstream(stream, 4, &valeur_Cr, true);
    jdesc->sampv_Cr = (uint8_t)valeur_Cr;
    read_bitstream(stream, 8, &valeur_Cr, true);
    jdesc->iQ_Cr = (uint8_t) valeur_Cr;

  }
}


/*Traitement de la section DHT*/
void section_DHT( struct jpeg_desc* jdesc, struct bitstream* stream){
  uint32_t entier;
  read_bitstream(stream, 16, &entier, true);
  uint32_t longueur = entier - 2;
  uint32_t compteur = 0;
  uint16_t indice, type;

  while (compteur < longueur){
    read_bitstream(stream, 3, &entier, true);
    assert(entier == 0 && "ERROR in jpeg_reader.c: Suite 000 non trouvée.");

    read_bitstream(stream, 1, &entier, true);
    type = entier;

    read_bitstream(stream, 4, &entier, true);
    indice = entier;

    uint16_t octets_lus = 0;
    struct huff_table *h_table = load_huffman_table(stream, &octets_lus);


    compteur += octets_lus + 1;

    /* table DC */
    if (type == 0) {
      jdesc->huffman_tables_DC[indice] = h_table;
      jdesc->nombre_huff_DC++;
    } else {
      /* table AC */
      jdesc->huffman_tables_AC[indice] = h_table;
      jdesc->nombre_huff_AC++;
    }
  }
}


/*Traitement de la section SOS*/
void section_SOS( struct jpeg_desc* jdesc, struct bitstream* stream){
  uint32_t longueur = 0;
  read_bitstream(stream, 16, &longueur, true);
  uint32_t nb_comp = 0;
  read_bitstream(stream, 8, &nb_comp, true);
  assert ((uint8_t) nb_comp == jdesc->nombre_comp && "ERROR in jpeg_reader.c: Nombre de composantes erroné.");

  uint32_t id_comp;
  for (uint8_t i = 0 ; i< jdesc->nombre_comp; i++) {
    read_bitstream(stream, 8, &id_comp, true);

    if ((uint8_t) id_comp == jdesc->id_Y) {
      uint32_t valeur = 0;
      jdesc->scan_i_Y = i;
      read_bitstream(stream, 4, &valeur, true);
      jdesc->i_DC_Y = (uint8_t) valeur;
      read_bitstream(stream, 4, &valeur, true);
      jdesc->i_AC_Y = (uint8_t)valeur;
    } else if (jdesc->nombre_comp == 3 && ((uint8_t) id_comp == jdesc->id_Cb)) {
      uint32_t valeur = 0;
      jdesc->scan_i_Cb = i;
      read_bitstream(stream, 4, &valeur, true);
      jdesc->i_DC_Cb = (uint8_t) valeur;
      read_bitstream(stream, 4, &valeur, true);
      jdesc->i_AC_Cb = (uint8_t)valeur;
    } else if (jdesc->nombre_comp == 3 && ((uint8_t) id_comp == jdesc->id_Cr)) {
      uint32_t valeur = 0;
      jdesc->scan_i_Cr = i;
      read_bitstream(stream, 4, &valeur, true);
      jdesc->i_DC_Cr = (uint8_t) valeur;
      read_bitstream(stream, 4, &valeur, true);
      jdesc->i_AC_Cr = (uint8_t)valeur;
    } else {
      printf("Scan faux\n");
    }
  }
  /* Amène le flux au debut des donnees brutes */
  uint32_t indice_debut = 0;
  uint32_t indice_fin = 0;
  read_bitstream(stream, 8, &indice_debut, true);
  read_bitstream(stream, 8, &indice_fin, true);
  uint32_t A_high = 0;
  uint32_t A_low = 0;
  read_bitstream(stream, 4, &A_high, true);
  read_bitstream(stream, 4, &A_low, true);

  jdesc->indice_debut = indice_debut;
  jdesc->indice_fin = indice_fin;
  jdesc->A_high = A_high;
  jdesc->A_low = A_low;

  // jdesc->fin = true;
}


/* Fonction qui initialise une structure jpeg*/
struct  jpeg_desc *initialise_jdesc(const char *filename, struct bitstream* stream) {
  struct  jpeg_desc *jdesc = malloc(sizeof(struct  jpeg_desc));
  uint8_t **qtables = malloc(4*sizeof(uint8_t *));
  jdesc->tables_quantifications = qtables;
  for(uint8_t i = 0; i < 4; i ++){
    jdesc->tables_quantifications[i] = NULL;
  }
  jdesc->nb_q_tab = 0;
  struct huff_table** huffman_tables_DC = malloc(4*sizeof(struct huff_table *));
  struct huff_table** huffman_tables_AC = malloc(4*sizeof(struct huff_table *));
  jdesc->huffman_tables_DC = huffman_tables_DC;
  for(uint8_t i = 0; i < 4; i ++){
    jdesc->huffman_tables_DC[i] = NULL;
  }
  jdesc->huffman_tables_AC = huffman_tables_AC;
  for(uint8_t i = 0; i < 4; i ++){
    jdesc->huffman_tables_AC[i] = NULL;
  }
  jdesc->nombre_huff_DC = 0;
  jdesc->nombre_huff_AC = 0;
  jdesc->filename = (char*)filename;
  jdesc->stream = stream;
  jdesc->fin = false;
  return jdesc;
}

/*#################################################READ-JPEG###################################################*/

struct  jpeg_desc *read_jpeg(const char *filename) {
  struct bitstream *stream = create_bitstream(filename);
  struct  jpeg_desc *jdesc = initialise_jdesc(filename, stream);
  test_debut_fichier(stream);
  section_APP(stream);
  uint32_t marqueur_dqt = section_COM(stream);
  assert(marqueur_dqt==0xffdb && "ERROR in jpeg_reader.c: Marqueur de la section_COM erroné.");
  section_DQT(jdesc,stream);

  uint32_t marqueur;
  while (!jdesc->fin) {
    marqueur = lire_marqueur(stream);
    switch (marqueur) {
      case 0xffdb:
        section_DQT(jdesc, stream);
        break;
      case 0xffc0: case 0xffc2:
        section_SOF(jdesc, stream);
        break;
      case 0xffc4:
        section_DHT(jdesc, stream);
        break;
      case 0xffda:
        section_SOS(jdesc, stream);
        break;
        case 0xffd9:
        jdesc->fin = true;
      default:
        printf("Marqueur %x inexistant !\n", marqueur);
        exit(1);
    }
  }
  return jdesc;
}


/*#######################################FONCTIONS-AUXILIAIRES-DU-MODULE###########################################*/

struct bitstream *get_bitstream(const struct  jpeg_desc *jdesc) {
  return jdesc->stream;
}

char *get_filename(const struct  jpeg_desc *jdesc) {
  return jdesc->filename;
}

void  close_jpeg(struct  jpeg_desc *jdesc) {
  close_bitstream(jdesc->stream);
  for(uint8_t i = 0; i < 4; i ++){
    if (jdesc->tables_quantifications[i] != NULL) free(jdesc->tables_quantifications[i]);
  }
  free(jdesc->tables_quantifications);
  for(uint8_t i = 0; i < 4; i ++){
    if (jdesc->huffman_tables_DC[i] != NULL) free_huffman_table(jdesc->huffman_tables_DC[i]);
    if (jdesc->huffman_tables_AC[i] != NULL) free_huffman_table(jdesc->huffman_tables_AC[i]);
  }
  free(jdesc->huffman_tables_DC);
  free(jdesc->huffman_tables_AC);
  free(jdesc);
}


uint8_t  get_nb_quantization_tables(const struct  jpeg_desc *jdesc) {
  return jdesc->nb_q_tab;
}



uint8_t *get_quantization_table (const struct  jpeg_desc *jdesc, uint8_t index ) {
  uint8_t *table = jdesc->tables_quantifications[index];
  return table;
}


uint8_t  get_nb_huffman_tables(const struct  jpeg_desc *jdesc, enum acdc acdc) {
  if (acdc == AC) {
    return jdesc->nombre_huff_AC;
  }
  return jdesc->nombre_huff_DC;
}


struct huff_table *get_huffman_table(const struct  jpeg_desc *jdesc, enum acdc acdc, uint8_t index ) {
  if (acdc == AC) {
    return jdesc->huffman_tables_AC[index];
  }
  return jdesc->huffman_tables_DC[index];
}


uint16_t  get_image_size(struct  jpeg_desc *jdesc, enum direction dir) {
  if (dir == DIR_V) {
    return jdesc->hauteur;
  }
  return jdesc->largeur;
}



uint8_t get_nb_components(const struct  jpeg_desc *jdesc) {
  return jdesc->nombre_comp;
}


uint8_t  get_frame_component_id(const struct  jpeg_desc *jdesc, uint8_t frame_comp_index ) {
  if (frame_comp_index == 0) {
    return jdesc->id_Y;
  } else if (frame_comp_index == 1) {
    return jdesc->id_Cb;
  }
  return jdesc->id_Cr;
}



uint8_t  get_frame_component_sampling_factor(const struct  jpeg_desc *jdesc, enum direction dir, uint8_t frame_comp_index){
  uint8_t samp_f = 0;
  if (frame_comp_index == 0) {
    if (dir == DIR_V) {
    samp_f = jdesc->sampv_y;
  } else {
    samp_f = jdesc->samph_y;
  }
    return samp_f;
  } else if (frame_comp_index == 1) {
    if (dir == DIR_V) {
    samp_f = jdesc->sampv_Cb;
  } else {
    samp_f = jdesc->samph_Cb;
  }
    return samp_f;
  }

  if (dir == DIR_V) {
  samp_f = jdesc->sampv_Cr;
} else {
  samp_f = jdesc->samph_Cr;
}
  return samp_f;
}


uint8_t  get_frame_component_quant_index(const struct  jpeg_desc *jdesc, uint8_t frame_comp_index){
  if (frame_comp_index == 0) {
    return jdesc->iQ_y;
  } else if (frame_comp_index == 1) {
    return jdesc->iQ_Cb;
  }
  return jdesc->iQ_Cr;
}


uint8_t get_scan_component_id(const struct  jpeg_desc *jdesc, uint8_t scan_comp_index) {
  if (scan_comp_index == jdesc->scan_i_Y) {
    return jdesc->id_Y;
  } else if (scan_comp_index == jdesc->scan_i_Cb) {
    return jdesc->id_Cb;
  }
  return jdesc->id_Cr;
}


uint8_t  get_scan_component_huffman_index(const struct jpeg_desc *jdesc, enum acdc acdc, uint8_t scan_comp_index) {
  uint8_t huff_index;
  if (scan_comp_index == jdesc->scan_i_Y) {
    if (acdc == AC){
       huff_index = jdesc->i_AC_Y; }
       else {
          huff_index = jdesc->i_DC_Y;}
    return huff_index;
  } else if (scan_comp_index == jdesc->scan_i_Cb) {
    if (acdc == AC){
       huff_index = jdesc->i_AC_Cb; }
       else {
          huff_index = jdesc->i_DC_Cb;}
    return huff_index;
  }
  if (acdc == AC){
     huff_index = jdesc->i_AC_Cr; }
     else {
        huff_index = jdesc->i_DC_Cr;}
  return huff_index;
}
