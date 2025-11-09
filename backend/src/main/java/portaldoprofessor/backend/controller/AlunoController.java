package portaldoprofessor.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import portaldoprofessor.backend.dto.CreatedAlunoDTO;
import portaldoprofessor.backend.entity.Aluno;
import portaldoprofessor.backend.service.AlunoService;

import java.util.List;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping
    public ResponseEntity<Aluno> criarAluno(@RequestBody CreatedAlunoDTO aluno) {
        return ResponseEntity.ok(alunoService.criarAluno(aluno));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> editarAluno(@PathVariable Long id, @RequestBody Aluno aluno) {
        return ResponseEntity.ok(alunoService.editarAluno(id, aluno));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarAluno(@PathVariable Long id) {
        alunoService.deletarAluno(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Aluno>> listarTodos() {
        return ResponseEntity.ok(alunoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> pegarAluno(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.pegarAluno(id));
    }

    @PostMapping("/{alunoId}/vincular-turma/{turmaId}")
    public ResponseEntity<Aluno> vincularAlunoATurma(@PathVariable Long alunoId, @PathVariable Long turmaId) {
        return ResponseEntity.ok(alunoService.vincularAlunoATurma(alunoId, turmaId));
    }

    @PostMapping("/{id}/desativar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Aluno> desativarAluno(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.desativarAluno(id));
    }
}
