package strategy.filtro;

import model.Item;
import model.Usuario;

import java.util.List;

public interface FiltroStrategy {
    List<Item> filtrar(Usuario usuario, List<Item> candidatos);
}
