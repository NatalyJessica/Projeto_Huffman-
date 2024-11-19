import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            String caminhoArquivoTxt1 = "C:\\Users\\NatalyJessica\\Documents\\teste1.txt";
            String caminhoArquivoTxt2 = "C:\\Users\\NatalyJessica\\Documents\\teste2.txt";
            String caminhoArquivoPdf = "C:\\Users\\NatalyJessica\\Documents\\teste3.pdf";
            String caminhoArquivoImg = "C:\\Users\\NatalyJessica\\Documents\\teste4.jpg";

            String textoOriginal = Huffman.lerArquivo(caminhoArquivoTxt1);
            System.err.println(textoOriginal);
            String caminhoArquivoCompactado = "C:\\Users\\NatalyJessica\\Documents\\compactado.huffman";

            // Etapa 1
            Map<Character, Integer> frequencias = Huffman.calcularFrequencias(textoOriginal);
            System.out.println("Frequencia calculada" + frequencias);
            System.out.println(Huffman.construirArvore(frequencias));
          /*
            // Etapa 2
            Huffman.No arvore = Huffman.construirArvore(frequencias);
            System.out.println("Arvore construida " + arvore);
            
            // Etapa 3
            Map<Character, String> codigos = new HashMap<>();
            Huffman.gerarCodigos(arvore, "", codigos);
            System.out.println("codigo gerado");

            // Etapa 4
            String textoCompactado = Huffman.compactar(textoOriginal, codigos);
            System.out.println("Texto compactado: " + textoCompactado);
              
            // Etapa 5
            Huffman.salvarArquivoHuffman(caminhoArquivoCompactado, textoCompactado, arvore);
            System.out.println("Arquivo compactado salvo em: " + caminhoArquivoCompactado);

            // Etapa 6
            String textoDescompactado = Huffman.descompactar(textoCompactado, arvore);
            System.out.println("Texto descompactado: " + textoDescompactado);

            // Verifique se o texto descompactado é igual ao original
            if (textoOriginal.equals(textoDescompactado)) {
                System.out.println("A compactação e descompactação foram bem-sucedidas!");
            } else {
                System.out.println("Houve um erro na compactação ou descompactação.");
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
