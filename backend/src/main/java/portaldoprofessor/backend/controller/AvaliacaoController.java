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

    @PostMapping("/criar")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<List<Avaliacao>> criarAvaliacoes(
            @RequestBody List<Avaliacao> avaliacoes,
            @RequestParam Long turmaId) {

        return ResponseEntity.ok(avaliacaoService.criarAvaliacoes(avaliacoes, turmaId));
    }

    @PutMapping("/editar")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<Avaliacao> editarAvaliacao(
            @RequestParam Long id,
            @RequestBody Avaliacao avaliacao) {

        return ResponseEntity.ok(avaliacaoService.editarAvaliacao(id, avaliacao));
    }

    @PostMapping("/estender-prazo")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<Avaliacao> estenderPrazo(
            @RequestParam Long id,
            @RequestParam LocalDateTime novoPrazo) {

        return ResponseEntity.ok(avaliacaoService.estenderPrazo(id, novoPrazo));
    }

    @DeleteMapping("/deletar")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<Void> deletarAvaliacao(@RequestParam Long id) {
        avaliacaoService.deletarAvaliacao(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detalhes")
    public ResponseEntity<Avaliacao> pegarAvaliacao(@RequestParam Long id) {
        return ResponseEntity.ok(avaliacaoService.pegarAvaliacao(id));
    }

    @GetMapping("/turma")
    public ResponseEntity<List<Avaliacao>> listarAvaliacoesDaTurma(@RequestParam Long turmaId) {
        return ResponseEntity.ok(avaliacaoService.listarAvaliacoesDaTurma(turmaId));
    }
}
