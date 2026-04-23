package models;

import java.time.LocalDateTime;

public class ClienteModel {
    private int idCliente;
    private String nome;
    private String cpf;
    private String telefone;
    private LocalDateTime createdAt;

    public ClienteModel(int idCliente, String nome, String cpf, String telefone) {
        this.idCliente = idCliente;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public ClienteModel(int idCliente, String nome, String cpf, String telefone, LocalDateTime createdAt) {
        this.idCliente = idCliente;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.createdAt = createdAt;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    @Override
    public String toString() {
        return nome + " - " + cpf;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ClienteModel)) return false;
        ClienteModel other = (ClienteModel) obj;
        return this.idCliente == other.idCliente;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idCliente);
    }
}
