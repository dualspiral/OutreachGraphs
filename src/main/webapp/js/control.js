/**
 * The JavaScript for the control page.
 *
 * Daniel Naylor, 2016
 */

var c = (function() {

    var checking = false;
    var inited = false;

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
                keys = Object.keys(res.dataMap).sort(function(a,b) { return Number(a)-Number(b) });
                for (var i = 0; i < keys.length; i++) {
                    $('#counters').append("<tr>" +
                        "<td>" +
                        keys[i] +
                        "</td><td>" +
                        res.dataMap[keys[i]] +
                        "</td>" +
                        "</tr>")
                }

                if (!inited) {
                    // Create the button list for the popup.
                    // Get the percentage by dividing by the number of boxes.
                    var per = (100 / keys.length);

                    for (var i = 0; i < keys.length; i++) {
                        var c = getColour(Number(keys[i]));
                        var t = getTextColour(Number(keys[i]));
                        $('.throwbtn').append("<div class='divthrow' data-colour='" + c + "' style='color:" + t + "; background-color:" + c + "; width:" + per + "%;'>" + keys[i] + "</div>");
                    }

                    inited = true;
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

    var getColour = function(number) {
        var n = Math.abs(number);
        if (n > 3) {
            return "#002cfb";
        } else if (n == 3) {
            return "#1c7e12";
        } else if (n == 2) {
            return "#fffc38";
        } else if (n == 1) {
            return "#fb6720";
        }

        return "#fa141b";
    };

    var getTextColour = function(number) {
        var n = Math.abs(number);
        if (n > 2 || n == 0) {
            return "white";
        }

        return "black";
    };

    return {
        init: function() {

            $('.viewtype > button').click(function(e) {
                setState($(this).attr("data-ret"));
            });

            $('.throwbtn').click(function(event) {
                var target = $(event.target);
                if (target.hasClass('divthrow')) {
                    // We only care for the div within
                    $(target).animate({backgroundColor: '#FFFFCC'}, 250, function() { $(target).animate({backgroundColor: $(target).attr('data-colour')}, 1000) });
                    $.ajax({
                        method: "POST",
                        url: "/postdata",
                        data: {
                            time: Date.now(),
                            bin: target.text()
                        }
                    }).done(function(e) {
                        $(target).animate({backgroundColor: '#5EFB6E'}, 250, function() { $(target).animate({backgroundColor: $(target).attr('data-colour')}, 1000) });
                    }).fail(function(res) {
                        $(target).animate({backgroundColor: '#800517'}, 250, function() { $(target).animate({backgroundColor: $(target).attr('data-colour')}, 1000) });
                    });
                }
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