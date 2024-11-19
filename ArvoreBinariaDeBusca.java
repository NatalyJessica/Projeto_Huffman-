import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ArvoreBinariaDeBusca<X extends Comparable<X>> implements Cloneable {
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

        public No(No esq, X info) {
            this(esq, info, null);
        }

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
    }

    private No raiz;

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

    // Método para construir a árvore de Huffman a partir de uma lista de caracteres e suas frequências
    public void construaHuffman(CaractereFreq[] caracteres) throws Exception {
        for (CaractereFreq caractereFreq : caracteres) {
            this.inclua((X) caractereFreq);
        }
    }

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

    public int getQtdDeNodos() {
        return getQtdDeNodos(this.raiz);
    }

    private int getQtdDeNodos(No r) {
        if (r == null)
            return 0;
        return 1 + getQtdDeNodos(r.getEsq()) + getQtdDeNodos(r.getDir());
    }

    public void balanceieSe()  {
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
}