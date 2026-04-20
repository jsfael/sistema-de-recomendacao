package service;

import model.Item;
import model.Usuario;
import strategy.filtro.FiltroStrategy;
import strategy.similaridade.SimilaridadeStrategy;

import java.util.*;

public class SistemaRecomendacao {

    private final SimilaridadeStrategy similaridadeStrategy;
    private final FiltroStrategy filtroStrategy;
    private final Map<String, Usuario> usuarios;
    private final ItemCatalog catalog;

    public SistemaRecomendacao(SimilaridadeStrategy similaridadeStrategy, FiltroStrategy filtroStrategy, ItemCatalog catalog){
        Objects.requireNonNull(similaridadeStrategy, "SimilaridadeStrategy não pode ser nula");
        Objects.requireNonNull(filtroStrategy, "FiltroStrategy não pode ser nula");
        Objects.requireNonNull(catalog, "ItemCatalog não pode ser nulo");

        this.similaridadeStrategy = similaridadeStrategy;
        this.filtroStrategy = filtroStrategy;
        this.catalog = catalog;
        usuarios = new HashMap<>();
    }

    public void adicionarUsuario(String nome){
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do usuário inválido");
        }

        if (usuarios.containsKey(nome)) {
            throw new IllegalArgumentException("Usuário já existe: " + nome);
        }

        usuarios.put(nome, new Usuario(nome));    }

    public void adicionarItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }
        catalog.adicionarItem(item);
    }

    public void curtirItem(String nome, String itemId) {
        Usuario usuario = usuarios.get(nome);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado: " + nome);
        }

        Item item = catalog.buscarPorId(itemId);

        if (item == null) {
            throw new IllegalArgumentException("Item não encontrado: " + itemId);
        }

        usuario.curtirItem(item);
    }

    public List<Item> recomendar(String nome) {
        Usuario base = usuarios.get(nome);

        if (base == null) {
            throw new IllegalArgumentException("Usuário não encontrado: " + nome);
        }

        Map<Item, Double> ranking = new HashMap<>();

        for (Usuario outro : usuarios.values()) {

            if (outro == base) {
                continue;
            }

            double similaridade = similaridadeStrategy.calcular(base, outro);

            if(similaridade == 0){
                continue;
            }

            for (Item item : outro.getItensCurtidos()) {

                if (base.getItensCurtidos().contains(item)) {
                    continue;
                }

                ranking.put(
                        item,
                        ranking.getOrDefault(item, 0.0) + (similaridade * item.getPeso())
                );
            }
        }

        List<Item> recomendados = ranking.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .toList();

        return filtroStrategy.filtrar(base, recomendados);

    }

}
