package model;

import java.util.Objects;

public class Item {

    private final String id;
    private final String nome;
    private final TipoItem tipo;
    private final String categoria;
    private final double peso;

    public Item(String id, String nome, TipoItem tipo, String categoria, double peso) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.categoria = categoria;
        this.peso = peso;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public TipoItem getTipo() { return tipo; }
    public String getCategoria() { return categoria; }
    public double getPeso() { return peso; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}