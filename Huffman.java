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

        public No(int freq, No esq, No dir) {
            this.frequencia = freq;
            this.esquerdo = esq;
            this.direito = dir;
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
                raf.close(); 
            }
        }
        return sb.toString().trim(); 
    }

    // Método para calcular a frequência e inserir na fila de prioridade
  /*  public static FilaDePrioridade<No> calcularFrequencias(String caminhoArquivo) throws Exception {
    Map<Character, Integer> frequencias = new HashMap<>();
    try (RandomAccessFile arquivo = new RandomAccessFile(caminhoArquivo, "r")) {
        byte[] buffer = new byte[1024];
        int bytesLidos;

        // Lê o arquivo em blocos
        while ((bytesLidos = arquivo.read(buffer)) != -1) {
            for (int i = 0; i < bytesLidos; i++) {
                byte b = buffer[i];
                frequencias.put(b, frequencias.getOrDefault(b, 0) + 1);
            }
        }
    }

    // Cria a fila de prioridade com base nas frequências
    FilaDePrioridade<No> fila = new FilaDePrioridade<>(frequencias.size());
    for (Map.Entry<Character, Integer> entrada : frequencias.entrySet()) {
        No no = new No(entrada.getValue(), entrada.getKey());
        fila.guardeUmItem(no);
    }

    return fila;
}*/

    
    // Método para calcular a frequência dos caracteres no texto
   /* public static Map<Character, Integer> calcularFrequencias(String texto) {
        Map<Character, Integer> frequencias = new HashMap<>();
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            frequencias.put(c, frequencias.getOrDefault(c, 0) + 1);
        }
        return frequencias;
    }*/ 

}
