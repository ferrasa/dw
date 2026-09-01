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

// ============================================================================
// ROTA PARA LISTAR TODOS OS PRODUTOS (COM SUPORTE A FILTRO POR CATEGORIA)
// Exemplo sem filtro: GET /produtos
// Exemplo com filtro: GET /produtos?categoria=Eletronicos
// ============================================================================
app.get('/produtos', async (req, res) => {
    try {
        // Captura o parâmetro de busca por categoria via Query Query (req.query)
        const { categoria } = req.query;

        let sql = 'SELECT * FROM produtos';
        const values = [];

        // Se uma categoria foi passada na URL, adiciona a cláusula WHERE
        if (categoria) {
            sql += ' WHERE categoria = $1';
            values.push(categoria);
        }

        // Executa a consulta com ou sem filtro parametrizado
        const produtos = await db.query(sql, values);
        
        // Retorna a lista de produtos encontrados
        res.status(200).send(produtos.rows);
    } 
    catch (e) {
        console.error(e);
        res.status(500).send({ erro: 'Um erro ocorreu ao buscar os produtos' });
    }
});

// ============================================================================
// ROTA PARA BUSCAR UM PRODUTO ESPECÍFICO PELO ID
// Exemplo: GET /produtos/1
// ============================================================================
app.get('/produtos/:id', async (req, res) => {
    // Extrai o 'id' dos parâmetros da rota.
    const id = req.params.id;

    try {
        // CORREÇÃO DE SEGURANÇA: Uso do placeholder $1 para evitar SQL Injection
        const sql = 'SELECT * FROM produtos WHERE id = $1';

        // Executa a consulta de forma segura passando o parâmetro no array
        const produtos = await db.query(sql, [id]);

        // Validação de existência: Se o array estiver vazio, o ID não existe no banco
        if (produtos.rows.length === 0) {
            return res.status(404).send({ erro: 'Produto não encontrado' });
        }

        // Retorna apenas o produto encontrado (primeira linha do array)
        res.status(200).send(produtos.rows[0]);
    } 
    catch (e) {
        console.error(e);
        res.status(500).send({ erro: 'Um erro ocorreu ao buscar o produto' });
    }
});

// ============================================================================
// ROTA PARA CADASTRAR UM NOVO PRODUTO
// Exemplo: POST /produtos (Corpo da requisição em JSON)
// ============================================================================
app.post('/produtos', async (req, res) => {
    try {
        // Desestrutura o corpo da requisição para obter os dados
        const { nome, preco, quantidade_estoque, categoria } = req.body;

        // VALIDAÇÃO BÁSICA: Nome e Preço são obrigatórios
        if (!nome || preco === undefined) {
            return res.status(400).send({ erro: 'Campos nome e preco são obrigatórios' });
        }

        // Caso a quantidade de estoque não seja informada, assume o valor padrão 0
        const estoque = quantidade_estoque !== undefined ? quantidade_estoque : 0;

        const values = [nome, preco, estoque, categoria];

        // SQL parametrizado com RETURNING * para devolver o objeto cadastrado (incluindo seu novo ID)
        const sql = 'INSERT INTO produtos (nome, preco, quantidade_estoque, categoria) VALUES ($1, $2, $3, $4) RETURNING *';

        const r = await db.query(sql, values);

        // Retorna status 201 (Created) e o produto recém-criado
        res.status(201).send(r.rows[0]);
    } 
    catch (e) {
        console.error(e);
        res.status(500).send({ erro: 'Um erro ocorreu ao cadastrar o produto' });
    }
});

// ============================================================================
// ROTA PARA ATUALIZAR INFORMAÇÕES DE UM PRODUTO
// Exemplo: PUT /produtos/1
// ============================================================================
app.put('/produtos/:id', async (req, res) => {
    const id = req.params.id;
    const produtoAlterar = req.body;

    // Obtém os nomes das colunas passadas no corpo do JSON
    const col = Object.keys(produtoAlterar);

    // Se nenhum parâmetro foi enviado para atualização
    if (col.length === 0) {
        return res.status(400).send({ erro: 'Nenhum dado informado para atualização' });
    }

    let temp = [];
    // Constrói dinamicamente os atrubutos: Ex: ["nome = $1", "preco = $2"]
    col.forEach((c, i) => {
        temp.push(`${c} = $${i + 1}`);
    });

    // CORREÇÃO DE SEGURANÇA NO WHERE: 
    // O id do produto é adicionado como o último parâmetro ($N + 1)
    const sql = `UPDATE produtos SET ${temp.join(', ')} WHERE id = $${col.length + 1} RETURNING *`;

    // Mapeia os valores a serem atualizados
    let valAtributos = col.map((c) => produtoAlterar[c]);
    
    // Insere o 'id' no final do array de valores para preencher o último placeholder ($)
    valAtributos.push(id);

    try {
        const r = await db.query(sql, valAtributos);

        // Se r.rowCount for 0, o ID não foi encontrado no banco
        if (r.rowCount === 0) {
            return res.status(404).send({ erro: 'Produto não encontrado' });
        }

        // Retorna o produto atualizado
        res.status(200).send(r.rows[0]);
    } 
    catch (e) {
        console.error(e);
        res.status(500).send({ erro: 'Um erro ocorreu ao atualizar o produto' });
    }
});

// ============================================================================
// ROTA PARA REMOVER UM PRODUTO
// Exemplo: DELETE /produtos/1
// ============================================================================
app.delete('/produtos/:id', async (req, res) => {
    const id = req.params.id;

    try {
        const sql = 'DELETE FROM produtos WHERE id = $1';
        const r = await db.query(sql, [id]);

        // Verifica se algum registro foi realmente deletado
        if (r.rowCount === 0) {
            return res.status(404).send({ erro: 'Produto não encontrado' });
        }

        res.status(200).send({ mensagem: 'Produto removido com sucesso' });
    } 
    catch (e) {
        console.error(e);
        res.status(500).send({ erro: 'Um erro interno ocorreu ao remover o produto' });
    }
});

// ============================================================================
// DESAFIO BÔNUS: ROTA PARA AUMENTAR OU DIMINUIR ESTOQUE
// Exemplo: PATCH /produtos/1/estoque
// Corpo JSON: { "quantidade": 5, "operacao": "adicionar" } ou "remover"
// ============================================================================
app.patch('/produtos/:id/estoque', async (req, res) => {
    const id = req.params.id;
    const { quantidade, operacao } = req.body;

    // Validação básica da requisição
    if (!quantidade || !['adicionar', 'remover'].includes(operacao)) {
        return res.status(400).send({ erro: 'Parâmetros "quantidade" e "operacao" (adicionar/remover) são obrigatórios' });
    }

    try {
        // Busca o estoque atual do produto no banco
        const prodQuery = await db.query('SELECT quantidade_estoque FROM produtos WHERE id = $1', [id]);

        // Verifica se o produto existe
        if (prodQuery.rows.length === 0) {
            return res.status(404).send({ erro: 'Produto não encontrado' });
        }

        const estoqueAtual = prodQuery.rows[0].quantidade_estoque;
        let novoEstoque = estoqueAtual;

        // Calcula o novo valor do estoque baseado na operação solicitada
        if (operacao === 'adicionar') {
            novoEstoque += quantidade;
        } else if (operacao === 'remover') {
            // REGRA DE NEGÓCIO: Impede estoque negativo e avisa o usuário detalhadamente
            if (estoqueAtual < quantidade) {
                return res.status(400).send({ 
                    erro: `Estoque insuficiente para esta operação. Estoque atual: ${estoqueAtual}` 
                });
            }
            novoEstoque -= quantidade;
        }

        // Atualiza a quantidade no banco de dados
        const sql = 'UPDATE produtos SET quantidade_estoque = $1 WHERE id = $2 RETURNING *';
        const r = await db.query(sql, [novoEstoque, id]);

        // Retorna o produto com o estoque atualizado
        res.status(200).send(r.rows[0]);
    } 
    catch (e) {
        console.error(e);
        res.status(500).send({ erro: 'Um erro ocorreu ao alterar o estoque' });
    }
});

// Inicia o servidor para escutar por requisições na porta 3000
app.listen(3000, () => console.log('CATÁLOGO DE PRODUTOS - API WEB executando na porta 3000'));