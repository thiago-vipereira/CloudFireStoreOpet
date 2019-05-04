package com.example.opet.cloudfirestoreopet;

/**
 * Created by opet on 01/04/2019.
 */

public class Produto {
    private String nome;
    private String categoria;
    private double valor;
    private boolean ativo;

    public Produto() {
    }

    public Produto(String nome, String categoria, double valor) {
        this.nome = nome;
        this.categoria = categoria;
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return  "Nome: " + nome + '\n' +
                "Categoria: " + categoria + '\n' +
                "Valor: " + valor + '\n';
    }
}
