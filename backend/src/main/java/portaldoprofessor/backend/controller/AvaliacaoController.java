package portaldoprofessor.backend.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portaldoprofessor.backend.dto.UpdateAvaliacaoDTO;
import portaldoprofessor.backend.entity.Avaliacao;
import portaldoprofessor.backend.service.AvaliacaoService;
import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @PostMapping("/criar")
    public ResponseEntity<List<Avaliacao>> criarAvaliacoes(
            @RequestBody List<Avaliacao> avaliacoes,
            @RequestParam Long turmaId) {

        return ResponseEntity.ok(avaliacaoService.criarAvaliacoes(avaliacoes, turmaId));
    }

    @PutMapping("/editar")
    public ResponseEntity<Avaliacao> editarAvaliacao(
            @RequestParam Long id,
            @RequestBody UpdateAvaliacaoDTO dto
    ) {
        Avaliacao avaliacaoAtualizada = avaliacaoService.editarAvaliacao(id, dto);
        return ResponseEntity.ok(avaliacaoAtualizada);
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<Void> deletarAvaliacao(@RequestParam Long id) {
        avaliacaoService.deletarAvaliacao(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detalhes")
    public ResponseEntity<Avaliacao> pegarAvaliacao(@RequestParam Long id) {
        return ResponseEntity.ok(avaliacaoService.pegarAvaliacao(id));
    }

}
