import agenda from "./db.js";
import express from "express";

const app = express();

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
})

app.listen(3000);
console.log('AGENDA - API WEB executando');