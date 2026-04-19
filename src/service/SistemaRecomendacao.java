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
        usuarios.putIfAbsent(nome, new Usuario(nome));
    }

    public void curtirItem(String nome, Item item) {
        Usuario usuario = usuarios.get(nome);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado: " + nome);
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
                        ranking.getOrDefault(item, 0.0) + similaridade
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
