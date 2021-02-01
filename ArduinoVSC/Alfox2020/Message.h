/*
 * File:   Message.h
 * Author: snir2
 *
 * Created on Mar 4, 2020
 */

#ifndef MESSAGE_H
#define MESSAGE_H

#include <Arduino.h>
#include "Global.h"

class Message {
public:
	static byte* gps4(bool pbGPS, int vitesseMoy, Position* p);
	static byte* parking(bool pbGPS, Position pos, long int distance, int vitesseMax);
};

#endif /* MESSAGE_H */

