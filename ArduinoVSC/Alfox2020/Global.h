/*
 *  Global.h
 *
 *  Created on: Mar 4, 2020
 *  Author: snir2
 */

#ifndef GLOBAL_H
#define GLOBAL_H

typedef enum {
	GPS4 = 0x00, PARKING = 0x01, DISTANCE = 0x02
} Mode;

struct Position {
	float latitude;				// en degré
	float longitude;			// en degré
	float speed;				// en km/h
	int nbSatellites;
	unsigned long epochTime;	// temps en secondes depuis le 1/1/1970
};

#endif /* GLOBAL_H */
