<%@page import="java.util.ArrayList"%>
<!--
La page a des soucis, il manque la barre de selection en bas pour pouvoir switcher entre les 3 pages
-->
<!DOCTYPE html>
<html>
    <head>
        <title>Informations</title> 
        <%@ include file="/includes/head.jspf" %>  
        <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAteLjItiBvWdZJNOm97mU-jWaqtJ857Fc&callback=initmap"></script>
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <script type="text/javascript" src="js/map.js"></script>
        
    </head>
    <body>
        <%@include file="/includes/controle.jspf" %>
        <div data-role="page" id="page1">
            <%@include file="/includes/headerGestion.jspf"%>
            <!-- panel d'info -->
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