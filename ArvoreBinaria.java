public class ArvoreBinaria<T> {
    private class No {
        T valor;
        No esquerdo, direito;

        public No(T valor) {
            this.valor = valor;
            esquerdo = direito = null;
        }
    }

    private No raiz;

    public ArvoreBinaria() {
        raiz = null;
    }

    public void inserir(T valor) {
        raiz = inserirRecursivo(raiz, valor);
    }

    private No inserirRecursivo(No raiz, T valor) {
        if (raiz == null) {
            raiz = new No(valor);
            return raiz;
        }
        // Caso a árvore já tenha valores, o código de inserção vai depender
        // do critério de comparação que você vai utilizar (dependerá do seu uso).
        return raiz;
    }

    public boolean isVazia() {
        return raiz == null;
    }

    public void exibir() {
        exibirRecursivo(raiz);
    }

    private void exibirRecursivo(No raiz) {
        if (raiz != null) {
            exibirRecursivo(raiz.esquerdo);
            System.out.println(raiz.valor);
            exibirRecursivo(raiz.direito);
        }
    }
}
