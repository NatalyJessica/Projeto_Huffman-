public class FilaDePrioridade<X extends Comparable<X>> implements Cloneable {
    private X[] elementos;
    int tamanhoAtual;
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

    //guarde um item
    public void guardeUmItem(X x) throws Exception {
        if (x == null)
            throw new Exception("Falta o que guardar");
    
        if (tamanhoAtual == tamanhoMaximo)
            throw new Exception("Fila cheia");
    
        // Adiciona o elemento no final da fila
        elementos[tamanhoAtual] = x;
        tamanhoAtual++;
    
        // Imprime o estado da fila para debug
       // System.out.println("Item adicionado: " + x);
       // System.out.println(this.toString());
    
        // Organiza a fila para manter a prioridade
        heapifyParaCima(tamanhoAtual - 1);
    }

    // Método para remover o item de maior prioridade
    public X recupereUmItem() throws Exception {
        if (tamanhoAtual == 0)
            throw new Exception("Nada a recuperar");
    
        X ret = elementos[0]; // O elemento de maior prioridade é sempre o primeiro
        // Troca o primeiro item com o último e diminui o tamanho
        tamanhoAtual--;
        elementos[0] = elementos[tamanhoAtual];
    
        // Reorganiza a fila após remoção
        heapifyParaBaixo(0);
    
        // Imprime o estado da fila para debug
       // System.out.println("Item removido: " + ret);
       // System.out.println(this.toString());
    
        return ret;
    }
    
        

    // Método para reorganizar a fila para cima (mantendo a propriedade da heap)
    private void heapifyParaCima(int index) {
        int pai = (index - 1) / 2;
        if (index > 0 && elementos[index].compareTo(elementos[pai]) > 0) {
            trocar(index, pai);
            heapifyParaCima(pai);
        }
    }

    // Método para reorganizar a fila para baixo (mantendo a propriedade da heap)
    private void heapifyParaBaixo(int index) {
        int esquerda = 2 * index + 1;
        int direita = 2 * index + 2;
        int maior = index;

        if (esquerda < tamanhoAtual && elementos[esquerda].compareTo(elementos[maior]) > 0) {
            maior = esquerda;
        }

        if (direita < tamanhoAtual && elementos[direita].compareTo(elementos[maior]) > 0) {
            maior = direita;
        }

        if (maior != index) {
            trocar(index, maior);
            heapifyParaBaixo(maior);
        }
    }

    // Método para trocar dois elementos do array
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

    // Método para obter o tamanho atual da fila
    public int getSize() {
        return tamanhoAtual;
    }

    // Método para clonar a fila
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

    // Método equals para comparar duas filas de prioridade
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (this.getClass() != obj.getClass())
            return false;

        FilaDePrioridade<X> fil = (FilaDePrioridade<X>) obj;

        if (this.tamanhoAtual != fil.tamanhoAtual)
            return false;

        for (int i = 0; i < tamanhoAtual; i++) {
            if (!this.elementos[i].equals(fil.elementos[i]))
                return false;
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

    // Método toString
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
