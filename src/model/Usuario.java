package model;

import java.util.HashSet;
import java.util.Set;

public class Usuario {

    private final String nome;
    private final Set<String> itensCurtidos;

    public Usuario(String nome) {
        this.nome = nome;
        this.itensCurtidos = new HashSet<>();
    }

    public String getNome() {
        return nome;
    }

    public Set<String> getItensCurtidos() {
        return itensCurtidos;
    }

    public void curtirItem(String item) {
        itensCurtidos.add(item);
    }
}