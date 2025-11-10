import { useState, useEffect } from "react";
import {
  Box,
  Typography,
  Button,
  TextField,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  IconButton,
  CircularProgress,
  Chip,
  Paper,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import alunoService, {
  type Aluno,
  type CreatedAlunoDTO,
} from "../services/AlunoService";
import AlunoFormModal from "../components/forms/AlunoFormModal";

export default function Alunos() {
  const [alunos, setAlunos] = useState<Aluno[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");

  const [modalOpen, setModalOpen] = useState(false);
  const [editingAluno, setEditingAluno] = useState<Aluno | null>(null);

  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [alunoToDelete, setAlunoToDelete] = useState<Aluno | null>(null);

  const loadAlunos = async () => {
    setLoading(true);
    try {
      const data = await alunoService.getAll();
      setAlunos(data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAlunos();
  }, []);

  const handleCreate = async (data: CreatedAlunoDTO) => {
    if (
      !data.name ||
      !data.email ||
      !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.email)
    ) {
      alunoService.showValidationError(
        "Nome e e-mail válido são obrigatórios."
      );
      return;
    }
    await alunoService.create(data);
    setModalOpen(false);
    loadAlunos();
  };

  const handleEdit = async (data: Partial<Aluno>) => {
    if (!editingAluno) return;
    if (
      !data.name ||
      !data.email ||
      !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.email)
    ) {
      alunoService.showValidationError(
        "Nome e e-mail válido são obrigatórios."
      );
      return;
    }
    await alunoService.update(editingAluno.id, {
      name: data.name!,
      email: data.email!,
    });
    setEditingAluno(null);
    setModalOpen(false);
    loadAlunos();
  };

  const handleDelete = async () => {
    if (!alunoToDelete) return;
    await alunoService.delete(alunoToDelete.id);
    setDeleteModalOpen(false);
    setAlunoToDelete(null);
    loadAlunos();
  };

  const filteredAlunos = alunos.filter(
    (a) =>
      a.name.toLowerCase().includes(search.toLowerCase()) ||
      a.matricula.includes(search)
  );

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" fontWeight="bold" mb={2}>
        Alunos
      </Typography>

      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 2 }}>
        <TextField
          placeholder="Buscar por nome ou matrícula"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          sx={{ width: "300px" }}
        />
        <Button
          variant="contained"
          onClick={() => {
            setEditingAluno(null);
            setModalOpen(true);
          }}
        >
          Criar Aluno
        </Button>
      </Box>

      <Paper sx={{ overflowX: "auto", border: "1px solid #ccc" }}>
        {loading ? (
          <Box sx={{ display: "flex", justifyContent: "center", p: 5 }}>
            <CircularProgress />
          </Box>
        ) : filteredAlunos.length === 0 ? (
          <Typography sx={{ p: 2 }}>Nenhum aluno encontrado.</Typography>
        ) : (
          <Table sx={{ borderCollapse: "collapse" }}>
            <TableHead>
              <TableRow sx={{ backgroundColor: "#f0f0f0" }}>
                <TableCell sx={{ border: "1px solid #ccc" }}>Nome</TableCell>
                <TableCell sx={{ border: "1px solid #ccc" }}>E-mail</TableCell>
                <TableCell sx={{ border: "1px solid #ccc" }}>
                  Matrícula
                </TableCell>
                <TableCell sx={{ border: "1px solid #ccc" }}>Status</TableCell>
                <TableCell sx={{ border: "1px solid #ccc" }}>Turmas</TableCell>
                <TableCell sx={{ border: "1px solid #ccc" }}>Ações</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredAlunos.map((aluno) => (
                <TableRow key={aluno.id}>
                  <TableCell sx={{ border: "1px solid #ccc" }}>
                    {aluno.name}
                  </TableCell>
                  <TableCell sx={{ border: "1px solid #ccc" }}>
                    {aluno.email}
                  </TableCell>
                  <TableCell sx={{ border: "1px solid #ccc" }}>
                    {aluno.matricula}
                  </TableCell>
                  <TableCell sx={{ border: "1px solid #ccc" }}>
                    <Chip
                      label={aluno.active ? "Ativo" : "Inativo"}
                      color={aluno.active ? "success" : "default"}
                      size="small"
                    />
                  </TableCell>
                  <TableCell sx={{ border: "1px solid #ccc" }}>
                    {aluno.turmaIds.length > 0
                      ? aluno.turmaIds.join(", ")
                      : "Nenhuma turma"}
                  </TableCell>
                  <TableCell sx={{ border: "1px solid #ccc" }}>
                    <IconButton
                      color="primary"
                      onClick={() => {
                        setEditingAluno(aluno);
                        setModalOpen(true);
                      }}
                    >
                      <EditIcon />
                    </IconButton>
                    <IconButton
                      color="error"
                      onClick={() => {
                        setAlunoToDelete(aluno);
                        setDeleteModalOpen(true);
                      }}
                    >
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        )}
      </Paper>

      {/* Modal criar/editar */}
      <AlunoFormModal
        open={modalOpen}
        onClose={() => {
          setModalOpen(false);
          setEditingAluno(null);
        }}
        onSubmit={editingAluno ? handleEdit : handleCreate}
        aluno={editingAluno || undefined}
      />

      {/* Modal deletar */}
      <Dialog open={deleteModalOpen} onClose={() => setDeleteModalOpen(false)}>
        <DialogTitle>Deletar Aluno</DialogTitle>
        <DialogContent>
          <Typography>
            Tem certeza que deseja deletar o aluno "{alunoToDelete?.name}"? Esta
            ação é irreversível.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteModalOpen(false)}>Cancelar</Button>
          <Button color="error" variant="contained" onClick={handleDelete}>
            Deletar
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
