import React from "react";
import { AppBar, Toolbar, Typography, Button, Box } from "@mui/material";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

export default function Header() {
  const navigate = useNavigate();
  const location = useLocation();
  const { isAuthenticated, logout } = useAuth();

  const menuItems = [
    { label: "Dashboard", path: "/dashboard" },
    { label: "Alunos", path: "/alunos" },
    { label: "Disciplinas", path: "/disciplinas" },
    { label: "Avaliações", path: "/avaliacoes" },
  ];

  return (
    <AppBar
      position="static"
      color="error"
      elevation={3}
      sx={{
        px: 2,
      }}
    >
      <Toolbar sx={{ justifyContent: "space-between" }}>
        {/* Logo / Nome do Portal */}
        <Typography
          variant="h6"
          fontWeight={700}
          sx={{
            cursor: "pointer",
            "&:hover": { opacity: 0.8 },
          }}
          onClick={() => navigate("/dashboard")}
        >
          Portal do Professor
        </Typography>

        {/* Menu de navegação */}
        {isAuthenticated && (
          <Box sx={{ display: "flex", gap: 2 }}>
            {menuItems.map((item) => (
              <Button
                key={item.path}
                color="inherit"
                onClick={() => navigate(item.path)}
                sx={{
                  fontWeight: location.pathname === item.path ? 700 : 400,
                  borderBottom:
                    location.pathname === item.path
                      ? "2px solid white"
                      : "2px solid transparent",
                  borderRadius: 0,
                }}
              >
                {item.label}
              </Button>
            ))}
          </Box>
        )}

        {/* Botão de Logout */}
        {isAuthenticated && (
          <Button
            color="inherit"
            variant="outlined"
            onClick={logout}
            sx={{
              borderColor: "white",
              ml: 2,
              "&:hover": { backgroundColor: "rgba(255,255,255,0.1)" },
            }}
          >
            Sair
          </Button>
        )}
      </Toolbar>
    </AppBar>
  );
}
