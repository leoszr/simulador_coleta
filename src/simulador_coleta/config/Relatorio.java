package simulador_coleta.config;

import simulador_coleta.model.TipoResiduo;
import java.util.Formatter;


public class Relatorio {
    public static void gerarRelatorio(int[] totalPorTipo, int totalReciclaveis, int totalNaoReciclaveis, long tempoColeta) {
        System.out.println("=== RELATÓRIO FINAL DA SIMULAÇÃO ===");
        System.out.println();
        System.out.println("Quantidade total de resíduos gerados por tipo:");
        for (TipoResiduo tipo : TipoResiduo.values()) {
            System.out.printf("%s: %d\n", tipo, totalPorTipo[tipo.ordinal()]);
        }
        System.out.println();
        System.out.println("Quantidade de recicláveis: " + totalReciclaveis);
        System.out.println("Quantidade de não recicláveis: " + totalNaoReciclaveis);
        System.out.println("Tempo total de coleta: " + tempoColeta + " ms");

        int total = totalReciclaveis + totalNaoReciclaveis;
        double percentualReciclaveis = total == 0 ? 0.0 : (double) totalReciclaveis / total * 100.0;
        System.out.printf("Percentual de recicláveis: %.2f%%\n", percentualReciclaveis);

        if (percentualReciclaveis >= 60.0) {
            System.out.println();
            System.out.println("Análise ambiental: Simulação sustentável.");
        } else {
            System.out.println();
            System.out.println("Análise ambiental: Simulação não sustentável.");
        }
        System.out.println("=====================================");
    }
}
