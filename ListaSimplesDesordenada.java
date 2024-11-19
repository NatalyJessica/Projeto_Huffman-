import java.lang.reflect.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaSimplesDesordenada<X> implements Iterable<X> {
    // Classe interna representando um nó da lista
    private class No {
        private X info; // Informação armazenada no nó
        private No prox; // Referência para o próximo nó na lista

        // Construtor que inicializa o nó com uma informação e o próximo nó
        public No(X i, No p) {
            this.info = i;
            this.prox = p;
        }

        // Construtor que inicializa o nó com uma informação, sem próximo nó (último nó)
        public No(X i) {
            this.info = i;
            this.prox = null;
        }

        // Método para obter a informação armazenada no nó
        public X getInfo() {
            return this.info;
        }

        // Método para obter a referência para o próximo nó
        public No getProx() {
            return this.prox;
        }
        // Método para definir a informação armazenada no nó
        /*
         * public void setInfo(X i) {
         * this.info = i;
         * }
         */

        // Método para definir a referência para o próximo nó
        public void setProx(No p) {
            this.prox = p;
        }
    } // fim da classe No

    // Referências para o primeiro e o último nó da lista
    private No primeiro, ultimo;

    // Construtor da lista, inicializa uma lista vazia
    public ListaSimplesDesordenada() {
        this.primeiro = this.ultimo = null;
    }

    public X getPrimeiro() {
        if (this.primeiro == null) {
            return null; // Retorna null se a lista estiver vazia
        }
        return this.primeiro.getInfo(); // Retorna a informação do primeiro nó
    }

    // Método privado para clonar um objeto do tipo X, se ele for clonável
    @SuppressWarnings("unchecked")
    private X meuCloneDeX(X x) {
        X ret = null;

        try {
            // Obtendo a classe do objeto e o método clone
            Class<?> classe = x.getClass();
            Method metodo = classe.getMethod("clone");
            // Invocando o método clone para criar uma cópia do objeto
            ret = (X) metodo.invoke(x);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException erro) {
            // Se o método clone não existir ou houver algum erro na clonagem, lança uma exceção em tempo de execução
            throw new RuntimeException("Erro ao clonar o objeto", erro);
        }

        return ret;
    }

    // Método para inserir um item no início da lista
    public void guardeUmItemNoInicio(X i) throws Exception {
        if (i == null)
            throw new Exception("Informação ausente");

        X inserir;
        // Se o item é clonável, clona o item antes de inserir
        if (i instanceof Cloneable)
            inserir = meuCloneDeX(i);
        else
            inserir = i;

        // Insere o novo nó no início da lista
        this.primeiro = new No(inserir, this.primeiro);

        // Se a lista estava vazia, o novo nó também é o último
        if (this.ultimo == null)
            this.ultimo = this.primeiro;
    }

    // Método para inserir um item no final da lista
    public void guardeUmItemNoFinal(X i) throws Exception {
        if (i == null)
            throw new Exception("Informação ausente");

        X inserir;
        // Se o item é clonável, clona o item antes de inserir
        if (i instanceof Cloneable)
            inserir = meuCloneDeX(i);
        else
            inserir = i;

        // Se a lista está vazia, o novo nó é o primeiro e o último
        if (this.ultimo == null) {
            this.ultimo = new No(inserir);
            this.primeiro = this.ultimo;
        } else {
            // Caso contrário, insere no final da lista
            this.ultimo.setProx(new No(inserir));
            this.ultimo = this.ultimo.getProx();
        }
    }

    // Método para recuperar o item do início da lista
    public X recupereItemDoInicio() throws Exception {
        if (this.primeiro == null)
            throw new Exception("Nada a obter");

        X ret = this.primeiro.getInfo();
        // Se o item é clonável, retorna uma cópia do item
        if (ret instanceof Cloneable)
            ret = meuCloneDeX(ret);

        return ret;
    }

    // Método para recuperar o item do final da lista
    public X recupereItemDoFinal() throws Exception {
        if (this.primeiro == null)
            throw new Exception("Nada a obter");

        X ret = this.ultimo.getInfo();
        // Se o item é clonável, retorna uma cópia do item
        if (ret instanceof Cloneable)
            ret = meuCloneDeX(ret);

        return ret;
    }

    public X buscarItem(X item) throws Exception {
        if (item == null) {
            throw new Exception("Informação não pode ser nula");
        }

        No atual = this.primeiro;

        // Percorre a lista para encontrar o item
        while (atual != null) {
            if (item.equals(atual.getInfo())) {
                return atual.getInfo();
            }
            atual = atual.getProx();
        }

        // Retorna null se o item não for encontrado
        return null;
    }

    public void removaItemIndicado(X i) throws Exception {
        if (i == null)
            throw new Exception("Informacao ausente");

        boolean removeu = false;

        while (this.primeiro != null && i.equals(this.primeiro.getInfo())) {
            if (this.ultimo == this.primeiro)
                this.ultimo = null;

            this.primeiro = this.primeiro.getProx();
            removeu = true;
        }

        if (this.primeiro != null) {
            No atual = this.primeiro;
            while (atual.getProx() != null) {
                if (i.equals(atual.getProx().getInfo())) {
                    if (this.ultimo == atual.getProx())
                        this.ultimo = atual;

                    atual.setProx(atual.getProx().getProx());
                    removeu = true;
                } else {
                    atual = atual.getProx();
                }
            }
        }

        if (!removeu)
            throw new Exception("Informacao inexistente");
    }

    // Método para remover o item do final da lista
    public void removaItemDoFinal() throws Exception {
        if (this.primeiro == null)
            throw new Exception("Nada a remover");

        // Se há apenas um nó, a lista ficará vazia
        if (this.primeiro == this.ultimo) {
            this.primeiro = this.ultimo = null;
            return;
        }

        // Percorre a lista para encontrar o penúltimo nó
        No atual;
        for (atual = this.primeiro; atual.getProx() != this.ultimo; atual = atual.getProx())
            ; // comando vazio

        // Remove o último nó
        atual.setProx(null);
        this.ultimo = atual;
    }

    // Método para obter a quantidade de itens na lista
    public int getQuantidade() {
        No atual = this.primeiro;
        int ret = 0; // Inicializa o contador

        // Conta o número de nós na lista
        while (atual != null) {
            ret++; // Incrementa o contador
            atual = atual.getProx(); // Avança para o próximo nó
        }

        return ret; // Retorna a quantidade total
    }

    // Método para verificar se um item está presente na lista
    public boolean tem(X i) throws Exception {
        if (i == null)
            throw new Exception("Informação ausente");

        No atual = this.primeiro;

        // Percorre a lista comparando cada nó com o item procurado
        while (atual != null) {
            if (i.equals(atual.getInfo()))
                return true;

            atual = atual.getProx();
        }

        return false;
    }

    // Método para verificar se a lista está vazia
    public boolean isVazia() {
        return this.primeiro == null;
    }

    // Método para representar a lista como uma string
    public String toString() {
        String ret = "[";

        No atual = this.primeiro;

        // Percorre a lista e adiciona cada item à string
        while (atual != null) {
            ret = ret + atual.getInfo();

            if (atual != this.ultimo)
                ret = ret + ",";

            atual = atual.getProx();
        }

        return ret + "]";
    }

    // Método para comparar se duas listas são iguais
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (this.getClass() != obj.getClass())
            return false;

        ListaSimplesDesordenada<X> lista = (ListaSimplesDesordenada<X>) obj;

        No atualThis = this.primeiro;
        No atualLista = lista.primeiro;

        // Compara cada nó das duas listas
        while (atualThis != null && atualLista != null) {
            if (!atualThis.getInfo().equals(atualLista.getInfo()))
                return false;

            atualThis = atualThis.getProx();
            atualLista = atualLista.getProx();
        }

        // Verifica se as duas listas têm o mesmo tamanho
        if (atualThis != null || atualLista != null)
            return false;

        return true;
    }

    // Método para calcular o hash code da lista
    public int hashCode() {
        final int PRIMO = 13; // Número primo usado no cálculo do hash
        int ret = 30; // Valor inicial arbitrário

        // Percorre a lista e calcula o hash code com base nos itens
        for (No atual = this.primeiro; atual != null; atual = atual.getProx())
            ret = PRIMO * ret + atual.getInfo().hashCode();

        // Garante que o hash code seja positivo
        if (ret < 0)
            ret = -ret;

        return ret;
    }

    // Construtor de cópia
    public ListaSimplesDesordenada(ListaSimplesDesordenada<X> modelo) throws Exception {
        if (modelo == null)
            throw new Exception("Modelo ausente");

        // Se o modelo está vazio, a nova lista também estará vazia
        if (modelo.primeiro == null)
            return;

        // Clona o primeiro nó da lista
        this.primeiro = new No(modelo.primeiro.getInfo());

        No atualDoThis = this.primeiro;
        No atualDoModelo = modelo.primeiro.getProx();

        // Clona os nós restantes
        while (atualDoModelo != null) {
            atualDoThis.setProx(new No(atualDoModelo.getInfo()));
            atualDoThis = atualDoThis.getProx();
            atualDoModelo = atualDoModelo.getProx();
        }

        this.ultimo = atualDoThis;
    }

    // Método para clonar a lista
    public Object clone() {
        ListaSimplesDesordenada<X> ret = null;

        try {
            ret = new ListaSimplesDesordenada<>(this);
        } catch (Exception erro) {
            // O construtor de cópia só lança exceção se o modelo for null, o que não ocorre
            // aqui
        }

        return ret;
    }

    // iterator é um metodo para percorrer a lista
    @Override
    public Iterator<X> iterator() {
        return new Iterator<X>() {
            private No atual = primeiro;

            @Override
            public boolean hasNext() {
                return atual != null;
            }

            @Override
            public X next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                X info = atual.getInfo();
                atual = atual.getProx();
                return info;
            }
        };

    }
}
