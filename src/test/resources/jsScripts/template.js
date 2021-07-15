
var viz, sheet, table;

function initiViz() {
   var containerDiv = document.getElementById("vizContainer"),
   url = $url
   viz = new tableau.Viz(containerDiv, url, "");
   setTimeout(getData, 4000)
}

function getData() {
   sheet = viz.getWorkbook().getActiveSheet().getWorksheets().get($reportName);

   options = {
      maxRows: 1,
      ignoreAliases: false,
      ignoreSelection: true,
      includeAllColumns: false
   };

   sheet.getUnderlyingDataAsync(options).then(function(t) {
      table = t;
      console.log(JSON.stringify(table.getData()));
      var tgt = document.getElementById("dataTarget");
      tgt.innerHTML = "<h4>Underlying Data:</h4><p>" + JSON.stringify(table.getData()) + "</p>"
   });
}