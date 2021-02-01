
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.persistence.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Accueil</title> 
        <%@ include file="/includes/head.jspf" %>  
    </head>
    <body>
        <%@ include file="/includes/controle.jspf" %>
        <!-- 1er page -->
        <div data-role="page" id="syntheseFlotte">
            <%@ include file="/includes/header.jspf" %>
            <script> $("#titre").text("ACCEUIL"); </script>
            <div role="main" class="ui-content">
                <center>
                    <br/><br/><br/><br/>
                    <div class="grid">
                        <div class="card">
                            <div class="cardTitre">Nombre de véhicules</div>
                            <div class="container">
                                <div id="cnv" class="cardValeur">
                                    <% out.print(Vehicule.size(con)); %>
                                </div>
                                <div class="cardUnite">Véhicules</div>
                            </div>
                        </div>
                        <div class="card">
                            <div class="cardTitre">Nombre de véhicules hors zone</div>
                            <div class="container">
                                <div id="cnvhz" class="cardValeur">
                                    <% out.print(Vehicule.nbVehiculesDehors(con)); %>
                                </div>
                                <div class="cardUnite">Véhicules</div>
                            </div>
                        </div>
                        <div class="card">
                            <div class="cardTitre">Age moyen</div>
                            <div class="container">
                                <div id="cam" class="cardValeur">
                                    <% out.print(String.format("%2.1f", Vehicule.getAgeMoyenFlotte(con)/365.0)); %>
                                </div>
                                <div class="cardUnite">Années</div>
                            </div>
                        </div>
                        <div class="card">
                            <div class="cardTitre">Kilométrage moyen</div>
                            <div class="container">
                                <div id="ckm" class="cardValeur">
                                    <% out.print(Vehicule.getKmMoyenFlotte(con)); %>
                                </div>
                                <div class="cardUnite">Km</div>
                            </div>
                        </div>
                        <div class="card">
                            <div class="cardTitre">Kilométrage mensuel moyen</div>
                            <div class="container">
                                <div id="ckmm" class="cardValeur">
                                    <% out.print(Vehicule.getKmMoyenMensuelFlotte(con)); %>
                                </div>
                                <div class="cardUnite">Km/mois</div>
                            </div>
                        </div>
                    </div>
                    <br/><br/><br/>
                </center>
            </div>
        </div>
        
        <!-- 2ieme page -->
        <div data-role="page" id="syntheseVehicule">
            <!-- panel des immatriculation des véhicules -->
            <div id="panelImmatriculation" data-role="panel" data-position="right"  
                 data-position-fixed="true" data-display="push" data-theme="c">
                <ol id="listeInfo" data-role="listview" data-icon="false">
                    <li data-role="list-divider">Sélection du véhicule</li>
                    <%
                        ArrayList<Vehicule> vehicules = Vehicule.getList(con);
                        for (Vehicule vehicule : vehicules) {
                            out.print("<li><a href='#' data-rel='close' ");
                            out.println(String.format("onclick='infosVehicule(\"%s\")'>%s</a></li>",
                               vehicule.getImmatriculation(), vehicule.getImmatriculation()));
                        }
                    %>
                </ol>
            </div>
            <%@include file="/includes/headerSynthese.jspf"%>
            <div role="main" class="ui-content">
                <center>
                    <br/><br/><br/> 
                    <br/><br/>
                    <br/><br/>
                    <br/><br/>
                    <div class="grid">
                        <div class="card" >
                            <% Vehicule v = Vehicule.getList(con).get(0); %>
                            <div class="cardTitre">Immatriculation</div>
                            <div class="container">
                                <div id="iImmatriculation" class="cardValeur">
                                    <% out.print(v.getImmatriculation()); %>
                                </div>

                            </div>
                        </div>
                        <div class="card">
                            <div class="cardTitre">Zone</div>
                            <div class="container">
                                <div id="iZone" class="cardValeur">
                                    <% out.print(ZoneLimite.getByID(con, v.getZoneLimiteID()).getNom()); %>
                                </div>
                            </div>
                        </div>
                        <div class="card">
                            <div class="cardTitre">Age du véhicule</div>
                            <div class="container">
                                <div id="iAge" class="cardValeur">
                                    <% out.print(String.format("%2.1f", v.getAge(con)/365.0)); %>
                                </div>
                                <div class="cardUnite">Années</div>
                            </div>
                        </div>
                        <div class="card">
                            <div class="cardTitre">Taux d'utilisation</div>
                            <div class="container">
                                <div id="iTaux" class="cardValeur">
                                    <% out.print(v.getTauxUtilisation() + "%"); %>
                                </div>
                                <div class="cardUnite">Taux</div>
                            </div>
                        </div>
                        <div class="card">
                            <div class="cardTitre">Kilométrage</div>
                            <div class="container">
                                <div id="iKilometrage" class="cardValeur">
                                    <% out.print((int)(v.getKilometrageReel() + v.getKilometrageCumule())); %>
                                </div>
                                <div class="cardUnite">Km</div>
                            </div>
                        </div>
                        <div class="card">
                            <div class="cardTitre">Kilométrage mensuel moyen</div>
                            <div class="container">
                                <div id="iKilometrageMensuel" class="cardValeur">
                                    <% out.print((int)((v.getKilometrageReel() + v.getKilometrageCumule())/(v.getAge(con)/30.5))); %>
                                </div>
                                <div class="cardUnite">Km/mois</div>
                            </div>
                        </div>
                    </div>
                </center>
            </div>
        </div>
        <%@include file="/includes/footer.jspf"%>
    </body>
</html>