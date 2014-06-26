//
//$.document.ready({
//	
//});

//$.ajax({
//  type: "GET",
//  url: "test.js",
//  dataType: "script",
//  success: function(data, textStatus, xhqr) {
//	  
//  }
//});

$('#signInButton').click(function () {
	
	var email = $('#loginEmail').val();
	var loginPassword = $('#loginPassword').val();
	
	$.ajax({
		type: 'GET',
		url: "http://localhost:8080/OBE/rest/users/" + email,
		data: {password: loginPassword},
		dataType: "json",
		success: function(data, textStatus, xhqr) {
//			var me = this;
			alert('Hello there Jerry!');
		}
	});
});

$('#loginForm').submit(function () {
	var email = $('#loginEmail').val();
	var loginPassword = $('#loginPassword').val();
	
	$.ajax({
		type: 'GET',
		url: "http://localhost:8080/OBE/rest/users/" + email,
		data: {password: loginPassword},
		dataType: "json",
		success: function(data, textStatus, xhqr) {
//			var me = this;
			if (data != null && data.length > 0) {
				alert('Hello there Jerry!');
			}
			
		}
	});
});