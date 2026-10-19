package dw.editora.control;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import dw.editora.entity.Artigo;
import dw.editora.entity.Autor;
import dw.editora.repository.ArtigoRepository;
import dw.editora.repository.AutorRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class AutorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private ArtigoRepository artigoRepository;

    @BeforeEach
    void setUp() {
        artigoRepository.deleteAll();
        autorRepository.deleteAll();
    }

    @Test
    @DisplayName("1. Deve cadastrar autor com sucesso, retornando 201 Created, corpo e header Location")
    void deveCadastrarAutorComSucesso() throws Exception {
        Autor autor = new Autor();
        autor.setNome("Machado de Assis");
        autor.setEmail("machado@editora.com");
        autor.setBiografia("Escritor brasileiro clássico.");

        mockMvc.perform(post("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(autor)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Machado de Assis"))
                .andExpect(jsonPath("$.email").value("machado@editora.com"))
                .andExpect(jsonPath("$.biografia").value("Escritor brasileiro clássico."));
    }

    @Test
    @DisplayName("2. Deve rejeitar autor com nome em branco retornando 400 Bad Request")
    void deveRejeitarAutorComNomeEmBranco() throws Exception {
        Autor autor = new Autor();
        autor.setNome("");
        autor.setEmail("semnome@editora.com");
        autor.setBiografia("Bio.");

        mockMvc.perform(post("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(autor)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.nome").exists());
    }

    @Test
    @DisplayName("3. Deve rejeitar autor com e-mail inválido retornando 400 Bad Request")
    void deveRejeitarAutorComEmailInvalido() throws Exception {
        Autor autor = new Autor();
        autor.setNome("Clarice Lispector");
        autor.setEmail("email-invalido");
        autor.setBiografia("Escritora.");

        mockMvc.perform(post("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(autor)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    @DisplayName("4. Deve rejeitar cadastro com e-mail duplicado retornando 409 Conflict")
    void deveRejeitarEmailDuplicado() throws Exception {
        Autor autorExistente = new Autor();
        autorExistente.setNome("Autor Original");
        autorExistente.setEmail("duplicado@editora.com");
        autorExistente.setBiografia("Original");
        autorRepository.save(autorExistente);

        Autor novoAutor = new Autor();
        novoAutor.setNome("Outro Autor");
        novoAutor.setEmail("duplicado@editora.com");
        novoAutor.setBiografia("Mesmo email");

        mockMvc.perform(post("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAutor)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message", containsString("Já existe um autor cadastrado com o e-mail")));
    }

    @Test
    @DisplayName("5. Deve listar todos os autores retornando 200 OK")
    void deveListarTodosAutores() throws Exception {
        Autor a1 = new Autor(null, "Autor Um", "autor1@editora.com", "Bio 1", null);
        Autor a2 = new Autor(null, "Autor Dois", "autor2@editora.com", "Bio 2", null);
        autorRepository.save(a1);
        autorRepository.save(a2);

        mockMvc.perform(get("/api/autores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome").exists());
    }

    @Test
    @DisplayName("6. Deve buscar autor por ID existente retornando 200 OK")
    void deveBuscarAutorPorIdExistente() throws Exception {
        Autor autor = autorRepository.save(new Autor(null, "Graciliano Ramos", "graciliano@editora.com", "Bio", null));

        mockMvc.perform(get("/api/autores/{id}", autor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(autor.getId()))
                .andExpect(jsonPath("$.nome").value("Graciliano Ramos"))
                .andExpect(jsonPath("$.email").value("graciliano@editora.com"));
    }

    @Test
    @DisplayName("7. Deve retornar 404 Not Found ao buscar autor por ID inexistente")
    void deveRetornar404AoBuscarAutorInexistente() throws Exception {
        mockMvc.perform(get("/api/autores/{id}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("8. Deve excluir autor sem artigos retornando 204 No Content")
    void deveExcluirAutorSemArtigos() throws Exception {
        Autor autor = autorRepository.save(new Autor(null, "Para Deletar", "deletar@editora.com", "Bio", null));

        mockMvc.perform(delete("/api/autores/{id}", autor.getId()))
                .andExpect(status().isNoContent());

        assertFalse(autorRepository.existsById(autor.getId()));
    }

    @Test
    @DisplayName("9. Deve retornar 404 Not Found ao excluir autor inexistente")
    void deveRetornar404AoExcluirAutorInexistente() throws Exception {
        mockMvc.perform(delete("/api/autores/{id}", 888888L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("10. Deve rejeitar exclusão de autor que possui artigos vinculados (409 Conflict)")
    void deveRejeitarExclusaoDeAutorComArtigos() throws Exception {
        Autor autor = autorRepository.save(new Autor(null, "Autor Vinculado", "vinculado@editora.com", "Bio", null));

        Artigo artigo = new Artigo();
        artigo.setTitulo("Artigo do Autor");
        artigo.setResumo("Resumo do artigo");
        artigo.setPublicado(true);
        artigo.setAutor(autor);
        artigoRepository.save(artigo);

        mockMvc.perform(delete("/api/autores/{id}", autor.getId()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("11. Deve listar artigos por autor com artigos retornando 200 OK e lista correta")
    void deveListarArtigosPorAutor() throws Exception {
        Autor autor = autorRepository.save(new Autor(null, "Jorge Amado", "jorge@editora.com", "Bio", null));

        Artigo art1 = new Artigo(null, "Capitães da Areia", "Resumo 1", true, autor);
        Artigo art2 = new Artigo(null, "Gabriela", "Resumo 2", true, autor);
        artigoRepository.save(art1);
        artigoRepository.save(art2);

        mockMvc.perform(get("/api/autores/{id}/artigos", autor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].titulo").exists())
                .andExpect(jsonPath("$[0].autor.id").value(autor.getId()));
    }

    @Test
    @DisplayName("12. Deve retornar lista vazia (200 OK) ao consultar artigos de autor que não tem artigos")
    void deveListarArtigosDeAutorSemArtigos() throws Exception {
        Autor autor = autorRepository.save(new Autor(null, "Autor Novo", "novo@editora.com", "Sem artigos", null));

        mockMvc.perform(get("/api/autores/{id}/artigos", autor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("13. Deve retornar 404 Not Found ao consultar artigos de autor inexistente")
    void deveRetornar404AoConsultarArtigosDeAutorInexistente() throws Exception {
        mockMvc.perform(get("/api/autores/{id}/artigos", 777777L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
