package dw.editora.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "artigo")
@Getter // Lombok: Gera todos os getters
@Setter // Lombok: Gera todos os setters
@NoArgsConstructor // Lombok: Construtor vazio exigido pelo JPA
@AllArgsConstructor // Lombok: Construtor com todos os argumentos
@ToString // Lombok: Gera o método toString()
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Boa prática: equals/hashCode apenas no ID
public class Artigo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Usa apenas o ID para comparar se dois objetos são o mesmo registro
    private Long id; // Boa prática: usar Long (objeto) ao invés de long (primitivo)

    
    
    @Column(nullable = false, length = 80)
    private String titulo;
    
    @Column(nullable = false)
    private String resumo;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE") 
    private boolean publicado = true; // Já inicializamos como true no objeto

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;
}

@Entity
@Table(name = "autor")
@Getter // Lombok: Gera todos os getters
@Setter // Lombok: Gera todos os setters
@NoArgsConstructor // Lombok: Construtor vazio exigido pelo JPA
@AllArgsConstructor // Lombok: Construtor com todos os argumentos
@ToString // Lombok: Gera o método toString()
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Boa prática: equals/hashCode apenas no ID
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Usa apenas o ID para comparar se dois objetos são o mesmo registro
    private Long id; // Boa prática: usar Long (objeto) ao invés de long (primitivo)

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(optional = true, columnDefinition = "TEXT")
    private String bibliografia;

    @ToString.Exclude //Evita loop infinito no ToString
    @JsonIgnore //Evita recursão infinita no json
    @OneToMany(mappedBy = "autor") //indica que um autor pode ter vários artigos, usando o atributo autor da classe Artigo para mapear a relação.
    private List<Artigo> artigos = new ArrayList<>();
}