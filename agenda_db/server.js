import express from 'express';
import pg from 'pg';
import dotenv from 'dotenv';

// Ativa arquivo de config.
dotenv.config();

//Cria Pool de conexoes
const { Pool } = pg;
const db = new Pool(
    {
        user: process.env.DB_USER,
        password: process.env.DB_PASS,
        host: process.env.DB_HOST,
        port: process.env.DB_PORT,
        database: process.env.DB_DBAS
    }
);
