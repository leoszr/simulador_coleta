# Simulador de Coleta de Resíduos

Simulação multithread em Java de um sistema de coleta e separação de resíduos. Múltiplos geradores produzem resíduos de diferentes tipos de forma concorrente, enquanto um coletor os separa em recicláveis e não recicláveis.

## Tecnologias

- Java
- Programação concorrente (Threads, sincronização)
- IntelliJ IDEA (projeto .iml incluído)

## Pré-requisitos

- Java 11+
- JDK configurado no PATH

## Como compilar e executar

### Via IntelliJ IDEA

1. Abra a pasta do projeto no IntelliJ IDEA
2. O projeto já está configurado com `.iml` e `.idea/`
3. Execute a classe `Main` diretamente pela IDE

### Via linha de comando

```bash
# Compilar
javac -d out src/simulador_coleta/Main.java src/simulador_coleta/**/*.java

# Executar
java -cp out simulador_coleta.Main
```

## Como funciona

A simulação cria e coordena três tipos de threads:

1. **GeradorResiduo** — N threads que geram resíduos aleatórios de diferentes tipos e os adicionam a uma lista compartilhada (`ListaColeta`)
2. **Coletor** — 1 thread que consome a lista compartilhada e separa os resíduos em `ListasSeparadas` (recicláveis e não recicláveis)
3. **Status** — 1 thread de monitoramento que exibe o progresso a cada 30 segundos

Ao final da simulação, um relatório é gerado com:

- Total de resíduos gerados por tipo
- Total de recicláveis e não recicláveis coletados
- Tempo total de coleta

## Parâmetros de configuração

Os parâmetros da simulação podem ser ajustados em `ParametrosSimulacao`:

- `NUM_GERADORES` — número de threads geradoras de resíduos

O tempo de simulação é controlado pela classe `TempoSimulacao`.

## Estrutura do projeto

```
src/simulador_coleta/
├── Main.java                   # Ponto de entrada e coordenação
├── model/
│   └── TipoResiduo.java        # Enum de tipos de resíduo
├── coleta/
│   ├── ListaColeta.java        # Fila compartilhada (thread-safe)
│   └── ListasSeparadas.java    # Listas de resultado separadas
├── threads/
│   ├── GeradorResiduo.java     # Thread geradora de resíduos
│   └── Coletor.java            # Thread coletora e separadora
└── config/
    ├── ParametrosSimulacao.java # Constantes de configuração
    ├── TempoSimulacao.java      # Controle de tempo
    └── Relatorio.java          # Geração do relatório final
```

## Observações

- A sincronização entre threads é feita via `synchronized` e `wait()/notifyAll()` no objeto `ListaColeta`.
- Projeto desenvolvido para fins acadêmicos, explorando concorrência em Java.
