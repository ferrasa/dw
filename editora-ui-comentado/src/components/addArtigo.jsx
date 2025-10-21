// Este componente contém o formulário para criar um novo artigo.

import { useState } from "react"; // Importa o hook useState do React para gerenciar o estado do componente.
import ArtigoDataService from "../services/artigoDataService"; // Importa o serviço de dados para interagir com a API.
import { Link } from "react-router-dom"; // Importa o componente Link para navegação.

function AddArtigo() { 
  // --- Estado usando useState ---
  const initialArtigoState = {
    // Define um objeto com o estado inicial (vazio) de um artigo.
    id: null,
    titulo: "",
    resumo: "",
    publicado: false,
  };
  // Acompanhe o artigo que está sendo editado/criado
  const [artigo, setArtigo] = useState(initialArtigoState);
  // Cria uma variável de estado 'artigo' e uma função 'setArtigo' para atualizá-la. Começa com o estado inicial.
  
  // Acompanhe o status do envio
  const [enviado, setEnviado] = useState(false);
  // Cria uma variável de estado 'enviado' (booleana) para controlar se o formulário foi submetido com sucesso.

  // --- Manipuladores de Eventos (Event Handlers) ---
  const handleInputChange = event => {
    // Função para lidar com mudanças nos campos de input (título e resumo).
    const { name, value } = event.target;
    // Pega o 'name' (ex: "titulo") e o 'value' (o texto digitado) do campo que disparou o evento.
    setArtigo({ ...artigo, [name]: value });
    // Atualiza o estado 'artigo'. A sintaxe `...artigo` copia o estado atual e `[name]: value` atualiza a propriedade correspondente.
  };

  const saveArtigo = () => {
    // Função para salvar o novo artigo.
    var data = {
      // Cria um objeto apenas com os dados necessários para a API.
      titulo: artigo.titulo,
      resumo: artigo.resumo
    };

    ArtigoDataService.create(data)
      // Chama o método 'create' do serviço de dados, passando os dados do novo artigo.
      .then(response => {
        // O '.then' é executado se a requisição for bem-sucedida.
        // Atualiza o estado com os dados do artigo recém-criado (incluindo o ID retornado pela API).
        setArtigo({
          id: response.data.id,
          titulo: response.data.titulo,
          resumo: response.data.resumo,
          publicado: response.data.publicado
        });
        setEnviado(true); // Define o estado 'enviado' como true para mostrar a mensagem de sucesso.
        console.log("Artigo criado:", response.data); // Exibe os dados retornados no console.
      })
      .catch(e => {
        // O '.catch' é executado se ocorrer um erro na requisição.
        console.error("Erro ao criar artigo:", e); // Exibe o erro no console.
      });
  };

  const newArtigo = () => {
    // Função para limpar o formulário e permitir adicionar um novo artigo.
    setArtigo(initialArtigoState); // Redefine o estado do artigo para o seu valor inicial (campos vazios).
    setEnviado(false); // Redefine o estado 'enviado' para false para mostrar o formulário novamente.
  };

  // --- Lógica de Renderização (Render Logic) ---
  return (
    <div style={{ maxWidth: "400px", margin: "auto" }}>
      {/* Container principal com estilo para centralizar e limitar a largura. */}
      {enviado ? (
        // Renderização condicional: Se 'enviado' for true, mostra a mensagem de sucesso.
        <div>
          <h5>O artigo foi enviado com sucesso!</h5>
          <button className="btn btn-success" onClick={newArtigo}>
            {/* Botão para chamar a função newArtigo e adicionar outro artigo. */}
            Adicionar outro artigo
          </button>          
          <div>
            <br />
            <Link to="/list" style={{ color: 'blue', textDecoration: 'none' }}>Voltar</Link>
            {/* Link para voltar à página de listagem. */}
          </div>
        </div>
      ) : (
        // Se 'enviado' for false, mostra o formulário de adição.
        <div>
          <div className="form-group mb-3"> 
            {/* Agrupamento de campo de formulário do Bootstrap. */}
            <label htmlFor="titulo"><strong>Título</strong></label>
            <input
              type="text"
              className="form-control" // Estilo do Bootstrap.
              id="titulo"
              required // Campo obrigatório.
              value={artigo.titulo} // O valor do input é controlado pelo estado 'artigo.titulo'.
              onChange={handleInputChange} // Quando o valor muda, a função handleInputChange é chamada.
              name="titulo" // O atributo 'name' é essencial para o handleInputChange unificado.
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
              value={artigo.resumo} // O valor da textarea é controlado pelo estado 'artigo.resumo'.
              onChange={handleInputChange} // Reutiliza a mesma função de manipulação de mudança.
              name="resumo" // O 'name' corresponde à chave no objeto de estado.
            />
          </div>

          <button onClick={saveArtigo} className="btn btn-success">
            {/* Botão que, ao ser clicado, chama a função saveArtigo para enviar os dados. */}
            Enviar
          </button>
        </div>
      )}
    </div>
  );
}

export default AddArtigo; // Exporta o componente.