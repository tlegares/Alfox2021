
drop schema if exists alfoxGPS;
create schema alfoxGPS;
use alfoxGPS;

CREATE TABLE Responsable (
    ID int not null auto_increment, 
    Mdp varchar(255) not null, 
    Mail varchar(255) not null, 
    PRIMARY KEY (ID)
);

CREATE TABLE Vehicule (
    ID int not null auto_increment, 
    Marque varchar(255) not null, 
    Modele varchar(255) not null, 
    Immatriculation varchar(255) not null unique, 
    Motorisation varchar(255) not null, 
    DateMiseEnService timestamp  not null, 
    HorsZone tinyint, 
    TauxUtilisation int, 
    KilometrageReel decimal(10, 3), 
    KilometrageCumule decimal(10, 3),
    ZoneLimiteID int not null,
    PRIMARY KEY (ID)
);

CREATE TABLE DonneesTR (
    ID int not null auto_increment, 
    SeqNumber int, 
    Datation timestamp not null, 
    Vitesse int, 
    VitesseMax int, 
    LatitudeGPS decimal(10, 6), 
    LongitudeGPS decimal(10, 6),
    DistanceParcourueDepuisReset bigint, 
    VehiculeID int not null, 
    PRIMARY KEY (ID)
);

/* On pourra ajouter des infos sur la qualité du signal dans cette table */
CREATE TABLE Boitier (
    SigfoxID varchar(255) not null, 
    Parking tinyint not null, 
    GPSActif tinyint not null, 
    VehiculeID int not null, 
    PRIMARY KEY (SigfoxID)
);

CREATE TABLE ZoneLimite (
    ID int not null auto_increment,
    Nom varchar(255) not null, 
    Zoom int not null, 
    PRIMARY KEY (ID)
);

CREATE TABLE Position (
    ID int not null auto_increment, 
    Ordre int not null, 
    Latitude decimal(10, 6) not null, 
    Longitude decimal(10, 6) not null, 
    ZoneLimiteID int not null, 
    PRIMARY KEY (ID)
);

ALTER TABLE Position ADD CONSTRAINT FKPosition217606 FOREIGN KEY (ZoneLimiteID) REFERENCES ZoneLimite (ID);
ALTER TABLE Vehicule ADD CONSTRAINT FKVehicule610678 FOREIGN KEY (ZoneLimiteID) REFERENCES ZoneLimite (ID);
ALTER TABLE Boitier ADD CONSTRAINT FKBoitier302509 FOREIGN KEY (VehiculeID) REFERENCES Vehicule (ID);
ALTER TABLE DonneesTR ADD CONSTRAINT FKDonneesTR404677 FOREIGN KEY (VehiculeID) REFERENCES Vehicule (ID);

# ----------------------------------------------------------------------------
#                   initialisation avec des données de test
# ----------------------------------------------------------------------------
insert into Responsable (Mdp, Mail) values 
    ('636D61CF9094A62A81836F3737D9C0DA','responsable@gmail.com');


insert into ZoneLimite (ID, Nom, Zoom) values
    (1, 'Alcis', 4),
    (2, 'Toulouse', 3),
    (3, 'GrandSud', 2),
    (4, 'France', 1);

insert into Position (ordre, latitude, longitude, ZoneLimiteID) values
    (1, 43.604014, 1.526581, 1),
    (2, 43.601590, 1.524203, 1),
    (3, 43.600355, 1.528461, 1),
    (4, 43.601920, 1.530292, 1),
    (1, 44.087321, 1.338782, 2),
    (2, 43.982670, 1.790594, 2),
    (3, 43.798589, 2.073492, 2),
    (4, 43.596040, 2.320685, 2),
    (5, 43.238933, 2.504706, 2),
    (6, 42.917949, 1.999335, 2),
    (7, 42.859591, 1.400580, 2),
    (8, 43.060593, 0.697455, 2),
    (9, 43.474581, 0.494208, 2),
    (10,43.818410, 0.785346, 2),
    (1, 41.459000, 3.620000, 3),
    (2, 43.461915,-2.181781, 3),
    (3, 45.376139,-1.233385, 3),
    (4, 44.100030, 4.945026, 3),
    (1, 51.231112, 2.035541, 4),
    (2, 48.369007,-5.693126, 4),
    (3, 43.359731,-2.249526, 4),
    (4, 41.459000, 3.620000, 4),
    (5, 43.000000, 8.361000, 4),
    (6, 46.437377, 6.842785, 4),
    (7, 49.000000, 8.500000, 4);

insert into Vehicule (ID, Marque, Modele, Immatriculation, Motorisation,
            DateMiseEnService, HorsZone, TauxUtilisation, KilometrageReel, 
            KilometrageCumule, ZoneLimiteID) values
    (1, 'Renault', 'Talisman', 'ED-592-CY', 'Diesel', '2018/01/01', false, 10, 40787.231, 110.0, 2),
    (2, 'Renault', 'Talisman', 'ED-593-VS', 'Diesel', '2018/01/01', true, 12, 76618.25, 0, 2),
    (3, 'Renault', 'Talisman', 'EE-239-QM', 'Diesel', '2018/01/01', false, 14, 41201.76, 0, 3),
    (4, 'Mercedes', 'Vito', 'EE-300-QM', 'Diesel', '2019/03/10', true, 21, 22700.098, 0, 2),
    (5, 'Mercedes', 'Vito', 'EK-462-GX', 'Diesel', '2019/03/10', false, 14, 26320.393, 0, 2),
    (6, 'Mercedes', 'Vito', 'EM-045-BC', 'Diesel', '2019/03/10', true, 35, 27791.992, 0, 4),
    (7, 'Mercedes', 'Vito', 'EM-150-BE', 'Diesel', '2019/03/10', false, 23, 32778.123, 0, 3),
    (8, 'Mercedes', 'Vito', 'EM-862-ML', 'Diesel', '2019/03/10', true, 31, 11826.920, 100.0, 4),
    (9, 'Toyota', 'RAV4', 'FP-903-LF', 'Hybride', '2020/03/15', false, 10, 3846.0, 0, 2);

insert into Boitier (SigfoxID, Parking, GPSActif, VehiculeID) values
    ('1D2289', false, true, 1),
    ('1D188E', false, true, 2),
    ('2E1233', false, true, 3),
    ('3R5894', false, true, 4),
    ('4A9203', true,  true, 5),
    ('5Q3729', false, true, 6),
    ('7A0022', true,  true, 7),
    ('8B2884', false, true, 8),
    ('1CF60F', false, true, 9);

insert into DonneesTR ( Datation, Vitesse, VitesseMax, LatitudeGPS, LongitudeGPS, 
        DistanceParcourueDepuisReset, SeqNumber, VehiculeID) values
    ('2020/04/20 00:00:00', 24,  24, 43.617621,  1.3094770, 2550, 20, 1),
    ('2020/04/20 00:10:00', 22,  22, 19.368961,  33.811088, 3264, 14, 2),
    ('2020/04/20 00:20:00', 32,  32, 40.283732,  59.196649, 1203, 15, 3), 
    ('2020/04/20 00:30:00', 43,  77, 39.836279,  48.398641, 5432, 13, 3),
    ('2020/04/20 00:40:00', 28,  30, 39.495084,  25.329905, 504,   1, 4), 
    ('2020/04/20 00:50:00', 45,  45, 42.870164,  17.555737, 9120,  9, 5),
    ('2020/04/20 01:00:00', 70, 119, 51.210434,  34.768429, 6729,  2, 6), 
    ('2020/04/20 01:10:00', 26,  26, 43.593468,   1.414471, 1234, 18, 7),
    ('2020/04/21 01:10:00', 26,  26, 43.593468,   1.414471, 1234, 18, 7),
    ('2020/04/23 01:10:00', 26,  26, 43.593468,   1.414471, 1234, 18, 7),
    ('2020/04/30 01:10:00', 26, 150, 43.593468,   1.414471, 1234, 18, 7),
    ('2020/04/01 01:10:00', 26,  26, 43.593468,   1.414471, 1234, 18, 7),
    ('2020/04/02 01:10:00', 26,  26, 43.593468,   1.414471, 1234, 18, 7),
    ('2020/04/07 01:10:00', 26,  26, 43.593468,   1.414471, 1234, 18, 7),
    ('2020/05/01 23:59:59', 26, 150, 43.593468,   1.414471, 1234, 18, 7),
    ('2020/05/02 01:10:00', 26,  26, 43.593468,   1.414471, 1234, 18, 7),
    ('2020/05/02 10:10:00', 26,  26, 43.593468,   1.414471, 1234, 18, 7),
    ('2020/05/03 00:10:00', 26,  26, 43.593468,   1.414471, 1234, 18, 7);

# -----------------------------------------------------------------------
#    crée les autorisation du user local pour l'accés du serveur à la BD
# -----------------------------------------------------------------------

use alfoxGPS;

drop user alfoxGPS@localhost;
create user alfoxGPS@localhost identified by 'alfox31';

grant all privileges on *.* to alfoxGPS@localhost with grant option;