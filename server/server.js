/*!
 * Grupo de Desarrollo de Software Calumet
 * Weather | Server
 * Romel Pérez @prhonedev, 2014
 **/

// This is a fake server to do tests with the application client
// This simulates the JSP server answering the requests

var port = 6000;
var http = require('http');

var server = http.createServer(function (req, res) {
    console.log('>>> New Request:');
    console.dir(req);
    res.end('OKAY');
});

server.listen(port, function () {
    console.log('>>> Server listening at port ' + port);
});
