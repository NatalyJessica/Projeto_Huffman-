import java.util.*;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Huffman {

    // Nodo da árvore de Huffman
    public static class No implements Comparable<No> {
        int frequencia;
        char caractere;
        No esquerdo, direito;

        public No(int frequencia, char caractere) {
            this.frequencia = frequencia;
            this.caractere = caractere;
            esquerdo = direito = null;
        }

        public No(int frequencia, No esquerdo, No direito) {
            this.frequencia = frequencia;
            this.esquerdo = esquerdo;
            this.direito = direito;
        }

        // Implementando o método compareTo para a interface Comparable
        @Override
        public int compareTo(No outro) {
            return Integer.compare(this.frequencia, outro.frequencia);
        }

        @Override
        public String toString() {
            return "No{caractere='" + caractere + "', frequencia=" + frequencia + "}";
        }

    }
     // Método para calcular a frequência dos caracteres no texto
     public static Map<Character, Integer> calcularFrequencias(String texto) {
        Map<Character, Integer> frequencias = new HashMap<>();
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            frequencias.put(c, frequencias.getOrDefault(c, 0) + 1);
        }
        return frequencias;
    }

    // Método para construir a árvore de Huffman
    public static No construirArvore(Map<Character, Integer> frequencias) throws Exception {
        FilaDePrioridade<No> fila = new FilaDePrioridade<>(frequencias.size());

        // Adicionar todos os caracteres à fila de prioridade
        for (Map.Entry<Character, Integer> entrada : frequencias.entrySet()) {
            fila.guardeUmItem(new No(entrada.getValue(), entrada.getKey()));
        }
        // Construção da árvore de Huffman
        while (fila.getSize() > 1) { // Altere esta condição
            No esquerda = fila.recupereUmItem();
            No direita = fila.recupereUmItem();
            No novoNo = new No(esquerda.frequencia + direita.frequencia, esquerda, direita);
            fila.guardeUmItem(novoNo);
        }
      

        // Retorna o nó raiz da árvore
        return fila.recupereUmItem();
    }

    

    // Método para gerar códigos de Huffman
    public static void gerarCodigos(No raiz, String prefixo, Map<Character, String> codigos) {
        if (raiz == null) {
            return;
        }

        if (raiz.esquerdo == null && raiz.direito == null) {
            codigos.put(raiz.caractere, prefixo);
        }

        gerarCodigos(raiz.esquerdo, prefixo + "0", codigos);
        gerarCodigos(raiz.direito, prefixo + "1", codigos);
    }

    // Compactação (transformar texto em binário usando os códigos de Huffman)
    public static String compactar(String texto, Map<Character, String> codigos) {
        StringBuilder compactado = new StringBuilder();

        for (int i = 0; i < texto.length(); i++) {
            compactado.append(codigos.get(texto.charAt(i)));
        }

        return compactado.toString();
    }

    public static void salvarArquivoHuffman(String caminho, String textoCompactado, No raiz) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(caminho, "rw")) {
            // Salvar a estrutura da árvore como uma string (pré-ordem)
            StringBuilder estruturaArvore = new StringBuilder();
            serializarArvore(raiz, estruturaArvore);
    
            // Escrever no arquivo: árvore e texto compactado
            raf.writeBytes(estruturaArvore.toString() + "\n");
            raf.writeBytes(textoCompactado);
        }
    }
    
    private static void serializarArvore(No no, StringBuilder sb) {
        if (no == null) {
            sb.append("null,");
            return;
        }
    
        if (no.esquerdo == null && no.direito == null) {
            sb.append("[").append(no.caractere).append("],");
        } else {
            sb.append("(),");
        }
    
        serializarArvore(no.esquerdo, sb);
        serializarArvore(no.direito, sb);
    }
    

    // Descompactação (transformar o binário de volta para o texto original)
    public static String descompactar(String binario, No raiz) {
        StringBuilder texto = new StringBuilder();
        No noAtual = raiz;

        for (int i = 0; i < binario.length(); i++) {
            noAtual = binario.charAt(i) == '0' ? noAtual.esquerdo : noAtual.direito;

            // Se chegarmos a uma folha, adicionamos o caractere ao texto
            if (noAtual.esquerdo == null && noAtual.direito == null) {
                texto.append(noAtual.caractere);
                noAtual = raiz;
            }
        }

        return texto.toString();
    }

     // Método para ler o conteúdo de um arquivo e retorná-lo como uma string
     public static String lerArquivo(String caminho) throws IOException {
        RandomAccessFile raf = null;
        StringBuilder sb = new StringBuilder();
        String linha;
    
        try {
            raf = new RandomAccessFile(caminho, "r");  
            while ((linha = raf.readLine()) != null) {
                sb.append(linha).append("\n");
            }
        } finally {
            if (raf != null) {
                raf.close();  // Garante que o arquivo será fechado
            }
        }
    
        return sb.toString().trim(); // Remove espaços e quebras de linha extras
    }
    
}
