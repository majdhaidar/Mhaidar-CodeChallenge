$('#btnStartBluetoothService').on('click', function() {
	//$.get( "http://localhost:8080/bluetoothConnect", function() {});
	$.ajax({
		method: "GET",
		url: "http://localhost:8080/bluetoothConnect"
	}).done(function(response){
		$('#btnStartBluetoothService').prop('disabled', true);
		alert(response);
	});
});
