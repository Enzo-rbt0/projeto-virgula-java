package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movimentacao {
    private int id;
    private int produtoId;
    private String produtoNome;
    private String tipo;
    private double quantidade;
    private LocalDateTime data;
    private int usuarioId;
    
    // Construtor sem id (para inserção)
    public Movimentacao(int produtoId, String tipo, double quantidade, int usuarioId) {
        this.produtoId = produtoId;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.usuarioId = usuarioId;
    }
    
    // Construtor completo (para consultas)
    public Movimentacao(int id, int produtoId, String produtoNome, String tipo, 
                       double quantidade, int usuarioId, LocalDateTime data) {
        this.id = id;
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.usuarioId = usuarioId;
        this.data = data;
    }
    
    // Getters e Setters
    public String getProdutoNome() {
        return produtoNome;
    }
    
    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }
    
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getProdutoId() {
        return produtoId;
    }
    
    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public double getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }
    
    public LocalDateTime getData() {
        return data;
    }
    
    public void setData(LocalDateTime data) {
        this.data = data;
    }
    
    public int getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    // Método para formatar a data
    public String getDataFormatada() {
        if (this.data == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return this.data.format(formatter);
    }
}