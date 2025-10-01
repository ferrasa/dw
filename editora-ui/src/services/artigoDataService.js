import http from "../http-common";

class ArtigoDataService {
  getAll() {
    return http.get("/");
  }

  get(id) {
    return http.get(`/${id}`);
  }

  create(data) {
    return http.post("/", data);
  }

  update(id, data) {
    return http.put(`/${id}`, data);
  }

  delete(id) {
    return http.delete(`/${id}`);
  }

  deleteAll() {
    return http.delete(`/`);
  }

  findByTitulo(titulo) { 
    return http.get(`/?titulo=${titulo || ""}`);
  }
}

export default new ArtigoDataService();