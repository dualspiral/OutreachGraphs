/**
 * Main JavaScript file for the main page.
 *
 * Daniel Naylor, 2016.
 */

var m = (function() {


    return {
        init: function() {
            var ctx = document.getElementById("chart");
            var myChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
                    datasets: [{
                        label: '# of Votes',
                        data: [12, 19, 3, 5, 2, 3]
                    }]
                },
                options:{
                    scales:{
                        yAxes:[{
                            ticks:{
                                beginAtZero:true
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