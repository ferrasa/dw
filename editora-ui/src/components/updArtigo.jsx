import { useState, useEffect, useCallback } from "react";
import { useParams, useNavigate } from 'react-router-dom'; // Import hooks for routing
import ArtigoDataService from "../services/artigoDataService";
import { Link } from "react-router-dom";


function UpdArtigo() { 
  console.log("UpdArtigo component loaded");
  const { id } = useParams(); // Obtenha o ID do caminho da URL
  const navigate = useNavigate(); // Hook utilizado para navegação (rotas)

  // --- State ---
  const initialArtigoState = {
    id: null,
    titulo: "",
    resumo: "",
    publicado: false
  };
  const [artigoAtual, setArtigoAtual] = useState(initialArtigoState);
  const [mensagem, setMensagem] = useState(""); // Para mensagens de status

  // --- Buscando Dados ---
  // Usar useCallback para memorizar getArtigo
  const getArtigo = useCallback((id) => {
    console.log("entrei getArtigo");
      ArtigoDataService.get(id)
        .then(response => {
          setArtigoAtual(response.data);
          console.log("Artigo carregado para edição:", response.data);
        })
        .catch(e => {
          console.error(`Erro ao buscar artigo ${id}:`, e);
          // Opcionalmente, navegue de volta ou mostre a mensagem de erro
          // navigate("/list");
        });
  }, []); // Matriz de dependência vazia significa que esta função é criada uma vez

  // Busca o artigo quando o componente é montado ou o ID é alterado
  useEffect(() => {
    if (id) { 
      getArtigo(id);
    }
  }, [id, getArtigo]); // Depende do id e do getArtigo memorizado

  // --- Event Handlers ---
  const handleInputChange = event => {
    const { name, value } = event.target;
    setArtigoAtual({ ...artigoAtual, [name]: value });
    setMensagem(""); 
  };

  const updateStatus = (status) => {
    var data = {
      id: artigoAtual.id,
      titulo: artigoAtual.titulo,
      resumo: artigoAtual.resumo,
      publicado: status
    };

    ArtigoDataService.update(artigoAtual.id, data)
      .then(response => {
        setArtigoAtual({ ...artigoAtual, publicado: status });
        setMensagem("Status atualizado com sucesso!");
        console.log("Status atualizado:", response.data);
      })
      .catch(e => {
        console.error("Erro ao atualizar status:", e);
        setMensagem("Erro ao atualizar status.");
      });
  };

  const updateArtigo = () => {
    ArtigoDataService.update(artigoAtual.id, artigoAtual)
      .then(response => {
        setMensagem("Artigo atualizado com sucesso!");
        console.log("Artigo atualizado:", response.data);
        // Opcionalmente, navegue de volta para a lista após uma atualização bem-sucedida
        // setTimeout(() => navigate("/list"), 1500);
      })
      .catch(e => {
        console.error("Erro ao atualizar artigo:", e);
        setMensagem("Erro ao atualizar artigo.");
      });
  };

  const deleteArtigo = () => {
    if (window.confirm("Tem certeza que deseja excluir este artigo?")) { 
      ArtigoDataService.delete(artigoAtual.id)
        .then(response => {
          console.log("Artigo excluído:", response.data);
          navigate("/list"); // Navegar de volta para a lista após a exclusão
        })
        .catch(e => {
          console.error("Erro ao excluir artigo:", e);
          setMensagem("Erro ao excluir artigo.");
        });
    }
  };

  // --- Render Logic ---
  return (
    <div style={{ maxWidth: "400px", margin: "auto" }}>
      {artigoAtual && artigoAtual.id ? ( // Verifique se os dados do artigo foram carregados
        <div>
          <h4>Editar Artigo</h4>
          <form onSubmit={(e) => e.preventDefault()}> {/* Impedir envio de formulário padrão */}
            <div className="form-group mb-3"> 
              <label htmlFor="titulo"><strong>Título</strong></label>
              <input
                type="text"
                className="form-control"
                id="titulo"
                name="titulo"
                value={artigoAtual.titulo}
                onChange={handleInputChange}
              />
            </div>
            <div className="form-group mb-3"> 
              <label htmlFor="resumo"><strong>Resumo</strong></label>
            <textarea 
              rows={3}
                className="form-control"
                id="resumo"
                name="resumo" 
                value={artigoAtual.resumo}
                onChange={handleInputChange}
              />
            </div>

            <div className="form-group">
              <label>
                <strong>Status:</strong>
              </label>
              <b className="ms-2"> 
                {artigoAtual.publicado ? "Publicado" : "Não publicado"}
              </b>
            </div>
          </form>

          {/* Action Buttons */}
          <div className="mt-3"> 
            {artigoAtual.publicado ? (
              <button
                className="btn btn-sm btn-primary me-2" 
                onClick={() => updateStatus(false)}
              >
                Marcar como Não Publicado
              </button>
            ) : (
              <button
                className="btn btn-sm btn-primary me-2"
                onClick={() => updateStatus(true)}
              >
                Publicar
              </button>
            )}

            <button
              className="btn btn-sm btn-danger me-2"
              onClick={deleteArtigo}
            >
              Excluir
            </button>

            <button
              type="button" 
              className="btn btn-sm btn-success"
              onClick={updateArtigo}
            >
              Atualizar
            </button>
          </div>

          {/* Feedback Message */}
          {mensagem && <p className="mt-3">{mensagem}</p>}
        </div>
      ) : (
        <div>
          <br />
          <p><i>Carregando artigo ou artigo não encontrado...</i></p>
        </div>
      )
      }
        <div>
          <br />
          <Link to="/list" style={{ color: 'blue', textDecoration: 'none' }}>Voltar</Link>
        </div>

    </div>
  );
}

export default UpdArtigo; 