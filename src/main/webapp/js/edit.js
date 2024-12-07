const contextPath = $('#contextId').val()
function sendNewUserData() {
    const changedUserData = {
        login: $('#login').val(),
        email: $('#email').val(),
        phoneNumber: $('#phone_number').val()
    };

    console.log(changedUserData);


    const path = contextPath + '/profile/edit';

    const errorContainer = document.querySelector('.error-container');
    const errorListElement = errorContainer.querySelector('.error-list');


    errorListElement.innerHTML = '';
    errorContainer.style.display = 'none';

    $.ajax({
        url: path,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(changedUserData),
        success: function (response) {
            if (response && response.message) {
                const successMessage = document.querySelector('.success-message');
                successMessage.style.display = 'block';
                successMessage.textContent = response.message;

            } else {
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


function uploadNewPhoto(){
    const formData = new FormData(document.getElementById('uploadForm'));
    const path = contextPath + '/profile/upload';
    console.log(path)

    $.ajax({
        url: path,
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error('Ошибка:', textStatus, errorThrown);
        }
    });
}