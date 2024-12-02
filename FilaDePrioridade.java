public class FilaDePrioridade<X extends Comparable<X>> {
    private NoHuffman[] elementos;
    private int tamanhoAtual;
    private int tamanhoMaximo;

    // Construtor da Fila de Prioridade para NoHuffman
    public FilaDePrioridadeHuffman(int tamanhoMaximo) throws Exception {
        if (tamanhoMaximo <= 0)
            throw new Exception("Tamanho inválido");

        this.elementos = new NoHuffman[tamanhoMaximo];
        this.tamanhoAtual = 0;
        this.tamanhoMaximo = tamanhoMaximo;
    }

    // Adiciona um nó Huffman à fila de prioridade
    public void guardeUmItem(NoHuffman no) throws Exception {
        if (no == null)
            throw new Exception("Falta o que guardar");

        if (tamanhoAtual == tamanhoMaximo)
            throw new Exception("Fila cheia");

        // Adiciona o nó no final da fila
        elementos[tamanhoAtual] = no;
        tamanhoAtual++;

        // Organiza a fila para manter a prioridade (Heap Min)
        reorganizarParaCima(tamanhoAtual - 1);
    }

    // Remove o nó de maior prioridade (menor frequência)
    public NoHuffman remove() throws Exception {
        if (tamanhoAtual == 0) {
            throw new Exception("Fila vazia");
        }

        // O nó de maior prioridade é o primeiro
        NoHuffman itemRemovido = elementos[0];

        // Troca o primeiro elemento com o último
        tamanhoAtual--;
        elementos[0] = elementos[tamanhoAtual];

        // Reorganiza a fila para manter a prioridade
        reorganizarParaBaixo(0);

        return itemRemovido;
    }

    // Reorganiza a fila para cima para manter a propriedade da heap
    private void reorganizarParaCima(int index) {
        int pai = (index - 1) / 2;
        if (index > 0 && elementos[index].compareTo(elementos[pai]) < 0) {
            trocar(index, pai);
            reorganizarParaCima(pai);
        }
    }

    // Reorganiza a fila para baixo para manter a propriedade da heap
    private void reorganizarParaBaixo(int index) {
        int esquerda = 2 * index + 1;
        int direita = 2 * index + 2;
        int menor = index;

        // Verifica qual filho tem menor prioridade
        if (esquerda < tamanhoAtual && elementos[esquerda].compareTo(elementos[menor]) < 0) {
            menor = esquerda;
        }
        if (direita < tamanhoAtual && elementos[direita].compareTo(elementos[menor]) < 0) {
            menor = direita;
        }

        // Se o menor não for o elemento atual, troca e organiza novamente
        if (menor != index) {
            trocar(index, menor);
            reorganizarParaBaixo(menor);
        }
    }

    // Troca dois elementos no array
    private void trocar(int i, int j) {
        NoHuffman temp = elementos[i];
        elementos[i] = elementos[j];
        elementos[j] = temp;
    }

    // Verifica se a fila está vazia
    public boolean isVazia() {
        return tamanhoAtual == 0;
    }

    // Verifica se a fila está cheia
    public boolean isCheia() {
        return tamanhoAtual == tamanhoMaximo;
    }

    // Retorna o tamanho atual da fila
    public int getSize() {
        return tamanhoAtual;
    }

    // Método clone
    public Object clone() {
        try {
            FilaDePrioridadeHuffman ret = new FilaDePrioridadeHuffman(this.tamanhoMaximo);
            for (int i = 0; i < tamanhoAtual; i++) {
                ret.elementos[i] = this.elementos[i];
            }
            ret.tamanhoAtual = this.tamanhoAtual;
            return ret;
        } catch (Exception e) {
            return null;
        }
    }

    // Método toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fila de Prioridade Huffman: [");
        for (int i = 0; i < tamanhoAtual; i++) {
            sb.append(elementos[i]);
            if (i < tamanhoAtual - 1)
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
