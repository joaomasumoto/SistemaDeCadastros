package util;
import entities.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String DIRECTORY_PATH = "C:\\Users\\jgabr\\OneDrive\\Sticky Notes 8\\Imagens\\Documentos\\T.i\\projeto sistema de cadastro\\";
    private static final String SEQUENCE_FILE = DIRECTORY_PATH + "sequence.txt";

    public static void saveUserToFile(User user, int sequence) {
        // Criar o nome do arquivo no formato "1-NOME.TXT"
        String fileName = String.format("%d-%s.TXT", sequence, user.getName().toUpperCase().replace(" ", ""));
        String filePath = DIRECTORY_PATH + fileName;

        // Criar diretório se não existir
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs(); // Cria a pasta e subpastas, se necessário
        }

        // Criar e escrever dados do usuário
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(user.toString());
            System.out.println("Usuário salvo no arquivo: " + fileName);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o usuário no arquivo: " + e.getMessage());
        }

        // Atualiza o número sequencial para o próximo cadastro
        updateSequence(sequence + 1);
    }

    public static List<String> readFileLines(String filePath) {
        List<String> linhas = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String linha;
            while ((linha = bufferedReader.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        return linhas;
    }

    public static void readUserFromFile(int sequence, String userName) {
        // Gerar o nome do arquivo baseado no usuário e número sequencial
        String fileName = String.format("%d-%s.TXT", sequence, userName.toUpperCase().replace(" ", ""));
        String filePath = DIRECTORY_PATH + fileName;

        // Chama o metodo genérico para ler o arquivo
        List<String> linhas = readFileLines(filePath);

        // Se o arquivo estiver vazio ou não for encontrado, exibe uma mensagem
        if (linhas.isEmpty()) {
            System.out.println("Erro: O arquivo " + fileName + " não foi encontrado ou está vazio.");
            return;
        }

        // Exibe os dados do usuário lido do arquivo
        System.out.println("\n📄 Dados do usuário no arquivo " + fileName + ":");
        for (String linha : linhas) {
            System.out.println(linha);
        }
    }

    public static int getSequence() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SEQUENCE_FILE))) {
            String line = reader.readLine();
            return (line != null && !line.isBlank()) ? Integer.parseInt(line) : 1;
        } catch (IOException | NumberFormatException e) {
            return 1; // Se não existir ou houver erro, começa com 1
        }
    }


    private static void updateSequence(int newSequence) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SEQUENCE_FILE, false))) {
            writer.write(String.valueOf(newSequence));
            writer.flush(); // Força a gravação no disco antes de fechar
        } catch (IOException e) {
            System.out.println("Erro ao atualizar o número sequencial: " + e.getMessage());
        }
    }


}
