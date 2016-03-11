/**
 * Main JavaScript file for the main page.
 *
 * Daniel Naylor, 2016.
 */

var m = (function() {
    var myChart;

    var callAjax = function() {
        $.ajax("/getdata?all=false").done(function(res) {
            $("#titlename").text(res.title);
            myChart.data.datasets[0].data = res.data[0];
            myChart.data.datasets[1].data = res.data[1];
            myChart.update();
        });
    };

    return {
        init: function() {
            Chart.defaults.bar.stacked = true;
            var ctx = document.getElementById("chart");
            myChart = new Chart(ctx, {
                animate: true,
                responsiveAnimationDuration: 500,
                type: 'bar',
                data: {
                    labels: ["-4", "-3", "-2", "-1", "0", "1", "2", "3", "4"],
                    datasets: [{
                        backgroundColor: "blue",
                        data: [0, 0, 0, 0, 0, 0, 0, 0, 0]
                    }, {
                        backgroundColor: "green",
                        data: [1, 0, 0, 0, 0, 0, 0, 0, 0]
                    }]
                },
                options:{
                    legend: {
                        display: false
                    },
                    stacked: true,
                    scales:{
                        xAxes:[{
                           barPercentage:1,
                           categoryPercentage:0.9,
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

            window.setInterval(callAjax, 1000);
        }
    }
})();

$(document).ready(function() {
    m.init();
});