function dealToTop() {
    $(window).scroll(function() {
        var _top = $(window).scrollTop();
        if (_top > 300) {
            $('.toTopH').fadeIn(400);
        } else {
            $('.toTopH').fadeOut(600);
        }
    });
    $(".toTopH").click(function() {
        $("html,body").animate({
            scrollTop: 0
        }, 500);
    });
}
