//Este arquivo centraliza a configuração do axios, o cliente HTTP que você usa para se comunicar com sua API.

import axios from "axios";
// Importa a biblioteca axios, que é usada para fazer requisições HTTP (ex: para uma API).

export default axios.create({
    // Cria e exporta uma instância personalizada do axios com configurações padrão.
    // Desta forma, você não precisa repetir a URL base e os cabeçalhos em toda chamada de API.

    // Inserir o endereço do servidor onde a API está hospedada
    // Se estiver rodando localmente, descomente a linha abaixo e comente a linha baseURL
    //baseURL: "http://localhost:8000/editora",
    // Esta é a URL base para todas as requisições. Todas as chamadas (get, post, etc.) serão prefixadas com esta URL.
    baseURL: "http://localhost:8000/editora",
    
    headers: {
        // Define os cabeçalhos (headers) padrão para todas as requisições.
        "Content-type": "application/json"
        // Informa à API que os dados enviados no corpo da requisição estão no formato JSON.
    }
});