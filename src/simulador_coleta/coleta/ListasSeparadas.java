package simulador_coleta.coleta;

import java.util.ArrayList;
import java.util.List;
import simulador_coleta.model.Residuo;

public class ListasSeparadas {
    private final List<Residuo> reciclaveis = new ArrayList<>();
    private final List<Residuo> naoReciclaveis = new ArrayList<>();

    public synchronized void adicionarReciclavel(Residuo r) {
        reciclaveis.add(r);
    }

    public synchronized void adicionarNaoReciclavel(Residuo r) {
        naoReciclaveis.add(r);
    }

    public synchronized List<Residuo> getReciclaveis() {
        return new ArrayList<>(reciclaveis);
    }

    public synchronized List<Residuo> getNaoReciclaveis() {
        return new ArrayList<>(naoReciclaveis);
    }
}
