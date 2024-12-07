$(document).ready(function () {
    $('#route-form').on('submit', function (event) {
        event.preventDefault();

        const tripData = {
            tripDateTime: $('#tripDateTime').val(),
            availableSeats: $('#availableSeats').val(),
            price: parseFloat($('#price').val())
        };
        const contextPath = $('#contextId').val()
        const routeId = $('#routeId').val();

        console.log(routeId);

        const path = contextPath + '/create-trip?id=' + routeId;
        console.log(path);

        $.ajax({
            url: path,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(tripData),
            success: function (response) {
                if (response && response.url) {
                    window.location.href = response.url;
                } else {
                    console.error('URL не найден в ответе:', response);
                }
            },
            error: function (xhr, status, error) {
                console.error('Ошибка:', status, error);
            }
        });
    });
});