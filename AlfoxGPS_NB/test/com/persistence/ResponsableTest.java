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
 * @author acros
 */
public class ResponsableTest {

    /**
     * Test of create method, of class Responsable.
     * @throws java.lang.Exception
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Connection con = ConnexionMySQL.getInstance();
        String mdp = "technicien";
        String mail = "technicien@gmail.com";
        Responsable result = Responsable.create(con, mdp, mail);
        assertEquals("technicien@gmail.com", result.getMail());
        result.delete(con);
    }

    /**
     * Test of save method, of class Responsable.
     * @throws java.lang.Exception
     */
    @Test
    public void testSave() throws Exception {
        System.out.println("save");
        Connection con = ConnexionMySQL.getInstance();
        Responsable instance = Responsable.getByMotDePasse(con, "responsable");
        instance.setMail("nouveau@gmail.com");
        instance.save(con);
        instance = Responsable.getByMotDePasse(con, "responsable");
        assertEquals("nouveau@gmail.com", instance.getMail());
        instance.setMail("responsable@free.fr");
        instance.save(con);
    }

    /**
     * Test of getByMotDePasse method, of class Responsable.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetByMotDePasse() throws Exception {
        System.out.println("getByMotDePasse");
        Connection con = ConnexionMySQL.getInstance();
        Responsable result = Responsable.getByMotDePasse(con, "responsable");
        assertEquals("responsable@free.fr", result.getMail());
    }

    /**
     * Test of getMdp method, of class Responsable.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetMdp() throws Exception {
        System.out.println("getMdp");
        Connection con = ConnexionMySQL.getInstance();
        Responsable instance = Responsable.getByMotDePasse(con, "responsable");
        assertEquals(Utils.encryptPassword("responsable"), instance.getMdp());
    }

    /**
     * Test of getMail method, of class Responsable.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetMail() throws Exception {
        System.out.println("getMail");
        Connection con = ConnexionMySQL.getInstance();
        Responsable instance = Responsable.getByMotDePasse(con, "responsable");
        assertEquals("responsable@free.fr", instance.getMail());
    }

    /**
     * Test of setMail method, of class Responsable.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetMail() throws Exception {
        System.out.println("setMail");
        Connection con = ConnexionMySQL.getInstance();
        Responsable instance = Responsable.getByMotDePasse(con, "responsable");
        instance.setMail("nouveaumail@gmail.com");
        instance.save(con);
        assertEquals(instance.getMail(), "nouveaumail@gmail.com");
        instance.setMail("responsable@free.fr");
        instance.save(con);
    }

    /**
     * Test of setMdp method, of class Responsable.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetMdp() throws Exception {
        System.out.println("setMdp");
        Connection con = ConnexionMySQL.getInstance();
        Responsable instance = Responsable.getByMotDePasse(con, "responsable");
        instance.setMdp("nouveau");
        instance.save(con);
        assertEquals(instance.getMdp(), "nouveau");
        instance.setMdp("responsable");
        instance.save(con);
    }
}
