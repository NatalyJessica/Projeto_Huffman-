import java.io.*;
import java.util.*;

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

    // Método para construir a árvore de Huffman
// Método para construir a árvore de Huffman
public static No construirArvore(Map<Character, Integer> frequencias) throws Exception {
    FilaDePrioridade<No> fila = new FilaDePrioridade<>(frequencias.size());

    System.err.println("Adicionando...");
    // Adicionar todos os caracteres à fila de prioridade
    for (Map.Entry<Character, Integer> entrada : frequencias.entrySet()) {
        fila.guardeUmItem(new No(entrada.getValue(), entrada.getKey()));
    }
    System.err.println("Adicionado...");

    System.err.println("Construindo árvore");
    // Construção da árvore de Huffman
    while (fila.getSize() > 1) {  // Altere esta condição
        No esquerda = fila.recupereUmItem();
        No direita = fila.recupereUmItem();
        No novoNo = new No(esquerda.frequencia + direita.frequencia, esquerda, direita);
        fila.guardeUmItem(novoNo);
    }
    System.err.println("Árvore construída");

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
        BufferedReader reader = new BufferedReader(new FileReader(caminho));
        StringBuilder sb = new StringBuilder();
        String linha;

        while ((linha = reader.readLine()) != null) {
            sb.append(linha).append("\n");
        }

        reader.close();
        return sb.toString();
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


}
