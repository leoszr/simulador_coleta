package simulador_coleta.threads;

import java.util.Random;
import simulador_coleta.coleta.ListaColeta;
import simulador_coleta.coleta.ListasSeparadas;
import simulador_coleta.model.Residuo;
import simulador_coleta.model.TipoResiduo;
import simulador_coleta.config.TempoSimulacao;
import simulador_coleta.config.ParametrosSimulacao;


public class Coletor implements Runnable {
    private final ListaColeta listaColeta;
    private final ListasSeparadas listas;
    private final TempoSimulacao tempo;
    private final Random rand = new Random();
    private long tempoColeta;

    public Coletor(ListaColeta listaColeta, ListasSeparadas listas, TempoSimulacao tempo) {
        this.listaColeta = listaColeta;
        this.listas = listas;
        this.tempo = tempo;
    }
        // Loop de coleta: processa itens da lista até o tempo esgotar

    @Override
    public void run() {
        long inicioColeta = System.currentTimeMillis();

        try {
            while (true) {
                if (tempo.tempoEsgotado()) break;
                // controle seguro para evitar deadlock:
                synchronized (listaColeta) {
                    if (listaColeta.estaVazia()) {
                        long restante = tempo.getTempoRestante();
                        if (restante <= 0) {
                            break; // tempo esgotado e nada na fila -> sair
                        }
                        // esperar até ser notificado ou tempo restante acabar
                        listaColeta.wait(restante);
                        // ao acordar, recomeça o loop e verifica as condições
                        continue;
                    }
                }

                // se chegou aqui, há item(s) na fila
                Residuo r = listaColeta.pollResiduo(); // não bloqueante
                if (r == null) continue; // alguém pegou antes

                // processar
                if (r.getTipo() == TipoResiduo.NAO_RECICLAVEL) {
                    listas.adicionarNaoReciclavel(r);
                } else {
                    listas.adicionarReciclavel(r);
                }

                // delay de processamento
                try {
                    int delay = ParametrosSimulacao.MIN_DELAY_COLETOR + rand.nextInt(ParametrosSimulacao.MAX_DELAY_COLETOR - ParametrosSimulacao.MIN_DELAY_COLETOR + 1);
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            long fimColeta = System.currentTimeMillis();
            tempoColeta = fimColeta - inicioColeta;
        }
    }

    public long getTempoColeta() {
        return tempoColeta;
    }
}
