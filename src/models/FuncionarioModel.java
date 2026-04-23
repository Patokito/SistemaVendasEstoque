package models;

public class FuncionarioModel {
    private int idFuncionario;
    private String nome;
    private String usuario;
    private String senha;
    private int idNivel;
    private String NomeNivel;

    public FuncionarioModel(int idFuncionario, String nome, String usuario, String senha, int idNivel) {
        this.idFuncionario = idFuncionario;
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
        this.idNivel = idNivel;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int id) {
        this.idFuncionario = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public String getNomeNivel() {
        return NomeNivel;
    }

    public void setNomeNivel(String nomeNivel) {
        NomeNivel = nomeNivel;
    }
}
