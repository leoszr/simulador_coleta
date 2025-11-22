package simulador_coleta;

import simulador_coleta.coleta.ListaColeta;
import simulador_coleta.coleta.ListasSeparadas;
import simulador_coleta.config.ParametrosSimulacao;
import simulador_coleta.model.TipoResiduo;
import simulador_coleta.config.Relatorio;
import simulador_coleta.threads.Coletor;
import simulador_coleta.threads.GeradorResiduo;
import simulador_coleta.config.TempoSimulacao;

        // Inicializa as estruturas compartilhadas

public class Main {
    public static void main(String[] args) {
        ListaColeta listaColeta = new ListaColeta();
        ListasSeparadas listas = new ListasSeparadas();
        // Cria e inicia os threads geradores de resíduos
        TempoSimulacao tempo = new TempoSimulacao();
        tempo.iniciar();

        int numGeradores = ParametrosSimulacao.NUM_GERADORES;
        Thread[] geradores = new Thread[numGeradores];
        GeradorResiduo[] geradorObjs = new GeradorResiduo[numGeradores];

        // Cria e inicia o thread coletor
        for (int i = 0; i < numGeradores; i++) {
            geradorObjs[i] = new GeradorResiduo(listaColeta, tempo);
            geradores[i] = new Thread(geradorObjs[i], "Gerador-" + (i+1));
            geradores[i].start();
        // Aguarda todos os geradores terminarem
        }

        Coletor coletor = new Coletor(listaColeta, listas, tempo);
        Thread threadColetor = new Thread(coletor, "Coletor");
        threadColetor.start();

        // Thread para mostrar status a cada 30 segundos
        Thread status = new Thread(() -> {
            while (!tempo.tempoEsgotado()) {
                System.out.println("Simulação em andamento...");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Status");
        status.start();

        // aguardar geradores terminarem (eles checam o tempo)
        for (Thread t : geradores) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Após geradores terminarem, acordar possíveis wait() do coletor
        synchronized (listaColeta) {
            listaColeta.notifyAll();
        }

        // Calcula totais e gera relatório final
        // aguardar coletor terminar
        try {
            threadColetor.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // aguardar thread de status terminar
        try {
            status.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // construir relatórios
        int[] totalPorTipo = new int[TipoResiduo.values().length];
        for (GeradorResiduo g : geradorObjs) {
            int[] cont = g.getContadores();
            for (int i = 0; i < cont.length; i++) {
                totalPorTipo[i] += cont[i];
            }
        }

        int totalReciclaveis = listas.getReciclaveis().size();
        int totalNaoReciclaveis = listas.getNaoReciclaveis().size();
        long tempoColeta = coletor.getTempoColeta();

        Relatorio.gerarRelatorio(totalPorTipo, totalReciclaveis, totalNaoReciclaveis, tempoColeta);
    }
}
