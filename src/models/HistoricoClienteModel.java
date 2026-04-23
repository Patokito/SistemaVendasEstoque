package models;

import java.time.LocalDateTime;

public class HistoricoClienteModel {

    private int idProduto;
    private String produto;
    private int quantidade;
    private double precoUnitario;
    private double precoTotal;
    private LocalDateTime data;

    public HistoricoClienteModel(int idProduto, String produto, int quantidade,
                                 double precoUnitario, double precoTotal, LocalDateTime data) {

        this.idProduto = idProduto;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.precoTotal = precoTotal;
        this.data = data;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public String getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public LocalDateTime getData() {
        return data;
    }
}