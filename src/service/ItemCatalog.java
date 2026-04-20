package service;

import model.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemCatalog {

    private final Map<String, Item> itens = new HashMap<>();

    public void adicionarItem(Item item) {
        Objects.requireNonNull(item, "Item não pode ser nulo");
        itens.putIfAbsent(item.getId(), item);
    }

    public Item buscarPorId(String id) {
        return itens.get(id);
    }

}