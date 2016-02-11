/**
 * Main JavaScript file for the main page.
 *
 * Daniel Naylor, 2016.
 */

var m = (function() {
    var myChart;

    return {
        updateData: function () {
            myChart.data.datasets[0].data = [1, 2, 3, 4, 5, 61];
            myChart.update();
        },

        init: function() {
            Chart.defaults.bar.stacked = true;
            var ctx = document.getElementById("chart");
            myChart = new Chart(ctx, {
                animate: true,
                responsiveAnimationDuration: 500,
                type: 'bar',
                data: {
                    labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
                    datasets: [{
                        backgroundColor: "rgba(0,0,220,0.5)",
                        data: [12, 19, 3, 5, 2, 3]
                    }, {
                        backgroundColor: "rgba(220,0,0,0.5)",
                        data: [12, 19, 3, 5, 2, 3]
                    }]
                },
                options:{
                    stacked: true,
                    scales:{
                        xAxes:[{
                           barPercentage:1,
                           categoryPercentage:1,
                           stacked: true,
                           gridLines: {
                               drawOnChartArea: false
                           }
                        }],
                        yAxes:[{
                           stacked: true,
                           ticks:{
                               beginAtZero:true
                           },
                           gridLines: {
                               drawOnChartArea: false
                           }
                        }]
                    }
                }
            });
        }
    }
})();

$(document).ready(function() {
    m.init();
});