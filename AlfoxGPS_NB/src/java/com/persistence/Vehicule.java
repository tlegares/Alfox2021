/*
 * Projet  : Alfox
 * Fichier : User.java
 * Description : Classe interface de la table Vehicule
 * Cette table stocke les infos sur les véhicules connus du logiciel
 * Modification : 18/03/2020
 * ajout de la méthode getID() pour les jointures avec les autres tables
 * necessaire pour retrouver le boitier associé
 *
 */
package com.persistence;

import java.sql.*;
import java.util.ArrayList;

public class Vehicule { 
    private String    Marque;
    private String    Modele; 
    private String    Immatriculation;
    private String    Motorisation;
    private Timestamp DateMiseEnService;
    private boolean   HorsZone;
    private int       TauxUtilisation;
    private double    KilometrageReel;
    private double    KilometrageCumule;
    private int       ZoneLimiteID;
    
    /**
     * Créer un nouvel objet persistant
     *
     * @param con
     * @return
     * @ return retourne un Vehicule si l'Immatriculation est unique sinon null
     * @throws Exception impossible d'accéder à la ConnexionMySQL 
     *                   ou le numero l'Immatriculation est deja dans la BD
     *
     */
    static public Vehicule create(Connection con, String Marque, 
            String Modele, String Immatriculation, String Motorisation,
            Timestamp DateMiseEnService, boolean HorsZone, int TauxUtilisation,
            double KilometrageReel, double KilometrageCumule, int ZoneLimiteID) throws Exception {
        Vehicule Vehicule = new Vehicule(
            Marque, Modele, Immatriculation, Motorisation,
            DateMiseEnService, HorsZone, TauxUtilisation,
            KilometrageReel, KilometrageCumule, ZoneLimiteID);

        String queryString =
          "insert into Vehicule (Marque," +
                " Modele, Immatriculation, Motorisation," +
                " DateMiseEnService, HorsZone, TauxUtilisation," +
                " KilometrageReel, KilometrageCumule, ZoneLimiteID)"  +
                " values ("
                + Utils.toString(Marque) + ", "
                + Utils.toString(Modele) + ", "
                + Utils.toString(Immatriculation) + ", "
                + Utils.toString(Motorisation) + ", "
                + Utils.toString(DateMiseEnService) + ", "
                + Utils.toString(HorsZone) + ", "
                + Utils.toString(TauxUtilisation) + ", "
                + Utils.toString(KilometrageReel) + ", "
                + Utils.toString(KilometrageCumule) + ", "
                + Utils.toString(ZoneLimiteID)
            + ")";
        Statement lStat = con.createStatement();
        lStat.executeUpdate(queryString, Statement.NO_GENERATED_KEYS);
        return Vehicule;
    }

    /**
     * update de l'objet Vehicule dans la ConnexionMySQL
     *
     * @param con
     * @throws Exception impossible d'accéder à la ConnexionMySQL
     */
    public void save(Connection con) throws Exception {
        String queryString =
            "update Vehicule set "
                + " Marque =" + Utils.toString(Marque) + ","
                + " Modele =" + Utils.toString(Modele) + ","
                + " Immatriculation =" + Utils.toString(Immatriculation) + ","
                + " Motorisation =" + Utils.toString(Motorisation) + ","
                + " DateMiseEnService =" + Utils.toString(DateMiseEnService) + ","
                + " HorsZone =" + Utils.toString(HorsZone) + ","
                + " TauxUtilisation =" + Utils.toString(TauxUtilisation) + ","
                + " KilometrageReel =" + Utils.toString(KilometrageReel) + ","
                + " KilometrageCumule =" + Utils.toString(KilometrageCumule) + ","
                + " ZoneLimiteID =" + Utils.toString(ZoneLimiteID)
                + " where Immatriculation ='" + Immatriculation + "'";
        Statement lStat = con.createStatement();
        lStat.executeUpdate(queryString, Statement.NO_GENERATED_KEYS);
    }
    
    /**
     * suppression de l'objet Vehicule dans la BD
     *
     * @param con
     * @return
     * @throws SQLException impossible d'accéder à la ConnexionMySQL
     */
    public boolean delete(Connection con) throws Exception {
        String queryString = "delete from Vehicule"
                        + " where Immatriculation='" + Immatriculation + "'";
        Statement lStat = con.createStatement();
        lStat.executeUpdate(queryString);
        return true;
    }
    
    /**
     * suppression de l'objet Vehicule dans la BD
     *
     * @param con
     * @return
     * @throws SQLException impossible d'accéder à la ConnexionMySQL
     */
    public static boolean deleteByImmatriculation(Connection con, String Immatriculation) throws Exception {
        String queryString = "delete from Vehicule where Immatriculation='" + Immatriculation + "'";
        Statement lStat = con.createStatement();
        lStat.executeUpdate(queryString);
        return true;
    }
    
    public static ArrayList<Vehicule> getList(Connection con) throws Exception {
        String queryString = "select * from Vehicule";
        Statement lStat = con.createStatement(
                                ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        ArrayList<Vehicule> Vehicules = new ArrayList<>();
        while (lResult.next ()) {
            Vehicules.add (creerParRequete (lResult));
        }
        return Vehicules;
    }
        
    public static ArrayList<String> getImmatriculations(Connection con) throws Exception {
        String queryString = "select Immatriculation from Vehicule"
                                + " order by Immatriculation";
        Statement lStat = con.createStatement(
                                ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        ArrayList<String> lstImmatriculation = new ArrayList<>();
        while (lResult.next()) {
            lstImmatriculation.add(lResult.getString("Immatriculation"));
        }
        return lstImmatriculation;
    }

    /**
     * Retourne un Vehicule trouve par son Immatriculation, saved is true
     *
     * @param con
     * @param Immatriculation l'Immatriculation a trouver
     * @return Vehicule trouvé par Immatriculation
     * @throws java.lang.Exception
     */
    public static Vehicule getByImmatriculation(Connection con, String Immatriculation) throws Exception {
        String queryString = "select * from Vehicule"
                            + " where Immatriculation='" + Immatriculation + "'";
        Statement lStat = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        // y en a t'il au moins un ?
        if (lResult.next()) {
            return creerParRequete(lResult);
        } else {
            return null;
        }
    }
    
     /**
     * Retourne un loueur trouve par son nom et prénom, saved is true
     * @param con
     * @param  id
     * @return véhicule trouvé id
     * @throws java.lang.Exception
     */
    public static Vehicule getByID(Connection con, int id) throws Exception {
        String queryString = "select * from Vehicule"
                                                + " where ID='" + id + "';";
        Statement lStat = con.createStatement(
                                ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        // y en a t'il au moins un ?
        if (lResult.next()) {
            return creerParRequete(lResult);
        }
        else
            return null;
    }
    
    public static int getKmMoyenFlotte(Connection con) throws Exception {
        double totalKm = 0;
        String queryString = "select KilometrageReel, KilometrageCumule from Vehicule";
        Statement lStat = con.createStatement(
                                ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        int nbVehicule = 0;
        while (lResult.next()) {
            nbVehicule++;
            totalKm += lResult.getDouble("KilometrageReel");
            totalKm += lResult.getDouble("KilometrageCumule");
        }
        return (int)(totalKm / nbVehicule);
    }
    
    public static int getKmMoyenMensuelFlotte(Connection con) throws Exception {
        double kmMoyenFlotte = Vehicule.getKmMoyenFlotte(con);
        int ageMoyenFlotte = (int)(Vehicule.getAgeMoyenFlotte(con) / 30.5);
        return (int)(kmMoyenFlotte / ageMoyenFlotte);
    }
    
    /**
     * Retourne l'age de la flotte en jours
     * @param con
     * @return
     * @throws Exception 
     */
    public static int getAgeMoyenFlotte(Connection con) throws Exception {
        long jour = 0;
        Timestamp dateDuJour = Utils.getDateDuJour();
        String queryString = "select DateMiseEnService from Vehicule";
        Statement lStat = con.createStatement(
                                ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        ArrayList<Timestamp> lstDateMiseEnService = new ArrayList<>();
        while (lResult.next()) {
            lstDateMiseEnService.add(lResult.getTimestamp("DateMiseEnService"));
        }
        for (int i = 0 ; i < lstDateMiseEnService.size() ; i++) {
            jour += (dateDuJour.getTime() - lstDateMiseEnService.get(i).getTime())/ 86400000; 
        }
        return (int)(jour/lstDateMiseEnService.size());
    }
    
    
    /**
     * Retourne l'age du véhicule en jours
     * @param con
     * @return
     * @throws Exception 
     */
    public int getAge(Connection con) throws Exception {
        long jour = 0;
        Timestamp dateMiseEnService;
        Timestamp dateDuJour = Utils.getDateDuJour();
        String queryString = "select DateMiseEnService from Vehicule"
                        + " where Immatriculation='" + Immatriculation + "'";
        Statement lStat = con.createStatement(
                                ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        if (lResult.next()) {
            dateMiseEnService = lResult.getTimestamp("DateMiseEnService");
            return (int)((dateDuJour.getTime() - dateMiseEnService.getTime())/ 86400000);
        }
        return 0;
    }
    
    public static int nbVehiculesDehors(Connection con) throws Exception {
        int nbVehiculesDehors = 0;
        
        String queryString = "select count(*) as count from Vehicule "
                                                    + "where HorsZone = true";
        Statement lStat = con.createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE, 
                            ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        if (lResult.next())
            return (lResult.getInt("count"));
        else 
            return 0;
    }
    
    /*
        retourne l'ID d'un objet
    */
    public int getID(Connection con) throws Exception {
        String queryString = "select ID from Vehicule where Immatriculation='" 
                                                + Immatriculation + "'";
        Statement lStat = con.createStatement(
                                ResultSet.TYPE_SCROLL_INSENSITIVE,
                                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        if (lResult.next()) {
            return lResult.getInt("ID");
        }
        else {
            return 0;
        }
    }
    
    // Met à jour HorsZone
    public boolean isHorsZone(Connection con) throws Exception {
        // Récupération les Positions de la zone associée au véhicule
        String queryString = "select *"
            + " from Position, ZoneLimite"
            + " where  ZoneLimiteID = ZoneLimite.ID"
            + " and ZoneLimite.Nom = "
                + "(select distinct ZoneLimite.Nom"
                + " from Vehicule, ZoneLimite"
                + " where ZoneLimiteID = ZoneLimite.ID"
                + " and Immatriculation='" + Immatriculation + "')"
            + " order by Ordre";
        Statement lStat = con.createStatement( //peut générer une exception si problème avec la requête SQL
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        // On met les points dans 2 collections Arraylist de Double
        ArrayList<Double> xap = new ArrayList<>();
        ArrayList<Double> yap = new ArrayList<>();
        while (lResult.next()) {
            xap.add(lResult.getDouble("Latitude"));
            yap.add(lResult.getDouble("Longitude"));
        }
        // On transforme les collections en tableaux d'objets
        int nbPoints = xap.size();
        Double[] xtp = xap.toArray(new Double[0]);
        Double[] ytp = yap.toArray(new Double[0]);

        // Les tableaux sont maintenant des tableaux de Double !
        // Récupération de la dernière latitude et longitude enregistrée
        String queryString2 = "select LatitudeGPS, LongitudeGPS"
                + " from DonneesTR, Vehicule"
                + " where DonneesTR.VehiculeID = Vehicule.ID"
                + " and Vehicule.Immatriculation = '" + Immatriculation + "'"
                + " order by Datation desc limit 1";
        Statement lStat2 = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet req = lStat2.executeQuery(queryString2);
        double latitudeGPS = 0;
        double longitudeGPS = 0;
        // Si il y a une donnée on récupère la latitude et longitude
        if (req.next()) {
            latitudeGPS = req.getDouble("LatitudeGPS");
            longitudeGPS = req.getDouble("LongitudeGPS");
        }// Sinon on génère une exception
        else {
            throw new Exception("Aucune donnée TR");
        }
        // On vérifie que le point se situe dans la zone
        int i, j;
        boolean isDehors = true;
        for (i = 0, j = nbPoints - 1; i < nbPoints; j = i++) {
            if ((((ytp[i] <= longitudeGPS) && (longitudeGPS < ytp[j])) || ((ytp[j] <= longitudeGPS) && (longitudeGPS < ytp[i])))
                    && (latitudeGPS < (xtp[j] - xtp[i]) * (longitudeGPS - ytp[i]) / (ytp[j] - ytp[i]) + xtp[i])) {
                isDehors = !isDehors;
            }
        }
        // On met à jour HorsZone avec la valeur donnée par la méthode isDehors()
        HorsZone = isDehors;
        return isDehors;
    }
    
    public static int getTempsAlcis(Connection con, String Immatriculation) throws Exception {
        long tempsAlcisEnMs = 0;
        int  tempsAlcisEnM = 0;
        // On récupère la date et heure actuelle
        Timestamp dateJour = Utils.getDateDuJour();
        // On défini la Position d'ALCIS (5km*5km)
        // défini en dur (à mettre dans les zones plus tard
        double latMin = 43.555692;
        double latMax = 43.646065;
        double lgMin = 1.465021;
        double lgMax = 1.591707;
        int VehiculeID = Vehicule.getByImmatriculation(con, Immatriculation).getID(con);
        // Récupération des donnéesTR associées au véhicule passé en paramètre
        ArrayList<DonneesTR> lstDonneesTR = DonneesTR.getByVehiculeID(con, VehiculeID);
        // On regarde si la dernière donnée donne le véhicule chez Alcis
        double lat0 = lstDonneesTR.get(0).getLatitudeGPS();
        double lg0 = lstDonneesTR.get(0).getLongitudeGPS();
        if ((lat0 >= latMin) && (lat0 <= latMax) && (lg0 >= lgMin) && (lg0 <= lgMax)) {
            // Si oui on récupère la dernière date à laquelle le véhicule était en dehors d'Alcis
            int i = 1;
            while ((i < lstDonneesTR.size()) 
                    && (lstDonneesTR.get(i).getLatitudeGPS() >= latMin) 
                    && (lstDonneesTR.get(i).getLatitudeGPS() <= latMax) 
                    && (lstDonneesTR.get(i).getLongitudeGPS() >= lgMin) 
                    && (lstDonneesTR.get(i).getLongitudeGPS() <= lgMax)) {
                i++;
            }
            if (i >= lstDonneesTR.size()) 
                i--;
            Timestamp dateDernierePosAlcis = lstDonneesTR.get(i).getDatation(); // On récupère la date
            tempsAlcisEnMs = dateJour.getTime() - dateDernierePosAlcis.getTime();
            tempsAlcisEnM = (int)(tempsAlcisEnMs / 60000);
            if (tempsAlcisEnM < 60) {
                tempsAlcisEnM = 0; // Si le véhicule y est depuis moins d'une heure on ne le considère pas
            }
        }
        else {
            tempsAlcisEnM = 0;
        }
        return tempsAlcisEnM;
    }
     
    /**
     * Indique le nb de Vehicules dans la base de données
     * @param con
     * @return le nombre de Vehicules
     * @throws java.lang.Exception
     */
    public static int size(Connection con) throws Exception {
        String queryString = "select count(*) as count from Vehicule";
        Statement lStat = con.createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE, 
                            ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        if (lResult.next())
            return (lResult.getInt("count"));
        else 
            return 0;
    }

    private static Vehicule creerParRequete(ResultSet result) throws Exception {
        String lMarque = result.getString("Marque");
        String lModele = result.getString("Modele");
        String lImmatriculation = result.getString("Immatriculation");
        Timestamp lDateMiseEnService = result.getTimestamp("DateMiseEnService");
        String lMotorisation = result.getString("Motorisation");
        boolean lHorsZone = result.getBoolean("HorsZone");
        int lTauxUtilisation = result.getInt("TauxUtilisation");
        double lKilometrageReel = result.getDouble("KilometrageReel");
        double lKilometrageCumule = result.getDouble("KilometrageCumule");
        int lZoneLimiteID = result.getInt("ZoneLimiteID");
        return new Vehicule(lMarque, lModele, lImmatriculation, lMotorisation,
            lDateMiseEnService, lHorsZone, lTauxUtilisation, lKilometrageReel,
            lKilometrageCumule, lZoneLimiteID);
    }
    
    /**
     * Cree et initialise completement Vehicule
     */
    private Vehicule(String Marque, 
            String Modele, String Immatriculation, String Motorisation,
            Timestamp DateMiseEnService, boolean HorsZone, int TauxUtilisation,
            double KilometrageReel, double KilometrageCumule, int ZoneLimiteID) {
        this.Marque = Marque;
        this.Modele = Modele;
        this.Immatriculation = Immatriculation;
        this.Motorisation = Motorisation;
        this.DateMiseEnService = DateMiseEnService;
        this.HorsZone = HorsZone;
        this.TauxUtilisation = TauxUtilisation;
        this.KilometrageReel = KilometrageReel;
        this.KilometrageCumule = KilometrageCumule;
        this.ZoneLimiteID = ZoneLimiteID;
    }

    // --------------------- les assesseurs ----------------------------
    public String getMarque() {
        return Marque;
    }

    public void setMarque(String Marque) {
        this.Marque = Marque;
    }

    public String getModele() {
        return Modele;
    }

    public void setModele(String Modele) {
        this.Modele = Modele;
    }

    public String getImmatriculation() {
        return Immatriculation;
    }

    public void setImmatriculation(String Immatriculation) {
        this.Immatriculation = Immatriculation;
    }

    public String getMotorisation() {
        return Motorisation;
    }

    public void setMotorisation(String Motorisation) {
        this.Motorisation = Motorisation;
    }

    public Timestamp getDateMiseEnService() {
        return DateMiseEnService;
    }

    public void setDateMiseEnService(Timestamp DateMiseEnService) {
        this.DateMiseEnService = DateMiseEnService;
    }

    public boolean isHorsZone() {
        return HorsZone;
    }

    public void setHorsZone(boolean HorsZone) {
        this.HorsZone = HorsZone;
    }

    public int getTauxUtilisation() {
        return TauxUtilisation;
    }

    public void setTauxUtilisation(int TauxUtilisation) {
        this.TauxUtilisation = TauxUtilisation;
    }

    public double getKilometrageReel() {
        return KilometrageReel;
    }

    public void setKilometrageReel(double KilometrageReel) {
        this.KilometrageReel = KilometrageReel;
    }

    public double getKilometrageCumule() {
        return KilometrageCumule;
    }

    public void setKilometrageCumule(double KilometrageCumule) {
        this.KilometrageCumule = KilometrageCumule;
    }

    public int getZoneLimiteID() {
        return ZoneLimiteID;
    }

    public void setZoneLimiteID(int ZoneLimiteID) {
        this.ZoneLimiteID = ZoneLimiteID;
    }

    /**
     * toString() operator overload
     *
     * @return the result string
     */
    @Override
    public String toString() {
        return "Immatriculation = " + Utils.toString(Immatriculation) + "\t"
            + " Modele =" + Utils.toString(Modele) + ","
            + " Marque =" + Utils.toString(Marque) + ","
            + " Motorisation =" + Utils.toString(Motorisation) + ","
            + " DateMiseEnService =" + Utils.toString(DateMiseEnService) + ","
            + " HorsZone =" + Utils.toString(HorsZone) + ","
            + " TauxUtilisation =" + Utils.toString(TauxUtilisation) + ","
            + " KilometrageReel =" + Utils.toString(KilometrageReel) + ","
            + " KilometrageCumule =" + Utils.toString(KilometrageCumule) + ","
            + " ZoneLimiteID =" + Utils.toString(ZoneLimiteID);
    }
    
    // Test
    public static void main(String[] argv) throws Exception {
        Connection con = ConnexionMySQL.getInstance();
        System.out.println(Vehicule.size(con));
    }
}
