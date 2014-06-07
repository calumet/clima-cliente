/*!
 * Grupo de Desarrollo de Software Calumet
 * Weather | Server
 * Romel Pérez @prhonedev, 2014
 **/

// This is a fake server to do tests with the application client
// This simulates the JSP server answering the requests

var port = 6000;
var http = require('http');
var url = require('url');
var qs = require('querystring');


var server = http.createServer(function (req, res) {
    var body = '';

    console.log('>>> New Request:');

    // Read data fragments
    req.on('data', function (data) {
        body += data;
    });

    // Complete data reading
    req.on('end',function () {
        var post = qs.parse(body);
        var data = JSON.parse(post.data);
        console.dir(data);

        // Send response
        res.writeHead(200);
        res.end('UPDATED');
    });
});


server.listen(port, function () {
    console.log('>>> Fake-server simulating the JSP server');
    console.log('>>> Server listening at port ' + port);
});
