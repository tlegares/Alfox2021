/*
 * Projet  : Alfox 2020
 * Fichier : Responsable.java
 * Description : Classe interface de la table user
 * Cette table stocke les infos sur les utilisateurs connus du logiciel
 */

package com.persistence;

import java.sql.*;

public class Responsable {
    private String    mdp;         // non null
    private String    mail;        // not null, unique
    
    /**
     * Créer un nouvel objet persistant 
     * @param con
     * @param mdp
     * @param mail
     * @return 
     * @ return  un user si le role est unique sinon null
     * @throws Exception    impossible d'accéder à la ConnexionMySQL
     *                      ou le role est deja dans la BD
     * 
     */
    static public Responsable create(Connection con, String mdp, String mail)  throws Exception {
        Responsable user = new Responsable(mdp, mail);
        
        String queryString =
         "insert into Responsable (Mdp, Mail) "
            + " values ("
                + Utils.toString(mdp) + ", " 
                + Utils.toString(mail)
            + ")";
        Statement lStat = con.createStatement();
        lStat.executeUpdate(queryString, Statement.NO_GENERATED_KEYS);
        return user;
    }
    
    /**
     * suppression de l'objet user dans la BD
     * @param con
     * @return 
     * @throws SQLException impossible d'accéder à la ConnexionMySQL
     */
    public boolean delete(Connection con) throws Exception {
        String queryString = "delete from Responsable where Mdp='" + mdp + "'";
        Statement lStat = con.createStatement();
        lStat.executeUpdate(queryString);
        return true;
    }
    
    /**
     * update de l'objet user dans la ConnexionMySQL
     * @param con
     * @throws Exception impossible d'accéder à la ConnexionMySQL
     */
    public void save(Connection con) throws Exception {
        String queryString =
         "update Responsable set "
                + " Mdp =" + Utils.toString(mdp) + ","  
                + " Mail =" + Utils.toString(mail)
                + " where Mdp ='" + mdp + "'";
        Statement lStat = con.createStatement();
        lStat.executeUpdate(queryString, Statement.NO_GENERATED_KEYS);
    }
    
    /**
     * Retourne un user trouve par son pseudo, saved is true
     * @param con
     * @param  mdp du pseudo à trouver
     * @return user trouve par mdp
     * @throws java.lang.Exception
     */
    public static Responsable getByMotDePasse(Connection con, String mdp) throws Exception {
        // cryptage du mdp
        String mdpCrypte = Utils.encryptPassword(mdp);
        String queryString = "select * from Responsable where Mdp='" + mdpCrypte + "'";
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
    
    private static Responsable creerParRequete(ResultSet result) throws Exception {
            String    lMdp = result.getString("Mdp");
            String    lMail = result.getString("Mail");
            return    new Responsable(lMdp,lMail);
    }
    
    /**
     * Cree et initialise completement User
     */
    private Responsable(String mdp, String mail) {
        this.mdp = mdp;
        this.mail = mail;
    }
    
    // --------------------- les assesseurs ----------------------------

    public String getMdp() {
        return mdp;
    }

    public String getMail() {
        return mail;
    }
    
    public void setMail(String mail) throws Exception {
        this.mail = mail;
    }

    public void setMdp(String mdp) throws Exception {
        this.mdp = mdp;
    }
    
    /**
     * toString() operator overload
     * @return   the result string
     */
    @Override
    public String toString() {
        return  " Mdp = " + Utils.toString(mdp) + 
                " Mail = " + Utils.toString(mail)
                + " ";
    }
}