

public class Main {
    public static void main(String[] args) {

        try {
            // caminho no mac
             String caminhoArquivoTxt1 = "/Users/u21446/Documents/teste1.txt";
            // String caminhoArquivoTxt2 =/Users/u21446/Documents/teste2.txt";
             String caminhoArquivoPdf = "/Users/u21446/Documents/teste3.pdf";
            // String caminhoArquivoImg = "/Users/u21446/Documents/teste4.jpg";
            String caminhoArquivoMp3 = "/Users/u21446/Documents/teste5.mp3";
            String caminhoArquivoMp4 = "/Users/u21446/Documents/teste6.mp4";
            // String caminhoArquivoEXE
            // ="C:\\Users\\NatalyJessica\\Documents\\7z2408-x64.exe";

            // caminho no windows
            //String caminhoArquivoTxt1 = "C:\\Users\\NatalyJessica\\Documents\\Projeto_Huffman-\\teste1.txt";
            //String caminhoArquivoTxt2 ="C:\\Users\\NatalyJessica\\Documents\\Projeto_Huffman-\\teste2.txt";
            //String caminhoArquivoPdf = "C:\\Users\\NatalyJessica\\Documents\\Projeto_Huffman-\\teste3.pdf";
            //String caminhoArquivoImg = "C:\\Users\\NatalyJessica\\Documents\\Projeto_Huffman-\\teste4.jpg";
            // String caminhoArquivoMp3 = "C:\\Users\\NatalyJessica\\Documents\\teste5.mp3";
            // String caminhoArquivoMp4 = "C:\\Users\\NatalyJessica\\Documents\\teste6.mp4";
            // String caminhoArquivoEXE ="C:\\Users\\NatalyJessica\\Documents\\7z2408-x64.exe";;
           // String img = "C:\\Users\\NatalyJessica\\Documents\\Projeto_Huffman-\\imgteste.jpg";

           //oque realmente vai ser rodado
            String caminhoArquivoCompactado = "/Users/u21446/Documents/Projeto_Huffman-/arquivoteste.huffman";
            String caminhoArquivoDescompactado = "/Users/u21446/Documents/Projeto_Huffman-/arquivoteste.txt";
            Huffman.compactarArquivo(caminhoArquivoTxt1, caminhoArquivoCompactado);
            //Huffman.descompactarArquivo(caminhoArquivoCompactado, caminhoArquivoDescompactado);

        
           //TESTEES
           //byte [] dados = Huffman.lerArquivo(caminhoArquivoTxt1);
           //System.out.println("dados: " + Arrays.toString(dados));
           //HashMap<Byte, Integer> frequencias = Huffman.calcularFrequencia(dados);
           //System.out.println(frequencias);
           //FilaDePrioridade<Huffman.NoHuffman> fila = Huffman.criaFilaDePrioridade(frequencias);
           //System.out.println(fila);
           //Huffman.NoHuffman raiz = Huffman.construirArvoreDeHuffman(fila);
           //System.out.println("\n" + raiz);
           //HashMap<Byte, String> codigos = Huffman.gerarCodigos(raiz);
           //System.out.println(codigos);

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
