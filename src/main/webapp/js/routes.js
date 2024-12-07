document.addEventListener('DOMContentLoaded', function() {
    const contextPath = $('#contextId').val()
    const routeCards = document.querySelectorAll('.route-card');

    routeCards.forEach(card => {
        card.addEventListener('click', function(event) {
            const routeId = this.querySelector('#id');
            window.location.href = contextPath + '/create-trip?id=' + routeId.value;
        });
    });
});
