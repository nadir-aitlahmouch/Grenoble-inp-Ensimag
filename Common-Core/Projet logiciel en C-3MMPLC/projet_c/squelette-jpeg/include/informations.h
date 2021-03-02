#ifndef __INFORMATIONS_H__
#define __INFORMATIONS_H__



uint16_t *blocs_par_components(struct jpeg_desc *jdesc);
uint32_t nb_mcu(struct jpeg_desc *jdesc);
uint32_t nb_bloc_tot(struct jpeg_desc *jdesc);

uint16_t get_size_reelle(struct jpeg_desc *jdesc,enum direction dir);

uint32_t *ordre_components(struct jpeg_desc *jdesc);
// extern void fct_tricroitableau(int tableau[], int tailletableau);

#endif
