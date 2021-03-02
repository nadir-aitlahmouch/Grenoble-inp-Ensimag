# Repertoires du projet

BIN_DIR = bin
SRC_DIR = src
INC_DIR = include
OBJ_DIR = obj
OBJPROF_DIR = obj-prof

# Options de compilation/édition des liens

CC = clang
LD = clang
INC = -I$(INC_DIR)

CFLAGS += $(INC) -Wall -std=c99 -O3 -g  -Wextra
LDFLAGS = -lm



# Liste des fichiers objet

# les notres...
OBJPROF_FILES =

# et les votres!
OBJ_FILES = $(OBJ_DIR)/huffman.o $(OBJ_DIR)/jpeg_reader.o $(OBJ_DIR)/jpeg2ppm.o $(OBJ_DIR)/treatment.o $(OBJ_DIR)/pgm_ppm.o $(OBJ_DIR)/extraction.o $(OBJ_DIR)/informations.o $(OBJ_DIR)/rgb.o $(OBJ_DIR)/upsampling.o  $(OBJ_DIR)/bitstream.o $(OBJ_DIR)/loeffler.o

# cible par défaut
TARGET = $(BIN_DIR)/jpeg2ppm

all: $(TARGET)
	echo -e "\e[44mSUCCESS COMPILATION !\033[0m"


$(TARGET): $(OBJPROF_FILES) $(OBJ_FILES)
	$(LD) $(LDFLAGS) $(OBJPROF_FILES) $(OBJ_FILES) -o $(TARGET)

# %.qlq_chose : tt les fichiers qui terminent par qlq_chose
# $^ : toutes les dépedances possibles
# $@ : cible courante
$(OBJ_DIR)/%.o: $(SRC_DIR)/%.c
	$(CC) $(CFLAGS) -c $^ -o $@


.PHONY: clean

clean:
	rm -f $(TARGET) $(OBJ_FILES)
	rm -f $(TARGET) images/*.ppm images/*.pgm images/Images_tests/*.ppm images/Images_tests/*.pgm
	echo -e "\e[44mSUCCESS CLEAN !\033[0m"
