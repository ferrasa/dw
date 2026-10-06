//Este arquivo é o ponto de partida do seu código React. Ele renderiza o componente principal (App) na div com id="root" do index.html.

import { StrictMode } from 'react'
// Importa o componente StrictMode do React. Ele ajuda a identificar potenciais problemas na aplicação durante o desenvolvimento.
import { createRoot } from 'react-dom/client'
// Importa a função createRoot, que é a nova API do React 18 para renderizar a aplicação.
import App from './App'
// Importa o componente principal da sua aplicação, chamado "App", do arquivo App.jsx.

// Acessa a div com o id "root" do arquivo index.html e cria a "raiz" da aplicação React nela.
createRoot(document.getElementById('root')).render(
  // O método render é chamado para exibir o conteúdo dentro da "raiz".
  <StrictMode>
    {/* Envolve o componente App com StrictMode para ativar verificações e avisos extras em modo de desenvolvimento. */}
    <App />
    {/* Renderiza o componente principal da aplicação. */}
  </StrictMode>,
)