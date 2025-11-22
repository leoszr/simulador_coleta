package simulador_coleta.coleta;

import java.util.LinkedList;
import java.util.Queue;
import simulador_coleta.model.Residuo;

public class ListaColeta {
    private final Queue<Residuo> fila = new LinkedList<>();

    public synchronized void adicionarResiduo(Residuo r) {
        fila.add(r);
        notifyAll();
    }

    // Bloqueante: espera até haver um item
    public synchronized Residuo removerResiduo() throws InterruptedException {
        while (fila.isEmpty()) {
            wait();
        }
        return fila.poll();
    }

    // Não bloqueante: retorna null se vazio
    public synchronized Residuo pollResiduo() {
        return fila.poll();
    }

    public synchronized boolean estaVazia() {
        return fila.isEmpty();
    }

    public synchronized int tamanho() {
        return fila.size();
    }
}
