package portaldoprofessor.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portaldoprofessor.backend.dto.AlunoDTO;
import portaldoprofessor.backend.dto.CreatedAlunoDTO;
import portaldoprofessor.backend.service.AlunoService;

import java.util.List;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping("/create")
    public ResponseEntity<AlunoDTO> criarAluno(@RequestBody CreatedAlunoDTO aluno) {
        return ResponseEntity.ok(alunoService.criarAluno(aluno));
    }

    @PutMapping("/edit")
    public ResponseEntity<AlunoDTO> editarAluno(
            @RequestParam Long id,
            @RequestBody CreatedAlunoDTO aluno
    ) {
        return ResponseEntity.ok(alunoService.editarAluno(id, aluno));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletarAluno(@RequestParam Long id) {
        alunoService.deletarAluno(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<AlunoDTO>> listarTodos() {
        return ResponseEntity.ok(alunoService.listarTodos());
    }

    @GetMapping("/get")
    public ResponseEntity<AlunoDTO> findAluno(@RequestParam Long id) {
        return ResponseEntity.ok(alunoService.findAluno(id));
    }

    @PostMapping("/vincular-turma")
    public ResponseEntity<AlunoDTO> vincularAlunoATurma(
            @RequestParam Long alunoId,
            @RequestParam Long turmaId
    ) {
        return ResponseEntity.ok(alunoService.vincularAlunoATurma(alunoId, turmaId));
    }

    @PostMapping("/deactivate")
    public ResponseEntity<AlunoDTO> desativarAluno(@RequestParam Long id) {
        return ResponseEntity.ok(alunoService.desativarAluno(id));
    }
}
