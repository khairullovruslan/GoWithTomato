function sendReview() {

    const reviewData = {
        rating: $('input[name="rating"]:checked').val(),
        reviewText: $('textarea[name="review"]').val()
    };
    console.log('Данные отзыва:', reviewData);

    const contextPath = $('#contextId').val()

    const tripId = $('#tripId').val();

    const errorContainer = document.getElementById('error-container');
    const errorListElement = errorContainer.querySelector('.error-list');


    errorListElement.innerHTML = '';
    errorContainer.style.display = 'none';

    const errors = [];

    if (!reviewData.rating) {
        errors.push("Поставьте балл от 1 до 5.");
    }


    if (errors.length > 0) {
        errors.forEach(function (error) {
            const li = document.createElement('li');
            li.textContent = error;
            errorListElement.appendChild(li);
        });
        errorContainer.style.display = 'block';
        return;
    }



    const path = contextPath + '/review?trip=' + tripId;
    $.ajax({
        url: path,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(reviewData),
        success: function (response) {
            if (response && response.url) {
                window.location.href = response.url;
            }
            else if(response && response.error){
                console.log("ошибочка " + response.error);
            }
            else {
                console.error('URL не найден в ответе:', response);
            }
        },
        error: function (xhr) {
            const responseJson = JSON.parse(xhr.responseText);
            console.log(responseJson)

            const errorList = responseJson.error.split(", ");
            console.log(errorList)
            errorList.forEach(function (error) {
                const li = document.createElement('li');
                li.textContent = error;
                errorListElement.appendChild(li);
            });

            errorContainer.style.display = 'block';

        }
    });
}