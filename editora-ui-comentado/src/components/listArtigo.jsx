/ /Este componente exibe a lista de todos os artigos, permite a busca por título e a seleção para ver detalhes.

import { useState, useEffect, useCallback } from "react"; // Importa os hooks do React.
import ArtigoDataService from "../services/artigoDataService"; // Importa o serviço de dados.
import { Link } from "react-router-dom"; // Importa o componente Link para navegação.

function ListArtigo() { 
  // --- Estado ---
  const [artigos, setArtigos] = useState([]);
  // 'artigos': um array para armazenar a lista de artigos vinda da API. Inicia como um array vazio.
  const [artigoSel, setArtigoSel] = useState(null); 
  // 'artigoSel': o artigo atualmente selecionado na lista. Inicia como nulo.
  const [indice, setIndice] = useState(-1); 
  // 'indice': o índice (posição) do artigo selecionado no array. Inicia como -1 (nenhum selecionado).
  const [tituloPesquisa, setTituloPesquisa] = useState(""); 
  // 'tituloPesquisa': o texto digitado no campo de busca.

  // --- Buscando Dados ---
  const retrieveArtigos = useCallback(() => {
    // `useCallback` memoriza esta função para que ela não seja recriada a cada renderização, otimizando a performance.
    ArtigoDataService.getAll() // Chama o método para buscar todos os artigos.
      .then(response => { // Se a busca for bem-sucedida...
        setArtigos(response.data); // Atualiza o estado 'artigos' com os dados recebidos.
        console.log("Artigos carregados:", response.data);
      })
      .catch(e => { // Se ocorrer um erro...
        console.error("Erro ao buscar artigos:", e);
      });
  }, []); // O array de dependências vazio `[]` significa que a função só será criada uma vez.

  useEffect(() => {
    // O hook `useEffect` executa uma função após a renderização do componente.
    retrieveArtigos(); // Chama a função para buscar os artigos.
  }, [retrieveArtigos]); // O array de dependências `[retrieveArtigos]` garante que o efeito seja executado quando o componente for montado.

  // --- Manipuladores de Eventos (Event Handlers) ---
  const onChangeTituloPesquisa = e => {
    // Atualiza o estado 'tituloPesquisa' conforme o usuário digita no campo de busca.
    setTituloPesquisa(e.target.value);
  };

  const refreshList = () => {
    // Função para recarregar a lista de artigos e limpar a seleção atual.
    retrieveArtigos();
    setArtigoSel(null);
    setIndice(-1);
  };

  const setArtigoAtivo = (artigo, index) => {
    // Função chamada ao clicar em um item da lista.
    setArtigoSel(artigo); // Define o artigo selecionado.
    setIndice(index); // Define o índice do artigo selecionado.
  };

  const removeAll = () => {
    // Função para excluir todos os artigos.
    ArtigoDataService.deleteAll()
      .then(response => {
        console.log("Todos artigos excluídos:", response.data);
        refreshList(); // Atualiza a lista (que agora estará vazia).
      })
      .catch(e => {
        console.error("Erro ao excluir todos artigos:", e);
      });
  };

  const searchTitulo = () => {
    // Função para buscar artigos pelo título.
    setArtigoSel(null); // Limpa a seleção atual ao iniciar uma nova busca.
    setIndice(-1);
    ArtigoDataService.findByTitulo(tituloPesquisa) // Chama o método de busca do serviço.
      .then(response => {
        setArtigos(response.data); // Atualiza a lista com os resultados da busca.
        console.log("Resultado da busca:", response.data);
      })
      .catch(e => {
        console.error("Erro ao buscar por título:", e);
      });
  };

  // --- Lógica de Renderização ---
  return (
    <div className="row"> {/* Layout de linha do Bootstrap. */}
      <div className="col-md-8"> {/* Coluna para a barra de busca. */}
        <div className="input-group mb-3">
          <input
            type="text"
            className="form-control"
            placeholder="Buscar por título"
            value={tituloPesquisa} // O valor é controlado pelo estado.
            onChange={onChangeTituloPesquisa} // Atualiza o estado ao digitar.
            onKeyDown={(e) => e.key === 'Enter' && searchTitulo()} // Permite pesquisar pressionando Enter.
          />
          <div className="input-group-append">
            <button
              className="btn btn-outline-secondary"
              type="button"
              onClick={searchTitulo} // Botão que aciona a busca.
            >
              Buscar
            </button>
          </div>
        </div>
      </div>
      {/* Lista de artigos */}
      <div className="col-md-6"> {/* Coluna para a lista. */}
        <h4>Artigos</h4>

        <ul className="list-group">
          {artigos && artigos.length > 0 ? (
            // Se o array 'artigos' não for nulo e tiver itens...
            artigos.map((artigo, index) => (
              // O método '.map()' itera sobre o array 'artigos' e cria um `<li>` para cada um.
              <li
                className={
                  "list-group-item " +
                  (index === indice ? "active" : "")
                  // A classe 'active' é adicionada se o índice deste item for o mesmo do item selecionado.
                }
                onClick={() => setArtigoAtivo(artigo, index)} // Define este artigo como ativo ao ser clicado.
                key={artigo.id || index} // Chave única para cada item da lista, importante para o React.
                style={{ cursor: "pointer" }} // Muda o cursor para indicar que o item é clicável.
              >
                {artigo.titulo} {/* Exibe o título do artigo. */}
              </li>
            ))
           ) : (
            // Se o array 'artigos' estiver vazio...
            <li className="list-group-item">Nenhum artigo encontrado.</li>
           )}
        </ul>

        <button
          className="m-3 btn btn-sm btn-danger" 
          onClick={removeAll} // Botão para excluir todos os artigos.
          disabled={artigos.length === 0} // O botão é desabilitado se não houver artigos.
        >
          Excluir todos
        </button>
      </div>
      {/* Detalhes do Artigo Selecionado */}
      <div className="col-md-6"> {/* Coluna para os detalhes. */}
        {artigoSel ? (
          // Se um artigo estiver selecionado ('artigoSel' não é nulo)...
          <div>
            <h4>Detalhes</h4>
            <div>
              <label><strong>Título:</strong></label>{" "}{artigoSel.titulo}
            </div>
            <div>
              <label><strong>Resumo:</strong></label>{" "}{artigoSel.resumo}
            </div>
            <div>
              <label><strong>Status:</strong></label>{" "}
              {artigoSel.publicado ? "Publicado" : "Não publicado"}
              {/* Operador ternário para exibir o status. */}
            </div>

            <Link
              to={"/artigos/" + artigoSel.id} // Link para a página de edição, passando o ID do artigo na URL.
              className="btn btn-sm btn-warning mt-2"
              role="button"
            >
              Editar
            </Link>
          </div>
        ) : (
          // Se nenhum artigo estiver selecionado...
          <div>
            <h4>&nbsp;</h4>
            <p><i>Selecione um artigo para ver os detalhes.</i></p>
          </div>
        )}
      </div>
    </div>
  );
}

export default ListArtigo;