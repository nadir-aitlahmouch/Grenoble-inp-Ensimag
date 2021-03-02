#include <stdlib.h>
#include <stdint.h>
#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <stdint.h>
#include <stdbool.h>
#include "../include/bitstream.h"
#include "../include/huffman.h"
#include "../include/jpeg_reader.h"

/*#############################################STRUCTURE-HUFF_table#######################################*/

struct huff_table {
    uint8_t value;
    // struct huff_table *fils[2];
    struct huff_table *fils_droit;
    struct huff_table *fils_gauche;
    struct huff_table *pere;
    bool still_depth;
};

/*#########################################################################################################*/


/*#############################################LOAD_HUFFMAN_TABLE#########################################*/

/*Fonction qui remplit la table de huffman*/
struct huff_table *load_huffman_table(struct bitstream *stream, uint16_t *nb_byte_read)
{
    /* Tableau avec le nombre de symboles par taille*/
    uint32_t symboles_par_taille[16];
    uint16_t nombre_symboles_total = 0;
    for (uint8_t i = 0; i < 16; i++){
        *nb_byte_read += read_bitstream(stream, 8, &symboles_par_taille[i], false);
        nombre_symboles_total += symboles_par_taille[i];
    }

    /* Tableau des symboles ordonnés*/
    uint32_t symbole[nombre_symboles_total];
    for (uint8_t j = 0; j < nombre_symboles_total; j++) {
        *nb_byte_read += read_bitstream(stream, 8, &symbole[j], false);
    }



    /* On initialise la table qu'on va retourner*/
    struct huff_table *h_table = malloc(sizeof(struct huff_table));
    h_table->pere = NULL;
    h_table->fils_gauche = NULL;
    h_table->fils_droit = NULL;
    h_table->still_depth = true;



    uint8_t indice_value = 0;
    uint8_t parcouru = 0;
    /* On remplit la table selon une structure arborescente*/
    for (uint8_t longueur = 1 ; longueur < 17; longueur++){
        uint8_t profondeur_arbre = longueur - parcouru;
        for (uint8_t j = 0; j < symboles_par_taille[longueur - 1]; j++, indice_value++, profondeur_arbre++, parcouru--){
            while (profondeur_arbre > 0) {
              /* Dans le cas où les deux fils sont non nuls (remplis) on revient vers le père pour remplir un autre côté de l'arbre*/
               if (h_table->fils_gauche != NULL && h_table->fils_droit != NULL){
                    h_table = h_table->pere;
                    profondeur_arbre++;parcouru--;
                } else {
                  /* Sinon on descend vers les fils pour les remplir*/
                  if (h_table->fils_gauche == NULL) {
                    struct huff_table *h_table_gauche = malloc(sizeof(struct huff_table));
                                h_table_gauche->pere = h_table;
                                h_table_gauche->fils_gauche = NULL;
                                h_table_gauche->fils_droit = NULL;
                                h_table_gauche->still_depth = true;


                      h_table->fils_gauche = h_table_gauche;
                      h_table = h_table->fils_gauche;
                      profondeur_arbre--; parcouru++;
                  } else if (h_table->fils_droit == NULL) {
                    struct huff_table *h_table_droit = malloc(sizeof(struct huff_table));
                                h_table_droit->pere = h_table;
                                h_table_droit->fils_gauche = NULL;
                                h_table_droit->fils_droit = NULL;
                                h_table_droit->still_depth = true;


                      h_table->fils_droit = h_table_droit;
                      h_table = h_table->fils_droit;
                      profondeur_arbre--;parcouru++;
                  }
                }
            }
            /*Une fois sorti de la boucle, le point le plus est atteint => still_depth devient false*/
            h_table->still_depth = false;
            /*On affecte la valeur ainsi au champ réservé dans la structure*/
            h_table->value = symbole[indice_value];
            h_table = h_table->pere;
        }
    }
    /*POINT TRES IMPORTANT : On revient vers la racine !*/
    while (h_table->pere != NULL) {
        h_table = h_table->pere;
    }
    return h_table;
}

/*#########################################################################################################*/


/*#########################################RESTE_DES_FONCTIONS##############################################*/

/*Retourne la prochaine valeur atteinte en parcourant la table de Huffman table
         selon les bits extraits du flux stream*/
int8_t next_huffman_value(struct huff_table *table, struct bitstream *stream)
{
    while (table->still_depth == true) {
        uint32_t bit;
        read_bitstream(stream, 1, &bit, true);
        if (bit == 0){
          assert(table->fils_gauche != NULL && "ERROR in huffman.c: Invalid huffman sequence in bitstream.");
          table = table->fils_gauche;
        }
        else {
        assert(table->fils_droit != NULL && "ERROR in huffman.c: Invalid huffman sequence in bitstream.");
        table = table->fils_droit;
      }
    }
    int8_t value = table->value;
    while (table->pere != NULL) {
        table = table->pere;
    }
    return value;
}


/* Sert à désallouer une table de Huffman (récursivement) */
void free_huffman_table(struct huff_table *table)
{
    if (table != NULL) {
        if (table->still_depth == true) {
            free_huffman_table(table->fils_gauche);
            free_huffman_table(table->fils_droit);
        }
        // free_huffman_table(table->pere);
        free(table);
    }
}
