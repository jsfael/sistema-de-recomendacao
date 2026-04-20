package model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Usuario {

    private final String nome;
    private final Set<Item> itensCurtidos;

    public Usuario(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do usuário inválido");
        }
        this.nome = nome.trim();
        this.itensCurtidos = new HashSet<>();
    }

    public String getNome() {
        return nome;
    }

    public Set<Item> getItensCurtidos() {
        return Collections.unmodifiableSet(itensCurtidos);
    }

    public void curtirItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }
        itensCurtidos.add(item);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(nome, usuario.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nome);
    }
}