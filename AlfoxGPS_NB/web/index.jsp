
<%@page import="java.sql.Connection"%>
<%@page import="com.persistence.ConnexionMySQL"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Accueil</title> 
        <%@ include file="/includes/head.jspf" %>
    </head>
    <body>
        <div data-role="page" id="page1">
            <%@ include file="/includes/header.jspf" %>
            <%
                String action = request.getParameter("action");
                if (action == "deconnexion") {
                    session.setAttribute("user", null); 
                }
            %>
            <div role="main" class="ui-content">
                <center>
                    <br/><br/><br/>
                    <h2>Suivi et Maintenance <br/>d'une flotte de véhicules <br/>V2.0</h2>
                    <br/><br/>
                    <div class="logo"><img src="images/logo.png"/></div>  
                    <br/>
                    <div>
                        <%
                            String message = request.getParameter("message");
                            if (message != null) {
                                if (message.equalsIgnoreCase("pbLogin")) {
                                    out.print("Vérifiez le mot de passe");
                                }
                            }
                        %>
                    </div>
                    <br/>
                    <form id="formLogin" class="form" method="post" action="loginReq.jsp">
                        <div data-role="fieldcontain">
                            <label for="mdp">Mot de passe:</label>
                            <input type="password" name="mdp" id="mdp"/>
                        </div>
                        <button type="submit" id="submitOK" name="submitOK">OK</button>
                    </form>
                    <p class="mini">V2.0<br/>Développement : BTS SNIR Colomiers</p>
                </center>
            </div>
        </div>
    </body>
</html>
