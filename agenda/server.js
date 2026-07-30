import agenda from "./db.js";
import express from 'express';

const app = express();

app.use(express.json());

//chamadas API
app.get('/', (req, res) => {
    res.status(200).send(agenda);
});

app.get('/:id', (req, res) => {
    const id = parseInt(req.params.id);
    const contato = agenda.find( (c) => c.id === id  );

    if (!contato){
        res.status(404).send({erro: 'ID inexistente.'});
        return;
    }
    res.status(200).send(contato);
} );

app.post('/', (req, res) => {
    const contato = req.body;

    const existe = agenda.some( (c) => c.id === contato.id  );

    if (existe){
        res.status(409).send({erro: 'ID duplicado.'});
        return;
    }

    agenda.push(contato);
    res.status(201).send(contato);
});

app.delete('/:id', (req, res) => {
    const id = parseInt(req.params.id);

    const indiceContato = agenda.findIndex( (c) => c.id === id );

    if (indiceContato === -1){
        res.status(404).send({erro: 'Contato inexistente'});
        return;
    }
    const contatoRemovido = agenda.splice(indiceContato, 1);
    res.status(200).send(contatoRemovido);
});

app.listen(3000, () => console.log('AGENDA - API WEB executandp...'));