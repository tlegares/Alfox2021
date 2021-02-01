/*
 * Projet  : Alfox
 * Fichier : User.java
 * Description : Classe interface de la table Position
 * Cette table stocke les points définissant chaque zone
 */

package com.persistence;

import java.sql.*;
import java.util.ArrayList;

public class Position {
    private int     ordre;           // non null
    private double  latitude;       // non null
    private double  longitude;      // non null
    private int     zoneID;
        
    /**
     * Créer un nouvel objet persistant 
     * @param con
     * @param ordre
     * @param zoneID
     * @param latitude
     * @param longitude
     * @return 
     * @ return retourne une Position
     * @throws Exception    impossible d'accéder à la ConnexionMySQL
     * 
     */
    static public Position create(Connection con, int zoneID, int ordre, double latitude, double longitude)  throws Exception {
        Position Position = new Position(zoneID, ordre, latitude, longitude);
        
        String queryString =
         "insert into Position (Ordre, ZoneLimiteID, Latitude, Longitude)"
            + " values ("
                + Utils.toString(ordre) + ", " 
                + Utils.toString(zoneID) + ", " 
                + Utils.toString(latitude) + ", " 
                + Utils.toString(longitude)
            + ")";
        Statement lStat = con.createStatement();
        lStat.executeUpdate(queryString, Statement.NO_GENERATED_KEYS);
        return Position;
    }
    
    /**
     * suppression de l'objet Position dans la BD
     * @param con
     * @return 
     * @throws SQLException impossible d'accéder à la ConnexionMySQL
     */
    public boolean delete(Connection con) throws Exception {
        String queryString = "delete from Position where Ordre='" + ordre + "'";
        Statement lStat = con.createStatement();
        lStat.executeUpdate(queryString);
        return true;
    }
    
    /**
     * update de l'objet Position dans la ConnexionMySQL
     * @param con
     * @throws Exception impossible d'accéder à la ConnexionMySQL
     */
    public void save(Connection con) throws Exception {
        String queryString =
         "update Position set "
                + " ZoneLimiteID =" + Utils.toString(zoneID) + ","
                + " Ordre =" + Utils.toString(ordre) + ","
                + " Latitude =" + Utils.toString(latitude) + "," 
                + " Longitude =" + Utils.toString(longitude)
                + " where Ordre = " + ordre
                + " and ZoneLimiteID = " + zoneID;
        Statement lStat = con.createStatement();
        lStat.executeUpdate(queryString, Statement.NO_GENERATED_KEYS);
    }
    
    public static Position getById(Connection con, int id) throws Exception {
        String queryString = "select * from Position where ID=" + id;
        Statement lStat = con.createStatement(
                                ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        if (lResult.next()) {
            int _zoneID = lResult.getInt("ZoneLimiteID");
            return creerParRequete(lResult, _zoneID);
        }
        return null;
    }
    
    /**
     * Retourne une collection de Positions trouvées par nom de zone, saved is true
     * @param con
     * @param zoneID
     * @return collection de Positions
     * @throws java.lang.Exception
     */
    public static ArrayList<Position> getByZone(Connection con, int zoneID) throws Exception {
        String queryString = "select * from Position"
            + " where ZoneLimiteID='" + zoneID + "'"
            + " order by Ordre";
        Statement lStat = con.createStatement(
                                ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        ArrayList<Position> lstPos = new ArrayList<>();
        while (lResult.next()) {
            lstPos.add(creerParRequete(lResult, zoneID));
        }
        return lstPos;
    }
    
    /*
        retourne l'ID d'un objet
    */
    public int getID(Connection con) throws Exception {
        String queryString = "select ID from Position"
            + " ZoneLimite='" + zoneID + "'";
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
    
     /**
     * Indique le nb de Position dans la base de données
     * @param con
     * @return le nombre de vehicules
     * @throws java.lang.Exception
     */
    public static int size(Connection con) throws Exception {
        String queryString = "select count(*) as count from Position";
        Statement lStat = con.createStatement(
                                            ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                            ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        if (lResult.next())
            return (lResult.getInt("count"));
        else 
            return 0;
    }
    
    /**
     * Retourne la liste des points par l'ID de la zone
     * @param con
     * @param  zoneID le numero à trouver
     * @return les points
     * @throws java.lang.Exception
     */
    public static  ArrayList<Position> getByZoneLimiteID(Connection con, int zoneID) throws Exception {
        ArrayList<Position> lesPositions = new ArrayList<>();
        String queryString = "select * from Position where ZoneLimiteID='" + zoneID + "'";
        Statement lStat = con.createStatement(
                                ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        // y en a t'il au moins un ?
        while (lResult.next()) {
             lesPositions.add(creerParRequete(lResult, zoneID));
        }
        return lesPositions;
        
    }
    
    private static Position creerParRequete(ResultSet result, int zoneID) throws Exception {
            int       lOrdre  = result.getInt("Ordre");
            double    lLatitude = result.getDouble("Latitude");
            double    lLongitude = result.getDouble("Longitude");
            return    new Position(zoneID, lOrdre, lLatitude, lLongitude);
    }
    
    /**
     * Cree et initialise completement Position
     */
    private Position(int zoneID, int ordre, double latitude, double longitude) {
        this.zoneID = zoneID;
        this.ordre = ordre;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // --------------------- les assesseurs ----------------------------
    public int getOrdre() {
        return ordre;
    }

    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) throws Exception {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) throws Exception {
        this.longitude = longitude;
    }

    public int getZoneID() {
        return zoneID;
    }
    
    /**
     * toString() operator overload
     * @return   the result string
     */
    @Override
    public String toString() {
        return  " zoneID =  " + zoneID + "\t" +
                " Ordre =  " + ordre + "\t" +
                " Latitude = " + Utils.toString(latitude) + 
                " Longitude = " + Utils.toString(longitude)
                + " ";
    }
}