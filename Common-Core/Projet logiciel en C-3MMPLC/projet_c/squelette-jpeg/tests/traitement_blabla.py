#!/usr/bin/env python3
import sys

if len(sys.argv) != 2:
    print("Usage : ./traitement_blabla mon_blabla.txt")
else:
    fichier = open(sys.argv[1])
    for ligne in fichier.readlines():    
        if ligne[:8] == "[   mcu]":
            tmp = ligne[9:]
            print(tmp[:len(tmp)-1], end='')
