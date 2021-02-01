/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.persistence;

import java.sql.Connection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author snir2g1
 */
public class BoitierTest {

    /**
     * Test of getByID method, of class Boitier.
     */
    @Test
    public void testGetVehiculeID() throws Exception {
        System.out.println("getByID");
        Connection con = ConnexionMySQL.getInstance();
        Boitier boitier = Boitier.getBySigfoxID(con, "1D2289");
        assertEquals(1, boitier.getVehiculeID());
        boitier = Boitier.getBySigfoxID(con, "1D188E");
        assertEquals(2, boitier.getVehiculeID());
    }

    /**
     * Test of getByVehiculeID method, of class Boitier.
     */
    @Test
    public void testGetByVehiculeID() throws Exception {
        System.out.println("getByVehiculeID");
        Connection con = ConnexionMySQL.getInstance();
        Boitier boitier = Boitier.getByVehiculeID(con, 1);
        assertEquals("1D2289", boitier.getSigfoxID());
        boitier = Boitier.getByVehiculeID(con, 2);
        assertEquals("1D188E", boitier.getSigfoxID());
    }

    /**
     * Test of size method, of class Boitier.
     */
    @Test
    public void testSize() throws Exception {
        System.out.println("size");
        Connection con = ConnexionMySQL.getInstance();
        int result = Boitier.size(con);
        assertEquals(9, result);
    }
}
