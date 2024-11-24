public class FilaDePrioridade<X extends Comparable<X>> implements Cloneable {
    private X[] elementos;
    private int tamanhoAtual;
    private int tamanhoMaximo;

    // Construtor da Fila de Prioridade
    @SuppressWarnings("unchecked")
    public FilaDePrioridade(int tamanhoMaximo) throws Exception {
        if (tamanhoMaximo <= 0)
            throw new Exception("Tamanho inválido");

        this.elementos = (X[]) new Comparable[tamanhoMaximo];
        this.tamanhoAtual = 0;
        this.tamanhoMaximo = tamanhoMaximo;
    }

    // Adiciona um item na fila de prioridade
    public void guardeUmItem(X x) throws Exception {
        if (x == null)
            throw new Exception("Falta o que guardar");

        if (tamanhoAtual == tamanhoMaximo)
            throw new Exception("Fila cheia");

        // Adiciona o elemento no final da fila
        elementos[tamanhoAtual] = x;
        tamanhoAtual++;

        // Organiza a fila para manter a prioridade (Heap Max)
        reorganizarParaCima(tamanhoAtual - 1);
    }

    // Método para remover o item de maior prioridade (primeiro elemento)
    public X recupereUmItem() throws Exception {
        if (tamanhoAtual == 0)
            throw new Exception("Nada a recuperar");

        X ret = elementos[0]; // O elemento de maior prioridade é o primeiro
        tamanhoAtual--;
        elementos[0] = elementos[tamanhoAtual]; // Troca o primeiro com o último

        // Reorganiza a fila para manter a propriedade da heap
        reorganizarParaBaixo(0);

        return ret;
    }

    // Reorganiza a fila para cima para manter a propriedade da heap
    private void reorganizarParaCima(int index) {
        int pai = (index - 1) / 2;
        if (index > 0 && elementos[index].compareTo(elementos[pai]) > 0) {
            trocar(index, pai);
            reorganizarParaCima(pai); // Recursão para continuar a organizar
        }
    }

    // Reorganiza a fila para baixo para manter a propriedade da heap
    private void reorganizarParaBaixo(int index) {
        int esquerda = 2 * index + 1;
        int direita = 2 * index + 2;
        int maior = index;

        // Verifica qual filho tem maior prioridade
        if (esquerda < tamanhoAtual && elementos[esquerda].compareTo(elementos[maior]) > 0) {
            maior = esquerda;
        }
        if (direita < tamanhoAtual && elementos[direita].compareTo(elementos[maior]) > 0) {
            maior = direita;
        }

        // Se o maior não for o elemento atual, troca e organiza novamente
        if (maior != index) {
            trocar(index, maior);
            reorganizarParaBaixo(maior); // Recursão para continuar a organizar
        }
    }

    // Troca dois elementos no array
    private void trocar(int i, int j) {
        X temp = elementos[i];
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
            FilaDePrioridade<X> ret = new FilaDePrioridade<>(this.tamanhoMaximo);
            for (int i = 0; i < tamanhoAtual; i++) {
                ret.elementos[i] = this.elementos[i];
            }
            ret.tamanhoAtual = this.tamanhoAtual;
            return ret;
        } catch (Exception e) {
            return null;
        }
    }

    // Método equals para comparar duas filas
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        FilaDePrioridade<X> fil = (FilaDePrioridade<X>) obj;
        if (this.tamanhoAtual != fil.tamanhoAtual) return false;

        for (int i = 0; i < tamanhoAtual; i++) {
            if (!this.elementos[i].equals(fil.elementos[i])) return false;
        }

        return true;
    }

    // Método hashCode
    public int hashCode() {
        int result = 17;
        for (int i = 0; i < tamanhoAtual; i++) {
            result = 31 * result + (elementos[i] == null ? 0 : elementos[i].hashCode());
        }
        return result;
    }

    // Método toString para representação em string
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fila de Prioridade: [");
        for (int i = 0; i < tamanhoAtual; i++) {
            sb.append(elementos[i]);
            if (i < tamanhoAtual - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
