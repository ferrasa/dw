// check.js
import Joi from 'joi';

// Regex simples para celular (DDD + 9 + 8 dígitos)
const phoneRegex = /^[1-9]{2}9?[0-9]{8}$/;

// Schema principal de criação
export const contatoModel = Joi.object({
    id: Joi.number().integer().positive().required(),
    nome: Joi.string().min(3).trim().required(),
    telefone: Joi.string().pattern(phoneRegex).required().messages({
        'string.pattern.base': 'Telefone deve estar no formato DDNÚMERO (ex: 42999999999).'
    }),
    email: Joi.string().email().lowercase().trim().required(),
    nota: Joi.string().allow('').default(''),
    ativo: Joi.boolean().default(true)
});

// Schema de atualização (Reaproveita o principal tornando os campos opcionais)
export const contatoModelUpdate = contatoModel
    .fork(['id', 'nome', 'telefone', 'email', 'nota', 'ativo'], (schema) => schema.optional());
    