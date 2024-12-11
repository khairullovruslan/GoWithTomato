const contextPath = $('#contextId').val();

function filterSend() {

    const from = $('#from').val();
    const to = $('#to').val();
    const date = $('#date').val();
    const count = $('#count').val();
    const organizer = $('#organizer').val();
    const status = $('#status').val();
    const owner_tickets = $('#owner_tickets').val();


    let path = contextPath + '/trips';
    console.log(path)
    console.log(owner_tickets)
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
    if (owner_tickets !== '') {
        params.push("owner_tickets=" + encodeURIComponent(owner_tickets));
    }
    if (count !== '' && count > 0) {
        params.push("count=" + encodeURIComponent(count));
    }
    if (params.length > 0) {
        path += "?" + params.join('&');
    }

    window.location.href = path;
}

function filterStatusSend(status) {
    const statusInput = document.getElementById("status");
    statusInput.value = status;
    filterSend();
}

function clearFilter() {
    window.location.href = contextPath + '/trips';
}

function openTrip(id) {
    console.log(id);
    window.location.href = contextPath + '/trip/' + id;
}

