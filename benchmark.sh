#!/bin/bash

#  valeurs de NbTuples pour exécuter le benchmark
listeNbTuples=(100 130 180 210 230 250 280 320 340 370 400 450)

# nombre de réseaux que à générer pour chaque NbTuples
nbRes=30

# Parcourir la liste des NbTuples et exécutele code Java pour chaque valeur
for i in "${listeNbTuples[@]}"
do
  for nb in $(seq 1 $nbRes)
  do
    ./urbcsp 10 15 10 $i 3 > csp$i.txt
  done
done
