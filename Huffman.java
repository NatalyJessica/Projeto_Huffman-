import java.io.*;
import java.util.*;

public class Huffman {

    public static class NoHuffman implements Comparable<NoHuffman> {
        // variavel
        private byte dado;
        private int frequencia;
        private NoHuffman filhoEsquerdo;
        private NoHuffman filhoDireito;

        // construtor
        public NoHuffman(byte dado, int frequencia) {
            this.dado = dado;
            this.frequencia = frequencia;
            this.filhoEsquerdo = null;
            this.filhoDireito = null;
        }

        // Construtor para nó interno (sem dado, com filhos esquerdo e direito)
        public NoHuffman(NoHuffman filhoEsquerdo, NoHuffman filhoDireito) {
            this.dado = '\0';
            this.frequencia = filhoEsquerdo.frequencia + filhoDireito.frequencia;
            this.filhoEsquerdo = filhoEsquerdo;
            this.filhoDireito = filhoDireito;
        }

        // Getters e Setters
        public byte getDado() {
            return dado;
        }
        public void setDado(byte dado) {
            this.dado = dado;
        }

        public int getFrequencia() {
            return frequencia;
        }
        public void setFrequencia(int frequencia) {
            this.frequencia = frequencia;
        }

        public NoHuffman getFilhoEsquerdo() {
            return filhoEsquerdo;
        }
        public void setFilhoEsquerdo(NoHuffman filhoEsquerdo) {
            this.filhoEsquerdo = filhoEsquerdo;
        }

        public NoHuffman getFilhoDireito() {
            return filhoDireito;
        }
        public void setFilhoDireito(NoHuffman filhoDireito) {
            this.filhoDireito = filhoDireito;
        }

        // metodos obrigatorios
        @Override
        public int compareTo(NoHuffman o) {
            return Integer.compare(this.frequencia, o.frequencia);
        }

        @Override
        public String toString() {
            return "{dado=" + (dado == '\0' ? "interno" : dado)
                    + ", frequencia=" + frequencia + "}\n";
        }
        //metodo auxilixar para verificar se é raiz ou folha
        public boolean isFolha() {
            if(filhoEsquerdo == null && filhoDireito == null){
                return true;
            }
            return false;
        }
    }

    // Ler arquivo
    public static byte[] lerArquivo(String caminho) throws Exception {
        // RandomAccessFile le os arquivos
        try (RandomAccessFile arquivo = new RandomAccessFile(caminho, "r")) {
            byte[] dados = new byte[(int) arquivo.length()]; // Cria um array do tamanho do arquivo
            arquivo.readFully(dados); // Lê todo o conteúdo no array
            return dados;
        }
    }

    // Calcular Frequencia dos Byte
    public static HashMap<Byte, Integer> calcularFrequencia(byte[] dados) throws Exception {
        // Cria o HashMap para armazenar a frequência dos bytes
        HashMap<Byte, Integer> frequencias = new HashMap<>(256, 0.75f, 0.5f);

        // Percorre os dados e calcula a frequência
        for (byte b : dados) {
            //guarda  o byte e se já existe aumente mais 1
            frequencias.put(b, frequencias.getValorPadrão(b, 0) + 1);
        }
        return frequencias;
    }

    // Método que cria a fila de prioridade com base nas frequências
    public static FilaDePrioridade<NoHuffman> criaFilaDePrioridade(HashMap<Byte, Integer> frequencias)
            throws Exception {
        // Criação da lista de nós
        ListaSimplesDesordenada<NoHuffman> nos = new ListaSimplesDesordenada<>();
        for (Byte dado : frequencias.keySet()) {
            int frequencia = frequencias.get(dado);
            // Cria o nó para cada byte e frequência e guarda na lista
            nos.guardeUmItemNoFinal(new NoHuffman(dado, frequencia));
        }

        // Criando a fila de prioridade com base nos nós de Huffman
        FilaDePrioridade<NoHuffman> fila = new FilaDePrioridade<NoHuffman>();
        // Ordena a lista de nós de Huffman conforme a frequência
        while (!nos.isVazia()) {
            NoHuffman no = nos.recupereItemDoInicio(); // Recupera o primeiro nó
            fila.guardeUmItem(no); // Adiciona o nó à fila de prioridade
            nos.removaItemIndicado(no); // Remove o nó da lista 
        }
        return fila;
    }

    public static NoHuffman construirArvoreDeHuffman(FilaDePrioridade<NoHuffman> filaDePrioridade) throws Exception {
        // Enquanto houver mais de um nó na fila de prioridade cria u nodo e 2 filho
        while (filaDePrioridade.getTamanho() > 1) {
            // Extrai os dois nós 
            NoHuffman no1 = filaDePrioridade.remove();
            NoHuffman no2 = filaDePrioridade.remove();

            // Cria um novo nó interno com a soma das frequências
            NoHuffman noInterno = new NoHuffman(no1, no2);

            // Insere o novo nó interno de volta na fila de prioridade
            filaDePrioridade.guardeUmItem(noInterno);
        }

        // O último nó restante na fila é a raiz da árvore de Huffman
        return filaDePrioridade.remove();
    }
    //metodopara gerar o codigo binario
    public static HashMap<Byte, String> gerarCodigos(NoHuffman raiz) throws Exception {
        HashMap<Byte, String> codigos = new HashMap<>(); // Map para armazenar os códigos
        gerarCodigosRecursivo(raiz, "", codigos); // Chamaa recursão
        return codigos; // Retorna o map gerado
    }

    //metodo recursivo para gerar o codigo binario
    private static void gerarCodigosRecursivo(NoHuffman no, String codigoAtual, HashMap<Byte, String> codigos)
            throws Exception {
        // Se o nó é folha, ele contém um byte válido
        if (no.isFolha()) {
            codigos.put(no.getDado(), codigoAtual); // Adiciona o byte e seu código ao mapa
            return;
        }

        // Recursão para o filho esquerdo, adicionando '0' ao código atual
        if (no.getFilhoEsquerdo() != null) {
            gerarCodigosRecursivo(no.getFilhoEsquerdo(), codigoAtual + "0", codigos);
        }

        // Recursão para o filho direito, adicionando '1' ao código atual
        if (no.getFilhoDireito() != null) {
            gerarCodigosRecursivo(no.getFilhoDireito(), codigoAtual + "1", codigos);
        }
    }


    //metodo para compactar arquivo
    public static void compactarArquivo(String caminhoArquivoOriginal, String caminhoArquivoCompactado)throws Exception {
        // Ler os dados do arquivo original
        byte[] dados = lerArquivo(caminhoArquivoOriginal);
        System.out.println("Dados lidos do arquivo original: " + Arrays.toString(dados));

        // Calcular a frequência dos bytes
        HashMap<Byte, Integer> frequencias = calcularFrequencia(dados);
        System.out.println("Frequências calculadas: " + frequencias);

        // Criar a fila de prioridade
        FilaDePrioridade<NoHuffman> filaDePrioridade = criaFilaDePrioridade(frequencias);
        System.out.println("Fila de prioridade criada: " + filaDePrioridade);

        // Construir a árvore de Huffman
        NoHuffman raiz = construirArvoreDeHuffman(filaDePrioridade);
        System.out.println("Árvore de Huffman construída. Raiz: " + raiz);

        // Gerar os códigos de Huffman
        HashMap<Byte, String> codigos = gerarCodigos(raiz);
        System.out.println("Códigos de Huffman gerados: " + codigos);

        // Converter os dados  para uma sequência de bits compactados
        //StringBulder é usado para concatenar de forma mais eficiente
        StringBuilder bitsCompactados = new StringBuilder();
        for (byte b : dados) {
            //pega a informação e guarda no final da string
            bitsCompactados.append(codigos.get(b));
        }
        System.out.println("Bits compactados gerados: " + bitsCompactados);

        // Salvar a árvoren e os dados compactados no arquivo de saída
        //DataOutputStream escreve no arquivo e FileOutputStream especifica o caminho pra salvar
        try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(caminhoArquivoCompactado))) {
            System.out.println("Iniciando salvamento no arquivo compactado.");

            //salvar a árvore no  arquivo
            salvarArvoreHuffman(outputStream, raiz);
            System.out.println("Árvore de Huffman salva no arquivo.");

            //writeInt salva o número total de bits 
            outputStream.writeInt(bitsCompactados.length());
            System.out.println("Número total de bits compactados salvo: " + bitsCompactados.length());

            // Salvar os dados compactados como bytes
            byte[] bytesCompactados = converterBitsParaBytes(bitsCompactados.toString());
            System.out.println("Bytes compactados gerados: " + Arrays.toString(bytesCompactados));
            outputStream.write(bytesCompactados);
        }
    }

    private static void salvarArvoreHuffman(DataOutputStream outputStream, NoHuffman no) throws Exception {
        if (no.isFolha()) {
            //System.out.println("nó folha: " + no.getDado());
            outputStream.writeBoolean(true);
            outputStream.writeByte(no.getDado());
        } else {
            //System.out.println(" nó interno.");
            outputStream.writeBoolean(false);
            salvarArvoreHuffman(outputStream, no.getFilhoEsquerdo());
            salvarArvoreHuffman(outputStream, no.getFilhoDireito());
        }
    }

    private static byte[] converterBitsParaBytes(String bits) {
        int tamanho = (bits.length() + 7) / 8; // Número total de bytes
        byte[] bytes = new byte[tamanho];

        for (int i = 0; i < bits.length(); i++) {
            int byteIndex = i / 8;
            int bitIndex = i % 8;
            if (bits.charAt(i) == '1') {
                bytes[byteIndex] |= (1 << (7 - bitIndex));
            }
        }

        return bytes;
    }

    public static void descompactarArquivo(String caminhoArquivoCompactado, String caminhoArquivoDescompactado)
            throws Exception {
        // Ler o arquivo compactado
        byte[] dadosCompactados = lerArquivo(caminhoArquivoCompactado);
        System.out.println("Dados lidos do arquivo compactado: " + Arrays.toString(dadosCompactados));

        // Cria o DataInputStream para ler do arquivo compactado
        try (DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(dadosCompactados))) {
            // Reconstruir a árvore de Huffman
            NoHuffman raiz = reconstruirArvoreHuffman(inputStream);
            System.out.println("Árvore de Huffman reconstruída: " + raiz);

            // Ler o número total de bits compactados
            int numeroBits = inputStream.readInt();
            System.out.println("Número total de bits compactados: " + numeroBits);

            // Ler os bytes compactados
            byte[] bytesCompactados = new byte[(numeroBits + 7) / 8];
            inputStream.readFully(bytesCompactados);

            // Converter os bytes compactados para a sequência de bits
            String bitsCompactados = converterBytesParaBits(bytesCompactados);
            System.out.println("Bits lidos: " + bitsCompactados);

            // Descompactar os bits usando a árvore de Huffman
            byte[] dadosDescompactados = descompactarBits(bitsCompactados, raiz);

            // Salvar os dados descompactados no arquivo
            try (FileOutputStream outputStream = new FileOutputStream(caminhoArquivoDescompactado)) {
                outputStream.write(dadosDescompactados);
                System.out.println("Arquivo descompactado salvo.");
            }
        }
    }

    private static NoHuffman reconstruirArvoreHuffman(DataInputStream inputStream) throws Exception {
        boolean ehFolha = inputStream.readBoolean();
        if (ehFolha) {
            byte dado = inputStream.readByte();
            return new NoHuffman(dado, 0); 
        } else {
            NoHuffman filhoEsquerdo = reconstruirArvoreHuffman(inputStream);
            NoHuffman filhoDireito = reconstruirArvoreHuffman(inputStream);
            return new NoHuffman(filhoEsquerdo, filhoDireito);
        }
    }

    private static String converterBytesParaBits(byte[] bytes) {
        StringBuilder bits = new StringBuilder();

        for (byte b : bytes) {
            for (int i = 7; i >= 0; i--) {
                bits.append((b >> i) & 1);
            }
        }

        return bits.toString();
    }

    private static byte[] descompactarBits(String bitsCompactados, NoHuffman raiz) {
        StringBuilder resultado = new StringBuilder();
        NoHuffman noAtual = raiz;
        for (int i = 0; i < bitsCompactados.length(); i++) {
            char bit = bitsCompactados.charAt(i);

            // Desce pela árvore de Huffman de acordo com o bit
            if (bit == '0') {
                noAtual = noAtual.getFilhoEsquerdo();
            } else {
                noAtual = noAtual.getFilhoDireito();
            }
            // Se chegou em uma folha, adicione o byte correspondente ao resultado
            if (noAtual.isFolha()) {
                resultado.append((char) noAtual.getDado());
                noAtual = raiz; // Volta para a raiz da árvore para processar o próximo byte
            }
        }

        // Converte a String resultante em um array de bytes
        byte[] dadosDescompactados = new byte[resultado.length()];
        for (int i = 0; i < resultado.length(); i++) {
            dadosDescompactados[i] = (byte) resultado.charAt(i);
        }

        return dadosDescompactados;
    }

}
