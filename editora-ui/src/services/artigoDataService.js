// Este arquivo, conhecido como "service" (serviço), isola a lógica de comunicação com a API (backend). 

import http from "../http-common";
// Importa a instância configurada do axios do arquivo http-common.js.

class ArtigoDataService {
  // Define uma classe para agrupar todos os métodos relacionados à API de artigos.

  getAll() {
    // Método para buscar todos os artigos.
    return http.get("/");
    // Faz uma requisição GET para a URL base (ex: "http://136.248.68.47/editora/").
  }

  get(id) {
    // Método para buscar um único artigo pelo seu ID.
    return http.get(`/${id}`);
    // Faz uma requisição GET para a URL base + /id (ex: "/editora/5").
  }

  create(data) {
    // Método para criar um novo artigo.
    return http.post("/", data);
    // Faz uma requisição POST para a URL base, enviando os dados do novo artigo no corpo da requisição.
  }

  update(id, data) {
    // Método para atualizar um artigo existente.
    return http.put(`/${id}`, data);
    // Faz uma requisição PUT para a URL base + /id, enviando os dados atualizados no corpo da requisição.
  }

  delete(id) {
    // Método para excluir um artigo pelo seu ID.
    return http.delete(`/${id}`);
    // Faz uma requisição DELETE para a URL base + /id.
  }

  deleteAll() {
    // Método para excluir todos os artigos.
    return http.delete(`/`);
    // Faz uma requisição DELETE para a URL base.
  }

  findByTitulo(titulo) { 
    // Método para buscar artigos por título.
    return http.get(`/?titulo=${titulo || ""}`);
    // Faz uma requisição GET com um parâmetro de busca na URL (ex: "/editora/?titulo=React").
  }
}

export default new ArtigoDataService();
// Exporta uma nova instância da classe, permitindo que os métodos sejam chamados diretamente (ex: ArtigoDataService.getAll()).