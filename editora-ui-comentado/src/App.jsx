//Este é o componente principal que organiza a estrutura de navegação da sua aplicação usando o react-router-dom.

import "bootstrap/dist/css/bootstrap.min.css";
// Importa o arquivo CSS do Bootstrap para estilizar a aplicação com componentes prontos.
import { Routes, BrowserRouter, Route, Link } from "react-router-dom";
// Importa os componentes necessários do 'react-router-dom' para gerenciar a navegação (rotas) na aplicação.

import ListArtigo from "./components/listArtigo";
// Importa o componente que lista os artigos.
import AddArtigo from "./components/addArtigo";
// Importa o componente que adiciona novos artigos.
import UpdArtigo from "./components/updArtigo"; 
// Importa o componente que atualiza um artigo existente.

function App() { // Converte para um componente funcional.
  return (
    // Retorna o JSX que será renderizado na tela.
    <div>
      {/* Envolve toda a aplicação com BrowserRouter para habilitar o roteamento. */}
      <BrowserRouter>
        {/* Barra Superior de Navegação */}
        <nav className="navbar navbar-expand-lg navbar-dark bg-secondary">
          {/* Componente de barra de navegação do Bootstrap. */}
          <div className="container">
            {/* Um contêiner para alinhar o conteúdo da barra de navegação. */}
            <Link to={"/list"} className="navbar-brand">
              {/* O componente Link cria um link de navegação. `to` define a URL de destino. */}
              <b><i>Editora</i></b>
              {/* O nome da aplicação, que também é um link para a lista de artigos. */}
            </Link>
            <div className="navbar-nav mr-auto">
              {/* Um contêiner para os itens de navegação. */}
              <li className="nav-item">
                {/* Um item da lista de navegação. */}
                <Link to={"/list"} className="nav-link">
                  {/* Link para a página de listagem de artigos. */}
                  Listar
                </Link>
              </li>
              <li className="nav-item">
                {/* Outro item da lista de navegação. */}
                <Link to={"/add"} className="nav-link">
                  {/* Link para a página de adicionar artigos. */}
                  Adicionar
                </Link>
              </li>
            </div>
          </div>
        </nav>
        <div className="container mt-3">
          {/* Contêiner principal onde o conteúdo da página será renderizado. `mt-3` adiciona uma margem no topo. */}
          <Routes>
            {/* O componente Routes envolve todas as definições de rotas. Ele garante que apenas uma rota seja renderizada por vez. */}
            <Route path="/" element={<ListArtigo />} />
            {/* Define a rota para a raiz do site ("/"). Quando o usuário acessar, o componente ListArtigo será renderizado. */}
            <Route path="/list" element={<ListArtigo />} />
            {/* Define a rota para "/list". Também renderiza o componente ListArtigo. */}
            <Route path="/add" element={<AddArtigo />} />
            {/* Define a rota para "/add". Renderiza o componente AddArtigo. */}
            <Route path="/artigos/:id" element={<UpdArtigo />} />
            {/* Define uma rota dinâmica. ":id" é um parâmetro que pode mudar (ex: /artigos/1, /artigos/2). Renderiza o componente UpdArtigo. */}
          </Routes>
        </div>
      </BrowserRouter>
    </div>
  );
}

export default App; // Exporta o componente App para que ele possa ser usado em outros arquivos (como o main.jsx).