import java.util.NoSuchElementException;

public class HashMap<K, V> implements Cloneable {
    private class Element {
        private K chave;
        private V valor;

        public Element(K k, V v) {
            this.chave = k;
            this.valor = v;
        }

        public K getChave() {
            return this.chave;
        }

        public V getValor() {
            return this.valor;
        }

        // @SuppressWarnings("unused")
        @SuppressWarnings("unused")
        public void setChave(K k) {
            this.chave = k;
        }

        public void setValor(V v) {
            this.valor = v;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Element element = (Element) o;
            return (chave != null ? chave.equals(element.chave) : element.chave == null);
        }

        @Override
        public int hashCode() {
            return chave != null ? chave.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "Element{" + "chave=" + chave + ", valor=" + valor + '}';
        }
    }

    private ListaSimplesDesordenada<Element>[] vetor;
    private int qtdElems = 0, qtdPosOcupadas = 0;
    private int capacidadeInicial;
    private float txMinDesperdicio, txMaxDesperdicio;

    @SuppressWarnings("unchecked")
    public HashMap(int capacidadeInicial, float txMinDesperdicio, float txMaxDesperdicio) {
        this.capacidadeInicial = capacidadeInicial;
        this.txMinDesperdicio = txMinDesperdicio;
        this.txMaxDesperdicio = txMaxDesperdicio;
        vetor = new ListaSimplesDesordenada[capacidadeInicial];
    }

    public void guardeUmItem(K chave, V valor) throws Exception {
        if (qtdElems / (float) vetor.length > txMaxDesperdicio) {
            redimensionarVetor(vetor.length * 2);
        }

        int hash = chave.hashCode(); // Correção aqui: método hashCode da chave
        int indice = Math.abs(hash) % vetor.length;

        if (vetor[indice] == null) {
            vetor[indice] = new ListaSimplesDesordenada<>();
            qtdPosOcupadas++;
        }

        Element newElement = new Element(chave, valor);
        ListaSimplesDesordenada<Element> lista = vetor[indice];

        // Verifica se o elemento já existe
        boolean chaveExistente = false;
        for (Element elem : lista) {
            if (elem.getChave().equals(chave)) {
                // Se a chave já existir, atualiza o valor
                elem.setValor(valor);
                chaveExistente = true;
                break;
            }
        }

        // Se a chave não foi encontrada, adiciona um novo elemento
        if (!chaveExistente) {
            lista.guardeUmItemNoInicio(newElement);
            qtdElems++;
        }
    }

    public void removaUmItem(K chave) throws Exception {
        int hash = chave.hashCode(); // Correção aqui: método hashCode da chave
        int indice = Math.abs(hash) % vetor.length;

        ListaSimplesDesordenada<Element> lista = vetor[indice];
        if (lista == null) {
            throw new NoSuchElementException("Chave não encontrada: " + chave);
        }

        for (Element elem : lista) {
            if (elem.getChave().equals(chave)) {
                lista.removaItemIndicado(elem);
                qtdElems--;
                if (lista.isVazia()) {
                    vetor[indice] = null;
                    qtdPosOcupadas--;
                }

                if ((vetor.length - qtdElems) / (float) vetor.length > txMinDesperdicio
                        && vetor.length > capacidadeInicial) {
                    redimensionarVetor(vetor.length / 2);
                }

                return;
            }
        }
        throw new NoSuchElementException("Chave não encontrada: " + chave);
    }

    // METODO PARA REDIMENCIONAR ITEM
    @SuppressWarnings("unchecked")
    private void redimensionarVetor(int novaCapacidade) throws Exception {
        ListaSimplesDesordenada<Element>[] novoVetor = new ListaSimplesDesordenada[novaCapacidade];

        for (ListaSimplesDesordenada<Element> lista : vetor) {
            if (lista != null) {
                for (Element elem : lista) {
                    int hash = elem.getChave().hashCode(); // Correção aqui
                    int indice = Math.abs(hash) % novaCapacidade;

                    if (novoVetor[indice] == null) {
                        novoVetor[indice] = new ListaSimplesDesordenada<>();
                    }

                    novoVetor[indice].guardeUmItemNoInicio(elem);
                }
            }
        }

        vetor = novoVetor;
    }

    public V recupereUmItem(K chave) throws Exception {
        int hash = chave.hashCode(); // Correção aqui: método hashCode da chave
        int indice = Math.abs(hash) % vetor.length;

        ListaSimplesDesordenada<Element> lista = vetor[indice];
        if (lista == null) {
            throw new NoSuchElementException("Chave não encontrada: " + chave);
        }

        for (Element elem : lista) {
            if (elem.getChave().equals(chave)) {
                return elem.getValor();
            }
        }

        throw new NoSuchElementException("Chave não encontrada: " + chave);
    }

    // Método para retornar as chaves armazenadas no HashMap
    public Iterable<K> getChaves() throws Exception {
        ListaSimplesDesordenada<K> chaves = new ListaSimplesDesordenada<>();
        for (ListaSimplesDesordenada<Element> lista : vetor) {
            if (lista != null) {
                for (Element elem : lista) {
                    chaves.guardeUmItemNoInicio(elem.getChave());
                }
            }
        }
        return chaves;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("{");
        boolean primeiroElemento = true;

        for (int i = 0; i < vetor.length; i++) {
            if (vetor[i] != null) {
                for (Element elem : vetor[i]) {
                    if (!primeiroElemento) {
                        result.append(", ");
                    }
                    result.append(elem.getChave()).append("=").append(elem.getValor());
                    primeiroElemento = false;
                }
            }
        }

        result.append("}");
        return result.toString();
    }

    public int getQtdElems() {
        return qtdElems;
    }

    // 1. Método get (equivalente a recupereUmItem)
    public V get(K chave) throws Exception {
        return recupereUmItem(chave); // Aproveitando o método já existente
    }

    // 2. Método size
    public int size() {
        return qtdElems; // Retorna a quantidade de elementos
    }

    // 3. Método keySet
    public java.util.Set<K> keySet() {
        java.util.Set<K> chaves = new java.util.HashSet<>();
        for (ListaSimplesDesordenada<Element> lista : vetor) {
            if (lista != null) {
                for (Element elem : lista) {
                    chaves.add(elem.getChave());
                }
            }
        }
        return chaves;
    }
}
