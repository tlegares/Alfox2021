
<%@page import="java.sql.Connection"%>
<%@page import="com.persistence.Vehicule"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ include file="/includes/controle.jspf" %>

<%
    // retourne par Ajax les infos du véhicule concernée
    String immatriculation = request.getParameter("immatriculation");
    Vehicule v = Vehicule.getByImmatriculation(con, immatriculation);
    out.println(
        v.getImmatriculation()
        + "|" + ZoneLimite.getByID(con, v.getZoneLimiteID()).getNom()
        + "|" + String.format("%2.1f", v.getAge(con)/365.0)
        + "|" + v.getTauxUtilisation() + "%"
        + "|" + (int)(v.getKilometrageReel() + v.getKilometrageCumule())
        + "|" + (int)((v.getKilometrageReel() + v.getKilometrageCumule())/(v.getAge(con)/30.5))
    );
%>