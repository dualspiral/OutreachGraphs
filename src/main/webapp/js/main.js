/**
 * Main JavaScript file for the main page.
 *
 * Daniel Naylor, 2016.
 */

var m = (function() {
    var myChart;

    return {
        updateData: function () {
            myChart.data.datasets[0].data = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19];
            myChart.data.datasets[1].data = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19];
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
                    labels: ["-9", "-8", "-7", "-6", "-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"],
                    datasets: [{
                        backgroundColor: "rgba(0,0,220,0.5)",
                        data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                    }, {
                        backgroundColor: "rgba(220,0,0,0.5)",
                        data: [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
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
                               beginAtZero:true,
                               min: 0
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