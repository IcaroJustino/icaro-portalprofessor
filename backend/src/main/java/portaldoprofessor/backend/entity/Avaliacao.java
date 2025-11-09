package portaldoprofessor.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacao", indexes = {
        @Index(columnList = "codigo", name = "idx_avaliacao_codigo")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 100)
    private String nome;

    // Peso em porcentagem (ex: 30 significa 30%)
    @Column(nullable = false)
    private Double peso;

    @Column(nullable = false)
    private LocalDateTime prazo;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    // Cada avaliação pertence a uma turma
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;
}
