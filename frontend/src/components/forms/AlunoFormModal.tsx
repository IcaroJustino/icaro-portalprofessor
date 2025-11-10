import { useState, useEffect } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  TextField,
  DialogActions,
  Button,
  FormControlLabel,
  Switch,
} from "@mui/material";
import type { Aluno, CreatedAlunoDTO } from "../../services/AlunoService";

interface Props {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: CreatedAlunoDTO | Partial<Aluno>) => void;
  aluno?: Aluno;
}

export default function AlunoFormModal({
  open,
  onClose,
  onSubmit,
  aluno,
}: Props) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [active, setActive] = useState(true);

  useEffect(() => {
    if (aluno) {
      setName(aluno.name);
      setEmail(aluno.email);
      setActive(aluno.active);
    } else {
      setName("");
      setEmail("");
      setActive(true);
    }
  }, [aluno, open]);

  const handleSubmit = () => {
    if (!name || !email) {
      window.toast?.error("Nome e e-mail são obrigatórios.");
      return;
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      window.toast?.error("E-mail inválido.");
      return;
    }

    if (aluno) {
      onSubmit({ name, email, active });
    } else {
      const matricula = Math.floor(10000 + Math.random() * 90000).toString();
      onSubmit({ name, email, matricula });
    }
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>{aluno ? "Editar Aluno" : "Criar Aluno"}</DialogTitle>
      <DialogContent>
        <TextField
          label="Nome"
          value={name}
          onChange={(e) => setName(e.target.value)}
          fullWidth
          margin="normal"
        />
        <TextField
          label="E-mail"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          fullWidth
          margin="normal"
        />
        {aluno && (
          <FormControlLabel
            control={
              <Switch
                checked={active}
                onChange={(e) => setActive(e.target.checked)}
              />
            }
            label="Ativo"
          />
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancelar</Button>
        <Button variant="contained" onClick={handleSubmit}>
          {aluno ? "Salvar" : "Criar"}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
