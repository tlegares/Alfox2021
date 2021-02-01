/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Modification 28 Mai 2018 :
 *      remplacement du terme 'Device' et du champ 'device' par 'VehiculeID'
 */
package com.persistence;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author acros
 */
public class DonneesTRTest {

    /**
     * Test of create method, of class DonneesTR.
     * @throws java.lang.Exception
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Connection con = ConnexionMySQL.getInstance();
        Timestamp datation = Utils.stringToTimestamp("2017/03/22 12:00:00");
        int vitesse = 50;
        int vitesseMax = 83;
        double latitudeGPS = 40.123456;
        double longitudeGPS = 50.123456;
        long distanceParcourue = 50;
        int seqNumber = 52;
        int vehiculeID = 1;
        DonneesTR result = DonneesTR.create(con, seqNumber, datation,
                vitesse, vitesseMax, latitudeGPS, longitudeGPS,
                distanceParcourue, vehiculeID);
        assertEquals(Utils.stringToTimestamp("2017/03/22 12:00:00.0"), result.getDatation());
        result.delete(con);
    }

    /**
     * Test of save method, of class DonneesTR.
     * @throws java.lang.Exception
     */
    @Test
    public void testSave() throws Exception {
        System.out.println("save");
        Connection con = ConnexionMySQL.getInstance();
        Timestamp datation = Utils.stringToTimestamp("2017/03/22 12:00:00");
        int vitesse = 50;
        int vitesseMax = 83;
        double latitudeGPS = 40.123456;
        double longitudeGPS = 50.123456;
        int radius = 0;
        long distanceParcourue = 50;
        int seqNumber = 53;
        int VehiculeID = 1;
        DonneesTR instance = DonneesTR.create(con, seqNumber, datation,
                vitesse, vitesseMax, latitudeGPS, longitudeGPS,
                distanceParcourue, VehiculeID);
        instance.save(con);
        assertEquals(50, instance.getVitesse());
        instance.delete(con);
    }

    /**
     * Test of getDatation method, of class DonneesTR.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetDatation() throws Exception {
        System.out.println("getDatation");
        Connection con = ConnexionMySQL.getInstance();
        DonneesTR instance = DonneesTR.getLastByImmatriculation(con, "ED-593-VS");
        assertEquals(Utils.stringToTimestamp("2020/04/20 00:10:00.0"), instance.getDatation());
    }

    /**
     * Test of getVitesse method, of class DonneesTR.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetVitesse() throws Exception {
        System.out.println("getVitesse");
        Connection con = ConnexionMySQL.getInstance();
        DonneesTR instance = DonneesTR.getLastByImmatriculation(con, "ED-593-VS");
        assertEquals(22, instance.getVitesse());
    }

    /**
     * Test of getVitesseMax method, of class DonneesTR.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetVitesseMax() throws Exception {
        System.out.println("getVitesseMax");
        Connection con = ConnexionMySQL.getInstance();
        DonneesTR instance = DonneesTR.getLastByImmatriculation(con, "ED-593-VS");
        assertEquals(22, instance.getVitesseMax());
    }

    /**
     * Test of getLatitude method, of class DonneesTR.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetLatitudeGPS() throws Exception {
        System.out.println("getLatitudeGPS");
        Connection con = ConnexionMySQL.getInstance();
        DonneesTR instance = DonneesTR.getLastByImmatriculation(con, "ED-593-VS");
        assertEquals(19.368961, instance.getLatitudeGPS(), 0.00001);
    }

    /**
     * Test of getLongitude method, of class DonneesTR.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetLongitudeGPS() throws Exception {
        System.out.println("getLongitudeGPS");
        Connection con = ConnexionMySQL.getInstance();
        DonneesTR instance = DonneesTR.getLastByImmatriculation(con, "ED-593-VS");
        assertEquals(33.811088, instance.getLongitudeGPS(), 0.00001);
    }

    /**
     * Test of getDistanceParcourue method, of class DonneesTR.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetDistanceParcourueDepuisReset() throws Exception {
        System.out.println("getDistanceParcourue");
        Connection con = ConnexionMySQL.getInstance();
        DonneesTR instance = DonneesTR.getLastByImmatriculation(con, "ED-593-VS");
        assertEquals(3264, instance.getDistanceParcourueDepuisReset());
    }

    /**
     * Test of getSeqNumber method, of class DonneesTR.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetSeqNumber() throws Exception {
        System.out.println("getSeqNumber");
        Connection con = ConnexionMySQL.getInstance();
        DonneesTR instance = DonneesTR.getLastByImmatriculation(con, "ED-593-VS");
        assertEquals(14, instance.getSeqNumber());
    }

    /**
     * Test of getVehiculeID method, of class DonneesTR.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetVehiculeID() throws Exception {
        System.out.println("getVehiculeID");
        Connection con = ConnexionMySQL.getInstance();
        DonneesTR instance = DonneesTR.getLastByImmatriculation(con, "ED-592-CY");
        assertEquals(1, instance.getVehiculeID());
    }

    /**
     * Test of getLastByImmatriculation method, of class DonneesTR.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetLastByImmatriculation() throws Exception {
        System.out.println("getLastByImmatriculation");
        Connection con = ConnexionMySQL.getInstance();
        DonneesTR result = DonneesTR.getLastByImmatriculation(con, "ED-592-CY");
        assertEquals(Utils.stringToTimestamp("2020/04/20 00:00:00.0"), result.getDatation());
    }

    /**
     * Test of getByDate method, of class DonneesTR.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetFromDayToDay() throws Exception {
        System.out.println("getByDate");
        Connection con = ConnexionMySQL.getInstance();
        // A FAIRE
    }

    /**
     * Test of getByVehiculeID method, of class DonneesTR.
     */
    @Test
    public void testGetByVehiculeID() throws Exception {
        System.out.println("getByVehiculeID");
        Connection con = ConnexionMySQL.getInstance();
        // A FAIRE
    }

    /**
     * Test of saveData method, of class DonneesTR.
     */
    @Test
    public void testSaveData() throws Exception {
        System.out.println("saveData");
        Connection con = ConnexionMySQL.getInstance();
        String sigfoxID = "1D2289";
        int seqNumber = 600;
        Timestamp datation = Utils.stringToTimestamp("2021/01/12 12:00:00");
        String data ="0106a4cd01c06c00045e5f80";
        DonneesTR.saveData(con, sigfoxID, seqNumber, datation, data);
        ArrayList<DonneesTR> instance = DonneesTR.getByVehiculeID(con, 1);
        assertEquals(286, instance.get(instance.size() - 1).getDistanceParcourueDepuisReset());
        assertEquals(128, instance.get(instance.size() - 1).getVitesseMax());
        assertEquals(0, instance.get(instance.size() - 1).getVitesse());
        assertEquals(43.5405, instance.get(instance.size() - 1).getLatitudeGPS(), 0.0001);
        assertEquals(1.4796, instance.get(instance.size() - 1).getLongitudeGPS(), 0.0001);
        instance.get(instance.size() - 1).delete(con);
    }
}