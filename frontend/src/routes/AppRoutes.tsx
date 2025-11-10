import { Routes, Route, Navigate } from "react-router-dom";
import Login from "../pages/Login";
import Register from "../pages/Cadastro";
import Dashboard from "../pages/Dashboard";
import Alunos from "../pages/Alunos";
import Disciplinas from "../pages/Disciplinas";
import Avaliacoes from "../pages/Avaliacoes";
import NotFound from "../pages/NotFound";
import MainLayout from "../components/applayout/MainLayout";
import ProtectedRoute from "./ProtectedRoute";
import { useAuth } from "../context/AuthContext";

export default function AppRoutes() {
  const { isAuthenticated } = useAuth();

  return (
    <Routes>
      <Route
        path="/login"
        element={isAuthenticated ? <Navigate to="/dashboard" /> : <Login />}
      />
      <Route
        path="/register"
        element={isAuthenticated ? <Navigate to="/dashboard" /> : <Register />}
      />

      <Route
        path="/"
        element={
          <ProtectedRoute>
            <MainLayout />
          </ProtectedRoute>
        }
      >
        <Route index element={<Navigate to="/dashboard" />} />
        <Route path="dashboard" element={<Dashboard />} />
        <Route path="alunos" element={<Alunos />} />
        <Route path="disciplinas" element={<Disciplinas />} />
        <Route path="avaliacoes" element={<Avaliacoes />} />
      </Route>

      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}
