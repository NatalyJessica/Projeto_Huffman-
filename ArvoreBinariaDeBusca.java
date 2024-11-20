import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ArvoreBinariaDeBusca<X extends Comparable<X>> {
    //sub classe no
    private class No {
        private No esq;
        private X info;
        private No dir;

        public No(No esq, X info, No dir) {
            this.esq = esq;
            this.info = info;
            this.dir = dir;
        }

        public No(X info) {
            this(null, info, null);
        }

        @SuppressWarnings("unused")
        public No(No esq, X info) {
            this(esq, info, null);
        }

        @SuppressWarnings("unused")
        public No(X info, No dir) {
            this(null, info, dir);
        }

        public No getEsq() {
            return this.esq;
        }

        public X getInfo() {
            return this.info;
        }

        public No getDir() {
            return this.dir;
        }

        public void setEsq(No esq) {
            this.esq = esq;
        }

        public void setInfo(X info) {
            this.info = info;
        }

        public void setDir(No dir) {
            this.dir = dir;
        }

        // métodos obrigatórios
        //clone
        @Override
        protected No clone() {
            No cloneEsq = this.esq == null ? null : this.esq.clone();
            No cloneDir = this.dir == null ? null : this.dir.clone();
            X cloneInfo = this.info instanceof Cloneable ? meuCloneDeX(this.info) : this.info;
            return new No(cloneEsq, cloneInfo, cloneDir);
        }
        //clone
        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;

            No other = (No) obj;
            return (info == null ? other.info == null : info.equals(other.info)) &&
                    (esq == null ? other.esq == null : esq.equals(other.esq)) &&
                    (dir == null ? other.dir == null : dir.equals(other.dir));
        }

        //hashCode
        @Override
        public int hashCode() {
            int result = (info == null ? 0 : info.hashCode());
            result = 31 * result + (esq == null ? 0 : esq.hashCode());
            result = 31 * result + (dir == null ? 0 : dir.hashCode());
            return result;
        }

    }

    private No raiz;

    public ArvoreBinariaDeBusca() {
        this.raiz = null;
    }

    // Adicionado para Huffman: Classe interna para armazenar caractere e frequência
    public static class CaractereFreq implements Comparable<CaractereFreq> {
        public char caractere;
        public int frequencia;

        public CaractereFreq(char caractere, int frequencia) {
            this.caractere = caractere;
            this.frequencia = frequencia;
        }


        @Override
        public int compareTo(CaractereFreq outro) {
            return Integer.compare(this.frequencia, outro.frequencia);
        }

        @Override
        public String toString() {
            return "[" + caractere + ", " + frequencia + "]";
        }
    }

    // Método para construir a árvore de Huffman a partir de uma lista de caracteres
    // e suas frequências
    @SuppressWarnings("unchecked")
    public void construaHuffman(CaractereFreq[] caracteres) throws Exception {
        for (CaractereFreq caractereFreq : caracteres) {
            this.inclua((X) caractereFreq);
        }
    }

    //metodo para adicionar elementos na arvore
    public void inclua(X inf) throws Exception {
        if (inf == null)
            throw new Exception("informacao ausente");

        X info;
        if (inf instanceof Cloneable)
            info = meuCloneDeX(inf);
        else
            info = inf;

        if (this.raiz == null) {
            this.raiz = new No(info);
            return;
        }

        No atual = this.raiz;

        for (;;) // forever
        {
            int comparacao = info.compareTo(atual.getInfo());
            if (comparacao == 0)
                throw new Exception("informacao repetida");

            if (comparacao < 0) // deve-se inserir info para o lado esquerdo
                if (atual.getEsq() != null)
                    atual = atual.getEsq();
                else // achei onde inserir; eh para a esquerda do atual
                {
                    atual.setEsq(new No(info));
                    return;
                }
            else // deve-se inserir info para o lado direito
            if (atual.getDir() != null)
                atual = atual.getDir();
            else // achei onde inserir; eh para a direito do atual
            {
                atual.setDir(new No(info));
                return;
            }
        }
    }

    //metodo para ver se existe o nodo procurado
    public boolean tem(X info) throws Exception {
        if (info == null)
            throw new Exception("informacao ausente");

        No atual = this.raiz;
        for (;;) // forever
        {
            if (atual == null)
                return false;

            int comparacao = info.compareTo(atual.getInfo());
            if (comparacao == 0)
                return true;

            if (comparacao < 0)
                atual = atual.getEsq();
            else // comparacao>0
                atual = atual.getDir();
        }
    }

    //metodos para quantidade de nos
    public int getQtdDeNodos() {
        return getQtdDeNodos(this.raiz);
    }

    private int getQtdDeNodos(No r) {
        if (r == null)
            return 0;
        return 1 + getQtdDeNodos(r.getEsq()) + getQtdDeNodos(r.getDir());
    }

    //metodos para balancear a arvore
    public void balanceieSe() {
        balanceieSe(this.raiz);
    }

    private void balanceieSe(No r) {
        if (r == null)
            return;

        int qtdDir = getQtdDeNodos(r.getDir());
        int qtdEsq = getQtdDeNodos(r.getEsq());

        while (Math.abs(qtdDir - qtdEsq) > 1) {
            if (qtdEsq - qtdDir > 1) {
                X antigaRaiz = r.getInfo();
                No atual = r;

                atual = atual.getEsq();
                while (atual.getDir() != null) {
                    atual = atual.getDir();
                }

                r.setInfo(atual.getInfo());
                try {
                    remova(atual.getInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    inclua(antigaRaiz);
                } catch (Exception e) {
                    e.printStackTrace(); // Trate a exceção conforme necessário
                }
                qtdEsq--;
                qtdDir++;
            }

            if (qtdDir - qtdEsq > 1) {
                X antigaRaiz = r.getInfo();
                No atual = r;

                atual = atual.getDir();
                while (atual.getEsq() != null) {
                    atual = atual.getEsq();
                }

                r.setInfo(atual.getInfo());
                try {
                    remova(atual.getInfo());
                } catch (Exception e) {
                    e.printStackTrace(); // Trate a exceção conforme necessário
                }

                try {
                    inclua(antigaRaiz);
                } catch (Exception e) {
                    e.printStackTrace(); // Trate a exceção conforme necessário
                }
                qtdEsq++;
                qtdDir--;
            }

            balanceieSe(r.getDir());
            balanceieSe(r.getEsq());
        }
    }
    //metodo para pegar menor
    public X getMenor() throws Exception {
        if (this.raiz == null)
            throw new Exception("arvore vazia");

        No atual = this.raiz;
        X ret = null;
        for (;;) // forever
        {
            if (atual.getEsq() == null) {
                if (atual.getInfo() instanceof Cloneable)
                    ret = meuCloneDeX(atual.getInfo());
                else
                    ret = atual.getInfo();

                break;
            } else
                atual = atual.getEsq();
        }

        return ret;
    }

    //metodo para pegar o maiorr
    public X getMaior() throws Exception {
        if (this.raiz == null)
            throw new Exception("arvore vazia");

        No atual = this.raiz;
        X ret = null;
        for (;;) // forever
        {
            if (atual.getDir() == null) {
                if (atual.getInfo() instanceof Cloneable)
                    ret = meuCloneDeX(atual.getInfo());
                else
                    ret = atual.getInfo();

                break;
            } else
                atual = atual.getDir();
        }

        return ret;
    }

    //metodo remover
    public void remova(X info) throws Exception {
        if (info == null) {
            throw new Exception("Informação ausente");
        }
        if (this.raiz == null) {
            throw new Exception("Nó nulo");
        }
        if (!tem(info)) {
            throw new Exception("Informação Inexistente");
        }

        No atual = this.raiz;
        No pai = null;
        boolean filhoEsquerdo = true;

        for (;;) {
            int comparacao = info.compareTo(atual.getInfo());
            if (comparacao == 0) {
                break;
            }
            pai = atual;
            if (comparacao < 0) {
                atual = atual.getEsq();
                filhoEsquerdo = true;
            } else {
                atual = atual.getDir();
                filhoEsquerdo = false;
            }
        }

        if (atual.getEsq() == null && atual.getDir() == null) {
            if (atual == this.raiz) {
                this.raiz = null;
            } else if (filhoEsquerdo) {
                pai.setEsq(null);
            } else {
                pai.setDir(null);
            }
        } else if (atual.getDir() == null && filhoEsquerdo) {
            if (atual == this.raiz) {
                this.raiz = atual.getEsq();
            } else {
                pai.setEsq(atual.getEsq());
            }
        } else if (atual.getDir() == null && !filhoEsquerdo) {
            if (atual == this.raiz) {
                this.raiz = atual.getEsq();
            } else {
                pai.setDir(atual.getEsq());
            }
        } else if (atual.getEsq() == null && filhoEsquerdo) {
            if (atual == this.raiz) {
                this.raiz = atual.getDir();
            } else {
                pai.setEsq(atual.getDir());
            }
        } else if (atual.getEsq() == null && !filhoEsquerdo) {
            if (atual == this.raiz) {
                this.raiz = atual.getDir();
            } else {
                pai.setDir(atual.getDir());
            }
        } else {
            No sucessor = null;
            if (getQtdDeNodos(atual.getEsq()) > getQtdDeNodos(atual.getDir())) {
                pai = atual;
                sucessor = atual.getEsq();
                filhoEsquerdo = true;
                while (sucessor.getDir() != null) {
                    pai = sucessor;
                    sucessor = sucessor.getDir();
                    filhoEsquerdo = false;
                }
            } else {
                pai = atual;
                sucessor = atual.getDir();
                filhoEsquerdo = false;
                while (sucessor.getEsq() != null) {
                    pai = sucessor;
                    sucessor = sucessor.getEsq();
                    filhoEsquerdo = true;
                }
            }
            atual.setInfo(sucessor.getInfo());

            if (filhoEsquerdo) {
                pai.setEsq(sucessor.getEsq());
            } else {
                pai.setDir(sucessor.getDir());
            }
        }
    }

    //metodos Obrigatorios
    @SuppressWarnings("unchecked")
    private X meuCloneDeX(X x) {
        X ret = null;

        try {
            Class<?> classe = x.getClass();
            Method metodo = classe.getMethod("clone");
            ret = (X) metodo.invoke(x);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException erro) {
        }

        return ret;
    }
    // clone
    @Override
    public ArvoreBinariaDeBusca<X> clone() {
        try {
            return new ArvoreBinariaDeBusca<>(this);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao clonar ArvoreBinariaDeBusca", e);
        }
    }

    // Construtor de cópia
    public ArvoreBinariaDeBusca(ArvoreBinariaDeBusca<X> modelo) throws Exception {
        if (modelo == null) {
            throw new Exception("Modelo ausente");
        }
        if (modelo.raiz != null) {
            this.raiz = modelo.raiz.clone();
        }
    }

    // equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        ArvoreBinariaDeBusca<?> other = (ArvoreBinariaDeBusca<?>) obj;
        return raiz == null ? other.raiz == null : raiz.equals(other.raiz);
    }

    // hascode
    @Override
    public int hashCode() {
        return 31 * (raiz == null ? 0 : raiz.hashCode());
    }

}