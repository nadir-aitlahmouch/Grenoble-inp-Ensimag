#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <math.h>
#include <time.h>
#include "../include/jpeg_reader.h"
#include "../include/bitstream.h"
#include "../include/huffman.h"
#include "../include/treatment.h"
#include "../include/pgm_ppm.h"
#include "../include/extraction.h"
#include "../include/informations.h"
#include "../include/rgb.h"
#include "../include/upsampling.h"




/*Générateur d'images selon si l'image est en noir ou blanc ou en couleurs*/
 uint32_t *generer_image(struct jpeg_desc *jdesc, struct bitstream *stream){
   /*sans couleurs*/
   if (get_nb_components(jdesc) == 1){
     return genere_pgm(jdesc, stream);
   }
   /*avec couleurs*/
   else {
     return genere_rgb(jdesc, stream);
   }
 }


int main(int argc, char **argv)
{
  clock_t start_t, end_t;
  start_t = clock();


  if (argc != 2){
    fprintf(stderr, "Usage: %s fichier.jpeg\n", argv[0]);
    return EXIT_FAILURE;
  }
  /*on recupere le nom du fichier JPEG sur la ligne de commande.*/
  const char *filename = argv[1];
   /*on cree un jpeg_desc qui permettra de lire ce fichier.*/
  struct jpeg_desc *jdesc = read_jpeg(filename);
  /*on recupere le flux de donnees brutes a partir du descripteur.*/
  struct bitstream *stream = get_bitstream(jdesc);
  uint32_t *image = generer_image(jdesc, stream);

  /*On génére l'image*/
  generateur_images_ppm_pgm(image, jdesc);
  /*liberer le tableau finale*/
  free(image);
  close_jpeg(jdesc);

  end_t = clock();
  double total_t = (double)(end_t - start_t) / CLOCKS_PER_SEC;
  printf("Temps total pris par le CPU: %f\n", total_t);


  return 0;
}
