const contextPath = "/gowithtomato";

let currentField;
let routeArray = [];
let start;
let finish;

function openModal(field) {
    currentField = field;
    document.getElementById('myModal').style.display = "block";
    document.getElementById('results').innerHTML = '';
}

function sendData() {
    console.log("отправляю данные")
    console.log(start.name)
    const data = {
        start: start,
        others: routeArray,
        finish: finish
    }
    console.log(data)
    const path = contextPath + '/new-route';
    $.ajax({
        url: path,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
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
    }
    else if (start != null && finish != null && routeArray.length === 0) {
        document.getElementById('routeOutput').textContent = start.name +  " - "  + finish.name ;
    }
    else if (routeArray.length === 0 && start != null){
        document.getElementById('routeOutput').textContent = start.name + " - ";
    }
    else if (routeArray.length === 0 && finish != null){
        document.getElementById('routeOutput').textContent = " - "  + finish.name ;
    }
    else {
        console.log("вывод")
        console.log(routeArray)
        let formattedIntermediates = start.name + " - ";
        for (i = 0; i < routeArray.length; i++){
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