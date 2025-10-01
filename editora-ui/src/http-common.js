import axios from "axios";

export default axios.create({
    // Inserir o endereço do servidor onde a API está hospedada
    // Se estiver rodando localmente, descomente a linha abaixo e comente a linha baseURL
    //baseURL: "http://localhost:8000/editora",
    baseURL: "http://136.248.68.47/editora/",
    headers: {
        "Content-type": "application/json"
    }

});

