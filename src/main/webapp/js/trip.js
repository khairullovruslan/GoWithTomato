function sendPutMethod() {

        console.log("put method ....")
        const contextPath = "/gowithtomato";
        const tripId = $('#tripId').val();
        console.log(tripId)

        const path = contextPath + '/trip/' + tripId;

        $.ajax({
            url: path,
            type: 'PUT',
            contentType: 'application/json',
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
       
    })
}