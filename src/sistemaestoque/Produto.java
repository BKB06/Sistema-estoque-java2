/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaestoque;
import java.math.BigDecimal;
/**
 *
 * @author bruno
 */
public class Produto {
    // Atributos que representam as colunas da tabela "produtos"
    private int id;
    private String nome;
    private String descricao;
    private int quantidade;
    private BigDecimal preco; // Usamos BigDecimal para precisão monetária
    private String fornecedor;
    
    // --- Getters e Setters ---
    // Métodos para obter (get) e definir (set) os valores dos atributos.
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
    public String getFornecedor() {
    return fornecedor;
}

public void setFornecedor(String fornecedor) {
    this.fornecedor = fornecedor;
}
}
