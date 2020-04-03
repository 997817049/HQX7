function readExam(id) {
    window.android.toContent('study/exam/examContent.html', "study", "exam", id);
}

function readBook(id) {
    window.android.toContent('study/book/bookContent.html', "study", "book", id);
}

function readVideo(part, id) {
    window.android.toContent('study/video/videoContent.html', "study", part, id);
}

function readBase(htmlUrl, id) {
    window.android.toContent(htmlUrl, "base", "", id);
}
