import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

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
        public int compareTo(NoHuffman o) {
            // Quanto menor a frequência, maior a prioridade
            return Integer.compare(this.frequencia, o.frequencia);
        }

        // Método toString
        @Override
        public String toString() {
            return "{caractere=" + (caractere == '\0' ? "interno" : caractere)
                    + ", frequencia=" + frequencia + "}\n";
        }

        // Verifica se o nó é uma folha
        public boolean isFolha() {
            return filhoEsquerdo == null && filhoDireito == null;
        }
    }

    // Ler arquivo
    public static byte[] lerArquivo(String caminho) throws IOException {
        try (RandomAccessFile arquivo = new RandomAccessFile(caminho, "r")) {
            byte[] dados = new byte[(int) arquivo.length()]; // Cria um array do tamanho do arquivo
            arquivo.readFully(dados); // Lê todo o conteúdo no array
            return dados;
        }
    }

    // Calcular frequencia
    public static HashMap<Character, Integer> calcularFrequencia(byte[] dados) throws Exception {
        // Cria o HashMap para armazenar a frequência dos caracteres
        HashMap<Character, Integer> frequencias = new HashMap<>(256, 0.25f, 0.75f);
        // Percorre os dados e calcula a frequência
        for (byte b : dados) {
            char caractere = (char) b;
            try {
                // Recupera a frequência atual (se existir)
                int freqAtual = frequencias.get(caractere);
                // Incrementa a frequência
                frequencias.put(caractere, freqAtual + 1);
            } catch (Exception e) {
                // Se não existir, adiciona com frequência inicial de 1
                frequencias.put(caractere, 1);
            }
        }
        return frequencias;
    }

    // Método que cria a fila de prioridade com base nas frequências
    public static FilaDePrioridade<NoHuffman> criaFilaDePrioridade(HashMap<Character, Integer> frequencias)
            throws Exception {
        // Cria uma fila de prioridade (min-heap) para armazenar os nós de Huffman
        FilaDePrioridade<NoHuffman> filaDePrioridade = new FilaDePrioridade<NoHuffman>(frequencias.size());

        // Para cada caractere e sua frequência, cria um nó e insere na fila
        for (Character c : frequencias.keySet()) {
            NoHuffman novoNo = new NoHuffman(c, frequencias.get(c));
            filaDePrioridade.guardeUmItem(novoNo);
        }

        return filaDePrioridade;
    }

    public static NoHuffman construirArvoreDeHuffman(FilaDePrioridade<NoHuffman> filaDePrioridade) throws Exception {
        // Enquanto houver mais de um nó na fila de prioridade, continue combinando
        while (filaDePrioridade.getSize() > 1) {
            // Extrai os dois nós com menor frequência
            NoHuffman no1 = filaDePrioridade.remove();
            NoHuffman no2 = filaDePrioridade.remove();

            // Cria um novo nó interno com a soma das frequências
            NoHuffman noInterno = new NoHuffman(no1, no2);

            // Insere o novo nó interno de volta na fila de prioridade
            filaDePrioridade.guardeUmItem(noInterno);
        }

        // Ao final, resta apenas um nó na fila, que é a raiz da árvore
        return filaDePrioridade.remove();
    }

    // Método modificado para retornar o HashMap com os códigos
    public static HashMap<Character, String> gerarCodigos(NoHuffman no) throws Exception {
        HashMap<Character, String> codigos = new HashMap<Character, String>(); // Criar o HashMap dentro do método

        // Função recursiva para gerar os códigos
        gerarCodigosRecursivo(no, "", codigos);

        return codigos; // Retornar o HashMap com os códigos
    }

    private static void gerarCodigosRecursivo(NoHuffman no, String codigoAtual, HashMap<Character, String> codigos)
            throws Exception {
        // Se o nó for uma folha, significa que é um caractere, então guardamos o código
        if (no.isFolha()) {
            codigos.put(no.getCaractere(), codigoAtual); // Aqui chamamos o método correto da sua classe
                                                         // HashMap
            return;
        }

        // Recursão para o filho esquerdo (adiciona '0' ao código)
        if (no.getFilhoEsquerdo() != null) {
            gerarCodigosRecursivo(no.getFilhoEsquerdo(), codigoAtual + "0", codigos);
        }

        // Recursão para o filho direito (adiciona '1' ao código)
        if (no.getFilhoDireito() != null) {
            gerarCodigosRecursivo(no.getFilhoDireito(), codigoAtual + "1", codigos);
        }
    }

    public static void compactarArquivo(String caminhoArquivoOriginal, String caminhoArquivoCompactado)
            throws Exception {
        byte[] dados = lerArquivo(caminhoArquivoOriginal);

        // Calcular a frequência dos caracteres
        HashMap<Character, Integer> frequencias = calcularFrequencia(dados);

        // Criar a fila de prioridade
        FilaDePrioridade<NoHuffman> filaDePrioridade = criaFilaDePrioridade(frequencias);

        // Construir a árvore de Huffman
        NoHuffman raiz = construirArvoreDeHuffman(filaDePrioridade);

        // Gerar os códigos de Huffman
        HashMap<Character, String> codigos = gerarCodigos(raiz);

        // Converter os dados para sequência de bits
        StringBuilder bitsCompactados = new StringBuilder();
        for (byte b : dados) {
            char caractere = (char) b;
            bitsCompactados.append(codigos.get(caractere));
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivoCompactado))) {
            // Salvar a árvore de Huffman no início do arquivo (em formato binário ou texto)
            salvarArvoreHuffman(writer, raiz);

            // Salvar os dados compactados
            writer.write(bitsCompactados.toString());
        }
    }

    // Método recursivo para salvar a árvore de Huffman no arquivo
    private static void salvarArvoreHuffman(BufferedWriter writer, NoHuffman no) throws IOException {
        if (no.isFolha()) {
            // Se for uma folha, salvar o caractere e indicar que é uma folha
            writer.write("1");
            writer.write(no.getCaractere());
        } else {
            // Se for um nó interno, salvar a indicação de que não é folha
            writer.write("0");
            // Recursivamente salvar os filhos esquerdo e direito
            salvarArvoreHuffman(writer, no.getFilhoEsquerdo());
            salvarArvoreHuffman(writer, no.getFilhoDireito());
        }
    }

    public static void descompactarArquivo(String caminhoArquivoCompactado, String caminhoArquivoDescompactado)
            throws Exception {
        // Ler os dados compactados e a árvore de Huffman
        StringBuilder bitsCompactados = new StringBuilder();
        NoHuffman raiz = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivoCompactado))) {
            // Ler e reconstruir a árvore de Huffman
            raiz = reconstruirArvoreHuffman(reader);

            // Ler os bits compactados
            String linha;
            while ((linha = reader.readLine()) != null) {
                bitsCompactados.append(linha);
            }
        }

        // Descompactar os dados usando a árvore de Huffman
        StringBuilder dadosDescompactados = new StringBuilder();
        NoHuffman noAtual = raiz;
        for (int i = 0; i < bitsCompactados.length(); i++) {
            char bit = bitsCompactados.charAt(i);
            // Se o bit for '0', vá para o filho esquerdo, se for '1', vá para o filho
            // direito
            if (bit == '0') {
                noAtual = noAtual.getFilhoEsquerdo();
            } else if (bit == '1') {
                noAtual = noAtual.getFilhoDireito();
            }

            // Se chegarmos a uma folha, significa que encontramos um caractere
            if (noAtual.isFolha()) {
                dadosDescompactados.append(noAtual.getCaractere());
                noAtual = raiz; // Voltar para a raiz para processar o próximo caractere
            }
        }

        // Salvar os dados descompactados no arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivoDescompactado))) {
            writer.write(dadosDescompactados.toString());
        }
    }

    private static NoHuffman reconstruirArvoreHuffman(BufferedReader reader) throws IOException {
        int bit = reader.read();
        if (bit == '1') {
            // Se for folha, ler o caractere e retornar o nó folha
            char caractere = (char) reader.read();
            return new NoHuffman(caractere, 0); // Frequência será definida posteriormente, se necessário
        } else {
            // Se for nó interno, criar um nó e reconstruir os filhos recursivamente
            NoHuffman filhoEsquerdo = reconstruirArvoreHuffman(reader);
            NoHuffman filhoDireito = reconstruirArvoreHuffman(reader);
            return new NoHuffman(filhoEsquerdo, filhoDireito);
        }
    }

}
