$(document).ready(function () {
	$('textarea').autosize();
	$('input[type=radio]').change(function () {
		if (this.value == 'Upload') {
			$('.paste').hide();
			$('.upload').show();
		} else if (this.value == 'Paste') {
			$('.paste').show();
			$('.upload').hide();
		}
	});
	$('form').removeAttr('onsubmit')
	.submit(function (event) {
		event.preventDefault();
		// This cancels the event...
	});
$('.logAppCNButton').click(function () {

var obj = {};
obj.appId=$('#appIdSingle').val();
obj.computeNode=$('#computeNodeSingle').val();
obj.eventToLog=$('#logTextAppCN').val();
var data=JSON.stringify(obj).toString();

	$.ajax({
				type : "POST",
				url : 'http://localhost:8177/trace-checking-service/'+obj.appId+'/log',
				data : data,
contentType: "text/plain",
crossDomain: true
			}).fail(function(jqXHR, textStatus, errorThrown){console.log(jqXHR);alert(textStatus);alert(errorThrown);alert(jqXHR.responseText);}).done(function (data){console.log(data);
			$('.list').append("<li>"+data.applicationId+"<span class='glyphicon glyphicon-remove'></span></li>");
			rebind();
			});
});
$('.logFileAppCNButton').click(function () {

var obj = {};
obj.appId=$('#appId').val();
obj.computeNode=$('#computeNode').val();
obj.logFileText=$('#logFileTextAppCN').val();
var data=JSON.stringify(obj).toString();

	$.ajax({
				type : "POST",
				url : 'http://localhost:8177/trace-checking-service/'+obj.appId+'/loadLogFile',
				data : data,
contentType: "text/plain",
crossDomain: true
			}).fail(function(jqXHR, textStatus, errorThrown){console.log(jqXHR);alert(textStatus);alert(errorThrown);alert(jqXHR.responseText);}).done(function (data){console.log(data);
			$('.list').append("<li>"+data.applicationId+"<span class='glyphicon glyphicon-remove'></span></li>");
			rebind();
			});
});
$('.logButton').click(function () {
var logLine = $('#logLine').val();
	$.ajax({
				type : "POST",
				url : 'http://localhost:8177/trace-checking-service/log',
				data : logLine,
contentType: "text/plain",
crossDomain: true
			}).fail(function(jqXHR, textStatus, errorThrown){console.log(jqXHR);alert(textStatus);alert(errorThrown);alert(jqXHR.responseText);}).done(function (data){console.log(data);
			$('.list').append("<li>"+data.applicationId+"<span class='glyphicon glyphicon-remove'></span></li>");
			rebind();
			});
});
$('.logFileButton').click(function () {
var obj = {};
obj.logFileName=$('#logFileName').val();
obj.logFileText=$('#logFileText').val();
var data=JSON.stringify(obj).toString();

	$.ajax({
				type : "POST",
				url : 'http://localhost:8177/trace-checking-service/loadLogFile',
				data : data,
contentType: "text/plain",
crossDomain: true
			}).fail(function(jqXHR, textStatus, errorThrown){console.log(jqXHR);alert(textStatus);alert(errorThrown);alert(jqXHR.responseText);}).done(function (data){console.log(data);
			$('.list').append("<li>"+data.applicationId+"<span class='glyphicon glyphicon-remove'></span></li>");
			rebind();
			});
});
	// This should fire your window opener...
	$('.modelPasteButton').click(function () {
		console.log('submit funcition is here');
		if ($('#upload').is(':checked')) {
			/*$('#file').fileUpload({
			namespace : 'file_upload_1',
			url : '/path/to/upload/handler.json',
			method : 'PUT'
			});*/
			$.ajax({
				url : "pathToYourFile",
				async : false, // asynchronous request? (synchronous requests are discouraged...)
				cache : false, // with this, you can force the browser to not make cache of the retrieved data
				dataType : "text/plain", // jQuery will infer this, but you can set explicitly
contentType: "text/plain",
           crossDomain: true,
				success : function (data, textStatus, jqXHR) {
					var resourceContent = data; // can be a global variable too...
					// process the content...
				}
			});
		}
		if ($('#paste').is(':checked')) {
var data = $('#exampleTextarea').val();
			/*$('#file').fileUpload({
			namespace : 'file_upload_1',
			url : '/path/to/upload/handler.json',
			method : 'PUT'
			});*/
			$.ajax({
				type : "POST",
				url : 'http://localhost:8177/trace-checking-service/application',
				data : data,
contentType: "text/plain",
crossDomain: true
			}).fail(function(jqXHR, textStatus, errorThrown){console.log(jqXHR);alert(textStatus);alert(errorThrown);alert(jqXHR.responseText);}).done(function (data){console.log(data);
			$('.list').append("<li>"+data.applicationId+"<span class='glyphicon glyphicon-remove'></span></li>");
			rebind();
			});
		}

	});
});
function rebind(){
	$('li').prop('onclick',null).off('click');
	
	$('li').click(function(){
$("#status").text("");
		var which =  $( this ).text();
		$('#which').text(which);
		$.ajax({
			type : "GET",
				url : 'http://localhost:8177/trace-checking-service/'+ which +'/application',
contentType: "text/plain",
crossDomain: true
		}).done(function (data){
for (var node in data) {
    // skip loop if the property is from prototype
    if (!data.hasOwnProperty(node)) continue;
$("#status").append(node +":<br>");
    var obj = data[node];
    for (var formula in obj) {
        // skip loop if the property is from prototype
        if(!obj.hasOwnProperty(formula)) continue;

        // your code
$("#status").append(formula + " = " + obj[formula] + "<br>")
       // alert(formula + " = " + obj[formula]);

    }
};
}).fail(function(jqXHR, textStatus, errorThrown){console.log(jqXHR);alert(textStatus);alert(errorThrown);alert(jqXHR.responseText);});
	});
	$('.glyphicon-remove').prop('onclick',null).off('click');
	$('.glyphicon-remove').click(function(event){
		var which =  $( this ).closest('li').text();
		event.stopPropagation();
		$.ajax({
			type : "DELETE",
				url : 'http://localhost:8177/trace-checking-service/'+ which +'/application',
crossDomain: true
		}).fail(function(jqXHR, textStatus, errorThrown){
console.log(jqXHR);alert(textStatus);alert(errorThrown);alert(jqXHR.responseText);});
	});
}
$(function () {
    'use strict';
    // Change this to the location of your server-side upload handler:
    var url = window.location.hostname === 'blueimp.github.io' ?
                '//jquery-file-upload.appspot.com/' : 'server/php';
    $('#fileupload').fileupload({
        url: url,
        dataType: 'json',
        done: function (e, data) {
            $.each(data.result.files, function (index, file) {
                $('<p/>').text(file.name).appendTo('#files');
            });
        },
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progress .progress-bar').css(
                'width',
                progress + '%'
            );
        }
    }).prop('disabled', !$.support.fileInput)
        .parent().addClass($.support.fileInput ? undefined : 'disabled');
});
