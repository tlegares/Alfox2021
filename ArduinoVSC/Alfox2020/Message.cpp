/*
 * File:   Message.cpp
 * Author: snir2 - Projet ALFOX1 2020
 *
 * Created on Mar 4, 2020
 * 	Version 2.0 : Permet de formatter la trame user du message SIGFOX
 *		Message GP4 : Envoi de la vitesse moyenne et des 4 dernières positions GPS
 *			// IM VM L1 L1 G1 G1 L2 G2 L3 G3 L4 G4
 *		Message PARKING : Envoi de la position GPS.
 *			// IM L1 L2 L3 L4 G1 G2 G3 G4 VX 00 00
 *		Message DISTANCE : Envoi de la distance cumulée et de la vitesse maxi.
 *			// IM K1 K2 K3 K4 VX 00 00 00 00 00 00
 *
 * Modif du 10 Juin 2020
 *	Version 2.1 :
 *		Message PARKING : Envoi de la position GPS, de la distance cumulée et
 *			de la vitesse maxi.
 *			// IM L1 L2 L3 G1 G2 G3 K1 K2 K3 K4 VX
 *		Suppression du message DISTANCE.
 */

#include <Arduino.h>
#include "Global.h"
#include "Message.h"

using namespace std;

const float Pi = 3.141592654;
float arrondi(const float x);
unsigned int intConvert(long value);
byte convertDelta(float lNew, float lOld);

// déclaration globale ?
byte bMsg[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

// retourne le message Sigfox contenant dans un tableau de byte : 4 position GPS (toutes les 4 minutes)
// la vitesse moyenne sur l'intervalle et le fonctionnement du GPS
// return : tableau de 12 bytes
byte* Message::gps4(bool pbGPS, int vitesseMoy, Position* p) {
	// IM VM L1 L1 G1 G1 L2 G2 L3 G3 L4 G4
	bMsg[0] = GPS4;    // mode GPS4 (0)
	bMsg[0] |= (pbGPS ? 0x80 : 0);
	bMsg[1] = vitesseMoy;			// vitesse moyenne

	// latitude et longitude ne doivent pas sortir de la zone acceptable (16 bits)
	// sinon on s'arrête aux frontières de la zone
	long liLat = (short)(10000 * (p[3].latitude - 36) / 4); // Position 4 corrigée
	unsigned int uiLat = intConvert(liLat);	// pas d'erreur de conversion possible
	bMsg[2] = (uiLat & 0xFF00) >> 8;
	bMsg[3] = uiLat & 0xFF;

	long liLong = (short)(10000 * (p[3].longitude + 10) / 4); // Position 4 corrigée
	unsigned int uiLng = intConvert(liLong);
	bMsg[4] = (uiLng & 0xFF00) >> 8;
	bMsg[5] = uiLng & 0xFF;

	// on convertit les trois autres positions GPS en valeur relative à la position précédente
	bMsg[6]  = convertDelta(p[2].latitude, p[3].latitude);		// Position 3 - Position 4
	bMsg[7]  = convertDelta(p[2].longitude, p[3].longitude);
	bMsg[8]  = convertDelta(p[1].latitude, p[2].latitude);		// Position 2 - Position 3
	bMsg[9]  = convertDelta(p[1].longitude, p[2].longitude);
	bMsg[10] = convertDelta(p[0].latitude, p[1].latitude);		// Position 1 - Position 2
	bMsg[11] = convertDelta(p[0].longitude, p[1].longitude);
	return bMsg;
}

// retourne le message Sigfox contenant la dernière position connue du véhicule garé
// pendant au moins 16 mn GPS identique ou inconnu et vitesse moyenne ou max nulle
// return : tableau de 12 bytes
byte* Message::parking(bool pbGPS, Position pos, long int distance, int vitesseMax) {
	// IM L1 L2 L3 G1 G2 G3 K1 K2 K3 K4 VX
	bMsg[0] = PARKING;    // mode PARKING (1)
	bMsg[0] |= (pbGPS ? 0x80 : 0);

	// conversion latitude en entier positif décomposé en octets
	long value = 10000*pos.latitude;
	bMsg[1] = (value & 0xFF0000) >> 16;
	bMsg[2] = (value & 0xFF00) >> 8;
	bMsg[3] = (value & 0xFF);

	// conversion longitude en entier positif
	value = 10000*(pos.longitude + 10);
	bMsg[4] = (value & 0xFF0000) >> 16;
	bMsg[5] = (value & 0xFF00) >> 8;
	bMsg[6] = (value);

	// A vérifier
	bMsg[7] = (distance & 0xFF000000) >> 24;
	bMsg[8] = (distance & 0xFF0000) >> 16;
	bMsg[9] = (distance & 0xFF00) >> 8;
	bMsg[10]= (distance & 0xFF);

	bMsg[11]= (byte)vitesseMax;

	return bMsg;
}

// limite la valeur à 16 bits non signés
unsigned int intConvert(long value) {
	if (value > 65535)
		value = 65535;
	else if (value < 0)
		value = 0;
	return (int) value;
}

// On ramène le déplacement sur 4mn à un byte (8 bits)
byte convertDelta(float lNew, float lOld) {
	float l = lNew - lOld + 0.127f;
	// Si la vitesse moyenne est supérieure à 160km/h !!!
	if (l > 0.255f)
		l = 0.255f;
	else if (l < 0.0f)
		l = 0.0f;
	int ret = (byte) arrondi(1000 * l);
	return (byte) ret;
}

// arrondi d'un float à sa valeur la plus proche
float arrondi(const float x) {
	if (0 <= x)
		return floor(x + 0.5);
	else
		return ceil(x - 0.5);
}
