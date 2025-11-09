package portaldoprofessor.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portaldoprofessor.backend.dto.*;
import portaldoprofessor.backend.entity.Avaliacao;
import portaldoprofessor.backend.service.TurmaService;

import java.util.List;

@RestController
@RequestMapping("/turmas")
@RequiredArgsConstructor
public class TurmaController {

    private final TurmaService turmaService;

    @PostMapping
    public ResponseEntity<TurmaDTO> criarTurma(@RequestBody CreatedTurmaDTO dto) {
        return ResponseEntity.ok(turmaService.criarTurma(dto));
    }

    @GetMapping("/getminhasturmas")
    public ResponseEntity<List<TurmaDTO>> getMinhasTurmas( @RequestParam Long idProfessor) {
        return ResponseEntity.ok(turmaService.listarTurmasDoProfessor(idProfessor));
    }

    @PutMapping
    public ResponseEntity<TurmaDTO> editarTurma(
            @RequestParam Long id,
            @RequestBody UpdateTurmaDTO dto
    ) {
        return ResponseEntity.ok(turmaService.editarTurma(id, dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarTurma(@RequestParam Long id) {
        turmaService.deletarTurma(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/listar")
    public ResponseEntity<List<TurmaDTO>> listarTodas() {
        return ResponseEntity.ok(turmaService.listarTodas());
    }

    @GetMapping("/detalhar")
    public ResponseEntity<TurmaDTO> detalharTurma(@RequestParam Long id) {
        return ResponseEntity.ok(turmaService.detalharTurma(id));
    }

    @GetMapping("/alunos")
    public ResponseEntity<List<AlunoDTO>> listarAlunosDaTurma(@RequestParam Long id) {
        return ResponseEntity.ok(turmaService.listarAlunosDaTurma(id));
    }

    @PostMapping("/vincularalunos")
    public ResponseEntity<TurmaDTO> vincularAlunos(@RequestBody VincularAlunosDTO dto) {
        return ResponseEntity.ok(turmaService.vincularAlunos(dto));
    }

    @GetMapping("/avaliacoes")
    public ResponseEntity<List<Avaliacao>> listarAvaliacoesDaTurma(@RequestParam Long id) {
        return ResponseEntity.ok(turmaService.listarAvaliacoesDaTurma(id));
    }
}
