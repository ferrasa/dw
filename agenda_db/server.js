import express from 'express';
import pg from 'pg';
import dotenv from 'dotenv'

// ativa arquivo de configuração
dotenv.config()

// Cria pool de conexões BD
const { Pool } = pg; 
const db = new Pool({
    user: process.env.DB_USER,
    password: process.env.DB_PASS,
    host: process.env.DB_HOST,
    port: process.env.DB_PORT,
    database: process.env.DB_DBAS
});


const app = express();

app.use(express.json());

//RETORNA TODOS OS CONTATOS EXISTENTES
app.get('/', async (req, res) => {
    try {
        const sql = 'SELECT * FROM contatos';
        const contatos = await db.query(sql);
            
        res.status(200).send(contatos.rows);
    }
    catch(e){
        console.log(e);
        res.status(500).send({erro: 'Um erro ocorreu'});
    }

});

//BUSCA UM CONTATO DADO UM ID
app.get('/:id', async (req, res) => {
    const id = req.params.id;
    try {
        const sql = 'SELECT * FROM contatos WHERE id = ' + id;
        const contatos = await db.query(sql);
            
        res.status(200).send(contatos.rows);
    }
    catch(e){
        console.log(e);
        res.status(500).send({erro: 'Um erro ocorreu'});
    }    
});

//ADICIONA UM NOVO CONTATO
app.post('/', async (req, res) => {
    try{
        const {nome, telefone, email, nota, ativo} = req.body;
        const values = [nome, telefone, email, nota, ativo];
        const sql = 'INSERT INTO contatos(nome, telefone, email, nota, ativo) VALUES ($1, $2, $3, $4, $5)';

        const r = await db.query(sql, values);
        res.status(201).send(r);
    }
    catch(e){
        console.log(e);
        res.status(500).send({erro: 'Um erro ocorreu'});
     }    
});

//ATUALIZA INFORMAÇÕES DE CONTATO
app.put('/:id', async (req, res) => {
    //Obtem valores
    const id = req.params.id;
    const contatoAlterar = req.body;

    // Criar a string de UPDATE
    // Ex.: UPDATE contatos SET nome = $1, telefone = $2 WHERE id = 1 
    var sqlTemp = ['UPDATE contatos'];
    sqlTemp.push('SET');
    var temp = [];

    const col = Object.keys(contatoAlterar);
    col.forEach( ( c, i ) => {
        temp.push( c + ' = $' + (i + 1) );
    })

    sqlTemp.push(temp.join(', '));
    sqlTemp.push('WHERE id = ' + id + ' RETURNING *');
    const sql = sqlTemp.join(' ');

    //Obtem os valores dos atributos
    var atributos = col.map( (c) => {
        return contatoAlterar[c];
    })

    try{
        const r = await db.query(sql, atributos);
        res.status(200).send(r.rows);
    }
    catch(e){
        console.log(e);
        res.status(500).send({erro: 'Um erro ocorreu'});
     }    
});

//REMOVE UM CONTATO
app.delete('/:id', async (req, res) => {
    const id = req.params.id;

    try{
        const sql = 'DELETE FROM contatos WHERE id = $1';
        const r = db.query(sql, [id]);
        res.status(200).send({mensagem: 'Contato removido'});
    }
    catch(e){
        console.log(e);
        res.status(500).send({erro: 'Um erro ocorreu'});
     }    

});


app.listen(process.env.APP_PORT, () =>console.log('AGENDA - API WEB executando'));