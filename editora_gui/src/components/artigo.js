import React, { Component } from "react";
import ArtigoDataService from "../services/artigoDataService";

import { useParams } from 'react-router-dom';

// Para obter parâmetros passados via Router v6
// Ex.: (em) this.props.match.params.id
export function withRouter(Children){
    return(props)=>{

       const match  = {params: useParams()};
       return <Children {...props}  match = {match}/>
   }
 }

class Artigo extends Component {
  constructor(props) {
    super(props);
    this.onChangeTitulo = this.onChangeTitulo.bind(this);
    this.onChangeResumo = this.onChangeResumo.bind(this);
    this.getArtigo = this.getArtigo.bind(this);
    this.updatePublicado = this.updatePublicado.bind(this);
    this.updateArtigo = this.updateArtigo.bind(this);
    this.deleteArtigo = this.deleteArtigo.bind(this);

    this.state = {
      artigoAtual: {
        id: null,
        titulo: "",
        resumo: "",
        publicado: false
      },
      mensagem: ""
    };
  }
  
  componentDidMount() {

    this.getArtigo(this.props.match.params.id);
  }

  onChangeTitulo(e) {
    const titulo = e.target.value;

    this.setState(function(prevState) {
      return {
        artigoAtual: {
          ...prevState.artigoAtual,
          titulo: titulo
        }
      };
    });
  }

  onChangeResumo(e) {
    const resumo = e.target.value;
    
    this.setState(prevState => ({
      artigoAtual: {
        ...prevState.artigoAtual,
        resumo: resumo
      }
    }));
  }

  getArtigo(id) {
    ArtigoDataService.get(id)
      .then(response => {
        this.setState({
          artigoAtual: response.data
        });
        console.log(response.data);
      })
      .catch(e => {
        
        console.log("Erro: "+e);
      });
  }

  updatePublicado(status) {
    var data = {
      id: this.state.artigoAtual.id,
      titulo: this.state.artigoAtual.titulo,
      resumo: this.state.artigoAtual.resumo,
      publicado: status
    };

    ArtigoDataService.update(this.state.artigoAtual.id, data)
      .then(response => {
        this.setState(prevState => ({
          artigoAtual: {
            ...prevState.artigoAtual,
            publicado: status
          }
        }));
        console.log(response.data);
      })
      .catch(e => {
        console.log(e);
      });
  }

  updateArtigo() {
    ArtigoDataService.update(
      this.state.artigoAtual.id,
      this.state.artigoAtual
    )
      .then(response => {
        console.log(response.data);
        this.setState({
          mensagem: "Artigo atualizado com sucesso!"
        });
      })
      .catch(e => {
        console.log(e);
      });
  }

  deleteArtigo() {    
    ArtigoDataService.delete(this.state.artigoAtual.id)
      .then(response => {
        console.log(response.data);
        this.props.history.push('/list')
      })
      .catch(e => {
        console.log(e);
      });
  }

  render() {
    const { artigoAtual } = this.state;

    return (
      <div>
        {artigoAtual ? (
          <div className="edit-form">
            <h4>Artigo</h4>
            <form>
              <div className="form-group">
                <label htmlFor="titulo"><strong>Título</strong></label>
                <input
                  type="text"
                  className="form-control"
                  id="titulo"
                  value={artigoAtual.titulo}
                  onChange={this.onChangeTitulo}
                />
              </div>
              <div className="form-group">
                <label htmlFor="resumo"><strong>Resumo</strong></label>
                <input
                  type="text"
                  className="form-control"
                  id="resumo"
                  value={artigoAtual.resumo}
                  onChange={this.onChangeResumo}
                />
              </div>

              <div className="form-group">
                <br />
                <label>
                  <strong>Status:</strong>
                </label>
                    <b>
                    {artigoAtual.publicado ? " publicado" : " não publicado"}
                    </b>
              </div>
            </form>
            

            {artigoAtual.publicado ? (
              <button
                className="m-2 btn btn-sm btn-primary mr-2"
                onClick={() => this.updatePublicado(false)}
              >
                Alterar status
              </button>
            ) : (
              <button
                className="m-2 btn btn-sm btn-primary mr-2"
                onClick={() => this.updatePublicado(true)}
              >
                Alterar status
              </button>
            )}

            <button
              className="m-2 btn btn-sm btn-danger mr-2"
              onClick={this.deleteArtigo}
            >
              Excluir
            </button>

            <button
              type="submit"
              className="m-2 btn btn-sm btn-success"
              onClick={this.updateArtigo}
            >
              Atualizar
            </button>
            <p>{this.state.mensagem}</p>
          </div>
        ) : (
          <div>
            <br />
            <p><i>Para detalhes, selecionar um artigo.</i></p>
          </div>
        )}
      </div>
    );
  }
}
export default withRouter(Artigo);