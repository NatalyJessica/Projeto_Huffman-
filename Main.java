import java.util.*;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Menu principal
            while (true) {
                System.out.println("Escolha uma opção:");
                System.out.println("1. Compactar arquivo");
                System.out.println("2. Descompactar arquivo");
                System.out.println("3. Sair");
                System.out.print("Opção: ");
                
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Para consumir a linha em branco

                if (opcao == 1) {
                    // Compactar arquivo
                    System.out.print("Informe o caminho do arquivo de texto para compactar: ");
                    String caminhoArquivoTxt = scanner.nextLine();
                    System.out.print("Informe o caminho onde o arquivo compactado será salvo (ex: 'caminho'\\arquivo.huffman): ");
                    String caminhoArquivoCompactado = scanner.nextLine();
                    
                    // Etapa 1: Ler o arquivo
                    String textoOriginal = Huffman.lerArquivo(caminhoArquivoTxt);
                    System.out.println("Texto lido: " + textoOriginal);

                    // Etapa 2: Calcular frequências e construir a árvore de Huffman
                    Map<Character, Integer> frequencias = Huffman.calcularFrequencias(textoOriginal);
                    Huffman.No arvore = Huffman.construirArvore(frequencias);
                    System.out.println("Árvore de Huffman construída: " + arvore);

                    // Etapa 3: Gerar códigos de Huffman
                    Map<Character, String> codigos = new HashMap<>();
                    Huffman.gerarCodigos(arvore, "", codigos);
                    System.out.println("Códigos gerados: " + codigos);

                    // Etapa 4: Compactar o texto
                    String textoCompactado = Huffman.compactar(textoOriginal, codigos);
                    System.out.println("Texto compactado: " + textoCompactado);

                    // Etapa 5: Salvar o arquivo compactado
                    Huffman.salvarArquivoHuffman(caminhoArquivoCompactado, textoCompactado, arvore);
                    System.out.println("Arquivo compactado salvo em: " + caminhoArquivoCompactado);
                    
                } else if (opcao == 2) {
                    System.out.println("ainda não descompacta");
                    /*// Descompactar arquivo
                    System.out.print("Informe o caminho do arquivo compactado para descompactar (ex: arquivo.huffman): ");
                    String caminhoArquivoCompactado = scanner.nextLine();
                    
                    // Etapa 6: Verificar se o arquivo é um arquivo compactado de Huffman
                    if (new File(caminhoArquivoCompactado).exists()) {
                        // Se o arquivo compactado existir, prosseguir com a descompactação
                        System.out.print("Informe o caminho onde o arquivo descompactado será salvo: ");
                        String caminhoArquivoDescompactado = scanner.nextLine();
                        
                        // Ler o arquivo compactado e obter a árvore de Huffman
                        String textoCompactado = Huffman.lerArquivo(caminhoArquivoCompactado);
                        Huffman.No arvore = Huffman.lerArvoreHuffman(caminhoArquivoCompactado);
                        
                        // Descompactar o texto
                        String textoDescompactado = Huffman.descompactar(textoCompactado, arvore);
                        System.out.println("Texto descompactado: " + textoDescompactado);
                        
                        // Salvar o arquivo descompactado
                        Huffman.salvarArquivoTexto(caminhoArquivoDescompactado, textoDescompactado);
                        System.out.println("Arquivo descompactado salvo em: " + caminhoArquivoDescompactado);
                    } else {
                        System.out.println("Arquivo compactado não encontrado ou não válido.");
                    }
                    */
                } else if (opcao == 3) {
                    // Sair do programa
                    System.out.println("Saindo...");
                    break;
                } else {
                    System.out.println("Opção inválida! Tente novamente.");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}


/* 
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
       
            // Etapa 2
            Huffman.No arvore = Huffman.construirArvore(frequencias);
            System.out.println("Arvore construida " + arvore);
            
            // Etapa 3
            Map<Character, String> codigos = new HashMap<>();
            Huffman.gerarCodigos(arvore, "", codigos);
            System.out.println("Códigos gerados: " + codigos);

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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/