

public class Main {
    public static void main(String[] args) {

        try {
            // caminho no mac
            //String caminhoArquivoTxt1 = "/Users/u21446/Documents/teste1.txt";
            // String caminhoArquivoTxt2 =/Users/u21446/Documents/teste2.txt";
            //String caminhoArquivoPdf = "/Users/u21446/Documents/teste3.pdf";
            // String caminhoArquivoImg = "/Users/u21446/Documents/teste4.jpg";
            //String caminhoArquivoMp3 = "/Users/u21446/Documents/teste5.mp3";
            //String caminhoArquivoMp4 = "/Users/u21446/Documents/teste6.mp4";
            // String caminhoArquivoEXE
            // ="C:\\Users\\NatalyJessica\\Documents\\7z2408-x64.exe";

            // caminho no windows
            //String caminhoArquivoTxt1 = "C:\\Users\\NatalyJessica\\Documents\\Projeto_Huffman-\\teste1.txt";
            //String caminhoArquivoTxt2 ="C:\\Users\\NatalyJessica\\Documents\\Projeto_Huffman-\\teste2.txt";
            //String caminhoArquivoPdf = "C:\\Users\\NatalyJessica\\Documents\\Projeto_Huffman-\\teste3.pdf";
            //String caminhoArquivoImg = "C:\\Users\\NatalyJessica\\Documents\\Projeto_Huffman-\\teste4.jpg";
            // String caminhoArquivoMp3 = "C:\\Users\\NatalyJessica\\Documents\\teste5.mp3";
             //String caminhoArquivoMp4 = "C:\\Users\\NatalyJessica\\Documents\\Projeto_Huffman-\\teste6.mp4";
             String caminhoArquivoEXE ="C:\\Users\\NatalyJessica\\Documents\\Projeto_Huffman-\\7z2408-x64.exe";;
           // String img = "C:\\Users\\NatalyJessica\\Documents\\Projeto_Huffman-\\imgteste.jpg";

           //oque realmente vai ser rodado
            String caminhoArquivoCompactado = "C:\\Users\\NatalyJessica\\Documents\\Projeto_Huffman-\\arquivoteste.huffman";
            String caminhoArquivoDescompactado = "C:\\Users\\NatalyJessica\\Documents\\Projeto_Huffman-\\arquivoteste.exe";
            //Huffman.compactarArquivo(caminhoArquivoEXE, caminhoArquivoCompactado);
            System.out.println("Caminho do arquivo compactado: " + caminhoArquivoCompactado);

            Huffman.descompactarArquivo(caminhoArquivoCompactado, caminhoArquivoDescompactado);


        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
