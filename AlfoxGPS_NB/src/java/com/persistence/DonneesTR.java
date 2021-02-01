/*
 * Projet  : AlfoxGPS
 * Fichier : DonneesTR.java
 * Description : Classe interface de la table DonneesTR
 * Cette table stocke les données TR obtenues par message SIGFOX
 *
 * Pb : l'enregistrement est réalisé en 2 temps :
 *      1. on recoit les datas du message
 *      2. on reçoit la position GPS
 *      Si la position GPS est nulle on récupère la position Sigfox Atlas 
 *      de l'origine du message.
 *
 */
package com.persistence;

import java.sql.*;
import java.util.ArrayList;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class DonneesTR {
    private int SeqNumber;
    private Timestamp Datation;
    private int Vitesse; 
    private int VitesseMax;
    private double LatitudeGPS; 
    private double LongitudeGPS;
    private long DistanceParcourueDepuisReset;
    private int VehiculeID;

    /**
     * Créer un nouvel objet DonneesTR persistant
     *
     * @param con
     * @return
     * @ return retourne une DonneesTR si la date est unique sinon null
     * @throws Exception impossible d'accéder à la ConnexionMySQL ou la date est
     * deja dans la BD
     */
    static public DonneesTR create(Connection con, int SeqNumber,
            Timestamp Datation, int Vitesse, int VitesseMax,
            double LatitudeGPS, double LongitudeGPS,
            long DistanceParcourueDepuisReset, int VehiculeID) throws Exception {
        // instanciation de l'objet avec les données initiales
        DonneesTR DonneesTR = new DonneesTR(SeqNumber, Datation,
                Vitesse, VitesseMax, LatitudeGPS, LongitudeGPS,
                DistanceParcourueDepuisReset, VehiculeID);
        // la sauvegarde est maintenant faite dans save()
        // pour une création ou une mise à jour
        DonneesTR.save(con);
        return DonneesTR;
    }

    /**
     * suppression de l'objet DonneesTR dans la BD
     *
     * @param con
     * @return
     * @throws SQLException impossible d'accéder à la ConnexionMySQL
     */
    public boolean delete(Connection con) throws Exception {
        String queryString = "delete from DonneesTR"
                                    + " where Datation = '" + Datation + "' "
                                    + " and VehiculeID = " + VehiculeID;
        Statement lStat = con.createStatement();
        lStat.executeUpdate(queryString);
        return true;
    }

    /**
     * update de l'objet DonneesTR dans la ConnexionMySQL
     *
     * @param con
     * @throws Exception impossible d'accéder à la ConnexionMySQL
     */
    public void save(Connection con) throws Exception {
        String queryString = "";
        // je regarde d'abord si l'enregistrement existe déjà pour ce message
        queryString = "select ID from DonneesTR "
                + " where Datation = '" + Datation + "' "
                + " and VehiculeID = " + VehiculeID;
        Statement lStat = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        // si il existe, on l'update par l'ID
        if (lResult.next()) {
            int id = lResult.getInt("ID");
            queryString = "update DonneesTR set "
                + " SeqNumber =" + Utils.toString(SeqNumber) + ","
                + " Datation = " + Utils.toString(Datation) + ","  // ajouté
                + " Vitesse =" + Utils.toString(Vitesse) + ","
                + " VitesseMax =" + Utils.toString(VitesseMax) + ","
                + " LatitudeGPS =" + Utils.toString(LatitudeGPS) + ","
                + " LongitudeGPS =" + Utils.toString(LongitudeGPS) + ","
                + " DistanceParcourueDepuisReset =" 
                                + Utils.toString(DistanceParcourueDepuisReset) + ","
                + " VehiculeID = " + VehiculeID // ajouté
                + " where ID=" + id;
        }
        // sinon on l'ajoute à la base
        else {
            queryString = "insert into DonneesTR" 
                + " (SeqNumber, Datation, Vitesse, VitesseMax, LatitudeGPS, "
                + " LongitudeGPS, DistanceParcourueDepuisReset, VehiculeID) values ("
                + Utils.toString(SeqNumber) + ","
                + Utils.toString(Datation) + ","  // ajouté
                + Utils.toString(Vitesse) + ","
                + Utils.toString(VitesseMax) + ","
                + Utils.toString(LatitudeGPS) + ","
                + Utils.toString(LongitudeGPS) + ","
                + Utils.toString(DistanceParcourueDepuisReset) + ","
                + VehiculeID
            + ")";
        }
        lStat = con.createStatement();
        lStat.executeUpdate(queryString, Statement.NO_GENERATED_KEYS);
    }

    /**
     * Sauvegarde des datas du message Sigfox DATA
     * @param con
     * @param sigfoxID
     * @param SeqNumber
     * @param Datation
     * @param data
     * @return 
     * @throws Exception 
     */
    public static String saveData(Connection con, String sigfoxID, 
            int SeqNumber, Timestamp Datation, String data) throws Exception {
        long DistanceParcourueDepuisReset = 0L;
        
        Boitier boitier = Boitier.getBySigfoxID(con, sigfoxID);
        if (boitier == null) {
            return "ERREUR";
        }
        int VehiculeID = boitier.getVehiculeID();
        
        // conversion de la chaine hexa en tableau de byte
        HexBinaryAdapter adapter = new HexBinaryAdapter();
        byte[] bData = adapter.unmarshal(data);

        if (data.startsWith("00")) {
            // System.out.println("Trame GPS4 : ");
            // GPS4 : IM VM L1 L1 G1 G1 L2 G2 L3 G3 L4 G4
            // 4 positions GPS une position GPS toutes les 4mn
            String s1 = data.substring(2, 4);
            int Vitesse = Integer.parseInt(s1, 16);
            if (Vitesse < 0) {
                Vitesse = 256 + Vitesse;
            }
            String l1 = data.substring(4, 8);
            double L1 = (4*Integer.parseInt(l1, 16))/10000.0 + 36.0;
            String g1 = data.substring(8, 12);
            double G1 = (4*Integer.parseInt(g1, 16))/10000.0 - 10.0;

            String l2 = data.substring(12, 14);
            double L2 = L1 + Integer.parseInt(l2, 16)/1000.0 - 0.127;
            String g2 = data.substring(14, 16);
            double G2 = G1 + Integer.parseInt(g2, 16)/1000.0 - 0.127;

            String l3 = data.substring(16, 18);
            double L3 = L2 + Integer.parseInt(l3, 16)/1000.0 - 0.127;
            String g3 = data.substring(18, 20);
            double G3 = G2 + Integer.parseInt(g3, 16)/1000.0 - 0.127;

            String l4 = data.substring(20, 22);
            double L4 = L3 + Integer.parseInt(l4, 16)/1000.0 - 0.127;
            String g4 = data.substring(22, 24);
            double G4 = G3 + Integer.parseInt(g4, 16)/1000.0 - 0.127;
            // Sauvegarde des données
            // Position il y a 12mn
            Timestamp date4 = new Timestamp(Datation.getTime() + 3*4*60*1000);
            DonneesTR.create(con, SeqNumber, date4, Vitesse, 0, 
                                                L4, G4, 0L, VehiculeID);
            // Position il y a 8mn
            Timestamp date3 = new Timestamp(Datation.getTime() + 2*4*60*1000);
            DonneesTR.create(con, SeqNumber, date3, Vitesse, 0, 
                                                L3, G3, 0L, VehiculeID);
            // Position il y a 4mn
            Timestamp date2 = new Timestamp(Datation.getTime() + 4*60*1000);
            DonneesTR.create(con, SeqNumber, date2, Vitesse, 0, 
                                                L2, G2, 0L, VehiculeID);
            // Position actuelle
            DonneesTR.create(con, SeqNumber, Datation, Vitesse, 0, 
                                                L1, G1, 0L, VehiculeID);
            return "OK";
        } else if (data.startsWith("01")) {
            // PARKING : IM L1 L2 L3 G1 G2 G3 K1 K2 K3 K4 VX
            // le véhicule est garé : une seule position GPS mais plus précise
            // plus kilométrage, plus vitesse maxi
            String s1 = data.substring(2, 8);
            double LatGPS = (Integer.parseInt(s1, 16))/10000.0;
            String s2 = data.substring(8, 14);
            double LongGPS = (Integer.parseInt(s2, 16))/10000.0 - 10.0;
            //System.out.println(String.format("%2.5f;%2.5f;",L1,G1));
            String dist = data.substring(14, 22);
            long Dist = Integer.parseInt(dist, 16);
            String vx = data.substring(22, 24);
            int VX = Integer.parseInt(vx, 16);
            DonneesTR.create(con, SeqNumber, Datation, 0, VX, 
                                    LatGPS, LongGPS, (long)(Dist/1000), VehiculeID);
            return "OK";
        }
        return "ERREUR";
    }
    
    /**
     * Sauvegarde des informations sur le signal et la géolocalisation SIGFOX
     *    du message GEOLOC
     * @param con
     * @param sigfoxID
     * @param SeqNumber
     * @param Datation
     * @param latitudeSigfoxGPS
     * @param longitudeSigfoxGPS
     * @throws Exception 
     */
    public static void saveGeo(Connection con, String sigfoxID, int SeqNumber,
        Timestamp Datation, double latitudeSigfoxGPS, double longitudeSigfoxGPS) throws Exception {
        // A REVOIR
    }
    
    public static DonneesTR getLastByVehiculeID(Connection con,
              int Vehiculeid) throws Exception {
        String queryString = "select * from DonneesTR"
            + " where VehiculeID='" + Vehiculeid 
            + "' order by Datation desc limit 1";
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

    /**
     * Retourne une DonneesTR trouve par sa date, saved is true
     *
     * @param con
     * @param immatriculation date de DonneesTR a trouver
     * @return DonneesTR trouv" par immatriculation
     * @throws java.lang.Exception
     */
    public static DonneesTR getLastByImmatriculation(Connection con,
            String immatriculation) throws Exception {
        String queryString = "select * from DonneesTR,Vehicule"
                + " where Immatriculation = '" + immatriculation + "'"
                + " and Vehicule.ID = DonneesTR.VehiculeID"
                + " order by Datation desc limit 1";
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
     * Retourne une DonneesTR trouve par sa date, saved is true
     *
     * @param con
     * @param immatriculation date de DonneesTR a trouver
     * @return DonneesTR trouvées par immatriculation
     * @throws java.lang.Exception
     */
    public static ArrayList<DonneesTR> getByImmatriculation(Connection con,
            String immatriculation) throws Exception {
        String queryString = "select * from DonneesTR,Vehicule"
                + " where Immatriculation = '" + immatriculation + "'"
                + " and Vehicule.ID = DonneesTR.VehiculeID"
                + " order by Datation desc limit 1";
        Statement lStat = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        ArrayList<DonneesTR> lesDonneesTR = new ArrayList<>();
        // y en a t'il au moins un ?
        while (lResult.next()) {
             lesDonneesTR.add(creerParRequete(lResult));
        }
        return lesDonneesTR;
    }
    
    /**
     * Retourne les toutes les DonneesTR 'un véhicule
     * @param con
     * @param VehiculeID
     * @return DonneesTR trouvées par véhicule
     * @throws java.lang.Exception
     */
    public static ArrayList<DonneesTR> getByVehiculeID(Connection con,
              int VehiculeID) throws Exception {
        String queryString = "select * from DonneesTR"
            + " where VehiculeID='" + VehiculeID + "' order by Datation";
        Statement lStat = con.createStatement(
                                ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        ArrayList<DonneesTR> lesDonneesTR = new ArrayList<>();
        // y en a t'il au moins un ?
        while (lResult.next()) {
             lesDonneesTR.add(creerParRequete(lResult));
        }
        return lesDonneesTR;
    }
    
    /**
     * Retourne les DonneesTR entre deux dates (comprises)
     * @param con
     * @param VehiculeID
     * @param Day1
     * @param Day2
     * @return DonneesTR trouvé par véhicule et période
     * @throws java.lang.Exception
     */
    public static ArrayList<DonneesTR> getByVehiculeFromDayToDay(
            Connection con, String VehiculeID, 
            Timestamp Day1, Timestamp Day2) throws Exception {
        String queryString = "select * from DonneesTR"
                + " where VehiculeID = " + VehiculeID + " and between "
                + " substring(" +  Utils.toString(Day1) + " FROM 1 FOR 10)"
                + " and date_add(substring(" +  Utils.toString(Day2) 
                + " FROM 1 FOR 10), interval 1 day)";
        Statement lStat = con.createStatement(
                                ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        ArrayList<DonneesTR> lesDonneesTR = new ArrayList<>();
        // y en a t'il au moins un ?
        while (lResult.next()) {
             lesDonneesTR.add(creerParRequete(lResult));
        }
        return lesDonneesTR;
    }
    
    /**
     * Indique le nb de DonneesTR dans la base de données
     * @param con
     * @return le nombre de Vehicules
     * @throws java.lang.Exception
     */
    public static int size(Connection con) throws Exception {
        String queryString = "select count(*) as count from DonneesTR";
        Statement lStat = con.createStatement(
                                            ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                            ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        if (lResult.next())
            return (lResult.getInt("count"));
        else 
            return 0;
    }
     
    private static DonneesTR creerParRequete(ResultSet result) throws Exception {
        int lSeqNumber = result.getInt("SeqNumber");
        Timestamp lDatation = result.getTimestamp("Datation");
        int lVitesse = result.getInt("Vitesse");
        int lVitesseMax = result.getInt("VitesseMax");
        double lLatitudeGPS = result.getDouble("LatitudeGPS");
        double lLongitudeGPS = result.getDouble("LongitudeGPS");
        long lDistanceParcourueDepuisReset = result.getLong("DistanceParcourueDepuisReset");
        int lVehiculeID = result.getInt("VehiculeID");
        return new DonneesTR(lSeqNumber, lDatation, lVitesse, lVitesseMax, 
                             lLatitudeGPS, lLongitudeGPS,
                             lDistanceParcourueDepuisReset, lVehiculeID);
    }

    /**
     * Cree et initialise completement DonneesTR
     */
    private DonneesTR(int SeqNumber,
            Timestamp Datation, int Vitesse, int VitesseMax,
            double LatitudeGPS, double LongitudeGPS,
            long DistanceParcourueDepuisReset, int VehiculeID) {
        this.SeqNumber = SeqNumber;
        this.Datation = Datation;
        this.Vitesse = Vitesse;
        this.VitesseMax = VitesseMax;
        this.LatitudeGPS = LatitudeGPS;
        this.LongitudeGPS = LongitudeGPS;
        this.DistanceParcourueDepuisReset = DistanceParcourueDepuisReset;
        this.VehiculeID = VehiculeID;
    }

    public int getSeqNumber() {
        return SeqNumber;
    }

    public void setSeqNumber(int SeqNumber) {
        this.SeqNumber = SeqNumber;
    }

    public Timestamp getDatation() {
        return Datation;
    }

    public void setDatation(Timestamp Datation) {
        this.Datation = Datation;
    }

    public int getVitesse() {
        return Vitesse;
    }

    public void setVitesse(int Vitesse) {
        this.Vitesse = Vitesse;
    }

    public int getVitesseMax() {
        return VitesseMax;
    }

    public void setVitesseMax(int VitesseMax) {
        this.VitesseMax = VitesseMax;
    }

    public double getLatitudeGPS() {
        return LatitudeGPS;
    }

    public void setLatitudeGPS(double LatitudeGPS) {
        this.LatitudeGPS = LatitudeGPS;
    }

    public double getLongitudeGPS() {
        return LongitudeGPS;
    }

    public void setLongitudeGPS(double LongitudeGPS) {
        this.LongitudeGPS = LongitudeGPS;
    }

    public long getDistanceParcourueDepuisReset() {
        return DistanceParcourueDepuisReset;
    }

    public void setDistanceParcourueDepuisReset(long DistanceParcourueDepuisReset) {
        this.DistanceParcourueDepuisReset = DistanceParcourueDepuisReset;
    }

    public int getVehiculeID() {
        return VehiculeID;
    }

    /**
     * toString() operator overload
     *
     * @return the result string
     */
    @Override
    public String toString() {
        return " SeqNumber = " + Utils.toString(SeqNumber)
                + " Datation = " + Utils.toString(Datation)
                + " Vitesse = " + Utils.toString(Vitesse)
                + " VitesseMax = " + Utils.toString(VitesseMax)
                + " LatitudeGPSGPS = " + Utils.toString(LatitudeGPS)
                + " LongitudeGPSGPS = " + Utils.toString(LongitudeGPS)
                + " DistanceParcourue = " + Utils.toString(DistanceParcourueDepuisReset)
                + " VehiculeID = " + Utils.toString(VehiculeID);
    }
}
