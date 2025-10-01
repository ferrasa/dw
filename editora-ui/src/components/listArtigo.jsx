import { useState, useEffect, useCallback } from "react"; // Import hooks
import ArtigoDataService from "../services/artigoDataService";
import { Link } from "react-router-dom";

function ListArtigo() { 
  console.log("UpdArtigo component loaded");
  // --- Estado ---
  const [artigos, setArtigos] = useState([]);
  const [artigoSel, setArtigoSel] = useState(null); 
  const [indice, setIndice] = useState(-1); 
  const [tituloPesquisa, setTituloPesquisa] = useState(""); 


  // --- Buscando Dados ---
  // useCallback garante que retrieveArtigos não mude em cada renderização, a menos que seja necessário
  const retrieveArtigos = useCallback(() => {
    ArtigoDataService.getAll()
      .then(response => {
        setArtigos(response.data);
        console.log("Artigos carregados:", response.data);
      })
      .catch(e => {
        console.error("Erro ao buscar artigos:", e);
      });
  }, []); // Matriz de dependência vazia significa que esta função é criada uma vez

  // Execute retrieveArtigos quando o componente for montado
  useEffect(() => {
    retrieveArtigos();
  }, [retrieveArtigos]); // Depende do retrieveArtigos memorizado

  // --- Event Handlers ---
  const onChangeTituloPesquisa = e => {
    setTituloPesquisa(e.target.value);
  };

  const refreshList = () => {
    retrieveArtigos();
    setArtigoSel(null);
    setIndice(-1);
  };

  const setArtigoAtivo = (artigo, index) => {
    setArtigoSel(artigo);
    setIndice(index);
  };

  const removeAll = () => {
    ArtigoDataService.deleteAll()
      .then(response => {
        console.log("Todos artigos excluídos:", response.data);
        refreshList(); // Atualiza a lista após a exclusão
      })
      .catch(e => {
        console.error("Erro ao excluir todos artigos:", e);
      });
  };

  const searchTitulo = () => {
    setArtigoSel(null); // Limpar seleção em nova pesquisa
    setIndice(-1);
    ArtigoDataService.findByTitulo(tituloPesquisa)
      .then(response => {
        setArtigos(response.data);
        console.log("Resultado da busca:", response.data);
      })
      .catch(e => {
        console.error("Erro ao buscar por título:", e);
      });
  };

  // --- Render Logic ---
  return (
    <div className="row">
      <div className="col-md-8">
        <div className="input-group mb-3">
          <input
            type="text"
            className="form-control"
            placeholder="Buscar por título"
            value={tituloPesquisa}
            onChange={onChangeTituloPesquisa}
            onKeyDown={(e) => e.key === 'Enter' && searchTitulo()} // Opcional: pesquisar com a tecla Enter
          />
          <div className="input-group-append">
            <button
              className="btn btn-outline-secondary"
              type="button"
              onClick={searchTitulo}
            >
              Buscar
            </button>
          </div>
        </div>
      </div>
      {/* Lista de artigos */}
      <div className="col-md-6">
        <h4>Artigos</h4>

        <ul className="list-group">
          {artigos && artigos.length > 0 ? (
            artigos.map((artigo, index) => (
              <li
                className={
                  "list-group-item " +
                  (index === indice ? "active" : "")
                }
                onClick={() => setArtigoAtivo(artigo, index)}
                key={artigo.id || index} 
                style={{ cursor: "pointer" }} // Indica item clicável
              >
                {artigo.titulo}
              </li>
            ))
           ) : (
            <li className="list-group-item">Nenhum artigo encontrado.</li>
           )}
        </ul>

        <button
          className="m-3 btn btn-sm btn-danger" 
          onClick={removeAll}
          disabled={artigos.length === 0} // Desabilitar se não existirem artigos
        >
          Excluir todos
        </button>
      </div>
      {/* Detalhes do Artigo Selecionado */}
      <div className="col-md-6">
        {artigoSel ? (
          <div>
            <h4>Detalhes</h4>
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
              {artigoSel.publicado ? "Publicado" : "Não publicado"}
            </div>

            <Link
              // Atualiza o link para corresponder à rota definida em App.js
              to={"/artigos/" + artigoSel.id}
              className="btn btn-sm btn-warning mt-2" 
              role="button"
            >
              Editar
            </Link>
          </div>
        ) : (
          <div>
            <h4> </h4>
            <p><i>Selecione um artigo para ver os detalhes.</i></p>
          </div>
        )}
      </div>
    </div>
  );
}

export default ListArtigo; 