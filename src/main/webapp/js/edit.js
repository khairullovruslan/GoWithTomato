const contextPath = $('#contextId').val()

function sendNewUserData() {
    const changedUserData = {
        login: $('#login').val(),
        email: $('#email').val(),
        phoneNumber: $('#phone_number').val()
    };

    console.log(changedUserData);


    const path = contextPath + '/profile/edit';

    const errorContainer = document.getElementById('edit-error-container');
    const errorListElement = errorContainer.querySelector('.error-list');


    errorListElement.innerHTML = '';
    errorContainer.style.display = 'none';

    const errors = [];

    if (!changedUserData.login) {
        errors.push("Логин не может быть пустым.");
    }
    if (!changedUserData.email) {
        errors.push("Email не может быть пустым.");
    }
    if (!changedUserData.phoneNumber) {
        errors.push("Телефон не может быть пустым.");
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
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(changedUserData),
        success: function (response) {
            if (response && response.message) {
                const successMessage = document.getElementById('successMessageUserData');
                successMessage.style.display = 'block';
                successMessage.textContent = response.message;

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


function uploadNewPhoto() {
    const formData = new FormData(document.getElementById('uploadForm'));
    const path = contextPath + '/profile/upload';
    console.log(path)
    const errorContainer = document.querySelector('.error-container');
    const errorListElement = errorContainer.querySelector('.error-list');

    errorListElement.innerHTML = '';
    errorContainer.style.display = 'none';
    const errors = [];
    const imageFile = formData.get('image');
    if (!imageFile || imageFile.size === 0) {
        errors.push("Фотография не выбрана!");
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
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            if (response && response.message) {
                const successMessage = document.getElementById('successMessageUserPhoto');
                successMessage.style.display = 'block';
                successMessage.textContent = response.message;

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

function sendNewPasswordData() {
    const changedPasswordData = {
        oldPassword: $('#old_pwd').val(),
        newPassword: $('#new_pwd').val(),
        repeatPassword: $('#new_pwd_rep').val()
    };

    const path = contextPath + '/profile/edit/password';
    console.log(changedPasswordData);
    console.log(path);

    const errorContainer = document.getElementById('password-error-container');
    const errorListElement = errorContainer.querySelector('.error-list');


    errorListElement.innerHTML = '';
    errorContainer.style.display = 'none';

    const errors = [];

    if (!changedPasswordData.oldPassword) {
        errors.push("Старый пароль не может быть пустым.");
    }
    if (!changedPasswordData.newPassword || !changedPasswordData.repeatPassword) {
        errors.push("Новый пароль не может быть пустым.");
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
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(changedPasswordData),
        success: function (response) {
            if (response && response.message) {
                const successMessage = document.getElementById('successMessageUserPasswordData');
                successMessage.style.display = 'block';
                successMessage.textContent = response.message;

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
