/*
 *  MKRGPS.cpp
 *
 *  Created on: Mar 12, 2020
 *  Author: SNIR2
 *
 *  Pour l'instant on ne met pas en oeuvre le mode standby()
 *  qui perturbe la précision de l'acquisition
 */

#include "MKRGPS.h"

bool debugGPS = false;

void afficherPosition(Position p);

// initialisation du GPS, retourne faux si pb !
bool MKRGPS::init(bool debug) {
	debugGPS = debug;
	// With MKR GPS as shield, use GPS_MODE_SHIELD in GPS.begin(...) for serial1
	if (!GPS.begin(GPS_MODE_SHIELD)) {
		Serial.println("Failed to initialize GPS!");
		return false;
	}
	return true;
}

// suspendre le GPS (économie d'énergie)
void MKRGPS::standby() {
	GPS.standby();
}

// réveil du GPS
void MKRGPS::wakeup() {
	GPS.wakeup();
}

// attente active d'une nouvelle valeur (sur la voie série)
Position MKRGPS::getPosition() {
	Position p;
	// si c'est trop long, çà vz merder, pour l'instant pas d'échapatoire :-)
	while (!GPS.available());	// wait for new GPS data to become available
	// read GPS values
	p.latitude  = GPS.latitude();
	p.longitude= GPS.longitude();
	p.speed = GPS.speed();
	p.epochTime = GPS.getTime();
	p.nbSatellites = GPS.satellites();
	if (debugGPS) {
		//afficherPosition(p);
	}
	return p;
}

void afficherPosition(Position p) {
	Serial.print("latitude : ");
	Serial.println(p.latitude);
	Serial.print("longitude : ");
	Serial.println(p.longitude);
	Serial.print("Speed : ");
	Serial.println(p.speed);
}

// ------------------- distance entre deux position GPS ----------------------

const double Pi = 3.141592654;

// conversion de degré en radian
inline double toRadians(double x) {
    return x / 180 * Pi;
}

// conversion de radian en degré
inline double toDegrees(double x) {
    return x / Pi * 180;
}

// distance en mètres entre deux positions GPS en mètre
double MKRGPS::distance(Position pos1, Position pos2) {
	float lat1 = pos1.latitude;
	float lat2 = pos2.latitude;
	float lon1 = pos1.longitude;
	float lon2 = pos2.longitude;
    if ((lat1 == lat2) && (lon1 == lon2)) {
        return 0;
    }
    else {
        double theta = lon1 - lon2;
        double dist = sin(toRadians(lat1))* sin(toRadians(lat2))
          + cos(toRadians(lat1))* cos(toRadians(lat2))* cos(toRadians(theta));
        dist = acos(dist);
        dist = toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return 1000*dist;
    }
}
