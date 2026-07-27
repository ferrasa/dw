import http from 'http';

function processa(req, res){
    if (req.method === 'GET' && req.url === '/teste'){
        res.writeHead(200, {'Content-Type': 'text/html'});
        res.write('<h1>Funcionou</h1>');
        res.end();
    }
    else {
        res.writeHead(404);
        res.end('Não encontrado');
  }
}
const servidor = http.createServer(processa);
servidor.listen(3000);
console.log('Servidor ON');
