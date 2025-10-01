import { useState } from "react"; // Import useState
import ArtigoDataService from "../services/artigoDataService";
import { Link } from "react-router-dom";

function AddArtigo() { 
  // --- Estado usando useState ---
  const initialArtigoState = {
    id: null,
    titulo: "",
    resumo: "",
    publicado: false,
  };
  // Acompanhe o artigo que está sendo editado/criado
  const [artigo, setArtigo] = useState(initialArtigoState);
  // Acompanhe o status do envio
  const [enviado, setEnviado] = useState(false);

  // --- Event Handlers ---
  const handleInputChange = event => {
    const { name, value } = event.target;
    setArtigo({ ...artigo, [name]: value });
  };

  const saveArtigo = () => {
    var data = {
      titulo: artigo.titulo,
      resumo: artigo.resumo
    };

    ArtigoDataService.create(data)
      .then(response => {
        // Atualiza o estado com os dados do artigo recém-criado (incluindo o ID)
        setArtigo({
          id: response.data.id,
          titulo: response.data.titulo,
          resumo: response.data.resumo,
          publicado: response.data.publicado
        });
        setEnviado(true); // Definir status de envio
        console.log("Artigo criado:", response.data);
      })
      .catch(e => {
        console.error("Erro ao criar artigo:", e);
        // Você pode adicionar feedback do usuário para erros
      });
  };

  const newArtigo = () => {
    setArtigo(initialArtigoState); // Redefinir campos do formulário
    setEnviado(false); // Redefinir status de envio
  };

  // --- Render Logic ---
  return (
    <div style={{ maxWidth: "400px", margin: "auto" }}>
      {enviado ? (
        <div>
          <h5>O artigo foi enviado com sucesso!</h5>
          <button className="btn btn-success" onClick={newArtigo}>
            Adicionar outro artigo
          </button>          
          <div>
            <br />
            <Link to="/list" style={{ color: 'blue', textDecoration: 'none' }}>Voltar</Link>
          </div>

        </div>

      ) : (
        <div>
          <div className="form-group mb-3"> 
            <label htmlFor="titulo"><strong>Título</strong></label>
            <input
              type="text"
              className="form-control"
              id="titulo"
              required
              value={artigo.titulo}
              onChange={handleInputChange} // Usar manipulador unificado
              name="titulo" // O atributo Name é imprescendível para o manipulador unificado
            />
          </div>

          <div className="form-group mb-3"> 
            <label htmlFor="resumo"><strong>Resumo</strong></label>
            <textarea 
              rows={3}
              placeholder="Digite o resumo do artigo aqui..."
              className="form-control"
              id="resumo"
              required
              value={artigo.resumo}
              onChange={handleInputChange} // Usar manipulador unificado
              name="resumo" // O atributo Name é imprescendível para o manipulador unificado
            />
          </div>

          <button onClick={saveArtigo} className="btn btn-success">
            Enviar
          </button>
        </div>
      )}
    </div>
  );
}

export default AddArtigo;