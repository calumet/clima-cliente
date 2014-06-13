/*!
 * Grupo de Desarrollo de Software Calumet
 * Weather | Server
 * Romel Pérez @prhonedev, 2014
 **/

// Esto simula ser el servidor de JSP que va a recibir y procesar

var port = 6000;
var http = require('http');
var url = require('url');
var qs = require('querystring');
var key = 'A1B2C3D4E5F6G7';
var stations = {
    'S1': '2-12-13-12-40',
    'S2': '2-12-13-12-45',
    'S3': 'NONE'
};


var processor = function (config, data) {

    var req = this.req;
    var res = this.res;

    console.log('>>> ' + config.method + ' ' + config.url.pathname);

    var unauthorized = function (sentKey) {
        if (key !== sentKey) {
            res.writeHead(500);
            res.end('NOT AUTHORIZED');
            console.log('NOT AUTHORIZED');
        }
    }

    if (config.method === 'GET') {

        unauthorized(config.url.query.key);
        console.dir(config.url.query);

        if (config.url.pathname === '/weather/getDataLast') {
            var answer = stations[config.url.query.station] || 'ERROR';
            res.writeHead(200);
            res.end(answer);
            console.log(answer);
            // ERROR | NONE | <<DATA>>
        }

    } else if (config.method === 'POST') {

        unauthorized(data.key);
        console.dir(data);

        if (config.url.pathname === '/weather/addData') {
            res.writeHead(200);
            res.end('PROCESSED');
            console.log('PROCESSED');
            // PROCESSED | ERROR
        }

    }

    res.writeHead(404);
    res.end('NOT FOUND');

};


var server = http.createServer(function (req, res) {
    
    var body = '';
    
    req.on('data', function (data) {
        body += data;
    });
    
    req.on('end',function () {
        var post = qs.parse(body);
        var data = post && post.data ? JSON.parse(post.data) : '';
        var config = {
            method: req.method.toUpperCase(),
            url: url.parse(req.url, true)
        };
        post.data = data;
        processor.call({req: req, res: res}, config, post);
    });
    
});


server.listen(port, function () {
    console.log('>>> Falso servidor JSP funcionando en el puerto ' + port);
});
