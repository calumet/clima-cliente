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


var app = {

    stations: {
        'S1': '2-12-13-12-40',
        'S2': 'NONE'
    },

    authorized: function (key) {
        if (key !== 'A1B2C3D4E5F6G7') {
            this.res.writeHead(200);
            this.res.setEncoding('utf8');
            this.res.end('ERROR');
            console.log(key);
            console.log('NOT AUTHORIZED');
            return false;
        }
        return true;
    },

    receive: function () {
        var req = this.req;
        var res = this.res;
        var config = this.config;
        var data = this.data;

        console.log('>>> ' + config.method + ' ' + config.url.pathname);

        if (config.method === 'GET') {

            console.dir(config.url.query);
            if (!app.authorized.call(this, config.url.query.key)) return;

            if (config.url.pathname === '/eisi/Clima/getLast.jsp') {
                var answer = stations[config.url.query.station] || 'ERROR';
                res.writeHead(200);
                res.setEncoding('utf8');
                res.end(answer);
                console.log(answer);
                // DD-MM-YY-hh-mm | NONE | ERROR
                return;
            }

        } else if (config.method === 'POST') {

            console.dir(data);
            if (!app.authorized.call(this, data.key)) return;

            if (config.url.pathname === '/eisi/Clima/addData.jsp') {
                res.writeHead(200);
                res.setEncoding('utf8');
                res.end('PROCESSED');
                console.log('PROCESSED');
                // PROCESSED | ERROR
                return;
            }

        }

        res.writeHead(404);
        res.setEncoding('utf8');
        res.end('NOT FOUND');
        console.log('NOT FOUND');
    }

};


var server = http.createServer(function (req, res) {
    
    var body = '';
    
    req.on('data', function (data) {
        body += data;
    });
    
    req.on('end',function () {
        var post = qs.parse(body);
        post.data = post && post.data ? JSON.parse(post.data) : undefined;
        data = '';
        app.receive.call({
            req: req,
            res: res,
            config: {
                method: req.method.toUpperCase(),
                url: url.parse(req.url, true)
            },
            data: post
        });
    });
    
});


server.listen(port, function () {
    console.log('>>> Falso servidor JSP funcionando en el puerto ' + port);
});
