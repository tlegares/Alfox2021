var map;

$(function () {
    google.charts.load('current', {packages: ['corechart']});
    google.charts.setOnLoadCallback(drawChart1);
    google.charts.setOnLoadCallback(drawChart2);
    google.charts.setOnLoadCallback(drawChart3);
    google.charts.setOnLoadCallback(drawChart4);
    google.charts.setOnLoadCallback(drawChart5);
});

$(document).ready ( function(){
   initMap();
});

function drawChart1() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Element');
    data.addColumn('number', 'Km');
    data.addRows([
        ['01', 66],
        ['02', 72],
        ['03', 35],
        ['04', 48],
        ['05', 54],
        ['06', 48]
    ]);
    // Instantiate and draw the chart.
    var chart = new google.visualization.ColumnChart(document.getElementById('stat1'));
    var options = {
        width: 300,
        height: 300,
        legend: 'none',
        colors: ['#E8972C'],
        backgroundColor: '#EEEEEE'
    };
    chart.draw(data, options);
}

function drawChart2() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Element');
    data.addColumn('number', 'Conso');
    data.addRows([
        ['01', 1200],
        ['02', 1300],
        ['03', 700],
        ['04', 950],
        ['05', 2000],
        ['06', 1335]
    ]);
    // Instantiate and draw the chart.
    var chart = new google.visualization.ColumnChart(document.getElementById('stat2'));
    var options = {
        width: 300,
        height: 300,
        legend: 'none',
        colors: ['#E8972C'],
        backgroundColor: '#EEEEEE'
    };
    chart.draw(data, options);
}

function drawChart3() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Element');
    data.addColumn('number', 'VitM');
    data.addRows([
        ['01', 5.6],
        ['02', 5.9],
        ['03', 6.7],
        ['04', 6.2],
        ['05', 5.9],
        ['06', 6.1]
    ]);
    // Instantiate and draw the chart.
    var chart = new google.visualization.ColumnChart(document.getElementById('stat3'));
    var options = {
        width: 300,
        height: 300,
        legend: 'none',
        colors: ['#E8972C'],
        backgroundColor: '#EEEEEE'
    };
    chart.draw(data, options);
}

function drawChart4() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Element');
    data.addColumn('number', 'VitMoy');
    data.addRows([
        ['01', 5.6],
        ['02', 5.9],
        ['03', 6.7],
        ['04', 6.2],
        ['05', 5.9],
        ['06', 6.1]
    ]);
    // Instantiate and draw the chart.
    var chart = new google.visualization.ColumnChart(document.getElementById('stat4'));
    var options = {
        width: 300,
        height: 300,
        legend: 'none',
        colors: ['#E8972C'],
        backgroundColor: '#EEEEEE'
    };
    chart.draw(data, options);
}

function drawChart5() {
    var data = google.visualization.arrayToDataTable([
        ['Year', 'Sales'],
        ['2004', 1000],
        ['2005', 1170],
        ['2006', 660],
        ['2007', 1030]
    ]);
    var options = {
        width: 300,
        height: 300,
        legend: 'none',
        colors: ['#E8972C'],
        backgroundColor: '#EEEEEE',
        curveType: 'function',
    };
    var chart = new google.visualization.LineChart(document.getElementById('stat5'));
    chart.draw(data, options);
}
//initialisation de la map et tracage des lignes en fonctions de coordonnées
function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 43.585140, lng: 1.304779},
        zoom: 15
    });

    setMarkers(map);

    var PlanCoordinates = [
        {lat: 43.582866, lng: 1.306572},
        {lat: 43.586455, lng: 1.316915},
        {lat: 43.598238, lng: 1.328816}
    ];
    var Path = new google.maps.Polyline({
        path: PlanCoordinates,
        geodesic: true,
        strokeColor: '#FF0000',
        strokeOpacity: 1.0,
        strokeWeight: 2
    });
    Path.setMap(map);
}

// Fonction dans laquelel on va placer nos marqueurs.
function setMarkers(map) {
    var shape = {
        coords: [1, 1, 1, 20, 18, 20, 18, 1],
        type: 'poly'
    };

    var marker = new google.maps.Marker({
        position: {lat: 43.582866, lng: 1.306572},
        map: map,
        label: 'Maison de Axel',
        icon: {
            path: google.maps.SymbolPath.CIRCLE,
            scale: 10,
            fillOpacity: 1,
            fillColor: '#000000',
            strokeColor: '#000000'
        },
        shape: shape,
    });

    var marker3 = new google.maps.Marker({
        position: {lat: 43.598238, lng: 1.328816},
        map: map,
        icon: {
            //path: google.maps.SymbolPath.CIRCLE,
            scale: 10,
            fillOpacity: 1,
            fillColor: '#0042FF',
            strokeColor: '#0042FF'
        },
        shape: shape,
    });
}