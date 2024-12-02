
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.RandomAccessFile;
import java.io.*;

public class Huffman {

    public static class NoHuffman implements Comparable<NoHuffman> {

        private byte dado; // O byte representado (usado nas folhas)
        private int frequencia; // A frequência do byte
        private NoHuffman filhoEsquerdo; // Filho esquerdo do nó
        private NoHuffman filhoDireito; // Filho direito do nó

        // Construtor para nó folha (com byte e frequência)
        public NoHuffman(byte dado, int frequencia) {
            this.dado = dado;
            this.frequencia = frequencia;
            this.filhoEsquerdo = null;
            this.filhoDireito = null;
        }

        // Construtor para nó interno (sem dado, com filhos esquerdo e direito)
        public NoHuffman(NoHuffman filhoEsquerdo, NoHuffman filhoDireito) {
            this.dado = '\0'; // Byte vazio para nós internos (usado como marcador)
            this.frequencia = filhoEsquerdo.frequencia + filhoDireito.frequencia; // Frequência combinada
            this.filhoEsquerdo = filhoEsquerdo;
            this.filhoDireito = filhoDireito;
        }

        // Getter para o dado (byte)
        public byte getDado() {
            return dado;
        }

        // Getter para a frequência
        public int getFrequencia() {
            return frequencia;
        }

        // Getter para o filho esquerdo
        public NoHuffman getFilhoEsquerdo() {
            return filhoEsquerdo;
        }

        // Getter para o filho direito
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

        // Método toString para visualização do nó
        @Override
        public String toString() {
            return "{dado=" + (dado == '\0' ? "interno" : dado)
                    + ", frequencia=" + frequencia + "}\n";
        }

        // Verifica se o nó é uma folha (se não tem filhos)
        public boolean isFolha() {
            return filhoEsquerdo == null && filhoDireito == null;
        }
    }

    // Ler arquivo
    public static byte[] lerArquivo(String caminho) throws Exception {
        try (RandomAccessFile arquivo = new RandomAccessFile(caminho, "r")) {
            byte[] dados = new byte[(int) arquivo.length()]; // Cria um array do tamanho do arquivo
            arquivo.readFully(dados); // Lê todo o conteúdo no array
            return dados;
        }
    }

    public static HashMap<Byte, Integer> calcularFrequencia(byte[] dados) throws Exception {
        // Cria o HashMap para armazenar a frequência dos bytes
        HashMap<Byte, Integer> frequencias = new HashMap<>(256, 0.75f, 0.5f);

        // Percorre os dados e calcula a frequência
        for (byte b : dados) {
            frequencias.put(b, frequencias.getValorPadrão(b, 0) + 1);
        }
        return frequencias;
    }

    // Método que cria a fila de prioridade com base nas frequências
    public static FilaDePrioridade<NoHuffman> criaFilaDePrioridade(HashMap<Byte, Integer> frequencias) throws Exception {
        // Criação da lista de nós de Huffman a partir das frequências usando ListaSimplesDesordenada
        ListaSimplesDesordenada<NoHuffman> nos = new ListaSimplesDesordenada<>();
        //System.out.println("ate aqui");
        // Preenchendo a lista com os nós de Huffman
        for (Byte dado : frequencias.keySet()) {
            //System.out.println("entrouNoFor");
            int frequencia = frequencias.get(dado);
            // Criação do nó para cada byte e frequência
            nos.guardeUmItemNoFinal(new NoHuffman(dado, frequencia));
            //System.out.println("Guardou na lista");
        }

        // Criando a fila de prioridade com base nos nós de Huffman
        FilaDePrioridade<NoHuffman> fila = new FilaDePrioridade<NoHuffman>();
        //System.out.println("fila criada");    
        // Ordena a lista de nós de Huffman conforme a frequência
        while (!nos.isVazia()) {
            NoHuffman no = nos.recupereItemDoInicio(); // Recupera o primeiro nó
            fila.guardeUmItem(no); // Adiciona o nó à fila de prioridade
            nos.removaItemIndicado(no); // Remove o nó da lista para evitar o loop infinito
            //System.out.println("GUARDOU NA FILA");
        }
        //System.out.println(fila);
        return fila;
    }

    public static NoHuffman construirArvoreDeHuffman(FilaDePrioridade<NoHuffman> filaDePrioridade) throws Exception {
        // Enquanto houver mais de um nó na fila de prioridade, combine os dois nós com menor frequência
        while (filaDePrioridade.getTamanho() > 1) {
            // Extrai os dois nós com menor frequência
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

    public static HashMap<Byte, String> gerarCodigos(NoHuffman raiz) throws Exception {
        HashMap<Byte, String> codigos = new HashMap<>(); // Mapa para armazenar os códigos
        gerarCodigosRecursivo(raiz, "", codigos);       // Chamada inicial para a recursão
        return codigos;                                 // Retorna o mapa gerado
    }

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

    public static void compactarArquivo(String caminhoArquivoOriginal, String caminhoArquivoCompactado) throws Exception {
        // Ler os dados do arquivo original
        byte[] dados = lerArquivo(caminhoArquivoOriginal);

        // Calcular a frequência dos bytes
        HashMap<Byte, Integer> frequencias = calcularFrequencia(dados);

        // Criar a fila de prioridade
        FilaDePrioridade<NoHuffman> filaDePrioridade = criaFilaDePrioridade(frequencias);

        // Construir a árvore de Huffman
        NoHuffman raiz = construirArvoreDeHuffman(filaDePrioridade);

        System.out.println("Raiz da arvore: " + raiz);

        // Gerar os códigos de Huffman
        HashMap<Byte, String> codigos = gerarCodigos(raiz);

        // Converter os dados do arquivo original para uma sequência de bits compactados
        StringBuilder bitsCompactados = new StringBuilder();
        for (byte b : dados) {
            System.out.println("dados do arquivo:" + b);
            bitsCompactados.append(codigos.get(b));
        }

        // Salvar a árvore de Huffman e os dados compactados no arquivo de saída
        try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(caminhoArquivoCompactado))) {

            System.out.println("Salvando arvore: " + outputStream);
            // Salvar a árvore de Huffman no início do arquivo
            salvarArvoreHuffman(outputStream, raiz);

            // Salvar o número total de bits compactados (para descompactação futura)
            outputStream.writeInt(bitsCompactados.length());

            // Salvar os dados compactados em forma de bytes
            byte[] bytesCompactados = converterBitsParaBytes(bitsCompactados.toString());
            System.out.println("Dados Compactados" + bytesCompactados);
            outputStream.write(bytesCompactados);
        }
    }

    private static void salvarArvoreHuffman(DataOutputStream outputStream, NoHuffman no) throws Exception {
        if (no.isFolha()) {
            outputStream.writeBoolean(true);  // Indica que é folha
            outputStream.writeByte(no.getDado());  // Salva o byte da folha
        } else {
            outputStream.writeBoolean(false); // Indica que não é folha
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

    public static void descompactarArquivo(String caminhoArquivoCompactado, String caminhoArquivoDescompactado) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivoCompactado))) {
            // Reconstruir a árvore de Huffman a partir do arquivo compactado
            NoHuffman raiz = reconstruirArvoreHuffman(reader);

            // Ler os dados compactados (bits)
            StringBuilder bitsCompactados = new StringBuilder();
            int caractere;
            while ((caractere = reader.read()) != -1) {
                bitsCompactados.append((char) caractere);
            }

            // Decodificar os bits usando a árvore de Huffman
            byte[] dadosOriginais = decodificarBits(bitsCompactados.toString(), raiz);

            // Escrever os dados decodificados no arquivo de saída
            try (FileOutputStream fos = new FileOutputStream(caminhoArquivoDescompactado)) {
                fos.write(dadosOriginais);
            }
        }
    }

// Método recursivo para reconstruir a árvore de Huffman
    private static NoHuffman reconstruirArvoreHuffman(BufferedReader reader) throws Exception {
        int tipoNo = reader.read();
        System.out.println("Lido: " + tipoNo); // Verifique o valor lido

        if (tipoNo == 49) { // ASCII de '1'
            byte dado = (byte) reader.read();
            System.out.println("Lido dado: " + dado); // Verifique o dado lido
            return new NoHuffman(dado, 0);
        } else if (tipoNo == 48) { // ASCII de '0'
            NoHuffman filhoEsquerdo = reconstruirArvoreHuffman(reader);
            NoHuffman filhoDireito = reconstruirArvoreHuffman(reader);
            return new NoHuffman(filhoEsquerdo, filhoDireito);
        } else {
            throw new Exception("Erro na reconstrução da árvore de Huffman. Valor inválido: " + tipoNo);
        }
    }

// Método para decodificar os bits compactados
    private static byte[] decodificarBits(String bitsCompactados, NoHuffman raiz) throws Exception {
        ListaSimplesDesordenada<Byte> dadosOriginais = new ListaSimplesDesordenada<>();
        NoHuffman atual = raiz;

        for (char bit : bitsCompactados.toCharArray()) {
            // Navega na árvore com base no bit atual
            if (bit == '0') {
                atual = atual.getFilhoEsquerdo();
            } else if (bit == '1') {
                atual = atual.getFilhoDireito();
            }

            // Se chegar em uma folha, recupera o dado
            if (atual.isFolha()) {
                dadosOriginais.guardeUmItemNoFinal(atual.getDado());
                atual = raiz; // Retorna para a raiz para continuar a decodificação
            }
        }

        // Converte a ListaSimplesDesordenada para um array de bytes
        byte[] resultado = new byte[dadosOriginais.getQuantidade()];
        int i = 0;

        // Itera pelos elementos da lista e os adiciona ao array
        while (!dadosOriginais.isVazia()) {
            resultado[i++] = dadosOriginais.recupereItemDoInicio();
            dadosOriginais.recupereItemDoInicio();
        }

        return resultado;
    }

}
