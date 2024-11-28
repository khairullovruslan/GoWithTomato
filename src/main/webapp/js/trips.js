 function filterSend(){

        const contextPath = "/gowithtomato";
        const from = $('#from').val();
        const to = $('#to').val();
        const date = $('#date').val();
        const count = $('#count').val();
        const organizer = $('#organizer').val();
        const status = $('#status').val();

        let path = contextPath + '/trips';
        const params = [];
        if (from !== '') {
            params.push("from=" + encodeURIComponent(from));
        }
        if (to !== '') {
            params.push("to=" + encodeURIComponent(to));
        }
        if (organizer !== '') {
            params.push("organizer=" + encodeURIComponent(organizer));
        }
        if (status !== '') {
            params.push("status=" + encodeURIComponent(status));
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
}

function filterStatusSend(status){
    const statusInput = document.getElementById("status");
    statusInput.value = status;
    filterSend();
}

function clearFilter(){
    const contextPath = "/gowithtomato";
    window.location.href =contextPath + '/trips';
}

document.querySelectorAll('.trip-card').forEach(card => {
    const contextPath = "/gowithtomato";
    card.addEventListener('click', function () {
        const tripId = $('#id').val();
        window.location.href =contextPath + '/trip/' + tripId;
    });
});