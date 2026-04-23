package models;

public class ProcessarClienteItemModel {

    private int id;
    private int processarClienteId;
    private int produtoId;
    private String nomeProduto;
    private int quantidade;
    private double precoUnitario;
    private double precoTotal;

    public ProcessarClienteItemModel(int id, int processarClienteId, int produtoId,
                                     String nomeProduto, int quantidade,
                                     double precoUnitario, double precoTotal) {
        this.id = id;
        this.processarClienteId = processarClienteId;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.precoTotal = precoTotal;
    }

    public ProcessarClienteItemModel(int processarClienteId, int produtoId,
                                     String nomeProduto, int quantidade,
                                     double precoUnitario) {
        this.processarClienteId = processarClienteId;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.precoTotal = quantidade * precoUnitario;
    }

    public int getId() {
        return id;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getProcessarClienteId() {
        return processarClienteId;
    }

    public void setProcessarClienteId(int processarClienteId) {
        this.processarClienteId = processarClienteId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
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

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        this.precoTotal = quantidade * this.precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {

    }

    public void setPrecoTotal(double v) {
    }
}