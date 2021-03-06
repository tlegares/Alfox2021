/*
 * Fichier : ConnexionMySQL.java
 * Description : Classe interface de la connexion à la base de données alfox
 * Cette table gère l'accès à la BD alfox
 */

package com.persistence;

import java.sql.*;

/**
 * Singleton instance that gives the connection to the database
 * @author  LVH
 * @since   02/2014 
 * 
 * @version 0.1.0
 *
 */
public class ConnexionMySQL {
// ---------------------------------------------------------------------
//                          MySQL changes
// ---------------------------------------------------------------------
    /** driverDst : MySQl driver  */
    private static String driver    = "com.mysql.cj.jdbc.Driver";
    /** url : ConnexionMySQL URL */
    private static String url       = "jdbc:mysql://localhost:3306/alfoxGPS?zeroDateTimeBehavior=convertToNull";
// ---------------------------------------------------------------------
    /** userName : eventskytracker */
    private static String userName  = "alfoxGPS";
    /** password : estNovae31 */
    private static String password  = "alfox31";
    /** destination connection on the database */
    private static Connection conn  = null;
    
    private ConnexionMySQL() throws Exception { }
    
    /**
     * getter for the ConnexionMySQL instance
     * @return the ConnexionMySQL instance
     * @throws java.lang.Exception
     */    
    public static Connection getInstance() throws Exception  {
       if (conn == null) {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, userName, password);
        }
        return conn;
    }
    
    // Test
    public static void main(String[] argv) throws Exception {
        Connection con = ConnexionMySQL.getInstance();
        System.out.println(Vehicule.size(con));
    }
}

