public class FilaDePrioridade<X extends Comparable<X>> {
    private X[] elementos;
    private int tamanhoAtual;
    private int tamanhoMaximo;


    public FilaDePrioridade() throws Exception {
        // Inicializa com um tamanho máximo padrão
        this(10);  // Pode ser outro valor padrão
    }

    @SuppressWarnings("unchecked")
    public FilaDePrioridade(int tamanhoMaximo) throws Exception {
        if (tamanhoMaximo <= 0)
            throw new Exception("Tamanho inválido");

        this.elementos = (X[]) new Comparable[tamanhoMaximo];
        this.tamanhoAtual = 0;
        this.tamanhoMaximo = tamanhoMaximo;
    }

    public void guardeUmItem(X x) throws Exception {
        if (x == null)
            throw new Exception("Falta o que guardar");

        if (tamanhoAtual == tamanhoMaximo) {
            redimensionarFila();
        }

        elementos[tamanhoAtual] = x;
        tamanhoAtual++;
        reorganizarParaCima(tamanhoAtual - 1);
    }

    // Método para redimensionar o array de elementos
    @SuppressWarnings("unchecked")
    private void redimensionarFila() {
        tamanhoMaximo *= 2;  // Dobra o tamanho do array
        X[] novoArray = (X[]) new Comparable[tamanhoMaximo];
        System.arraycopy(elementos, 0, novoArray, 0, tamanhoAtual); // Copia os elementos para o novo array
        elementos = novoArray;  // Atualiza o array de elementos
    }

    private void reorganizarParaCima(int index) {
        int pai = (index - 1) / 2;
        if (index > 0 && elementos[index].compareTo(elementos[pai]) < 0) {
            trocar(index, pai);
            reorganizarParaCima(pai);
        }
    }

    private void trocar(int i, int j) {
        X temp = elementos[i];
        elementos[i] = elementos[j];
        elementos[j] = temp;
    }

    public X remove() throws Exception {
        if (tamanhoAtual == 0) {
            throw new Exception("Fila vazia");
        }
        X itemRemovido = elementos[0];
        tamanhoAtual--;
        elementos[0] = elementos[tamanhoAtual];
        reorganizarParaBaixo(0);
        return itemRemovido;
    }

    private void reorganizarParaBaixo(int index) {
        int esquerda = 2 * index + 1;
        int direita = 2 * index + 2;
        int maior = index;

        if (esquerda < tamanhoAtual && elementos[esquerda].compareTo(elementos[maior]) < 0) {
            maior = esquerda;
        }
        if (direita < tamanhoAtual && elementos[direita].compareTo(elementos[maior]) < 0) {
            maior = direita;
        }

        if (maior != index) {
            trocar(index, maior);
            reorganizarParaBaixo(maior);
        }
    }
}
