package dw.editora.control;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import tools.jackson.databind.ObjectMapper;

import dw.editora.entity.Artigo;
import dw.editora.entity.Autor;
import dw.editora.repository.ArtigoRepository;
import dw.editora.repository.AutorRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ArtigoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private ArtigoRepository artigoRepository;

    private Autor autorSalvo;

    @BeforeEach
    void setUp() {
        artigoRepository.deleteAll();
        autorRepository.deleteAll();
        autorSalvo = autorRepository.save(new Autor(null, "Monteiro Lobato", "monteiro@editora.com", "Criador do Sítio", null));
    }

    @Test
    @DisplayName("1. Deve cadastrar artigo vinculado a autor existente, retornando 201 Created e Location")
    void deveCadastrarArtigoComAutorExistente() throws Exception {
        Map<String, Object> autorPayload = new HashMap<>();
        autorPayload.put("id", autorSalvo.getId());

        Map<String, Object> artigoPayload = new HashMap<>();
        artigoPayload.put("titulo", "O Picapau Amarelo");
        artigoPayload.put("resumo", "Aventuras no sítio.");
        artigoPayload.put("publicado", true);
        artigoPayload.put("autor", autorPayload);

        mockMvc.perform(post("/api/artigos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artigoPayload)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.titulo").value("O Picapau Amarelo"))
                .andExpect(jsonPath("$.resumo").value("Aventuras no sítio."))
                .andExpect(jsonPath("$.publicado").value(true))
                .andExpect(jsonPath("$.autor.id").value(autorSalvo.getId()))
                .andExpect(jsonPath("$.autor.nome").value("Monteiro Lobato"));
    }

    @Test
    @DisplayName("2. Deve rejeitar artigo sem autor retornando 400 Bad Request")
    void deveRejeitarArtigoSemAutor() throws Exception {
        Map<String, Object> artigoPayload = new HashMap<>();
        artigoPayload.put("titulo", "Artigo Sem Autor");
        artigoPayload.put("resumo", "Resumo sem autor");
        artigoPayload.put("publicado", true);

        mockMvc.perform(post("/api/artigos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artigoPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("3. Deve rejeitar artigo com autor sem ID retornando 400 Bad Request")
    void deveRejeitarArtigoComAutorSemId() throws Exception {
        Map<String, Object> autorPayload = new HashMap<>(); // sem ID
        Map<String, Object> artigoPayload = new HashMap<>();
        artigoPayload.put("titulo", "Artigo Autor Sem ID");
        artigoPayload.put("resumo", "Resumo do artigo");
        artigoPayload.put("publicado", true);
        artigoPayload.put("autor", autorPayload);

        mockMvc.perform(post("/api/artigos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artigoPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("4. Deve rejeitar artigo com autor inexistente retornando 404 Not Found")
    void deveRejeitarArtigoComAutorInexistente() throws Exception {
        Map<String, Object> autorPayload = new HashMap<>();
        autorPayload.put("id", 999999L);

        Map<String, Object> artigoPayload = new HashMap<>();
        artigoPayload.put("titulo", "Artigo Fantasma");
        artigoPayload.put("resumo", "Resumo fantasma");
        artigoPayload.put("publicado", true);
        artigoPayload.put("autor", autorPayload);

        mockMvc.perform(post("/api/artigos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artigoPayload)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message", containsString("Autor não encontrado")));
    }

    @Test
    @DisplayName("5. Deve rejeitar artigo com título em branco retornando 400 Bad Request")
    void deveRejeitarArtigoComTituloEmBranco() throws Exception {
        Map<String, Object> autorPayload = new HashMap<>();
        autorPayload.put("id", autorSalvo.getId());

        Map<String, Object> artigoPayload = new HashMap<>();
        artigoPayload.put("titulo", "");
        artigoPayload.put("resumo", "Resumo válido");
        artigoPayload.put("publicado", true);
        artigoPayload.put("autor", autorPayload);

        mockMvc.perform(post("/api/artigos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artigoPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.titulo").exists());
    }

    @Test
    @DisplayName("6. Deve listar todos os artigos retornando 200 OK")
    void deveListarTodosArtigos() throws Exception {
        artigoRepository.save(new Artigo(null, "Artigo 1", "Resumo 1", true, autorSalvo));
        artigoRepository.save(new Artigo(null, "Artigo 2", "Resumo 2", false, autorSalvo));

        mockMvc.perform(get("/api/artigos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("7. Deve buscar artigo por ID existente retornando 200 OK")
    void deveBuscarArtigoPorIdExistente() throws Exception {
        Artigo artigo = artigoRepository.save(new Artigo(null, "Artigo Específico", "Resumo", true, autorSalvo));

        mockMvc.perform(get("/api/artigos/{id}", artigo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(artigo.getId()))
                .andExpect(jsonPath("$.titulo").value("Artigo Específico"))
                .andExpect(jsonPath("$.autor.id").value(autorSalvo.getId()));
    }

    @Test
    @DisplayName("8. Deve retornar 404 Not Found ao buscar artigo inexistente")
    void deveRetornar404AoBuscarArtigoInexistente() throws Exception {
        mockMvc.perform(get("/api/artigos/{id}", 888888L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("9. Deve listar artigos publicados retornando 200 OK")
    void deveListarArtigosPublicados() throws Exception {
        artigoRepository.save(new Artigo(null, "Publicado", "Resumo", true, autorSalvo));
        artigoRepository.save(new Artigo(null, "Rascunho", "Resumo", false, autorSalvo));

        mockMvc.perform(get("/api/artigos/publicados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo").value("Publicado"));
    }

    @Test
    @DisplayName("10. Deve excluir artigo por ID retornando 204 No Content")
    void deveExcluirArtigoPorId() throws Exception {
        Artigo artigo = artigoRepository.save(new Artigo(null, "Para Excluir", "Resumo", true, autorSalvo));

        mockMvc.perform(delete("/api/artigos/{id}", artigo.getId()))
                .andExpect(status().isNoContent());

        assertFalse(artigoRepository.existsById(artigo.getId()));
    }

    @Test
    @DisplayName("11. Deve garantir serialização JSON sem recursão infinita e com dados do autor")
    void deveGarantirSerializacaoJsonSemRecursaoInfinita() throws Exception {
        Artigo artigo = artigoRepository.save(new Artigo(null, "Dom Casmurro Análise", "Estudo", true, autorSalvo));

        MvcResult result = mockMvc.perform(get("/api/artigos/{id}", artigo.getId()))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertNotNull(responseBody);
        // Garante que o autor está no JSON
        Artigo artigoDesserializado = objectMapper.readValue(responseBody, Artigo.class);
        assertNotNull(artigoDesserializado.getAutor());
        assertNotNull(artigoDesserializado.getAutor().getId());
    }

    @Test
    @DisplayName("12. Deve retornar 400 Bad Request ao enviar JSON malformado")
    void deveRetornar400AoEnviarJsonMalformado() throws Exception {
        mockMvc.perform(post("/api/artigos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"titulo\": \"Incompleto\""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
