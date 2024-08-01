import agenda from './db.js';
import express from 'express';
import { contatoModel, contatoModelUpdate } from './check.js';


const app = express();

app.use(express.json());

app.get('/', (req, res) => {
    res.status(200).send(agenda);
});

app.get('/:id', (req, res) => {
    const id = parseInt(req.params.id);
    const contato = agenda.find( (c) => c.id === id);
    if (!contato) {
        res.status(404).send({mens: 'Não existe um contato para o ID informado.'});
        return; // sai da execução do método
    }
    res.status(200).send(contato);
});

app.put('/:id', (req, res) => {
    const id = parseInt(req.params.id);
    const contato = agenda.find( (c) => c.id === id);

    if (!contato) {
        res.status(404).send({mens: 'Não existe um contato para o ID informado.'});
        return; // sai da execução do método
    }

    const contatoAlterar = req.body;

    const {error} = contatoModelUpdate.validate(contatoAlterar);
    if (error){
        //error.details[0].message : obtem somente o texto da mensagem de erro.
        res.status(400).send({mens: error.details[0].message});
        return; // sai da execução do método
    }

    const campos = Object.keys(contatoAlterar);
    console.log(campos);
    for (let c of campos) {  
        contato[c] = contatoAlterar[c];
    }
    res.status(200).send(contato);
});

app.post('/', (req, res) => {
    const contato = req.body;

    const {error} = contatoModel.validate(contato);
    if (error){
        //error.details[0].message : obtem somente o texto da mensagem de erro.
        res.status(400).send({mens: error.details[0].message});
        return; // sai da execução do método
    }

    agenda.push(contato);
    res.status(201).send(contato);
});

app.delete('/:id', (req, res) => {
    const id = parseInt(req.params.id);
    const indiceContato = agenda.findIndex((c) => c.id === id);

    if (indiceContato === -1) {
        res.status(404).send({mens: 'Não existe um contato para o ID informado.'});
        return; // sai da execução do método
    }
    
    const contatoRemovido = agenda.splice(indiceContato, 1);
    res.status(200).send(contatoRemovido);
});


app.listen(3000, () =>console.log('AGENDA - API WEB executando'));