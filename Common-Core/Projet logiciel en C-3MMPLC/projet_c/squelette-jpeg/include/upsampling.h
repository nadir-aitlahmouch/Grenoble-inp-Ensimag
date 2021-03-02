#ifndef __UPSAMPLING_H__
#define __UPSAMPLING_H__




extern int16_t **upsampling_h(int16_t *tableau, uint8_t fact_h);

extern int16_t **upsampling_v(int16_t *tableau, uint8_t fact_v);

extern uint8_t **type_echantillonage(struct jpeg_desc *jdesc);

extern void upsampling(struct jpeg_desc *jdesc, struct mcu *my_mcu);



#endif
