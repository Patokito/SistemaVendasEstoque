package models;

import java.time.LocalDateTime;

public class EstoqueModel {
    private int id;
    private int idProduto;
    private String produtoNome;
    private int idFuncionario;
    private String funcionarioNome;
    private int quantidade;
    private String tipo;
    private LocalDateTime dataHora;

    public EstoqueModel(int id, int idProduto, String produtoNome, int idFuncionario, String funcionarioNome, int quantidade, String tipo, LocalDateTime dataHora) {
        this.id = id;
        this.idProduto = idProduto;
        this.produtoNome = produtoNome;
        this.idFuncionario = idFuncionario;
        this.funcionarioNome = funcionarioNome;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.dataHora = dataHora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getFuncionarioNome() {
        return funcionarioNome;
    }

    public void setFuncionarioNome(String funcionarioNome) {
        this.funcionarioNome = funcionarioNome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
