/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.persistence;

import java.sql.Connection;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author acros
 */
public class PositionTest {

    /**
     * Test of create method, of class Position.
     * @throws java.lang.Exception
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Connection con = ConnexionMySQL.getInstance();
        int ordre = 12;
        double latitude = 40.123456;
        double longitude = 50.123456;
        int zoneLimiteID = 1;
        Position result = Position.create(con, zoneLimiteID, ordre, latitude, longitude);
        assertEquals(12, result.getOrdre());
        result.delete(con);
    }

    /**
     * Test of save method, of class Position.
     * @throws java.lang.Exception
     */
    @Test
    public void testSave() throws Exception {
        System.out.println("save");
        Connection con = ConnexionMySQL.getInstance();
        Position result = Position.getById(con, 1);
        result.setLatitude(43.546266); 
        result.save(con);
        result = Position.getById(con, 1);
        assertEquals(43.546266, result.getLatitude(), 0.000001);
        // on restitue l'état initial
        result.setLatitude(43.604014);
        result.save(con);
    }

    /**
     * Test of getByZone method, of class Position.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetByZone() throws Exception {
        System.out.println("getByZone");
        Connection con = ConnexionMySQL.getInstance();
        ArrayList<Position> result = Position.getByZone(con, 2);
        assertEquals(result.size(), 10);
        assertEquals(43.798589, result.get(2).getLatitude(), 0.000001);
    }
    
    /**
     * Test of getOrdre method, of class Position.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetOrdre() throws Exception {
        System.out.println("getOrdre");
        Connection con = ConnexionMySQL.getInstance();
        ArrayList<Position> result = Position.getByZone(con, 2);
        assertEquals(1, result.get(0).getOrdre());
    }

    /**
     * Test of getLatitude method, of class Position.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetLatitude() throws Exception {
        System.out.println("getLatitude");
        Connection con = ConnexionMySQL.getInstance();
        ArrayList<Position> result = Position.getByZone(con, 2);
        assertEquals(44.087321, result.get(0).getLatitude(), 0.000001);
    }

    /**
     * Test of getLongitude method, of class Position.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetLongitude() throws Exception {
        System.out.println("getLongitude");
        Connection con = ConnexionMySQL.getInstance();
        ArrayList<Position> result = Position.getByZone(con, 2);
        assertEquals(1.338782, result.get(0).getLongitude(), 0.000001);
    }

    /**
     * Test of setLatitude method, of class Position.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetLatitude() throws Exception {
        System.out.println("setLatitude");
        Connection con = ConnexionMySQL.getInstance();
        Position result = Position.getById(con, 2);
        result.setLatitude(43.546266);
        result.save(con);
        result = Position.getById(con, 2);
        assertEquals(43.546266, result.getLatitude(), 0.000001);
        // on restitue l'état initial
        result.setLatitude(43.601590);
        result.save(con);
    }

    /**
     * Test of setLongitude method, of class Position.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetLongitude() throws Exception {
        System.out.println("setLongitude");
        Connection con = ConnexionMySQL.getInstance();
        Position result = Position.getById(con, 2);
        result.setLongitude(43.546266);
        result.save(con);
        result = Position.getById(con, 2);
        assertEquals(43.546266, result.getLongitude(), 0.000001);
        // on restitue l'état initial
        result.setLongitude(1.524203);
        result.save(con);
    }

    /**
     * Test of getByZoneLimiteID method, of class Position.
     */
    @Test
    public void testGetByZoneLimiteID() throws Exception {
        System.out.println("getByZoneLimiteID");
        Connection con = ConnexionMySQL.getInstance();
        ArrayList<Position> position = Position.getByZoneLimiteID(con, 2);
        assertEquals(10, position.size());
        assertEquals(1, position.get(0).getOrdre());
        assertEquals(3, position.get(2).getOrdre());   
        assertEquals(4, position.get(3).getOrdre());
    }

    /**
     * Test of size method, of class Position.
     */
    @Test
    public void testSize() throws Exception {
        System.out.println("size");
        Connection con = ConnexionMySQL.getInstance();
        int result = Position.size(con);
        assertEquals(25, result);
    }
}
