$(document).ready(function () {
    $('#search-btn').on('click', function (event) {
        event.preventDefault();

        const contextPath = "/gowithtomato";
        const from = $('#from').val();
        const to = $('#to').val();
        const date = $('#date').val();
        const count = $('#count').val();

        let path = contextPath + '/trips';
        const params = [];
        if (from !== '') {
            params.push("from=" + encodeURIComponent(from));
        }
        if (to !== '') {
            params.push("to=" + encodeURIComponent(to));
        }
        if (date !== '') {
            params.push("date=" + encodeURIComponent(date));
        }
        if (count !== '' && count > 0) {
            params.push("count=" + encodeURIComponent(count));
        }
        if (params.length > 0) {
            path += "?" + params.join('&');
        }

        window.location.href = path;
    });
});
document.querySelectorAll('.trip-card').forEach(card => {
    const contextPath = "/gowithtomato";
    card.addEventListener('click', function () {
        const tripId = $('#id').val();
        window.location.href =contextPath + '/trip/' + tripId;
    });
});