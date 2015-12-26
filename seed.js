/*
 *	Program used to test the insert invite endpoint for concurrent requests.
 */
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
			console.log(str + "\n\n");
		});
	}

	var req = http.request(options, callback);
	req.write(request_body);
	req.end();
}

function insert_invitation(post_data) {
	make_request('localhost', '/api/invite', '8080', 'POST', post_data);
}

(function(){
	var invitations = [
		{"inviter": 1, invited: 2},
		{"inviter": 2, invited: 3},
		{"inviter": 3, invited: 4},
		{"inviter": 4, invited: 5},
		{"inviter": 5, invited: 6},
		{"inviter": 6, invited: 7},
		{"inviter": 7, invited: 8},
		{"inviter": 8, invited: 9},
		{"inviter": 9, invited: 10},
		{"inviter": 10, invited: 11},
		{"inviter": 11, invited: 12},
		{"inviter": 12, invited: 13},
		{"inviter": 13, invited: 14},
		{"inviter": 14, invited: 15},
		{"inviter": 15, invited: 16},
		{"inviter": 16, invited: 17},
		{"inviter": 17, invited: 18},
		{"inviter": 18, invited: 19},
		{"inviter": 19, invited: 20},
		{"inviter": 20, invited: 21},
		{"inviter": 21, invited: 22},
		{"inviter": 22, invited: 23},
		{"inviter": 23, invited: 24},
		{"inviter": 24, invited: 25},
		{"inviter": 25, invited: 26},
		{"inviter": 26, invited: 27},
		{"inviter": 27, invited: 28},
		{"inviter": 28, invited: 29},
		{"inviter": 29, invited: 30},
		{"inviter": 30, invited: 31},
		{"inviter": 31, invited: 32},
		{"inviter": 32, invited: 33},
		{"inviter": 33, invited: 34},
		{"inviter": 34, invited: 35},
		{"inviter": 35, invited: 36},
	]

	invitations.forEach(function(invite){
		insert_invitation(invite, {});
	})
}());
