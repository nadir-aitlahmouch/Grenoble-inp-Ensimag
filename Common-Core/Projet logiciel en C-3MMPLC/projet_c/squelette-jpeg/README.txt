#####################
Projet Décodeur JPEG
Equipe : Nadir Ait Lahmouch - Younes Zaïbila - Omar Benchekroun
#####################

Bienvenue dans un petit tour de visite de notre décodeur JPEG baseline ! 

---------------------
Sommaire
---------------------
I-) Modules
II-) Tests supplémentaires et résultats
III-) Scripts et Makefile


---------------------
I-) Modules
---------------------
Les modules écrits sont dans le dossier src/, en plus des modules des profs, on a rédigé plusieurs selon la fonction désirée:
extraction.c : extraire une MCU à partir du stream (module qui a pris le plus de temps).
informations.c : les informations dont on aura besoin dans le décodage (ex: nombre de MCUs dans l'image)
jpeg2ppm.c : notre main() ainsi que la fonction qui génére l'image selon si elle est en N&B ou en 		    couleurs.
loeffler.c : implémentation de l'algo de loeffler (iDCT rapide)
pgm_ppm.c : écriture des fichiers pgm et ppm
rgb.c : conversion rgb et al fonction qui génére une image couleur.
treatment.c : fonctions d'ordre général pour le traitement des MCUs.
upsampling.c : Ah!


Les modules ont été implémentés en assurant une gestion optimale de la mémoire (tous les allocs ont été libérés) ainsi qu'une rapidité de l'exécution du programme (1,9s pour biiiiig.jpg). On a également essayé de gérer le maximum de cas quant à l'upsampling (voir section II-Tests).



--------------------------------------
II-) Tests supplémentaires et résultats
--------------------------------------
D'abord, on se félicite d'avoir réussi le décodage de toutes les images données dans sujet.
Wouah!

De plus, on a généré à l'aide de cjpeg quelques images pour tester le upsampling:
-- apres_upsampling_ixj_kxl_oxp.jpg : images pour notre upsampling dans les cas les plus marginaux.
-- SLJ_3x2_1x2_1x1.jpg: Cas d'upsampling un peu dur mais qui marche quand même, ce cas n'est pris en compte par l'executable jpeg2blabla.
-- progressive.jpg : Pour s'assurer que notre code ne décode pas les images progressives.
                     Réponse attendue: "We don't do progressive images in here".

-- notre_image.jpg : Nous tout contents après avoir réussi l'upsampling (échantillonnage : 2x2,1x1,1x1)
--------------------------------------
III-) Scripts et Makefile
--------------------------------------
Le Makefile utilise automatiquement les modules qu'on a implémentés. On supprime également les .pgm et .ppm générés dans make clean.

make : pour compiler tout le projet.
make clean: pour supprimer les images générées ainsi que les ".o".

Les scripts se trouvent dans le dossier tests/.
-- autotest.sh : teste l'executable sur toutes les images.

-- traitement_hexdump.py : prend comme argument le hexdump -C d'une image (mis dans un fichier txt), et fait le traitement pour en ressortir la suite des hexadécimaux.
-- traitement_blabla.py : de même pour les fichiers .blabla
-- comparateur_octet : compare byte par byte deux fichiers, quand on compare les ppm générés par notre décodeur et jpeg2blabla, on a 98% de bytes identiques, les 2% sont dus à des erreurs d'arrondi dans le calcul idct.
-- comparateur_bits.py : de même pour les bits (utilisé dans le déboggage de bitstream).


#############################################################################################
#############################################################################################

Le dossier progressive contient notre avancement dans le mode progressive.
Tout est expliqué dans le fichier pdf "Notre_avancement_dans_le_proressif.pdf".


N-B: Pour tester la rapidité du code, nous avons effectué plusieurs tests sur des images de taille significative, notamment une de taille 10mb, le temps d'execution est de : 4.54s. 
On n'a pas pu inclure cette image dans le rendu vu sa taille.
temps d'execution complexite.jpg : 0.28s
temps d'execution biiiiiig.jpg : 1.9s

FIN.



