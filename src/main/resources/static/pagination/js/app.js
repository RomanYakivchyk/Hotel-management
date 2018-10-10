$(document).ready(function() {
	changePageAndSize();
});

function changePageAndSize() {
	$('#clientsPageSizeSelect').change(function(evt) {
		window.location.replace("/clients?pageSize=" + this.value + "&page=1");
	});

	$('#vacationsPageSizeSelect').change(function(evt) {
    		window.location.replace("/vacations?pageSize=" + this.value + "&page=1");
    });
}
