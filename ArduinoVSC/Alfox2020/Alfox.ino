
/*
 * File:   Alfox2020.cpp Version 5.0
 * Author: Projet ALFOX1 2020
 * Envoi sur serveur SIGFOX de trames GPS en mode roulage ou parking.
 * Le boitier fait une acquisition GPS toute les 3mn
 * On envoie une trame Sigfox toutes les 12 mn
 * La trame contient donc 4 positions GPS à chaque envoi + la vitesse moyenne sur 12 mn
 * De plus le boitier met à jour le kilométrage parcouru toutes les 5s
 * Ce kilométrage est stocké en EEPROM pour être conservé même si le boitier 
 * n'est plus alimenté.
 * Si le véhicule est considéré comme garé, la trame envoyée contient la position GPS
 * le kilométrage et la vitesseMax depuis le précédent arrêt.
 * 
 * Rmq : pas de standby GPS, car semble poser des pbs de précision.
 *
 * La distance parcourue est conservée en EEPROM et envoyée avec la trame de parking
 * Attention : Si on recharge un nouveau programme l'EEPROM est effacée.
 * 	en tenir compte sur le serveur ALFOX
 *
 * Created on Mar 4, 2020
 * 	Version 3.0 :
 * 		Envoi de messages GP4: 4 positions GPS par msg
 * Modification du 11 Juin 2020
 * 	Version 3.3 :
 * 		Le boitier sera branché sur une prise USB qui s'éteint lorsque l'on coupe le véhicule
 * 		On donne la position GPS à chaque démarrage du véhicule
 * Modification du 19 Juin 2020
 * 	Version 4.0 :
 * 		Distance en EEPROM, plus que 2 types de msgs SIGFOX
 * 		Maintenant un msg toutes les 12 minutes
 * 		Donc une position GPS toutes les 3 mn.
 * 		L'émetteur SIGFOX reste en mode DEBUG (led indique l'envoi)
 * Modification du 3 Juillet 2020
 * 	Version 5.0 :
 * 		Date du dernier msg en EEPROM, au format epoch en secondes
 * 		Duré de la boucle de base qui passe à 5s au lieu de 10s
 * 		Pour voir si çà améliore la précision % compteur de la voiture
 * Modification du 7 Juillet 2020
 * 		Erreur sur le calcul des distances avec 5s il faut diviser par 2 % 10s
 * 		La précision du kilométrage est maintenant de 0.6% avec 5s au lieu de 1,8% avec 10s
 * 		Test fait sur 350km avec mon véhicule.
 */

#include <Arduino.h>
#include "MKRGPS.h"
#include "CommSigfox.h"
#include "FlashStorage.h"

// configuration du programme
// Données globales !
bool DEBUG = false;			// ajout de print sur la voie série
bool SEND = true;			// autorise l'envoi des msgs SIGFOX
int  ERREUR_PARKING = 100;	// il n'a pas fait 100m en 12 mn, si il est à l'arrêt !!!
long int _24H = 86400;		// 24*60*60 (ne rentre pas sur 16 bits)
int DUREE_BOUCLE = 5000;	// 5s
int INC_BOUCLE = 5;			// 5s

long int secondes = 0;
Position pos[4];			// Les 4 positions GPS acquises en 12mn
Position posActuelle;		// dernière position GPS acquise
Position posOld;			// position GPS précédente
unsigned long epochTime;	// dernière date de la dernière acquisition GPS
int numPosition = 0;		// [0 .. 3] parmis les 4 pos GPS
int vitesseMoyenne = 0;		// [0 .. 200]km/h
long somme = 0;				// pour calcul de la vitesse moyenne sur 16 min
int nbValeurs = 0;			// Toute les 10s, remis à zéro au bout de 16mn (16*60/10 = 96 fois)
int vitesseMax = 0;			// [0 .. 200]	km/h

// la distance parcourue est sauvegardée en EEPROM dans l'arduino mais
// la distance parcourue est remise à zéro à chaque chargement d'un nouveau programme !!!
// Donc si le serveur reçoit une distance parcourue < à la précédente
// c'est qu'il y a eu une nouvelle version du logiciel sur le boitier.
// C'est à prendre en compte sur le serveur qui doit donc garder la dernière
// distance parcourue reçue.
// la coupure d'alimentation du boitier conserve la distance parcourue
double distanceParcourue = 0.0;	// en m, max possible = 4294967295m (4.3 millions de km)

unsigned long tDebut = millis();
unsigned long tCPU = 0;
bool pbGPS = false;

// post déclaration de fonction utiles
void majData();
void debugPrint(String texte, int value);

// réserve la taille d'un double en mémoire EEPROM pour la distance parcourue en m
FlashStorage(flashDistance, double);
FlashStorage(flashDate, long);

// initialisation du boitier
void setup() {
	pinMode(LED_BUILTIN, OUTPUT);
	// Serial.begin(9600);			// supprimé pour permettre le rest par alim USB
	// while(!Serial) {};			// si uniquement alim par USB
	pbGPS = !(MKRGPS::init(DEBUG));
	CommSigfox::init(DEBUG, SEND);
	delay(100);
	//CommSigfox::printID();
	digitalWrite(LED_BUILTIN, HIGH);
	posActuelle = MKRGPS::getPosition();
	// on init la distanceParcourue avec la valeur sauvée dans l'EEPROM
	distanceParcourue = flashDistance.read();
	// on récupère la dernière date sauvée d'émission d'un msg Sigfox
	epochTime = flashDate.read();
	// A la mise en route du véhicule on envoie la position de départ
	// sauf si ca fait moins de 12 minutes qu'il a coupé son moteur
	// epochTime est en secondes depuis le 1/1/1970
	if (posActuelle.epochTime - epochTime >= 720) {
		CommSigfox::sendParking(pbGPS, posActuelle, distanceParcourue, 0);
		// on sauve la date du dernier envoi d'un message au format GPS epoch (sec depuis
		flashDate.write(posActuelle.epochTime);
	}
	debugPrint("epochTime : ", posActuelle.epochTime);
	debugPrint("flashDate : ", flashDate.read());
}

// boucle de travail du boitier
void loop() {
	// ------------------- toutes le 5 secondes ---------------
	// debugPrint("tCPU : ", tCPU);
	if (tCPU < DUREE_BOUCLE)
		delay(DUREE_BOUCLE - tCPU);	// DUREE_BOUCLE - durée du traitement de la loop
	// mettre le calcul du tCPU après l'attente : delay()
	// millis() retourne à zéro tous les 50 jours si la carte reste allumée !
	tDebut = millis();
	digitalWrite(LED_BUILTIN, (digitalRead(LED_BUILTIN) ? LOW : HIGH)); // turn the LED on/off pour voir que le programme marche
	secondes = secondes + INC_BOUCLE;	// 240 au lieu de DUREE_BOUCLE si on veut accelerer l'envoi des messages
	if (secondes >= _24H)		// une fois par jour (divisible par 3', 12' et 24h)
		secondes = 0;
	debugPrint("Secondes : ", secondes);
	posOld = posActuelle;		// on garde l'ancienne position
	// acquisition de la position GPS actuelle
	posActuelle = MKRGPS::getPosition();
	// misa à jour des données de déplacement : vitesses moy et max, distance parcourue
	majData();

	// ----------------------------------- toute les 3 mn -----------------------------------
	if (secondes % 180 == 0) {
		debugPrint(">>>>>>>>> 3mn : ", secondes);
		pos[numPosition] = posActuelle;	// On garde 4 positions GPS (toute les 4min)
		numPosition++;
	}

	// ----------------------------------- toute les 12 mn ----------------------------------
	if (secondes % 720 == 0) {
		debugPrint(">>>>>>>>> 12mn : ", secondes);
		// si la distance entre les positions n'a pas changé, on est garé
		double dist = MKRGPS::distance(pos[0], pos[3]);
		if (dist < ERREUR_PARKING)
			CommSigfox::sendParking(pbGPS, pos[3], distanceParcourue, vitesseMax);
		else {
			vitesseMoyenne = somme/nbValeurs;
			CommSigfox::sendGPS4(pbGPS, vitesseMoyenne, pos);
		}
		// on sauve la date du dernier envoi d'un message au format GPS epoch (sec depuis 1/1/1970)
		flashDate.write(posActuelle.epochTime);
		debugPrint("flashDate : ", flashDate.read());
		// ------------------------------ RAZ des datas -------------------------------------
		numPosition = 0;			// on recommence l'acquisition des 4 positions GPS
		vitesseMoyenne = 0;
		somme = 0;
		nbValeurs = 0; 				// pour vitesse moyenne
	}
	tCPU = millis() - tDebut;		// on calcule la durée du traitement
}

// maj de la vitesse moyenne, vitesse max et distance parcourue
void majData() {
	if (vitesseMax < posActuelle.speed)
		vitesseMax = posActuelle.speed;
	// calcul de la vitesse moyenne
	if (posActuelle.speed > 5)
		somme += posActuelle.speed;
	nbValeurs++;		// une valeur de plus dans le calcul de la vitesseMoyenne
	// calcul de la distance parcourue par la vitesse
	if (posActuelle.speed > 5) {	// le véhicule avance à plus de 5km/h
		// la vitesse est en km/h ramenée en m/5s, le résultat est un réel !
		distanceParcourue += 50*posActuelle.speed/36.0;
		flashDistance.write(distanceParcourue);
	}
}

void debugPrint(String texte, int value) {
	if (DEBUG) {
		Serial.print(texte); Serial.print(value);
		Serial.print("   millis : "); Serial.println(millis());
	}
}
