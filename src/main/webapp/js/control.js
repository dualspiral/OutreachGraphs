/**
 * The JavaScript for the control page.
 *
 * Daniel Naylor, 2016
 */

var c = (function() {

    var checking = false;

    var setState = function(state) {
        $.ajax({
            method: "POST",
            url: "/setstate",
            data: {
                state: state
            }
        });
    };

    var callAjax = function() {
        if (!checking) {
            checking = true;

            $.ajax("/getdata?all=true").done(function(res) {
                if (res.state == "CURRENT") {
                    $("button[data-ret=allcurrent]").removeClass("btn-info");
                    $("button[data-ret=current]").addClass("btn-info");
                } else {
                    $("button[data-ret=allcurrent]").addClass("btn-info");
                    $("button[data-ret=current]").removeClass("btn-info");
                }

                $("#count").text(res.numberOfThrows);
                $("#counters").html('');

                // First array
                keys = Object.keys(res.dataMap).sort();
                for (var i = 0; i < keys.length; i++) {
                    $('#counters').append("<tr>" +
                        "<td>" +
                        keys[i] +
                        "</td><td>" +
                        res.dataMap[keys[i]] +
                        "</td>" +
                        "</tr>")
                }

                $('#status').text("Link Status: OK");
                $('#status').addClass("green");
                $('#status').removeClass("red");
            }).fail(function(res) {
                $('#status').text("Link Status: Error");
                $('#status').addClass("red");
                $('#status').removeClass("green");
            }).always(function() {
                checking = false;
            });
        }
    };

    return {
        init: function() {

            $('.viewtype > button').click(function(e) {
                setState($(this).attr("data-ret"));
            });

            $('.throwbtn button').click(function(e) {
                $.ajax({
                    method: "POST",
                    url: "/postdata",
                    data: {
                        time: Date.now(),
                        bin: $(this).text()
                    }
                })
            });

            $('#commit').click(function(e) {
                $('#modal').modal('hide');
                $.ajax({
                    method: "POST",
                    url: "/commitdata"
                }).done(function() {
                    setState("allcurrent");
                })
            });

            $('#startModal').click(function(e) {
                setState("current");
                $('#modal').modal('show');
            });

            window.setInterval(function() {
                callAjax();
            }, 1000);
        }
    }

})();

$(document).ready(function() {
    c.init();
});