package util;

import models.FuncionarioModel;

public class Sessao {

    private static FuncionarioModel usuarioLogado;

    public static void setUsuario(FuncionarioModel usuario) {
        usuarioLogado = usuario;
    }

    public static FuncionarioModel getUsuario() {
        return usuarioLogado;
    }

    public static void logout() {
        usuarioLogado = null;
    }
}