/*
 * CommSigfox.h
 *
 *  Created on: Mar 13, 2020
 *      Author: jean-pierredumas
 */

#ifndef COMMSIGFOX_H_
#define COMMSIGFOX_H_

#include <SigFox.h>
#include "Message.h"

class CommSigfox {
public :
	// init et test de présence du module Sigfox
	static void init(bool debugMode, bool sendMode);
	// affiche sur Serial l'ID et le PAC de la carte Arduino
	static void printID();
	// Envoi d'un message GP4
	static void sendGPS4(bool pbGPS, int vitesseMoy, Position* p);
	// Envoi d'un message PARKING
	static void sendParking(bool pbGPS, Position pos, long int distance, int vitesseMax);
private :
	// Envoi d'un message de 12 octets
	static void sendBytes(byte* tByte);
};

#endif /* COMMSIGFOX_H_ */
