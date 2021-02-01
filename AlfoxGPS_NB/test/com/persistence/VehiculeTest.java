/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.persistence;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author acros
 */
public class VehiculeTest {
    /**
     * Test of create method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Connection con = ConnexionMySQL.getInstance();
        String marque = "Citroën";
        String modele = "DS5";
        String immatriculation = "DD-000-EE";
        String motorisation = "Diesel";
        Timestamp dateMiseEnService = Utils.stringToTimestamp("2018/03/26 00:00:00");
        boolean horsZone = false;
        int tauxUtilisation = 0;
        double KilometrageReel = 5000.0;
        double KilometrageCumule = 0.0;
        int ZoneLimiteID = 1;
        Vehicule result = Vehicule.create(con,
            marque, modele, immatriculation, motorisation,
            dateMiseEnService, horsZone, tauxUtilisation,
            KilometrageReel, KilometrageCumule, ZoneLimiteID);
        assertEquals("DS5", result.getModele());
        result.delete(con);
    }

    /**
     * Test of save method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testSave() throws Exception {
        System.out.println("save");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule instance = Vehicule.getByImmatriculation(con, "ED-592-CY");
        instance.setKilometrageReel(2540.652);
        instance.save(con);
        instance = Vehicule.getByImmatriculation(con, "ED-592-CY");
        assertEquals(2540.652F, instance.getKilometrageReel(), 0.1);
        instance.setKilometrageReel(40787.0);
        instance.save(con);
    }

    /**
     * Test of getByImmatriculation method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetByImmatriculation() throws Exception {
        System.out.println("getByImmatriculation");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule result = Vehicule.getByImmatriculation(con, "ED-592-CY");
        assertEquals("ED-592-CY", result.getImmatriculation());
    }
    
    /**
     * Test of isHorsZone method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testIsDehors() throws Exception {
        System.out.println("isHorsZone");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule result = Vehicule.getByImmatriculation(con, "ED-592-CY");
        assertEquals(false, result.isHorsZone(con));
        result = Vehicule.getByImmatriculation(con, "ED-593-VS");
        assertEquals(true, result.isHorsZone(con));
        result = Vehicule.getByImmatriculation(con, "EE-239-QM");
        assertEquals(true, result.isHorsZone(con));
        
        // A vérifier celui là, car il est juste à l'Est de Balma !!!
        result = Vehicule.getByImmatriculation(con, "EE-300-QM");
        assertEquals(true, result.isHorsZone(con));
        
        result = Vehicule.getByImmatriculation(con, "EK-462-GX");
        assertEquals(true, result.isHorsZone(con));
        result = Vehicule.getByImmatriculation(con, "EM-045-BC");
        assertEquals(true, result.isHorsZone(con));
        result = Vehicule.getByImmatriculation(con, "EM-150-BE");
        assertEquals(false, result.isHorsZone(con));
    }

    /**
     * Test of nbVehiculesDehors method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testNbVehiculesDehors() throws Exception {
        System.out.println("nbVehiculesDehors");
        Connection con = ConnexionMySQL.getInstance();
        int result = Vehicule.nbVehiculesDehors(con);
        assertEquals(4, result);
    }
    
    /**
     * Test of getMarque method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetMarque() throws Exception {
        System.out.println("getMarque");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule result = Vehicule.getByImmatriculation(con, "ED-592-CY");
        assertEquals("Renault", result.getMarque());
    }

    /**
     * Test of getModele method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetModele() throws Exception {
        System.out.println("getModele");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule result = Vehicule.getByImmatriculation(con, "ED-592-CY");
        assertEquals("Talisman", result.getModele());
    }

    /**
     * Test of getImmatriculation method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetImmatriculation() throws Exception {
        System.out.println("getImmatriculation");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule result = Vehicule.getByImmatriculation(con, "ED-592-CY");
        assertEquals("ED-592-CY", result.getImmatriculation());
    }

    /**
     * Test of getDateMiseEnService method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetDateMiseEnService() throws Exception {
        System.out.println("getDateMiseEnService");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule result = Vehicule.getByImmatriculation(con, "ED-592-CY");
        assertEquals(Utils.stringToTimestamp("2018/01/01 00:00:00.0"), result.getDateMiseEnService());
    }

    /**
     * Test of getMotorisation method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetMotorisation() throws Exception {
        System.out.println("getMotorisation");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule result = Vehicule.getByImmatriculation(con, "ED-592-CY");
        assertEquals("Diesel", result.getMotorisation());
    }

    /**
     * Test of getHorsZone method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetHorsZone() throws Exception {
        System.out.println("getHorsZone");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule result = Vehicule.getByImmatriculation(con, "ED-592-CY");
        assertEquals(false, result.isHorsZone());
    }

    /**
     * Test of getTauxUtilisation method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetTauxUtilisation() throws Exception {
        System.out.println("getTauxUtilisation");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule result = Vehicule.getByImmatriculation(con, "ED-592-CY");
        assertEquals(100, result.getTauxUtilisation());
    }

    /**
     * Test of getKilometrageReel method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetKilometrageReel() throws Exception {
        System.out.println("getKilometrageReel");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule result = Vehicule.getByImmatriculation(con, "ED-592-CY");
        assertEquals(result.getKilometrageReel(), 40787.0, 0.1);
    }

    /**
     * Test of setHorsZone method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetHorsZone() throws Exception {
        System.out.println("setHorsZone");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule instance = Vehicule.getByImmatriculation(con, "ED-592-CY");
        instance.setHorsZone(true);
        instance.save(con);
        assertEquals(instance.isHorsZone(), true);
        instance.setHorsZone(false);
        instance.save(con);
    }

    /**
     * Test of setTauxUtilisation method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetTauxUtilisation() throws Exception {
        System.out.println("setTauxUtilisation");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule instance = Vehicule.getByImmatriculation(con, "ED-592-CY");
        instance.setTauxUtilisation(13);
        instance.save(con);
        assertEquals(instance.getTauxUtilisation(), 13);
        instance.setTauxUtilisation(100);
        instance.save(con);
    }

    /**
     * Test of setKilometrageReel method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetKilometrageReel() throws Exception {
        System.out.println("setKilometrageReel");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule instance = Vehicule.getByImmatriculation(con, "ED-592-CY");
        instance.setKilometrageReel(45270.31F);
        instance.save(con);
        assertEquals(instance.getKilometrageReel(), 45270.31F, 0.01);
        instance.setKilometrageReel(40787.0F);
        instance.save(con);
    }

    /**
     * Test of getImmatriculations method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetImmatriculations() throws Exception {
        System.out.println("getImmatriculations");
        Connection con = ConnexionMySQL.getInstance();
        ArrayList<String> result = Vehicule.getImmatriculations(con);
        assertEquals(9, result.size());
        assertEquals("ED-592-CY", result.get(0));
        assertEquals("EM-862-ML", result.get(7));
    }

    /**
     * Test of getKmMoyenFlotte method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetKmMoyenFlotte() throws Exception {
        System.out.println("getKmMoyenFlotte");
        Connection con = ConnexionMySQL.getInstance();
        int result = Vehicule.getKmMoyenFlotte(con);
        assertEquals(31564, result);
    }

    /**
     * Test of getKmMoyenMensuelFlotte method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetKmMoyenMensuelFlotte() throws Exception {
        System.out.println("getKmMoyenMensuelFlotte");
        Connection con = ConnexionMySQL.getInstance();
        int result = Vehicule.getKmMoyenMensuelFlotte(con);
        assertEquals(1214, result);
    }

    /**
     * Test of getAgeMoyenFlotte method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetAgeMoyenFlotte() throws Exception {
        System.out.println("getAgeMoyenFlotte");
        Connection con = ConnexionMySQL.getInstance();
        int result = Vehicule.getAgeMoyenFlotte(con);
        assertEquals(797, result);  // le 1/2/2021
    }

    /**
     * Test of size method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testSize() throws Exception {
        System.out.println("size");
        Connection con = ConnexionMySQL.getInstance();
        int result = Vehicule.size(con);
        assertEquals(9, result);
    }

    /**
     * Test of getTempsAlcis method, of class Vehicule.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetTempsAlcis() throws Exception {
        System.out.println("getTempsAlcis");
        Connection con = ConnexionMySQL.getInstance();
        int result = Vehicule.getTempsAlcis(con, "EE-300-QM");
        // au garage depuis le 20 mars
        // (12 + 30 + 28)*24*60 = 100 800 mn au 29 Mai
        // (12 + 30 + 31 + 30)*24*60 = 148 320 au 31 Juin
        // assertTrue(result > 100800);
        // assertTrue(result < 148320);
    }

    /**
     * Test of getID method, of class Vehicule.
     */
    @Test
    public void testGetID() throws Exception {
        System.out.println("getID");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule instance = Vehicule.getByImmatriculation(con, "ED-592-CY");
        int id = instance.getID(con);
        assertEquals(1, id);
        instance = Vehicule.getByImmatriculation(con, "EM-862-ML");
        id = instance.getID(con);
        assertEquals(8, id);
    }

    /**
     * Test of getByID method, of class Vehicule.
     */
    @Test
    public void testGetByID() throws Exception {
        System.out.println("getByID");
        Connection con = ConnexionMySQL.getInstance();
        Vehicule result = Vehicule.getByID(con, 1);
        assertEquals("ED-592-CY", result.getImmatriculation());
    }
}
