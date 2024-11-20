import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.NoSuchElementException;

public class Huffman {

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

    //calculando a frequencia
    public static HashMap<Character, Integer> calcularFrequencia(String texto) throws Exception {
        HashMap<Character, Integer> frequencias = new HashMap<>(10, 0.75f, 0.9f); // Cria o HashMap com capacidade inicial e fatores de desperdício
    
        // Itera sobre cada caractere do texto
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            
            try {
                // Tenta recuperar a frequência atual do caractere
                int freqAtual = frequencias.recupereUmItem(c);
                // Atualiza a frequência do caractere
                frequencias.guardeUmItem(c, freqAtual + 1);
            } catch (NoSuchElementException e) {
                // Se o caractere não existe, adiciona com frequência 1
                frequencias.guardeUmItem(c, 1);
            }
        }
    
        return frequencias; // Retorna o HashMap com as frequências
    }
    

    // Testando os métodos
  
}
