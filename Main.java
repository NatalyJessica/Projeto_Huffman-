import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Caminho do arquivo de entrada
            String caminhoArquivo = "C:\\Users\\NatalyJessica\\Documents\\teste4.jpg"; // Substitua pelo caminho do seu arquivo
            String textoOriginal = Huffman.lerArquivo(caminhoArquivo);
            
            // Etapa 1: Calcular a frequência dos caracteres
            Map<Character, Integer> frequencias = Huffman.calcularFrequencias(textoOriginal);
            System.out.println("Frequencia calculada");
            
            // Etapa 2: Construir a árvore de Huffman
            Huffman.No raiz = Huffman.construirArvore(frequencias);
            System.out.println("Arvore construida");
            
            // Etapa 3: Gerar os códigos de Huffman
            Map<Character, String> codigos = new HashMap<>();
            Huffman.gerarCodigos(raiz, "", codigos);
            System.out.println("codigo gerado");
            
            // Etapa 4: Compactar o texto
            String textoCompactado = Huffman.compactar(textoOriginal, codigos);
            System.out.println("Texto compactado: " );
            
            // Etapa 5: Descompactar o texto
            String textoDescompactado = Huffman.descompactar(textoCompactado, raiz);
            System.out.println("Texto descompactado: " );
            
            // Verifique se o texto descompactado é igual ao original
            if (textoOriginal.equals(textoDescompactado)) {
                System.out.println("A compactação e descompactação foram bem-sucedidas!");
            } else {
                System.out.println("Houve um erro na compactação ou descompactação.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
