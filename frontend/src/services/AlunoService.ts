/* eslint-disable @typescript-eslint/no-explicit-any */
import api from "../api/api";
import { toast } from "react-hot-toast";

export interface Aluno {
  id: number;
  name: string;
  email: string;
  matricula: string;
  active: boolean;
  createdAt: string;
  turmaIds: number[];
}

export interface CreatedAlunoDTO {
  name: string;
  email: string;
  matricula: string;
}

const alunoService = {
  getAll: async (): Promise<Aluno[]> => {
    try {
      const res = await api.get<Aluno[]>("/alunos/list");
      return res.data;
    } catch (err: any) {
      toast.error("Erro ao carregar alunos.");
      throw err;
    }
  },

  get: async (id: number): Promise<Aluno> => {
    try {
      const res = await api.get<Aluno>(`/alunos/get?id=${id}`);
      return res.data;
    } catch (err: any) {
      toast.error("Erro ao buscar aluno.");
      throw err;
    }
  },

  create: async (data: CreatedAlunoDTO): Promise<Aluno> => {
    if (!data.name || !data.email) {
      toast.error("Nome e e-mail são obrigatórios.");
      throw new Error("Campos obrigatórios vazios");
    }

    try {
      const res = await api.post<Aluno>("/alunos/create", data);
      toast.success("Aluno criado com sucesso!");
      return res.data;
    } catch (err: any) {
      toast.error("Erro ao criar aluno.");
      throw err;
    }
  },

  update: async (id: number, data: Partial<Aluno>): Promise<Aluno> => {
    if (!data.name || !data.email) {
      toast.error("Nome e e-mail não podem estar vazios.");
      throw new Error("Campos obrigatórios vazios");
    }

    try {
      const res = await api.put<Aluno>(`/alunos/edit?id=${id}`, data);
      toast.success("Aluno atualizado com sucesso!");
      return res.data;
    } catch (err: any) {
      toast.error("Erro ao atualizar aluno.");
      throw err;
    }
  },

  delete: async (id: number): Promise<void> => {
    try {
      await api.delete(`/alunos/delete?id=${id}`);
      toast.success("Aluno removido com sucesso!");
    } catch (err: any) {
      toast.error("Erro ao remover aluno.");
      throw err;
    }
  },

  deactivate: async (id: number): Promise<Aluno> => {
    try {
      const res = await api.post<Aluno>(`/alunos/deactivate?id=${id}`);
      toast.success("Aluno desativado com sucesso!");
      return res.data;
    } catch (err: any) {
      toast.error("Erro ao desativar aluno.");
      throw err;
    }
  },
};

export default alunoService;
