const contextPath = $('#contextId').val()
let currentField;
let routeArray = [];
let start;
let finish;
var distance;

function openModal(field) {
    currentField = field;
    document.getElementById('myModal').style.display = "block";
    document.getElementById('results').innerHTML = '';
}

async function sendData() {
    console.log("start send data")
    await getInfo();
    const data = {
        infoType: "location-info",
        routeInfo: {
            start: start,
            others: routeArray,
            finish: finish,
            distance: distance
        }

    }


    console.log(data)
    const path = contextPath + '/new-route';
    $.ajax({
        url: path,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
            console.log('url ' + response.url)
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
    console.log("finish send data")
}

function getInfo() {
    return new Promise((resolve, reject) => {
        console.log("get info starting");
        const data = {
            infoType: "time-distance",
            routeInfo: {
                start: start,
                others: routeArray,
                finish: finish
            }
        };


        const path = contextPath + '/graph-hopper-api';

        const errorContainer = document.getElementById('error-container');
        const errorListElement = errorContainer.querySelector('.error-list');

        errorListElement.innerHTML = '';
        errorContainer.style.display = 'none';


        const errors = [];

        if (!data.routeInfo.start) {
            errors.push("Начальная точка не может быть пустым.");
        }
        if (!data.routeInfo.finish) {
            errors.push("Конечная не может быть пустым.");
        }


        if (errors.length > 0) {
            errors.forEach(function (error) {
                const li = document.createElement('li');
                li.textContent = error;
                errorListElement.appendChild(li);
            });
            errorContainer.style.display = 'block';
            reject(new Error('не все поля заполнены'));
            return;
        }
        $.ajax({
            url: path,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                console.log("res" + response);
                if (response) {
                    document.getElementById('routeTime').textContent = response.time;
                    document.getElementById('routeDistance').textContent = response.distance;

                    distance = response.distance;
                    resolve();
                } else {
                    console.error('info не найдена в ответе:', response);
                    reject(new Error('info не найден'));
                }
            },
            error: function (xhr, status, error) {
                console.error('Ошибка:', status, error);
                reject(error);
            }
        });
        console.log("get info finished");
    });
}


function closeModal() {
    document.getElementById('myModal').style.display = "none";
}

function selectPoint(item) {
    if (currentField === 'start') {
        document.getElementById('start').value = item.name;
        start = item;
    } else if (currentField === 'end') {
        document.getElementById('end').value = item.name;
        finish = item;
    } else if (currentField === 'intermediate') {
        const pointDiv = document.createElement('div');
        pointDiv.classList.add('intermediate-item');
        pointDiv.innerHTML = `<span>${item.name}</span>
                <button type="button" onclick="removeIntermediate(this)">Удалить</button>`;

        document.getElementById('intermediate-points').appendChild(pointDiv);
        routeArray.push(item);
    }

    updateRouteDisplay();
    closeModal();
}

function updateRouteDisplay() {
    if (start == null && finish == null) {
        document.getElementById('routeOutput').textContent = 'Не указан';
    } else if (start != null && finish != null && routeArray.length === 0) {
        document.getElementById('routeOutput').textContent = start.name + " - " + finish.name;
    } else if (routeArray.length === 0 && start != null) {
        document.getElementById('routeOutput').textContent = start.name + " - ";
    } else if (routeArray.length === 0 && finish != null) {
        document.getElementById('routeOutput').textContent = " - " + finish.name;
    } else {
        console.log("вывод")
        console.log(routeArray)
        let formattedIntermediates = start.name + " - ";
        for (i = 0; i < routeArray.length; i++) {
            formattedIntermediates += routeArray[i].name + " - "
        }
        formattedIntermediates += finish.name;
        document.getElementById('routeOutput').textContent = formattedIntermediates;
    }
}

function searchPoints() {
    const query = $('#search-input').val();
    if (query.length === 0) {
        $('#results').empty();
        return;
    }
    const path = contextPath + "/points-search?q=" + encodeURIComponent(query);

    console.log(path);
    $.ajax({
        url: path,
        type: 'GET',
        success: function (response) {
            $('#results').empty();
            if (Array.isArray(response) && response.length > 0) {
                response.forEach(item => {
                    console.log(item)
                    const pointName = item.name;
                    const country = item.country;
                    const state = item.state;
                    const osmValue = item.osm_value;
                    const lat = item.point.lat;
                    const lng = item.point.lng;

                    const resultItem = $('<div class="result-item"></div>').text(pointName);
                    resultItem.click(() => {
                        selectPoint(item);
                        closeModal();
                    });
                    resultItem.append(`<br><small>${osmValue}, ${state ? state : 'Не указано'}, ${country}, ${lat}, ${lng}</small>`);
                    $('#results').append(resultItem);
                });
            } else {
                $('#results').append('<div>Ничего не найдено</div>');
            }
        },
        error: function (xhr, status, error) {
            console.error('Ошибка:', status, error);
        }
    });
}

function removeIntermediate(button) {
    const pointDiv = button.parentElement;
    const pointName = pointDiv.querySelector('span').textContent;

    routeArray = routeArray.filter(point => point.name !== pointName);
    pointDiv.remove();

    updateRouteDisplay();
    console.log(routeArray);
}


document.getElementById('route-form').onsubmit = function () {
    document.getElementById('routeData').value = JSON.stringify(routeArray);
};