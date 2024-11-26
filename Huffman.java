import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.NoSuchElementException;

public class Huffman {
    public static class NoHuffman implements Comparable<NoHuffman> {
        private char caractere; // O caractere representado (usado nas folhas)
        private int frequencia; // A frequência do caractere
        private NoHuffman filhoEsquerdo; // Filho esquerdo do nó
        private NoHuffman filhoDireito; // Filho direito do nó

        // Construtor para nó folha (com caractere e frequência)
        public NoHuffman(char caractere, int frequencia) {
            this.caractere = caractere;
            this.frequencia = frequencia;
            this.filhoEsquerdo = null;
            this.filhoDireito = null;
        }

        // Construtor para nó interno (sem caractere, com filhos esquerdo e direito)
        public NoHuffman(NoHuffman filhoEsquerdo, NoHuffman filhoDireito) {
            this.caractere = '\0'; // Caractere vazio para nós internos
            this.frequencia = filhoEsquerdo.frequencia + filhoDireito.frequencia; // Frequência combinada
            this.filhoEsquerdo = filhoEsquerdo;
            this.filhoDireito = filhoDireito;
        }

        public char getCaractere() {
            return caractere;
        }

        public int getFrequencia() {
            return frequencia;
        }

        public NoHuffman getFilhoEsquerdo() {
            return filhoEsquerdo;
        }

        public NoHuffman getFilhoDireito() {
            return filhoDireito;
        }

        // Implementação do compareTo para a prioridade de construção da árvore de
        // Huffman
        @Override
        public int compareTo(NoHuffman outro) {
            // Compara pela frequência de forma crescente (menor frequência tem maior
            // prioridade)
            return Integer.compare(this.frequencia, outro.frequencia);
        }

        // Método toString
        @Override
        public String toString() {
            return "NoHuffman{caractere=" + (caractere == '\0' ? "interno" : caractere)
                    + ", frequencia=" + frequencia + "}";
        }

        // Verifica se o nó é uma folha
        public boolean isFolha() {
            return filhoEsquerdo == null && filhoDireito == null;
        }
    }

    // Método para ler o conteúdo de um arquivo e retorná-lo como uma string
    public static String lerArquivo(String caminho) throws IOException {

        RandomAccessFile raf = null;
        StringBuilder sb = new StringBuilder();
        String linha;
        try {
            // Inicializa o RandomAccessFile para ler o arquivo, com o modo de acesso "r"
            // (somente leitura)
            raf = new RandomAccessFile(caminho, "r");

            // Laço que vai ler o arquivo linha por linha até o fim (raf.readLine() retorna
            // null quando não há mais linhas)
            while ((linha = raf.readLine()) != null) {
                // Adiciona a linha lida ao StringBuilder e adiciona uma quebra de linha "\n"
                sb.append(linha).append("\n");
            }
        } finally {
            if (raf != null) {
                // Fecha o arquivo após a leitura para liberar os recursos
                raf.close();
            }
        }
        // Retorna o conteúdo lido como uma string
        return sb.toString().trim();
    }

    // calculando a frequencia
    public static HashMap<Character, Integer> calcularFrequencia(String texto) throws Exception {
        HashMap<Character, Integer> frequencias = new HashMap<>(10, 0.75f, 0.9f);
        // percore cada caractere do texto
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            try {
                // recupera a frequência atual do caractere
                int freqAtual = frequencias.recupereUmItem(c);
                // Atualiza a frequência do caractere
                frequencias.guardeUmItem(c, freqAtual + 1);
            } catch (NoSuchElementException e) {
                // Se o caractere não existe, adiciona com frequência 1
                frequencias.guardeUmItem(c, 1);
            }
        }
        return frequencias;

    }

    // criando fila de prioridade
    public FilaDePrioridade<NoHuffman> criarFilaDePrioridade(HashMap<Character, Integer> frequencias) throws Exception {
        // Cria a fila de prioridade com o tamanho de caracter do hashMap
        FilaDePrioridade<NoHuffman> fila = new FilaDePrioridade<>(frequencias.getQtdElems());
        // Percorre as chaves do HashMap de frequências
        for (Character caractere : frequencias.getChaves()) {
            // Recupera a frequência do caractere
            int frequencia = frequencias.recupereUmItem(caractere);
            // Cria um nó Huffman com o caractere e a sua frequência
            NoHuffman no = new NoHuffman(caractere, frequencia);
            // Adiciona o nó na fila de prioridade
            fila.guardeUmItem(no);
        }
        return fila;
    }

    // construindo arvore
    public static NoHuffman construirArvore(FilaDePrioridade<NoHuffman> fila) throws Exception {
        while (fila.getSize() > 1) {
            // Remover os dois nós de menor frequência
            NoHuffman noEsquerdo = fila.recupereUmItem();
            NoHuffman noDireito = fila.recupereUmItem();

            // Criar um novo nó com a soma das frequências
            int frequenciaSomada = noEsquerdo.getFrequencia() + noDireito.getFrequencia();
            NoHuffman novoNo = new NoHuffman('\0', frequenciaSomada); 

            // Definir os filhos esquerdo e direito e enfileirar o novo nó
            novoNo.filhoEsquerdo = noEsquerdo;
            novoNo.filhoDireito = noDireito;
            fila.guardeUmItem(novoNo);

        }
        // Retorna o último nó restante, que é a raiz da árvore
        NoHuffman raiz = fila.recupereUmItem();
        return raiz;
    }

    // gerando codigos
    public static HashMap<Character, String> gerarCodigos(NoHuffman raiz) throws Exception {
        // Criar instância do HashMap personalizado
        HashMap<Character, String> codigos = new HashMap<>(10, 0.2f, 0.8f);
        gerarCodigosRecursivo(raiz, "", codigos);
        return codigos;
    }

    // metodo recursivo para gerar os codigos
    private static void gerarCodigosRecursivo(NoHuffman no, String codigoAtual, HashMap<Character, String> codigos)
            throws Exception {
        // Se o nó é uma folha, o código está completo
        if (no.getFilhoEsquerdo() == null && no.getFilhoDireito() == null) {
            codigos.guardeUmItem(no.getCaractere(), codigoAtual);
            return;
        }
        // Percorre para o filho esquerdo (adiciona '0')
        if (no.getFilhoEsquerdo() != null) {
            gerarCodigosRecursivo(no.getFilhoEsquerdo(), codigoAtual + "0", codigos);
        }
        // Percorre para o filho direito (adiciona '1')
        if (no.getFilhoDireito() != null) {
            gerarCodigosRecursivo(no.getFilhoDireito(), codigoAtual + "1", codigos);
        }
    }

    // compactando arquivo
    public static String compactar(String texto, HashMap<Character, String> codigos) throws Exception {
        StringBuilder textoCompactado = new StringBuilder();

        // percorre os  caractere do texto
        for (int i = 0; i < texto.length(); i++) {
            char caractere = texto.charAt(i);
            // Recupera o código de Huffman correspondente ao caractere
            String codigoHuffman = codigos.recupereUmItem(caractere);
            // Adiciona o código ao texto compactado
            textoCompactado.append(codigoHuffman);
        }
        return textoCompactado.toString(); // Retorna o texto compactado em formato binário
    }

    // Método para salvar o arquivo Huffman com a árvore e o texto compactado
    public static String salvarArquivoHuffman(String caminho, String textoCompactado, NoHuffman raiz)
            throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(caminho, "rw")) {
            // Serializar a árvore em pré-ordem
            StringBuilder estruturaArvore = new StringBuilder();
            serializarArvore(raiz, estruturaArvore);

            // Escrever a árvore e o texto compactado no arquivo
            //OBS:O método writeBytes é da classe RandomAccessFile
            //utilizado para escrever uma sequência de byte
            raf.writeBytes(estruturaArvore.toString() + "\n"); 
            raf.writeBytes(textoCompactado);
        }
        return  caminho; 
    }

    // Método para serializar a árvore em pré-ordem
    private static void serializarArvore(NoHuffman no, StringBuilder sb) {
        if (no == null) {
            sb.append("null,");
            return;
        }
        // Se for uma folha, gravar o caractere
        if (no.filhoEsquerdo == null && no.filhoDireito == null) {
            sb.append("[").append(no.caractere).append("],");
        } else {
            sb.append("(),");
        }
        // Recursivamente serializar os filhos
        serializarArvore(no.filhoEsquerdo, sb);
        serializarArvore(no.filhoDireito, sb);
    }

    // Descompactação (transformar o binário de volta para o texto original)
    //StringBuilder usada para concatenar os caracter 
    public static String descompactar(String binario, NoHuffman raiz) {
        StringBuilder texto = new StringBuilder();
        NoHuffman noAtual = raiz;

        for (int i = 0; i < binario.length(); i++) {
            noAtual = binario.charAt(i) == '0' ? noAtual.filhoEsquerdo : noAtual.filhoDireito;
            // Se chegarmos a uma folha, adicionamos o caractere ao texto
            if (noAtual.filhoEsquerdo == null && noAtual.filhoDireito == null) {
                texto.append(noAtual.caractere);
                noAtual = raiz;
            }
        }

        return texto.toString();
    }

    //BufferedWriter usado para passar informações para o texto
    public static void salvarArquivoDescompactado(String caminho, String textoDescompactado) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {
        writer.write(textoDescompactado);
    }
}

}
