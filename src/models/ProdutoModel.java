package models;

public class ProdutoModel {
    private int idProduto;
    private String nome;
    private String descricao;
    private double preco;
    private int quantidade;
    private String codBarras;
    private String categoria;
    private int categoriaId;

    public ProdutoModel(int idProduto, String nome, String descricao, double preco, int quantidade, String categoria) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoria = categoria;
    }

    public ProdutoModel(int idProduto, String nome, String descricao, double preco, int quantidade, String codBarras, String categoria) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.codBarras = codBarras;
        this.categoria = categoria;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getCodBarras() {
        return codBarras;
    }

    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }
    @Override
    public String toString() {
        return nome + " | R$ " + preco + " | Estoque: " + quantidade;
    }

    public static String gerarEAN13() {
        StringBuilder codigo = new StringBuilder();
        java.util.Random radom = new java.util.Random();

        for (int i = 0; i < 12; i++) {
            codigo.append(radom.nextInt(10));
        }
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            int digito = Character.getNumericValue(codigo.charAt(i));
            soma += (i % 2 == 0) ? digito : digito * 3;
        }

        int verificador = (10 - (soma % 10)) % 10;
        codigo.append(verificador);

        return codigo.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ProdutoModel)) return false;
        ProdutoModel other = (ProdutoModel) obj;
        return this.idProduto == other.idProduto;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idProduto);
    }
}
