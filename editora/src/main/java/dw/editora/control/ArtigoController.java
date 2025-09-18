// Define o pacote (namespace) onde esta classe está localizada.
// Ajuda a organizar o código e evitar conflitos de nomes.
package dw.editora.control;

// Importa a classe ArrayList, que é uma implementação de lista redimensionável.
import java.util.ArrayList;
// Importa a interface List, que representa uma coleção ordenada de elementos.
import java.util.List;
// Importa a classe Optional, um contêiner que pode ou não conter um valor não nulo.
// É usada para evitar NullPointerExceptions.
import java.util.Optional;

// Importa a anotação @Autowired do Spring, usada para injeção de dependência automática.
import org.springframework.beans.factory.annotation.Autowired;
// Importa a classe HttpStatus, que contém a lista de códigos de status HTTP (ex: 200 OK, 404 NOT FOUND).
import org.springframework.http.HttpStatus;
// Importa a classe ResponseEntity, que representa toda a resposta HTTP: status, cabeçalhos e corpo.
import org.springframework.http.ResponseEntity;
// Importa a anotação @CrossOrigin, para permitir requisições de origens diferentes (ex: um frontend em outra porta).
import org.springframework.web.bind.annotation.CrossOrigin;
// Importa a anotação @DeleteMapping, para mapear requisições HTTP DELETE para um método específico.
import org.springframework.web.bind.annotation.DeleteMapping;
// Importa a anotação @GetMapping, para mapear requisições HTTP GET.
import org.springframework.web.bind.annotation.GetMapping;
// Importa a anotação @PathVariable, para extrair valores da URL (ex: o 'id' em "/{id}").
import org.springframework.web.bind.annotation.PathVariable;
// Importa a anotação @PostMapping, para mapear requisições HTTP POST.
import org.springframework.web.bind.annotation.PostMapping;
// Importa a anotação @PutMapping, para mapear requisições HTTP PUT.
import org.springframework.web.bind.annotation.PutMapping;
// Importa a anotação @RequestBody, para vincular o corpo de uma requisição HTTP a um objeto.
import org.springframework.web.bind.annotation.RequestBody;
// Importa a anotação @RequestParam, para extrair parâmetros de uma URL (ex: "?titulo=...").
import org.springframework.web.bind.annotation.RequestParam;
// Importa a anotação @RestController, que marca a classe como um controlador REST, onde os métodos retornam dados (JSON/XML) e não views.
import org.springframework.web.bind.annotation.RestController;

// Importa a classe de modelo Artigo, que representa a nossa entidade.
import dw.editora.model.Artigo;
// Importa a interface do repositório ArtigoRepository, que lida com o acesso aos dados.
import dw.editora.repository.ArtigoRepository;

// @RestController combina @Controller e @ResponseBody, simplificando a criação de serviços RESTful.
@RestController
// Declaração da classe do controlador.
public class ArtigoController {

    // @Autowired instrui o Spring a injetar uma instância de ArtigoRepository aqui.
    // Isso nos permite usar os métodos do repositório sem precisar instanciá-lo manualmente.
    @Autowired
    ArtigoRepository rep;

    /*
     * GET / : listar todos os artigos
     */

    // Mapeia requisições HTTP GET para a URL raiz ("/") deste controlador.
    @GetMapping("/")
    // Declara o método que retorna uma ResponseEntity contendo uma Lista de Artigos.
    // @RequestParam extrai o parâmetro "titulo" da URL. 'required = false' significa que ele é opcional.
    public ResponseEntity<List<Artigo>> getAllArtigos(@RequestParam(required = false) String titulo) {
        // Inicia um bloco try-catch para tratamento de exceções.
        try {
            // Cria uma nova lista vazia para armazenar os artigos.
            List<Artigo> la = new ArrayList<Artigo>();

            // Verifica se o parâmetro "titulo" foi fornecido na requisição.
            if (titulo == null)
                // Se não foi, busca todos os artigos no banco e adiciona cada um à lista 'la'.
                // 'forEach(la::add)' é uma forma concisa de 'forEach(artigo -> la.add(artigo))'.
                rep.findAll().forEach(la::add);
            else
                // Se foi, busca artigos cujo título contenha a string fornecida e os adiciona à lista.
                rep.findByTituloContaining(titulo).forEach(la::add);

            // Verifica se a lista de resultados está vazia.
            if (la.isEmpty())
                // Se estiver vazia, retorna uma resposta com status 204 NO CONTENT.
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            
            // Se a lista não estiver vazia, retorna a lista de artigos e o status 200 OK.
            return new ResponseEntity<>(la, HttpStatus.OK);
        // Captura qualquer exceção que possa ocorrer durante a execução do bloco 'try'.
        } catch (Exception e) {
            // Em caso de erro, retorna uma resposta com status 500 INTERNAL SERVER ERROR.
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * POST / : criar um artigo
     */
    // Mapeia requisições HTTP POST para a URL raiz ("/").
    @PostMapping("/")
    // Declara o método que recebe um objeto Artigo do corpo da requisição (@RequestBody).
    public ResponseEntity<Artigo> createArtigo(@RequestBody Artigo ar) {
        try {
            // Cria uma nova instância de Artigo usando os dados recebidos e a salva no banco de dados.
            // O método 'save' retorna a entidade salva (agora com um ID gerado).
            Artigo a = rep.save(new Artigo(ar.getTitulo(), ar.getResumo(), ar.isPublicado()));
            // Retorna o artigo recém-criado e o status 201 CREATED.
            return new ResponseEntity<>(a, HttpStatus.CREATED);
        } catch (Exception e) {
            // Em caso de erro, retorna status 500.
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * GET /:id : listar artigo dado um id
     */
    // Mapeia requisições GET para URLs com um ID, como "/123".
    @GetMapping("/{id}")
    // Declara o método que recebe o 'id' da URL como um parâmetro (@PathVariable).
    public ResponseEntity<Artigo> getArtigoById(@PathVariable("id") long id) {
        // Busca um artigo pelo ID. O resultado é um 'Optional', que pode conter o artigo ou estar vazio.
        Optional<Artigo> data = rep.findById(id);

        // Verifica se o Optional contém um valor (se o artigo foi encontrado).
        if (data.isPresent())
            // Se encontrou, retorna o artigo (usando .get()) e o status 200 OK.
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        else
            // Se não encontrou, retorna um status 404 NOT FOUND.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * PUT /:id : atualizar artigo dado um id
     */
    // Mapeia requisições HTTP PUT para URLs com um ID.
    @PutMapping("/{id}")
    // Declara o método que recebe o 'id' da URL e os novos dados do artigo do corpo da requisição.
    public ResponseEntity<Artigo> updateArtigo(@PathVariable("id") long id, @RequestBody Artigo a) {
        // Tenta encontrar o artigo existente no banco de dados pelo ID.
        Optional<Artigo> data = rep.findById(id);

        // Verifica se o artigo foi encontrado.
        if (data.isPresent()) {
            // Se foi, obtém a instância do artigo do banco.
            Artigo ar = data.get();
            // Atualiza os campos do artigo com os novos dados recebidos.
            ar.setPublicado(a.isPublicado());
            ar.setResumo(a.getResumo());
            ar.setTitulo(a.getTitulo());

            // Salva o artigo atualizado no banco e o retorna com status 200 OK.
            return new ResponseEntity<>(rep.save(ar), HttpStatus.OK);
        } else {
            // Se o artigo não foi encontrado, retorna status 404 NOT FOUND.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*
     * DEL /:id : remover artigo dado um id
     */
    // Mapeia requisições HTTP DELETE para URLs com um ID.
    @DeleteMapping("/{id}")
    // Declara o método que recebe o 'id' do artigo a ser deletado.
    public ResponseEntity<HttpStatus> deleteArtigo(@PathVariable("id") long id) {
        try {
            // Tenta deletar o artigo do banco de dados usando o ID fornecido.
            rep.deleteById(id);
            // Se a deleção for bem-sucedida, retorna status 204 NO CONTENT (indicando sucesso sem corpo de resposta).
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            // Se ocorrer um erro (ex: artigo não existe), retorna status 500.
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * DEL / : remover todos os artigos
     */
    // Mapeia requisições HTTP DELETE para a URL raiz ("/").
    @DeleteMapping("/")
    // Declara o método para deletar todos os artigos.
    public ResponseEntity<HttpStatus> deleteAllArtigo() {
        try {
            // Chama o método do repositório para deletar todas as entradas da tabela.
            rep.deleteAll();
            // Retorna status 204 NO CONTENT.
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            // Em caso de erro, retorna status 500.
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * GET /publicado : buscar por artigos publicados
     */
    // Mapeia requisições GET para a URL "/publicado".
    @GetMapping("/publicado")
    // Declara o método para buscar todos os artigos que estão publicados.
    public ResponseEntity<List<Artigo>> getAllPublicado() {
        try {
            // Cria uma nova lista para armazenar os resultados.
            List<Artigo> la = new ArrayList<Artigo>();

            // Chama o método customizado do repositório para buscar artigos onde 'publicado' é true.
            rep.findByPublicado(true).forEach(la::add);

            // Se a lista de resultados estiver vazia.
            if (la.isEmpty())
                // Retorna status 204 NO CONTENT.
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            
            // Retorna a lista de artigos publicados e status 200 OK.
            return new ResponseEntity<>(la, HttpStatus.OK);
        } catch (Exception e) {
            // Em caso de erro, retorna status 500.
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}