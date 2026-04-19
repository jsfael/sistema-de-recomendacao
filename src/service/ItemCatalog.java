package service;

import model.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemCatalog {

    private final Map<String, Item> itens = new HashMap<>();

    public void adicionarItem(Item item) {
        itens.putIfAbsent(item.getId(), item);
    }

    public Item buscarPorId(String id) {
        return itens.get(id);
    }

}