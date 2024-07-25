import agenda from './db.js';
import express from 'express';

const app = express();

app.listen(3000, () =>console.log('AGENDA - API WEB executando'));