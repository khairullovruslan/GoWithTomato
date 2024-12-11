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
        const errorContainer = document.querySelector('.error-container');
        const errorListElement = errorContainer.querySelector('.error-list');


        errorListElement.innerHTML = '';
        errorContainer.style.display = 'none';


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
            error: function (xhr) {
                const responseJson = JSON.parse(xhr.responseText);
                console.log(responseJson)

                const errorList = responseJson.error.split(";");
                console.log(errorList)
                errorList.forEach(function (error) {
                    const li = document.createElement('li');
                    li.textContent = error;
                    errorListElement.appendChild(li);
                });

                errorContainer.style.display = 'block';
            }
        });
    });
});