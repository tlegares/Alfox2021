/*
 * MKRGPS.h
 *
 *  Created on: Mar 12, 2020
 *      Author: jean-pierredumas
 */

#ifndef MKRGPS_H_
#define MKRGPS_H_

#include <Arduino_MKRGPS.h>
#include "Global.h"

class MKRGPS {
public:
	static bool init(bool debug);	// initialisation du GPS, retourne faux si pb !
	static void standby();			// suspendre le GPS (économie d'énergie)
	static void wakeup();			// réveil du GPS
	static Position getPosition();	// attente active d'une nouvelle valeur (sur la voie série)
	static double distance(Position pos1, Position pos2);	// distance en m entre 2 positions GPS
};

#endif /* MKRGPS_H_ */
