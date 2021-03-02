# Ensimag 2A Projet BD - TP 2018/19
# ============================
#
# Ce Makefile permet de compiler le projet BD sans utiliser d'IDE




all:
	javac -Xlint -d bin -classpath lib/ojdbc8.jar -sourcepath src src/Transaction/*.java
	javac -Xlint -d bin -classpath lib/ojdbc8.jar -sourcepath src src/Interface/*.java
	java -classpath bin:lib/ojdbc8.jar Interface.MainInterface

clean:
	rm -rf bin/*
