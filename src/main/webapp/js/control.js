/**
 * The JavaScript for the control page.
 *
 * Daniel Naylor, 2016
 */

var c = (function() {

    var setState = function(state) {
        $.ajax({
            method: "POST",
            url: "/setstate",
            data: {
                state: state
            }
        })
    }

    return {
        init: function() {

            $('.viewtype > button').click(function(e) {
                setState($(this).attr("data-ret"));
            });

            $('.throwbtn > button').click(function(e) {
                $.ajax({
                    method: "POST",
                    url: "/postdata",
                    data: {
                        time: Date.now(),
                        bin: $(this).text()
                    }
                }).done(function() {
                    setState("current");
                })
            });

            $('#commit').click(function(e) {
                $.ajax({
                    method: "POST",
                    url: "/commitdata"
                }).done(function() {
                    setState("allcurrent");
                })
            });
        }
    }

})();

$(document).ready(function() {
    c.init();
});