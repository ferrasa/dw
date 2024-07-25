// EXEMPLO DE CHAMADA GET USANDO FRAMEWORK EXPRESS
import express from 'express';

const app = express();

function handleRequest(req, res){
    res.status(200).send('<h1>FUNCIONOU!</h1>');    
};

app.get('/', handleRequest);

app.listen(3000);
console.log('SERVIDOR EM EXECUCAO');

/* // EXEMPLO DE CHAMADA GET SEM USO DE FRAMEWORK

import http from 'http';


function handleRequest(req, res){
    if (req.method === 'GET' && req.url === '/'){
        res.writeHead(200, {'Content-Type': 'text/html'});
        res.write('<h1>FUNCIONOU</h1>');
        res.end();
    }
}
const server = http.createServer(handleRequest);

server.listen(3000);
console.log('SERVIDOR EM EXECUCAO');*/