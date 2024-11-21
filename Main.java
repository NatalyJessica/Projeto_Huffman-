public class Main {
    public static void main(String[] args) {

        try {
            // caminho no mac
            // String caminhoArquivoTxt1 = "/Users/u21446/Documents/teste1.txt";

            // caminho no windows
            String caminhoArquivoTxt1 = "C:\\Users\\NatalyJessica\\Documents\\teste1.txt";
            String caminhoArquivoTxt2 ="C:\\Users\\NatalyJessica\\Documents\\teste2.txt";
            String caminhoArquivoPdf = "C:\\Users\\NatalyJessica\\Documents\\teste3.pdf";
            String caminhoArquivoImg = "C:\\Users\\NatalyJessica\\Documents\\teste4.jpg";
            String caminhoArquivoMp3 = "C:\\Users\\NatalyJessica\\Documents\\teste5.mp3";
            String caminhoArquivoMp4 = "C:\\Users\\NatalyJessica\\Documents\\teste6.mp4";
            String caminhoArquivoEXE = "C:\\Users\\NatalyJessica\\Documents\\7z2408-x64.exe";

            // Etapa 1 ler arquivos
            String textoOriginal = Huffman.lerArquivo(caminhoArquivoTxt1);
            System.err.println("Conteudo do arquivo: " + textoOriginal);
            // Etapa 2 calcular frequencia
            HashMap<Character, Integer> frequencias = Huffman.calcularFrequencia(textoOriginal);
            // Exibe as frequências dos caracteres
            System.out.println("\n\nfrequencia");
            for (Character c : frequencias.getChaves()) {
                System.out.println("Caractere: " + c + " | Frequência: " + frequencias.recupereUmItem(c));
            }
            //Etapa 3
            FilaDePrioridade<Huffman.NoHuffman> fila = new Huffman().criarFilaDePrioridade(frequencias);
            System.out.println("\n\nFila de Prioridade:");
            while (!fila.isVazia()) {
                Huffman.NoHuffman no = fila.recupereUmItem();
                System.out.println("Caractere: " + no.getCaractere() + " | Frequência: " + no.getFrequencia());
            }
           

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
