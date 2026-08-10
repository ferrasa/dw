// Importa a lista de contatos (banco de dados simulado) a partir de um arquivo local chamado "db.js"
import agenda from "./db.js";

// Importa os schemas de validação do Joi do arquivo check.js
import { contatoModel, contatoModelUpdate } from './check.js';

// Importa o framework Express para a criação do servidor e manipulação de rotas HTTP
import express from 'express';

// Inicializa a aplicação Express e atribui à constante "app" para configurar rotas e middlewares
const app = express();

// Configura o Express para interpretar requisições que chegam com o corpo (body) no formato JSON
app.use(express.json());

// ==========================================
// CHAMADAS API (ROTAS)
// ==========================================

// Rota GET raiz ('/'): Retorna a lista completa de contatos com o status HTTP 200 (OK)
app.get('/', (req, res) => {
    res.status(200).send(agenda);
});

// Rota GET com parâmetro dinâmico 'id' ('/:id'): Busca um contato específico pelo identificador
app.get('/:id', (req, res) => {
    // Extrai o parâmetro "id" da URL e o converte de string para número inteiro
    const id = parseInt(req.params.id);
    
    // Procura na "agenda" o primeiro contato cujo ID seja igual ao ID informado na URL
    const contato = agenda.find( (c) => c.id === id  );

    // Se o contato não for encontrado (retorna undefined/falsy), envia erro 404 (Não Encontrado)
    if (!contato){
        res.status(404).send({erro: 'ID inexistente.'});
        return; // Interrompe a execução da função
    }
    
    // Se encontrou, retorna o contato encontrado com o status HTTP 200 (OK)
    res.status(200).send(contato);
} );

// Rota POST ('/'): Cadastra um novo contato na agenda
app.post('/', (req, res) => {
    // Obtém os dados enviados pelo cliente no corpo da requisição (body)
    const contato = req.body;

    //Opcional: verifica formato de dados enviados para cadastro
    const {error} = contatoModel.validate(contato);
    if (error){
        //error.details[0].message : obtem somente o texto da mensagem de erro.
        res.status(400).send({mens: error});
        return; // sai da execução do método
    }


    // Verifica se já existe algum contato na agenda com o mesmo ID informado
    const existe = agenda.some( (c) => c.id === contato.id  );

    // Se o ID já existir, interrompe o cadastro e retorna erro 409 (Conflito)
    if (existe){
        res.status(409).send({erro: 'ID duplicado.'});
        return;
    }

    // Adiciona o novo contato ao final do array "agenda"
    agenda.push(contato);
    
    // Retorna o contato recém-criado com o status HTTP 201 (Criado com sucesso)
    res.status(201).send(contato);
});

// Rota DELETE com parâmetro dinâmico 'id' ('/:id'): Remove um contato da agenda
app.delete('/:id', (req, res) => {
    // Extrai e converte o ID da URL para número inteiro
    const id = parseInt(req.params.id);

    // Busca o *índice* (posição no array) do contato que possui o ID informado
    const indiceContato = agenda.findIndex( (c) => c.id === id );

    // Se o índice for -1, significa que o contato não foi encontrado; retorna erro 404
    if (indiceContato === -1){
        res.status(404).send({erro: 'Contato inexistente'});
        return;
    }
    
    // Remove o contato do array usando o método splice a partir do índice encontrado
    const contatoRemovido = agenda.splice(indiceContato, 1);
    
    // Retorna o contato que foi removido com o status HTTP 200 (OK)
    res.status(200).send(contatoRemovido);
});

// Rota PUT com parâmetro dinâmico 'id' ('/:id'): Atualiza os dados de um contato existente
app.put('/:id', (req, res) => {
    // Extrai e converte o ID recebido na URL para número inteiro
    const idParam = parseInt(req.params.id);
    
    // Procura o contato na agenda que corresponde ao ID da URL
    const contato = agenda.find( (c) => c.id === idParam);

    // Se o contato não existir, retorna erro 404 (Não Encontrado)
    if (!contato){
        res.status(404).send({erro: 'Nao existe contato para este ID'});
        return;
    }
    
    // Obtém os novos dados enviados no corpo da requisição
    const contatoAlterar = req.body;

    //Opcional: verifica formato de dados enviados para alteração
    const {error} = contatoModelUpdate.validate(contatoAlterar);
    if (error){
        //error.details[0].message : obtem somente o texto da mensagem de erro.
        res.status(400).send({mens: error});
        return; // sai da execução do método
    }


    // Extrai todas as chaves (propriedades) enviadas no objeto de alteração
    const atributos = Object.keys(contatoAlterar);
    
    // Percorre cada atributo enviado para atualizar dinamicamente o contato existente
    for (let a of atributos){
        contato[a] = contatoAlterar[a];
    }
    
    // Retorna o contato atualizado com o status HTTP 201 (ou 200, indicando sucesso na alteração)
    res.status(201).send(contato);
});

// Inicializa o servidor HTTP na porta 3000 e exibe uma mensagem no console quando estiver rodando
app.listen(3000, () => console.log('AGENDA - API WEB executando...'));