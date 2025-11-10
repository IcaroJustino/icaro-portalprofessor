/* eslint-disable @typescript-eslint/no-explicit-any */
import { useState, useEffect } from "react";
import {
  Box,
  Card,
  CardContent,
  Typography,
  CircularProgress,
  Grid,
  Button,
  Paper,
} from "@mui/material";
import { useNavigate } from "react-router-dom";

const metricColors = ["#f28b82", "#fbbc04", "#34a853"]; // cores dos cards de metricas

export default function Dashboard() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState<any>(null);

  useEffect(() => {
    setTimeout(() => {
      setData({
        metrics: [
          { label: "Turmas Ativas", value: 5 },
          { label: "Alunos", value: 132 },
          { label: "Pendências", value: 8 },
        ],
        atividades: [
          "Álgebra Linear",
          "Estatística",
          "Programação Java",
          "Física",
          "Química",
        ],
        noticias: [
          "Reunião sexta às 14h",
          "Envio de notas até dia 18/11",
          "Atualizar plano de aula",
        ],
      });
      setLoading(false);
    }, 1000);
  }, []);

  if (loading)
    return (
      <Box
        sx={{
          height: "100vh",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        <CircularProgress color="error" />
      </Box>
    );

  return (
    <Box sx={{ width: "100vw", minHeight: "100vh", bgcolor: "#f5f5f5" }}>
      <div className=" w-full flex justify-center items-center">
        <Grid
          container
          spacing={5}
          sx={{ mt: 2, mb: 2, justifyContent: "center" }}
        >
          {data.metrics.map((m: any, i: number) => (
            <Grid item xs={12} sm={4} key={i}>
              <Card
                sx={{
                  borderRadius: 4,
                  boxShadow: 3,
                  minWidth: 550,
                  bgcolor: metricColors[i],
                  color: "#fff",
                }}
              >
                <CardContent sx={{ textAlign: "center" }}>
                  <Typography variant="h6">{m.label}</Typography>
                  <Typography variant="h4" fontWeight="bold">
                    {m.value}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </div>

      {/* Próximas atividades */}
      <Typography
        variant="h6"
        color="text.primary"
        fontWeight="bold"
        mb={2}
        pt={4}
        ml={9}
      >
        Próximas Atividades
      </Typography>
      <Box
        sx={{
          flexGrow: 1,
          mb: 4,
          mt: 2,
          width: "100%",
          justifyContent: "center",
        }}
        container
        spacing={2}
        display="flex"
        gap={2}
      >
        {data.atividades.slice(0, 4).map((a: string, i: number) => {
          const deliveryDate = new Date();
          deliveryDate.setDate(deliveryDate.getDate() + (i + 1) * 2);
          const formattedDate = deliveryDate.toLocaleDateString("pt-BR", {
            day: "2-digit",
            month: "short",
            year: "numeric",
          });

          return (
            <Grid container spacing={2} xs={12} sm={6} md={3} key={i}>
              <Card
                sx={{
                  minWidth: 420,
                  minHeight: 150,
                  borderRadius: 2,
                  boxShadow: 2,
                  p: 2,
                  cursor: "pointer",
                  textAlign: "left",
                  bgcolor: "#ffffff",
                  transition: "all 0.3s ease",
                  "&:hover": {
                    transform: "translateY(-5px)",
                    boxShadow: 5,
                    bgcolor: "#ffebee",
                  },
                  display: "flex",
                  flexDirection: "column",
                  justifyContent: "space-between",
                  height: "100%",
                }}
              >
                <Box
                  sx={{
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "center",
                    padding: 4,
                  }}
                >
                  <Typography
                    variant="subtitle1"
                    fontWeight="bold"
                    gutterBottom
                  >
                    {a}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Entrega: {formattedDate}
                  </Typography>
                </Box>
              </Card>
            </Grid>
          );
        })}
      </Box>
      <Grid
        containe
        sx={{
          width: 350,
          height: 50,
          borderRadius: 8,
          mb: 4,
          ml: 9,
        }}
      >
        <Button
          variant="contained"
          color="error"
          fullWidth
          sx={{ height: "100%" }}
          onClick={() => navigate("/disciplinas")}
        >
          Ver mais
        </Button>
      </Grid>
      <Box sx={{ px: 9, mb: 6, margin: "auto" }}></Box>
      {(() => {
        const navItems = [
          {
            label: "Disciplinas",
            icon: "📚",
            route: "/disciplinas",
            color: "#ef9a9a",
          },
          { label: "Turmas", icon: "🏫", route: "/turmas", color: "#ffcc80" },
          { label: "Alunos", icon: "🧑‍🎓", route: "/alunos", color: "#a5d6a7" },
          {
            label: "Calendário",
            icon: "📅",
            route: "/calendario",
            color: "#90caf9",
          },
          { label: "Avisos", icon: "🔔", route: "/avisos", color: "#ce93d8" },
          {
            label: "Configurações",
            icon: "⚙️",
            route: "/configuracoes",
            color: "#b0bec5",
          },
        ];

        return (
          <Grid container spacing={4} sx={{ px: 9, mb: 6 }}>
            {navItems.map((item, idx) => (
              <Grid item xs={12} sm={6} md={4} key={idx}>
                <Card
                  role="button"
                  tabIndex={0}
                  onClick={() => navigate(item.route)}
                  onKeyDown={(e) => {
                    if (e.key === "Enter" || e.key === " ")
                      navigate(item.route);
                  }}
                  sx={{
                    height: 140,
                    display: "flex",
                    alignItems: "center",
                    gap: 2,
                    px: 3,
                    borderRadius: 2,
                    boxShadow: 3,
                    cursor: "pointer",
                    bgcolor: item.color,
                    color: "#212121",
                    transition: "transform 0.18s ease, box-shadow 0.18s ease",
                    "&:hover": {
                      transform: "translateY(-6px)",
                      boxShadow: 6,
                    },
                  }}
                >
                  <Box
                    sx={{
                      width: 64,
                      height: 64,
                      borderRadius: 2,
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                      fontSize: 32,
                      background: "rgba(255,255,255,0.6)",
                    }}
                  >
                    {item.icon}
                  </Box>

                  <CardContent sx={{ p: 0 }}>
                    <Typography variant="subtitle1" fontWeight="bold">
                      {item.label}
                    </Typography>
                    <Typography
                      variant="body2"
                      color="text.secondary"
                      sx={{ mt: 0.5 }}
                    >
                      Acesse {item.label.toLowerCase()}
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        );
      })()}
    </Box>
  );
}
