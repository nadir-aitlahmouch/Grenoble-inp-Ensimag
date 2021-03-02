#!/usr/bin/env python3
import sys

if len(sys.argv) != 3:
    print("Usage : ./traitement_blabla f1.txt f2.txt")
else:
    fichier = open(sys.argv[1])
    bytes1 = fichier.readline()
    print("Nombre d'octets du premier : ", len(bytes1))

    fichier2 = open(sys.argv[2])
    bytes2 = fichier2.readline()
    print("Nombre d'octets du deuxième : ", len(bytes2))

    if len(bytes1) == len(bytes2):
        c = 0
        for byte1, byte2 in zip(bytes1, bytes2):
            if byte1 != byte2:
                c += 1
        print((1-c/len(bytes1))*100, "% Match")
