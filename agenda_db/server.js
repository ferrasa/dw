// Importa o framework Express para a criação de servidores web.
import express from 'express';
// Importa o driver do PostgreSQL para interagir com o banco de dados.
import pg from 'pg';
// Importa a biblioteca dotenv para gerenciar variáveis de ambiente a partir de um arquivo .env.
import dotenv from 'dotenv'

// Carrega as variáveis de ambiente do arquivo .env para process.env.
dotenv.config()

// Desestrutura o pg para obter a classe Pool, que gerencia um conjunto de conexões com o banco de dados.
const { Pool } = pg; 

// Cria uma nova instância do Pool, configurando a conexão com o banco de dados
// utilizando as variáveis de ambiente carregadas anteriormente.
const db = new Pool({
    user: process.env.DB_USER,      // Usuário do banco de dados.
    password: process.env.DB_PASS,  // Senha do banco de dados.
    host: process.env.DB_HOST,      // Endereço do servidor do banco de dados.
    port: process.env.DB_PORT,      // Porta de conexão do banco de dados.
    database: process.env.DB_DBAS    // Nome do banco de dados.
});

// Inicializa a aplicação Express.
const app = express();

// Adiciona um middleware que analisa o corpo das requisições com formato JSON.
// Isso permite acessar os dados enviados no corpo de requisições POST, PUT, etc., através de `req.body`.
app.use(express.json());

// ROTA PARA RETORNAR TODOS OS CONTATOS EXISTENTES
// Define um endpoint para requisições GET na raiz ('/').
app.get('/', async (req, res) => {
    try {
        // Define a consulta SQL para selecionar todos os registros da tabela 'contatos'.
        const sql = 'SELECT * FROM contatos';

        // Executa a consulta no banco de dados e aguarda o resultado.
        const contatos = await db.query(sql);
            
        // Se a consulta for bem-sucedida, envia uma resposta com status 200 (OK)
        // e o corpo da resposta contendo os registros encontrados (contatos.rows).
        res.status(200).send(contatos.rows);
    }
    catch(e){
        // Em caso de erro, exibe o erro no console do servidor.
        console.log(e);
        // Envia uma resposta com status 500 (Erro Interno do Servidor) e uma mensagem de erro genérica.
        res.status(500).send({erro: 'Um erro ocorreu'});
    }
});

// ROTA PARA BUSCAR UM CONTATO ESPECÍFICO DADO UM ID
// Define um endpoint para requisições GET que espera um parâmetro 'id' na URL (ex: /123).
app.get('/:id', async (req, res) => {

    // Extrai o 'id' dos parâmetros da rota.
    const id = req.params.id;
    try {
        // Define a consulta SQL para selecionar um contato específico pelo seu 'id'.
        // Obs.: Concatenar strings para montar queries SQL é vulnerável a SQL Injection.
        const sql = 'SELECT * FROM contatos WHERE id = ' + id;

        // Executa a consulta.
        const contatos = await db.query(sql);
            
        // Envia uma resposta com status 200 (OK) e os dados do contato encontrado.
        res.status(200).send(contatos.rows);
    }
    catch(e){
        // Captura e trata possíveis erros.
        console.log(e);
        res.status(500).send({erro: 'Um erro ocorreu'});
    }    
});

// ROTA PARA ADICIONAR UM NOVO CONTATO
// Define um endpoint para requisições POST na raiz ('/').
app.post('/', async (req, res) => {
    try{
        // Desestrutura o corpo da requisição (req.body) para obter os dados do novo contato.
        const {nome, telefone, email, nota, ativo} = req.body;

        // Agrupa os valores em um array para serem usados na query parametrizada.
        const values = [nome, telefone, email, nota, ativo];
        
        // Define a consulta SQL para inserir um novo registro.
        // Usa placeholders ($1, $2, etc.) para evitar SQL Injection.
        const sql = 'INSERT INTO contatos(nome, telefone, email, nota, ativo) VALUES ($1, $2, $3, $4, $5)';

        // Executa a consulta, passando a query e os valores separadamente.
        const r = await db.query(sql, values);

        // Envia uma resposta com status 201 (Criado) e o resultado da operação de inserção.
        res.status(201).send(r);
    }
    catch(e){
        // Captura e trata possíveis erros.
        console.log(e);
        res.status(500).send({erro: 'Um erro ocorreu'});
     }    
});

// ROTA PARA ATUALIZAR INFORMAÇÕES DE UM CONTATO
// Define um endpoint para requisições PUT que espera um parâmetro 'id' na URL.
app.put('/:id', async (req, res) => {
    // Obtém o 'id' da URL e os dados para atualização do corpo da requisição.
    const id = req.params.id;
    const contatoAlterar = req.body;

    // Lógica para construir a string de UPDATE dinamicamente.
    // Exemplo: UPDATE contatos SET nome = $1, telefone = $2 WHERE id = <id>
    var sqlTemp = ['UPDATE contatos'];
    sqlTemp.push('SET');
    var temp = [];

    // Obtém as chaves (nomes das colunas) do objeto recebido no corpo da requisição.
    const col = Object.keys(contatoAlterar);

    // Para cada coluna, cria uma atribuição parametrizada (ex: "nome = $1").
    col.forEach( ( c, i ) => {
        temp.push( c + ' = $' + (i + 1) );
    })

    // Junta as atribuições com vírgulas.
    sqlTemp.push(temp.join(', '));

    // Adiciona a condição WHERE e a cláusula RETURNING * para retornar o registro atualizado.
    sqlTemp.push('WHERE id = ' + id + ' RETURNING *');

    // Junta todas as partes para formar a consulta SQL final.
    const sql = sqlTemp.join(' ');

    // Obtém os valores correspondentes às colunas que serão atualizadas.
    var atributos = col.map( (c) => {
        return contatoAlterar[c];
    })

    try{
        // Executa a consulta SQL de atualização com os valores parametrizados.
        const r = await db.query(sql, atributos);
    
        // Envia uma resposta com status 200 (OK) e os dados do contato atualizado.
        res.status(200).send(r.rows);
    }
    catch(e){
        // Captura e trata possíveis erros.
        console.log(e);
        res.status(500).send({erro: 'Um erro ocorreu'});
     }    
});

// ROTA PARA REMOVER UM CONTATO
// Define um endpoint para requisições DELETE que espera um parâmetro 'id' na URL.
app.delete('/:id', async (req, res) => {
    // Extrai o 'id' dos parâmetros da rota.
    const id = req.params.id;

    try{
        // Define a consulta SQL para deletar um registro usando um placeholder.
        const sql = 'DELETE FROM contatos WHERE id = $1';
    
        // Executa a consulta passando o 'id' como parâmetro.
        const r = db.query(sql, [id]);
    
        // Envia uma resposta com status 200 (OK) e uma mensagem de confirmação.
        res.status(200).send({mensagem: 'Contato removido'});
    }
    catch(e){
        // Captura e trata possíveis erros.
        console.log(e);
        res.status(500).send({erro: 'Um erro ocorreu'});
     }    
});

// Inicia o servidor para escutar por requisições na porta definida pela variável de ambiente APP_PORT.
app.listen(process.env.APP_PORT, () => console.log('AGENDA - API WEB executando'));