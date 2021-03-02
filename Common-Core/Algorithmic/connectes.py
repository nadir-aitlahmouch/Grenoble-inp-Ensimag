#!/usr/bin/env python3
"""
compute sizes of all connected components.
sort and display.
"""
from time import time
from sys import argv
from collections import defaultdict
from math import sqrt
from itertools import combinations, chain


def load_instance(filename):
    """
    loads .pts file.
    returns distance limit and points.
    """
    with open(filename, "r") as instance_file:
        lines = iter(instance_file)
        distance = float(next(lines))
        dict = {}
        for l in lines:
            point = tuple(float(f) for f in l.split(","))
            dict[point] = [point,]
    return distance, dict


def appartient_au_quadrant(point, quadrant):
    """
    verifie si le point appartient a la bande ou non
    """
    x_min, y_min = quadrant[0]
    x_max, y_max = quadrant[1]
    x, y = point
    return x_min <= x <= x_max and y_min <= y <= y_max


def diviser_pour_regner(points, dict, distance, quadrant):
    """
    Cette fonction établit maintenant un chevauchement entre les quatres quadrants pour supprimer le traitement fait aux points
    critiques comme vu dans les algos précedents. En effet, un point étant par avant dans la marge critique d'un quadrant
    appartient à plus qu'un maintenant, ce qui relie directement directement les connexes appartenant à ces différent quadrants.
    Un schéma plus clair du chevauchement de seuil/2 est montré dans le rapport.
    """
    x_min, y_min = quadrant[0]
    x_max, y_max = quadrant[1]
    cote_quadrant = x_max - x_min
    """
    Comme condition d'arrêt dans cet algo, on privéligie le nombre de points dans un quadrant pour stopper l'exécution.
    La précedent cdt d'arrêt n'avait de sens que dans le cas où les marges critiques étaient existantes.
    """
    if len(points) <= 20:
        force_brute(points, dict, distance*distance)
    elif cote_quadrant <= 4*distance:
        if len(points) >= 200 and tous_connectes(points, distance, cote_quadrant):
            connecter_tous(points, dict)
        else:
            force_brute(points, dict, distance*distance)
    else:
        x_centre, y_centre = (x_min+x_max)/2, (y_min+y_max)/2

        quadrants = [[[x_min,y_min],                [x_centre+distance/2, y_centre+distance/2]],
                     [[x_centre-distance/2, y_min], [x_max, y_centre+distance/2]],
                     [[x_min, y_centre-distance/2], [x_centre+distance/2, y_max]],
                     [[x_centre-distance/2, y_centre-distance/2], [x_max, y_max]]]

        for indice, quadrant in enumerate(quadrants):
            sub_points = []
            for point in points:
                if appartient_au_quadrant(point, quadrant):
                    sub_points.append(point)
            diviser_pour_regner(sub_points, dict, distance, quadrant)


def connecter_tous(points, dict):
    """ connecte tous les points entre eux """
    pt1 = next(iter(points))
    for pt2 in points:
        set_pt1, set_pt2 = dict[pt1], dict[pt2]
        if set_pt1 is not set_pt2:
            fusionner(set_pt1, set_pt2, dict)


def tous_connectes(points, distance, cote_quadrant):
    """
    reduit le probleme a des cases au lieu des points pour n'importe quels points dans deux cases adjacentes
    proches
    """
    reduction = set()
    cote_case = cote_quadrant*sqrt(5)/distance
    for point in points:
        x, y = point
        case_point = (x//cote_case, y//cote_case)
        reduction.add(case_point)
    return one_component(reduction)


def one_component(indices):
    """ indices est une iterable sur des couples (i, j) """
    depart = indices.pop()
    pile = [depart,]
    while pile:
        i, j = pile.pop()
        for voisin in [(i,j+1), (i,j-1), (i+1,j), (i-1,j)]:
            if voisin in indices:
                indices.remove(voisin)
                pile.append(voisin)
    return len(indices) == 0


def force_brute(points, dict, distance2):
    """
    focntion qui met à jour l'ensemble des composantes selon si la distance entre deux points
    est plus petite que le seuil ou pas.
    """
    for pt1, pt2 in combinations(points, 2):
        x1, y1 = pt1
        x2, y2 = pt2
        if (x2-x1)**2+(y2-y1)**2 <= distance2:
            set_pt1, set_pt2 = dict[pt1], dict[pt2]
            if set_pt1 is not set_pt2:
                fusionner(set_pt1, set_pt2, dict)


def fusionner(set_pt1, set_pt2, dict):
    """
    Fonction updatant (mélangeant) chaque connexe dans l'autre pour en ressortir les composantes finales.
    """
    if len(set_pt2) > len(set_pt1):
        set_pt1, set_pt2 = set_pt2, set_pt1
    set_pt1.extend(set_pt2)
    for pt in set_pt2:
        dict[pt] = set_pt1


def main():
    """
    ne pas modifier: on charge une instance et on affiche les tailles
    """
    for instance in argv[1:]:
        debut = time()

        distance, dict = load_instance(instance)
        quadrant = [[0.0, 0.0], [1.0, 1.0]]
        points = dict.keys()

        diviser_pour_regner(points, dict, distance, quadrant)

        composantes = {}
        for point in dict:
            composante = dict[point]
            composantes[id(composante)] = len(composante)
        print(sorted(composantes.values(),reverse = True))

main()
