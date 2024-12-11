function sendUserLoginData(){
    const userLoginData = {
        login: $('#login').val(),
        password: $('#pwd').val(),
    };
    const contextPath = $('#contextId').val()

    const path = contextPath + '/login';

    const errorContainer = document.querySelector('.error-container');
    const errorListElement = errorContainer.querySelector('.error-list');


    errorListElement.innerHTML = '';
    errorContainer.style.display = 'none';

    const errors = [];

    if (!userLoginData.login) {
        errors.push("Логин не может быть пустым.");
    }
    if (!userLoginData.password) {
        errors.push("Пароль не может быть пустым.");
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


    $.ajax({
        url: path,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(userLoginData),
        success: function (response) {
            if (response && response.url) {
                window.location.href = response.url;
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
}