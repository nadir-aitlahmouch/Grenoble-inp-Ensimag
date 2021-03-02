#!/usr/bin/env python3
import sys


if len(sys.argv) != 2:
    print("Usage : ./traitement_hexdump mon_hex.txt")
else:
    fichier = open(sys.argv[1])
    nv_lignes = []
    for ligne in fichier.readlines():
        nv_ligne = ligne[10:10+23]+ligne[10+24:10+48]
        nv_lignes.append(nv_ligne)
    print(' '.join(nv_lignes))
