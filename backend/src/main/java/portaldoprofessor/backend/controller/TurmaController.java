package portaldoprofessor.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import portaldoprofessor.backend.entity.Aluno;
import portaldoprofessor.backend.entity.Avaliacao;
import portaldoprofessor.backend.entity.Turma;
import portaldoprofessor.backend.service.TurmaService;

import java.util.List;

@RestController
@RequestMapping("/turmas")
@RequiredArgsConstructor
public class TurmaController {

    private final TurmaService turmaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<Turma> criarTurma(@RequestBody Turma turma) {
        return ResponseEntity.ok(turmaService.criarTurma(turma));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<Turma> editarTurma(@PathVariable Long id, @RequestBody Turma turma) {
        return ResponseEntity.ok(turmaService.editarTurma(id, turma));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ResponseEntity<Void> deletarTurma(@PathVariable Long id) {
        turmaService.deletarTurma(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Turma>> listarTodas() {
        return ResponseEntity.ok(turmaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Turma> pegarTurma(@PathVariable Long id) {
        return ResponseEntity.ok(turmaService.pegarTurma(id));
    }

    @GetMapping("/{id}/alunos")
    public ResponseEntity<List<Aluno>> listarAlunosDaTurma(@PathVariable Long id) {
        return ResponseEntity.ok(turmaService.listarAlunosDaTurma(id));
    }

    @GetMapping("/{id}/avaliacoes")
    public ResponseEntity<List<Avaliacao>> listarAvaliacoesDaTurma(@PathVariable Long id) {
        return ResponseEntity.ok(turmaService.listarAvaliacoesDaTurma(id));
    }
}
