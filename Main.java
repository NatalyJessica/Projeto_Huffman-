
 
public class Main {
    public static void main(String[] args) {
        try {
            //caminho no mac
            String caminhoArquivoTxt1 = "/Users/u21446/Documents/teste1.txt";

            //caminho no windows
            //String caminhoArquivoTxt1 = "C:\\Users\\NatalyJessica\\Documents\\teste1.txt";
            //String caminhoArquivoTxt2 = "C:\\Users\\NatalyJessica\\Documents\\teste2.txt";
            //String caminhoArquivoPdf = "C:\\Users\\NatalyJessica\\Documents\\teste3.pdf";
            //String caminhoArquivoImg = "C:\\Users\\NatalyJessica\\Documents\\teste4.jpg";

            String textoOriginal = Huffman.lerArquivo(caminhoArquivoTxt1);
            System.err.println("Conteudo do arquivo: "+ textoOriginal);

          // Etapa 1
         /* FilaDePrioridade<Huffman.No> fila = Huffman.calcularFrequencias(caminhoArquivoTxt1);

          while (!fila.isVazia()) {
              Huffman.No no = fila.recupereUmItem();
              System.out.println(no);
          }
              Map<Character, Integer> frequencias = Huffman.calcularFrequencias(textoOriginal);
            System.out.println("Frequencia calculada" + frequencias);*/ 


        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
