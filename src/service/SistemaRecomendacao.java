package service;

import model.Item;
import model.Usuario;
import strategy.SimilaridadeStrategy;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaRecomendacao {

    private final SimilaridadeStrategy strategy;
    private final Map<String, Usuario> usuarios;
    private final ItemCatalog catalog;

    public SistemaRecomendacao(SimilaridadeStrategy strategy, ItemCatalog catalog){
        this.strategy = strategy;
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

            double similaridade = strategy.calcular(base, outro);

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

        return ranking.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .toList();

    }

}
