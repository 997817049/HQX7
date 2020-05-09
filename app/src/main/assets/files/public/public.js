serverIp = "http://192.168.0.102:8888/";
// serverIp = "http://49.4.114.114:8888/";
screen_h = $(window).height();
screen_w = $(window).width();

//简单提示框
function showMsg(text, position) {
    var show = $('.show_msg').length;
    if (show > 0) {
    } else {
        var div = $('<div></div>');
        div.addClass('show_msg');
        var span = $('<span></span>');
        span.addClass('show_span');
        span.appendTo(div);
        span.text(text);
        $('body').append(div);
    }
    $(".show_span").text(text);
    if (position == 'bottom') {
        $(".show_msg").css('bottom', '5%');
    } else if (position == 'center') {
        $(".show_msg").css('top', '');
        $(".show_msg").css('bottom', '50%');
    } else {
        $(".show_msg").css('bottom', '95%');
    }
    $('.show_msg').hide();
    $('.show_msg').fadeIn(1000);
    $('.show_msg').fadeOut(1000);
}
