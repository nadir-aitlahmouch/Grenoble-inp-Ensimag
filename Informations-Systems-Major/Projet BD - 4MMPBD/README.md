# Gestionnaire de spectacles

## Pour commencer

### Pré-requis

 *  Java 8 >=

### Mise en place

Vous pouvez utiliser la base de données de Mehdi Adam via `benkerre` sur `oracle1.ensimag.fr:1521:oracle1`.

Mais si vous souhaitez réaliser la même base de données chez vous, veuillez lire les consignes suivantes :

 - Modifiez le fichier java `ConnectionManagement.java` dans le dossier `src/Transaction`
afin de modifier la variable `CONN_URL` qui pointera vers votre propre base de données Oracle.
(Modifiez aussi les variables `USER` et `PASSWD` en conséquence).

 - Pour avoir le squelette et la peupler en même temps avec des éléments utilisés pour nos tests, exécutez sur votre client SQL favori le fichier SQL `SQL/export.sql`

 - Pour avoir seulement le squelette, exécutez `SQL/expertDBonly.sql`. Ensuite pour la peupler avec nos valeurs executez `exportDataOnly.sql`.
 
## Usage

Pour l'usage, compilez les ressources Java en utilisant le Makefile situé à la racine du projet en exécutant la commande `make all`. 
(Vérifiez qu'un répertoire `bin` soit présent à la racine du projet avant d'éxécuter la commande)

Après avoir compilé une fois, vous pourrez lancer le programme principal avec :
```
java -classpath bin:lib/ojdbc8.jar Interface.MainInterface
```
