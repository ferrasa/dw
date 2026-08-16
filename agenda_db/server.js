import express from 'express';
import pg from 'pg';
import dotenv from 'dotenv';

dotenv.config();

const { Pool } = pg;
const db = new Pool({
    user: process.env.DB_USER,      // Usuário do banco de dados.
    password: process.env.DB_PASS,  // Senha do banco de dados.
    host: process.env.DB_HOST,      // Endereço do servidor do banco de dados.
    port: process.env.DB_PORT,      // Porta de conexão do banco de dados.
    database: process.env.DB_DBAS   // Nome do banco de dados.

});

const app = express();

app.use(express.json());

//ROTAS - API

app.get('/', async (req, res) => {
    try{
            const sql = 'SELECT * FROM contatos';

            const contatos = await db.query(sql);

            res.status(200).send(contatos.rows);
    }
    catch(e){
        console.log(e);
        res.status(500).send({erro: 'Um erro ocorreu'});
    }
});

app.listen(3000, () => console.log('AGENDA - API WEB executando'));