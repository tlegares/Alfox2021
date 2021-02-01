/*
 * CommSigfox.cpp
 *
 *  Created on: Mar 13, 2020
 *      Author: jean-pierredumas
 *
 *  Pour l'instant on travaille toujours avec l'émetteur Sigfox en mode debug
 *  Permet d'obsever la led pendant l'émission d'un message
 */

#include "Global.h"
#include "CommSigfox.h"
#include "MKRGPS.h"

bool debug = true;
bool send = true;

void printHex(byte* buf);

// init et test de présence du module Sigfox
void CommSigfox::init(bool debugMode, bool sendMode) {
	if (!SigFox.begin()) {
		Serial.println("Shield error or not present!");
		return;
	}
	debug = debugMode;
	send = sendMode;
	if (debugMode)	// on le garde pour l'instant en mode debug tout le temps
		Serial.println("Emetteur en mode Debug");
	SigFox.debug();		// Enable debug led and disable automatic deep sleep
	// SigFox.end();	// Send the module to the deepest sleep
}

// --------------------- Donne l'ID et le PAC SIGFOX ---------------------
void CommSigfox::printID() {
	if (debug) {
		String version = SigFox.SigVersion();
		String ID = SigFox.ID();
		String PAC = SigFox.PAC();
		// ------------- Display module informations ---------------
		Serial.println("MKRFox1200 Sigfox first configuration");
		Serial.println("SigFox FW version " + version);
		Serial.println("ID  = " + ID);
		Serial.println("PAC = " + PAC);
		delay(100);
	}
}

// --------------------- Envoi d'un message de 12 octets ---------------------
void CommSigfox::sendBytes(byte* tByte) {
	int ret = 0;
	if (send) {
		SigFox.begin();		// Start the module
		delay(100);    		// Wait at least 30mS after first configuration (100mS before)
		SigFox.status();	// Clears all pending interrupts
		delay(1);

		SigFox.beginPacket();
		SigFox.write(tByte, 12);
		int ret = SigFox.endPacket();  // send buffer to SIGFOX network
		SigFox.end();		// Send the module to the deepest sleep
	}
	if (debug) {
		if (ret > 0)
			Serial.println("No transmission");
		else {
			printHex(tByte);
			Serial.println("Transmission ok");
		}
	}
}

// --------------------- Envoi d'un message GP4 ---------------------
void CommSigfox::sendGPS4(bool pbGPS, int vitesseMoy, Position* p) {
	// envoi de 4 positions GPS espacées de 3mn
	byte* bMsg = Message::gps4(pbGPS, vitesseMoy, p);
	sendBytes(bMsg);
}

// --------------------- Envoi d'un message PARKING ---------------------
void CommSigfox::sendParking(bool pbGPS, Position pos, long int distance, int vitesseMax) {
	// message de parking avec position GPS, Distance et Vitesse Maxi
	byte* bMsg = Message::parking(pbGPS, pos, distance, vitesseMax);
	// printHex(bMsg);
	sendBytes(bMsg);
}

// affiche le message en hexa
void printHex(byte* buf) {
    for (int i = 0; i < 12; i++) {
        Serial.print(buf[i], HEX);
        Serial.print(" ");
    }
    Serial.println();
}

