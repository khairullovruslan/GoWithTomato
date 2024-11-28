function sendReview(){

        const reviewData = {
            rating: $('input[name="rating"]:checked').val(),
            reviewText: $('textarea[name="review"]').val()
        };
        console.log('Данные отзыва:', reviewData);

        const contextPath = "/gowithtomato";
        const tripId = $('#tripId').val();


        const path = contextPath + '/review?trip=' + tripId;
        $.ajax({
            url: path,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(reviewData),
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
}