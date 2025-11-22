package simulador_coleta.config;

import simulador_coleta.config.ParametrosSimulacao;


public class TempoSimulacao {
    private long inicio;
    private final long DURACAO = ParametrosSimulacao.DURACAO_MS;

    public void iniciar() {
        inicio = System.currentTimeMillis();
    }

    public boolean tempoEsgotado() {
        return System.currentTimeMillis() - inicio >= DURACAO;
    }

    public long getTempoRestante() {
        return Math.max(0, DURACAO - (System.currentTimeMillis() - inicio));
    }
}