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

$('#loading-example-btn').click(function () {
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/OBE/rest/users",
		dataType: "script",
		success: function(data, textStatus, xhqr) {
			var me = this;
		}
	});
});