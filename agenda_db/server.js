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
        const sql = 'SELECT * FROM agenda';
        const contatos = await db.query(sql);
            
        res.status(200).send(contatos.rows);
    }
    catch(e){
        console.log(e);
        res.status(500).send({erro: 'Um erro ocorreu'});
    }

});

//BUSCA UM CONTATO DADO UM ID
app.get('/:id', (req, res) => {
    const id = req.params.id;

});

//ADICIONA UM NOVO CONTATO
app.post('/', (req, res) => {
    const {nome, telefone, email, nota, ativo} = req.body;
});

//ATUALIZA INFORMAÇÕES DE CONTATO
app.put('/:id', (req, res) => {
    //Obtem valores
    const id = req.params.id;
    const contatoAlterar = req.body;

    // Criar a string de UPDATE
    // Ex.: UPDATE agenda SET nome = $1, telefone = $2 WHERE id = 1 
    
});

//REMOVE UM CONTATO
app.delete('/:id', (req, res) => {
    const id = req.params.id;
});


app.listen(process.env.APP_PORT, () =>console.log('AGENDA - API WEB executando'));