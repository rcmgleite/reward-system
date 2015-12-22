var http = require('http');

function make_request(host, path, port, method, request_body){
	request_body = JSON.stringify(request_body);
	var	headers = {
			'Content-Type': 'application/json',
			'Content-Length': request_body.length,
		}
	
	var options = {
		host: host,
		path: path,
		port: port,
		method: method,
		headers: headers
	}

	callback = function(response) {
		var str = ''
		response.on('data', function (chunk) {
			str += chunk;
		});

		response.on('end', function () {
			console.log(str);
		});
	}

	var req = http.request(options, callback);
	req.write(request_body);
	req.end();
}

function insert_invitation(querystring, post_data) {
	make_request('localhost', '/api/invitations' + querystring, '8080', 'POST', post_data);
}

(function(){
	var invitations = [
		"?inviter=1&invited=2",
		"?inviter=1&invited=3",
		"?inviter=3&invited=4",
		"?inviter=4&invited=3",
		"?inviter=2&invited=4",
		"?inviter=4&invited=5",
		"?inviter=4&invited=6",
		"?inviter=4&invited=7",
		"?inviter=5&invited=6",
		"?inviter=6&invited=8",
		"?inviter=6&invited=9",
		"?inviter=7&invited=1",
		"?inviter=5&invited=10",
		"?inviter=10&invited=11",
	]			
	
	invitations.forEach(function(invite){
		insert_invitation(invite, {});
	})

}());
