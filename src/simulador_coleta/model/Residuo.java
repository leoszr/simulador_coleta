package simulador_coleta.model;


public class Residuo {
    private final int id;
    private final TipoResiduo tipo;
    private final long timestampGeracao;

    public Residuo(int id, TipoResiduo tipo, long timestampGeracao) {
        this.id = id;
        this.tipo = tipo;
        this.timestampGeracao = timestampGeracao;
    }

    public int getId() {
        return id;
    }

    public TipoResiduo getTipo() {
        return tipo;
    }

    public long getTimestampGeracao() {
        return timestampGeracao;
    }
}
