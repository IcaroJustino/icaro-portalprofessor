package portaldoprofessor.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import portaldoprofessor.backend.entity.Avaliacao;
import portaldoprofessor.backend.service.AvaliacaoService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @PostMapping("/turma/{turmaId}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<Avaliacao> criarAvaliacao(@RequestBody Avaliacao avaliacao, @PathVariable Long turmaId) {
        return ResponseEntity.ok(avaliacaoService.criarAvaliacao(avaliacao, turmaId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<Avaliacao> editarAvaliacao(@PathVariable Long id, @RequestBody Avaliacao avaliacao) {
        return ResponseEntity.ok(avaliacaoService.editarAvaliacao(id, avaliacao));
    }

    @PostMapping("/{id}/estender-prazo")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<Avaliacao> estenderPrazo(@PathVariable Long id, @RequestParam LocalDateTime novoPrazo) {
        return ResponseEntity.ok(avaliacaoService.estenderPrazo(id, novoPrazo));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<Void> deletarAvaliacao(@PathVariable Long id) {
        avaliacaoService.deletarAvaliacao(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> pegarAvaliacao(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.pegarAvaliacao(id));
    }

    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<Avaliacao>> listarAvaliacoesDaTurma(@PathVariable Long turmaId) {
        return ResponseEntity.ok(avaliacaoService.listarAvaliacoesDaTurma(turmaId));
    }
}
