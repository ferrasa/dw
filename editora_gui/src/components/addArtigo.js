import React, { Component } from "react";
import ArtigoDataService from "../services/artigoDataService";

export default class AddArtigo extends Component {

    constructor(props) {
        super(props);

        this.onChangeTitulo = this.onChangeTitulo.bind(this);
        this.onChangeResumo = this.onChangeResumo.bind(this);
        this.saveArtigo = this.saveArtigo.bind(this);
        this.newArtigo = this.newArtigo.bind(this);    
    
        this.state = {
            id: null,
            titulo: "",
            resumo: "", 
            publicado: false,      
            enviado: false
          };
    }

    onChangeTitulo(e) {
        this.setState({
          titulo: e.target.value
        });
      }

      onChangeResumo(e) {
        this.setState({
          resumo: e.target.value
        });
      }

      saveArtigo() {
        var data = {
          titulo: this.state.titulo,
          resumo: this.state.resumo
        };
    
        ArtigoDataService.create(data)
          .then(response => {
            this.setState({
              id: response.data.id,
              titulo: response.data.titulo,
              resumo: response.data.resumo,
              publicado: response.data.publicado,
    
              enviado: true
            });
            console.log(response.data);
          })
          .catch(e => {
            console.log(e);
          });
      }

    newArtigo() {
        this.setState({
          id: null,
          titulo: "",
          resumo: "",
          publicado: false,
    
          enviado: false
        });
    }

    render() {
        return (
            <div className="submit-form">
                { this.state.enviado ? (
                              <div>
                              <h4>O artigo foi enviado com sucesso!</h4>
                              <button className="btn btn-success" onClick={this.newArtigo}>
                                Adicionar outro artigo
                              </button>
                            </div>
                  
                ) : (
                    <div>
                    <div className="form-group">
                      <label htmlFor="titulo"><strong>Título</strong></label>
                      <input
                        type="text"
                        className="form-control"
                        id="titulo"
                        required
                        value={this.state.titulo}
                        onChange={this.onChangeTitulo}
                        name="titulo"
                      />
                    </div>
        
                    <div className="form-group">
                      <label htmlFor="resumo"><strong>Resumo</strong></label>
                      <input
                        type="text"
                        className="form-control"
                        id="resumo"
                        required
                        value={this.state.resumo}
                        onChange={this.onChangeResumo}
                        name="resumo"
                      />
                    </div>
                   <p></p>
                    <button onClick={this.saveArtigo} className="btn btn-success">
                      Enviar
                    </button>
                  </div>
                )}
            </div>
        )
    } 
}