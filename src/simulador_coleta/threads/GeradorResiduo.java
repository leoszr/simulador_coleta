package simulador_coleta.threads;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import simulador_coleta.coleta.ListaColeta;
import simulador_coleta.model.Residuo;
import simulador_coleta.model.TipoResiduo;
import simulador_coleta.config.TempoSimulacao;
import simulador_coleta.config.ParametrosSimulacao;


public class GeradorResiduo implements Runnable {
    private static final AtomicInteger idCounter = new AtomicInteger(0);
    private final ListaColeta listaColeta;
    private final TempoSimulacao tempo;
    private final int[] contadores = new int[TipoResiduo.values().length];
    private final Random rand = new Random();

    public GeradorResiduo(ListaColeta listaColeta, TempoSimulacao tempo) {
        this.listaColeta = listaColeta;
        // Loop principal: gera resíduos enquanto o tempo não esgotar
        this.tempo = tempo;
    }

    @Override
    public void run() {
        while (!tempo.tempoEsgotado()) {
            TipoResiduo tipo = TipoResiduo.values()[rand.nextInt(TipoResiduo.values().length)];
            Residuo r = new Residuo(idCounter.incrementAndGet(), tipo, System.currentTimeMillis());
            listaColeta.adicionarResiduo(r);
            contadores[tipo.ordinal()]++;
            try {
                int delay = ParametrosSimulacao.MIN_DELAY_GERADOR + rand.nextInt(ParametrosSimulacao.MAX_DELAY_GERADOR - ParametrosSimulacao.MIN_DELAY_GERADOR + 1);
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public int[] getContadores() {
        return contadores.clone();
    }
}
