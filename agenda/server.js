import agenda from "./db.js";
import express from "express";

const app = express();
app.use(express.json());

app.get('/', (req, res) =>{
    res.status(200).send(agenda);
}); 

app.get('/:id', (req, res) => {
    const id = parseInt(req.params.id);
    const contato = agenda.find( (c) => c.id === id );

    if (!contato){
        res.status(404).send({mens : 'Não existe ID'});
        return; // sai da execução do método
    }
    res.status(200).send(contato);
});

app.post('/', (req, res) => {
    const contato = req.body;

    agenda.push(contato);
    res.status(201).send(contato);
});

app.delete('/:id', (req, res) =>{
    const id = parseInt(req.params.id);
    const indiceContato = agenda.findIndex( (c) => c.id === id);
    agenda.splice(indiceContato, 1);
    
    res.status(200).send(agenda);

});

app.put('/:id', (req, res) => {
    const id = parseInt(req.params.id);
    const contato = agenda.find( (c) => c.id === id);

    const contatoAlterar = req.body;
    const atributos = Object.keys(contatoAlterar);
    
    for (let c of atributos)
        contato[c] = contatoAlterar[c];

    res.status(201).send(contato);

});

app.listen(3000);
console.log('AGENDA - API WEB executando');