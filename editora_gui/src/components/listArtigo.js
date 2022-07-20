import React, { Component } from "react";
import ArtigoDataService from "../services/artigoDataService";
import { Link } from "react-router-dom";


export default class ListArtigo extends Component {
  constructor(props) {
    super(props);
    this.onChangeSearchTitulo = this.onChangeSearchTitulo.bind(this);
    this.retrieveArtigos = this.retrieveArtigos.bind(this);
    this.refreshList = this.refreshList.bind(this);
    this.setArtigoSel = this.setArtigoSel.bind(this);
    this.removeAll = this.removeAll.bind(this);
    this.searchTitulo = this.searchTitulo.bind(this);

    this.state = {
      artigos: [],
      artigoSel: null,
      indice: -1,
      titulo: ""
    };
  }

  componentDidMount() {
    this.retrieveArtigos();
  }

  onChangeSearchTitulo(e) {
    const searchTitulo = e.target.value;

    this.setState({
      titulo: searchTitulo
    });
  }

  retrieveArtigos() {
    ArtigoDataService.getAll()
      .then(response => {
        this.setState({
          artigos: response.data
        });
        console.log(response.data);
      })
      .catch(e => {
        console.log(e);
      });
  }

  refreshList() {
    this.retrieveArtigos();
    this.setState({
      artigoSel: null,
      indice: -1
    });
  }

  setArtigoSel(artigo, index) {
    this.setState({
      artigoSel: artigo,
      indice: index
    });
  }

  removeAll() {
    ArtigoDataService.deleteAll()
      .then(response => {
        console.log(response.data);
        this.refreshList();
      })
      .catch(e => {
        console.log(e);
      });
  }

  searchTitulo() {
    this.setState({
      artigoSel: null,
      indice: -1
    });

    ArtigoDataService.findByTitulo(this.state.titulo)
      .then(response => {
        this.setState({
          artigos: response.data
        });
        console.log(response.data);
      })
      .catch(e => {
        console.log(e);
      });
  }

  render() {
    const { titulo, artigos, artigoSel, indice } = this.state;

    return (
      <div className="list row">
        <div className="col-md-8">
          <div className="input-group mb-3">
            <input
              type="text"
              className="form-control"
              placeholder="Buscar por título"
              value={titulo}
              onChange={this.onChangeSearchTitulo}
            />
            <div className="input-group-append">
              <button
                className="btn btn-outline-secondary"
                type="button"
                onClick={this.searchTitulo}
              >
                Buscar
              </button>
            </div>
          </div>
        </div>
        <div className="col-md-6">
          <h4>Artigos</h4>

          <ul className="list-group">
            {artigos &&
              artigos.map((artigo, index) => (
                <li
                  className={
                    "list-group-item " +
                    (index === indice ? "active" : "")
                  }
                  onClick={() => this.setArtigoSel(artigo, index)}
                  key={index}
                >
                  {artigo.titulo}
                </li>
              ))}
          </ul>

          <button
            className="m-1 btn btn-sm btn-danger"
            onClick={this.removeAll}>Excluir todos
          </button>
        </div>
        <div className="col-md-6">
          {artigoSel ? (
            <div>
              <h4>&nbsp;</h4>
              <div>
                <label>
                  <strong>Título:</strong>
                </label>{" "}
                {artigoSel.titulo}
              </div>
              <div>
                <label>
                  <strong>Resumo:</strong>
                </label>{" "}
                {artigoSel.resumo}
              </div>
              <div>
                <label>
                  <strong>Status:</strong>
                </label>{" "}
                {artigoSel.publicado ? "Publicado" : "Pendente"}
              </div>

              <Link
                to={"/list/" + artigoSel.id}
                className="btn btn-sm btn-warning"
                role="button"
                >
                Editar
              </Link>
            </div>
          ) : (
            <div>
              <h4>&nbsp;</h4>
              
              <p><i>Para detalhes, selecionar um artigo.</i></p>
            </div>
          )}
        </div>
      </div>
    );
  }
}