import java.io.IOException;
import java.io.RandomAccessFile;


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
    public static byte[] lerArquivo(String caminho) throws IOException {
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
    /*
     * public static void compactarArquivo(String caminhoArquivoOriginal, String
     * caminhoArquivoCompactado)
     * throws Exception {
     * byte[] dados = lerArquivo(caminhoArquivoOriginal);
     * 
     * // Calcular a frequência dos caracteres
     * HashMap<Character, Integer> frequencias = calcularFrequencia(dados);
     * 
     * // Criar a fila de prioridade
     * FilaDePrioridade<NoHuffman> filaDePrioridade =
     * criaFilaDePrioridade(frequencias);
     * 
     * // Construir a árvore de Huffman
     * NoHuffman raiz = construirArvoreDeHuffman(filaDePrioridade);
     * 
     * // Gerar os códigos de Huffman
     * HashMap<Character, String> codigos = gerarCodigos(raiz);
     * 
     * // Converter os dados para sequência de bits
     * StringBuilder bitsCompactados = new StringBuilder();
     * for (byte b : dados) {
     * char caractere = (char) b;
     * bitsCompactados.append(codigos.get(caractere));
     * }
     * 
     * try (BufferedWriter writer = new BufferedWriter(new
     * FileWriter(caminhoArquivoCompactado))) {
     * // Salvar a árvore de Huffman no início do arquivo (em formato binário ou
     * texto)
     * salvarArvoreHuffman(writer, raiz);
     * 
     * // Salvar os dados compactados
     * writer.write(bitsCompactados.toString());
     * }
     * }
     * 
     * // Método recursivo para salvar a árvore de Huffman no arquivo
     * private static void salvarArvoreHuffman(BufferedWriter writer, NoHuffman no)
     * throws IOException {
     * if (no.isFolha()) {
     * // Se for uma folha, salvar o caractere e indicar que é uma folha
     * writer.write("1");
     * writer.write(no.getCaractere());
     * } else {
     * // Se for um nó interno, salvar a indicação de que não é folha
     * writer.write("0");
     * // Recursivamente salvar os filhos esquerdo e direito
     * salvarArvoreHuffman(writer, no.getFilhoEsquerdo());
     * salvarArvoreHuffman(writer, no.getFilhoDireito());
     * }
     * }
     */

    /*
     * public static void descompactarArquivo(String caminhoArquivoCompactado,
     * String caminhoArquivoDescompactado)
     * throws Exception {
     * // Ler os dados compactados e a árvore de Huffman
     * StringBuilder bitsCompactados = new StringBuilder();
     * NoHuffman raiz = null;
     * 
     * try (BufferedReader reader = new BufferedReader(new
     * FileReader(caminhoArquivoCompactado))) {
     * // Ler e reconstruir a árvore de Huffman
     * raiz = reconstruirArvoreHuffman(reader);
     * 
     * // Ler os bits compactados
     * String linha;
     * while ((linha = reader.readLine()) != null) {
     * bitsCompactados.append(linha);
     * }
     * }
     * 
     * // Descompactar os dados usando a árvore de Huffman
     * StringBuilder dadosDescompactados = new StringBuilder();
     * NoHuffman noAtual = raiz;
     * for (int i = 0; i < bitsCompactados.length(); i++) {
     * char bit = bitsCompactados.charAt(i);
     * // Se o bit for '0', vá para o filho esquerdo, se for '1', vá para o filho
     * // direito
     * if (bit == '0') {
     * noAtual = noAtual.getFilhoEsquerdo();
     * } else if (bit == '1') {
     * noAtual = noAtual.getFilhoDireito();
     * }
     * 
     * // Se chegarmos a uma folha, significa que encontramos um caractere
     * if (noAtual.isFolha()) {
     * dadosDescompactados.append(noAtual.getCaractere());
     * noAtual = raiz; // Voltar para a raiz para processar o próximo caractere
     * }
     * }
     * 
     * // Salvar os dados descompactados no arquivo
     * try (BufferedWriter writer = new BufferedWriter(new
     * FileWriter(caminhoArquivoDescompactado))) {
     * writer.write(dadosDescompactados.toString());
     * }
     * }
     */

    /*
     * private static NoHuffman reconstruirArvoreHuffman(BufferedReader reader)
     * throws IOException {
     * int bit = reader.read();
     * if (bit == '1') {
     * // Se for folha, ler o caractere e retornar o nó folha
     * char caractere = (char) reader.read();
     * return new NoHuffman(caractere, 0); // Frequência será definida
     * posteriormente, se necessário
     * } else {
     * // Se for nó interno, criar um nó e reconstruir os filhos recursivamente
     * NoHuffman filhoEsquerdo = reconstruirArvoreHuffman(reader);
     * NoHuffman filhoDireito = reconstruirArvoreHuffman(reader);
     * return new NoHuffman(filhoEsquerdo, filhoDireito);
     * }
     * }
     */

}
