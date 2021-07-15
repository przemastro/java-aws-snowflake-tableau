

var viz, sheet, table;

            function initViz() {
		var containerDiv = document.getElementById("vizContainer"),
                    url = "https://public.tableau.com/views/RegionalSampleWorkbook/Storms"
                viz = new tableau.Viz(containerDiv, url, "");
                setTimeout(getData,4000)
            }

            function getData(){
                sheet = viz.getWorkbook().getActiveSheet().getWorksheets().get("Storm Map Sheet");

                options = {
                    maxRows: 1, // Max rows to return. Use 0 to return all rows
                    ignoreAliases: false,
                    ignoreSelection: true,
                    includeAllColumns: false
                };

                sheet.getUnderlyingDataAsync(options).then(function(t){
                       table = t;
                                                   			console.log(JSON.stringify(table.getData()));

			var tgt = document.getElementById("dataTarget");
			tgt.innerHTML = "<h4>Underlying Data:</h4><p>" + JSON.stringify(table.getData()) + "</p>";
                });
            }
