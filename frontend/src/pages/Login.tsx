import React, { useState, useEffect } from "react";
import {
  Box,
  Button,
  CircularProgress,
  TextField,
  Typography,
  Paper,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { useAuth } from "../context/AuthContext";
import api from "../api/api";

export default function Login() {
  const navigate = useNavigate();
  const { login, isAuthenticated } = useAuth();

  const [form, setForm] = useState({ email: "", password: "" });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isAuthenticated) {
      navigate("/dashboard");
    }
  }, [isAuthenticated, navigate]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await api.post("/auth/login", {
        email: form.email,
        password: form.password,
      });

      const jwtToken = response.data;
      if (!jwtToken) {
        toast.error("Não foi possível obter o token do servidor.");
        setLoading(false);
        return;
      }

      login(jwtToken);
      navigate("/dashboard");
    } catch (err: any) {
      console.error(err);
      toast.error(
        err.response?.data?.message ||
          "Falha ao realizar login. Verifique suas credenciais."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box
      sx={{
        height: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        backgroundColor: "#f5f5f5",
      }}
    >
      <Paper
        elevation={3}
        sx={{
          p: 4,
          width: 400,
          textAlign: "center",
          borderRadius: 3,
        }}
      >
        <Typography variant="h5" mb={3} fontWeight={700}>
          Portal do Professor
        </Typography>

        <form onSubmit={handleSubmit}>
          <TextField
            label="E-mail"
            type="email"
            fullWidth
            margin="normal"
            value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
            required
          />

          <TextField
            label="Senha"
            type="password"
            fullWidth
            margin="normal"
            value={form.password}
            onChange={(e) => setForm({ ...form, password: e.target.value })}
            required
          />

          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="error"
            sx={{ mt: 2 }}
            disabled={loading}
          >
            {loading ? (
              <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                <CircularProgress size={20} color="inherit" />
                Entrando...
              </Box>
            ) : (
              "Entrar"
            )}
          </Button>
        </form>

        <Typography variant="body2" mt={2}>
          Não tem uma conta?{" "}
          <Button color="error" onClick={() => navigate("/register")}>
            Cadastre-se
          </Button>
        </Typography>
      </Paper>
    </Box>
  );
}
