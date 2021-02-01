
// --------------------- callback ----------------------- 

$(function () {
    // initialisation des header, footer et panel
    $("[data-role='header'], [data-role='footer']").toolbar();
    $("[data-role='panel']").panel().enhanceWithin();
    
    $(".datepicker").datepicker();
    $(".datepicker1").datepicker({maxDate: -0});
    $( ".datepicker" ).datepicker({
        firstDay: 1,
        altField: "#datepicker",
        closeText: 'Fermer',
        prevText: 'Précédent',
        nextText: 'Suivant',
        currentText: 'Aujourd\'hui',
        monthNames: ['Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin', 'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre'],
        monthNamesShort: ['Janv.', 'Févr.', 'Mars', 'Avril', 'Mai', 'Juin', 'Juil.', 'Août', 'Sept.', 'Oct.', 'Nov.', 'Déc.'],
        dayNames: ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'],
        dayNamesShort: ['Dim.', 'Lun.', 'Mar.', 'Mer.', 'Jeu.', 'Ven.', 'Sam.'],
        dayNamesMin: ['D', 'L', 'M', 'M', 'J', 'V', 'S'],
        weekHeader: 'Sem.',
        dateFormat: 'yy-mm-dd'
    });
});

function infosVehicule(immatriculation) {
    // infos du vehicule sélectionné
    $.ajax({
        url  : 'ajax_infoVehicule.jsp',
        type : 'POST',
        data : 'immatriculation=' + immatriculation,
        success: function(data) {
            var tabInfos = data.split("|");
            $("#iImmatriculation").text(tabInfos[0]);
            $("#iZone").text(tabInfos[1]);
            $("#iAge").text(tabInfos[2]);
            $("#iTaux").text(tabInfos[3]);
            $("#iKilometrage").text(tabInfos[4]);
            $("#iKilometrageMensuel").text(tabInfos[5]);
            $("#iDateControleTechnique").text(tabInfos[6]);
            $("#iDateDernierControle").text(tabInfos[7]);
        },
        error : function(resultat, statut, erreur) {
            alert("Impossible de récupérer les infos.\n Msg: " + erreur);
        }
    });
}

