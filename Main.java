public class Main {
    public static void main(String[] args) {

        try {
            // caminho no mac
             String caminhoArquivoTxt1 = "/Users/u21446/Documents/teste1.txt";

            // caminho no windows
            //String caminhoArquivoTxt1 = "C:\\Users\\NatalyJessica\\Documents\\teste1.txt";
            // String caminhoArquivoTxt2 ="C:\\Users\\NatalyJessica\\Documents\\teste2.txt";
            //String caminhoArquivoPdf = "C:\\Users\\NatalyJessica\\Documents\\teste3.pdf";
            // String caminhoArquivoImg = "C:\\Users\\NatalyJessica\\Documents\\teste4.jpg";
            //String caminhoArquivoMp3 = "C:\\Users\\NatalyJessica\\Documents\\teste5.mp3";
            // String caminhoArquivoMp4 = "C:\\Users\\NatalyJessica\\Documents\\teste6.mp4";
            // String caminhoArquivoEXE =
            // "C:\\Users\\NatalyJessica\\Documents\\7z2408-x64.exe";

            // Etapa 1 ler arquivos
            String textoOriginal = Huffman.lerArquivo(caminhoArquivoTxt1);
            System.out.println("Conteudo do arquivo: " + textoOriginal);

            HashMap<Character, Integer> frequencias = Huffman.calcularFrequencia(textoOriginal);
            System.out.println(textoOriginal + "\n");

            // Etapa 3
            FilaDePrioridade<Huffman.NoHuffman> fila = new Huffman().criarFilaDePrioridade(frequencias);
            System.out.println(fila + "\n");

            // etapa 4
            Huffman.NoHuffman raiz = Huffman.construirArvore(fila);
            System.out.println(raiz + "\n");

            // Gerar os códigos
            HashMap<Character, String> codigo = Huffman.gerarCodigos(raiz);
            System.out.println(codigo + "\n");

            // Compactar o texto
            String textoCompactado = Huffman.compactar(textoOriginal, codigo);
   
            String caminhoCompactado = "/Users/u21446/Documents/arquivo.huffman";
            Huffman.salvarArquivoHuffman(caminhoCompactado, textoCompactado, raiz);

            System.out.println("Salvo");
         
            String textoDescompactado = Huffman.descompactar(textoCompactado, raiz);

            String caminhoDescompactado = "/Users/u21446/Documents/arquivo.txt";
            Huffman.salvarArquivoDescompactado(caminhoDescompactado, textoDescompactado);
           

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
