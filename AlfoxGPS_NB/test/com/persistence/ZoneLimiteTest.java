/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.persistence;

import java.sql.Connection;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author acros
 */
public class ZoneLimiteTest {
    /**
     * Test of create method, of class ZoneLimite.
     * @throws java.lang.Exception
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Connection con = ConnexionMySQL.getInstance();
        String nom = "Paris";
        ZoneLimite result = ZoneLimite.create(con, nom, 10);
        assertEquals("Paris", result.getNom());
        assertEquals(10, result.getZoom());
        result.delete(con);
    }

    /**
     * Test of save method, of class ZoneLimite.
     * @throws java.lang.Exception
     */
    @Test
    public void testSave() throws Exception {
        System.out.println("save");
        Connection con = ConnexionMySQL.getInstance();
        ZoneLimite instance = ZoneLimite.getByNom(con, "Toulouse");
        instance.setNom("Toulouse2");
        instance.save(con);
        instance = ZoneLimite.getByNom(con, "Toulouse2");
        assertEquals("Toulouse2", instance.getNom());
        instance.setNom("Toulouse");
        instance.save(con);
    }

    /**
     * Test of getByNom method, of class ZoneLimite.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetByNom() throws Exception {
        System.out.println("getByNom");
        Connection con = ConnexionMySQL.getInstance();
        ZoneLimite result = ZoneLimite.getByNom(con, "Alcis");
        assertEquals("Alcis", result.getNom());
    }

    /**
     * Test of getNom method, of class ZoneLimite.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetNom() throws Exception {
        System.out.println("getNom");
        Connection con = ConnexionMySQL.getInstance();
        ZoneLimite result = ZoneLimite.getByNom(con, "Alcis");
        assertEquals("Alcis", result.getNom());
    }

    /**
     * Test of setNom method, of class ZoneLimite.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetNom() throws Exception {
        System.out.println("setNom");
        Connection con = ConnexionMySQL.getInstance();
        ZoneLimite instance = ZoneLimite.getByNom(con, "Alcis");
        instance.setNom("Alcis2");
        instance.save(con);
        assertEquals(instance.getNom(), "Alcis2");
        instance.setNom("Alcis");
        instance.save(con);
    }

    /**
     * Test of getLatCentre method, of class ZoneLimite.
     */
    @Test
    public void testGetLatCentre() throws Exception {
        System.out.println("getLatCentre");
        Connection con = ConnexionMySQL.getInstance();
        assertEquals(43.473456, ZoneLimite.getLatCentre(con, "Toulouse"), 0.000001);
    }

    /**
     * Test of getLgCentre method, of class ZoneLimite.
     */
    @Test
    public void testGetLgCentre() throws Exception {
        System.out.println("getLgCentre");
        Connection con = ConnexionMySQL.getInstance();
        assertEquals(1.499457, ZoneLimite.getLgCentre(con, "Toulouse"), 0.000001);
    }

    /**
     * Test of size method, of class ZoneLimite.
     */
    @Test
    public void testSize() throws Exception {
        System.out.println("size");
        Connection con = ConnexionMySQL.getInstance();
        int result = ZoneLimite.size(con);
        assertEquals(4, result);
    }

    /**
     * Test of getLstZone method, of class ZoneLimite.
     */
    @Test
    public void testGetLstZone() throws Exception {
        System.out.println("getLstZone");
        Connection con = ConnexionMySQL.getInstance();
        ArrayList<ZoneLimite> result = ZoneLimite.getLstZone(con);
        assertEquals(4, result.size());
        assertEquals("Alcis", result.get(0).getNom());
        assertEquals("Toulouse", result.get(1).getNom());
    }
    
    /**
     * Test of getZoneLimites method, of class ZoneLimite.
     */
    @Test
    public void testGetZoneLimites() throws Exception {
        System.out.println("getZoneLimites");
        Connection con = ConnexionMySQL.getInstance();
        ArrayList<Double> result = ZoneLimite.getZoneLimites(con, "Alcis");
        assertEquals(8, result.size());
        assertEquals(43.604014, result.get(0), 0.000001);
        assertEquals(1.526581, result.get(1), 0.000001);
        assertEquals(43.60192, result.get(6), 0.000001);
        assertEquals(1.530292, result.get(7), 0.000001);
    }

    /**
     * Test of getID method, of class ZoneLimite.
     */
    @Test
    public void testGetID_Connection() throws Exception {
        System.out.println("getID");
        Connection con = ConnexionMySQL.getInstance();
        ZoneLimite result = ZoneLimite.getByNom(con, "Alcis");
        assertEquals(1, result.getID());
    }
}
