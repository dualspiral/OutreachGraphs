/**
 * Main JavaScript file for the main page.
 *
 * Daniel Naylor, 2016.
 */

var m = (function() {
    var myChart;

    var callAjax = function() {
        $.ajax("/getdata").done(function(res) {
            $("#titlename").text(res.title);
            myChart.data.datasets[0].data = res.data[0];
            myChart.data.datasets[1].data = res.data[1];
            myChart.update();

            window.setTimeout(callAjax, 1000);
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
                        backgroundColor: "rgba(0,0,220,0.5)",
                        data: [0, 0, 0, 0, 0, 0, 0, 0, 0]
                    }, {
                        backgroundColor: "rgba(220,0,0,0.5)",
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

            window.setTimeout(callAjax, 1000);
        }
    }
})();

$(document).ready(function() {
    m.init();
});