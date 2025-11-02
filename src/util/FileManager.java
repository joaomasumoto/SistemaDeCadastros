package util;

import entities.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileManager {
    private static final String DIRECTORY_PATH = "C:\\Users\\jgabr\\OneDrive\\Sticky Notes 8\\Imagens\\Documentos\\T.i\\projeto sistema de cadastro\\";
    private static final String DATA_DIR = DIRECTORY_PATH + "data" + File.separator;
    private static final String SEQUENCE_FILE = DATA_DIR + "sequence.txt";
    private static final String FORMS_FILE = DIRECTORY_PATH + "formulario.txt";

    public static String getUsersDirectory() {
        return DIRECTORY_PATH;
    }

    static {
        ensureDirectoryExists(DATA_DIR);
        ensureDirectoryExists(DIRECTORY_PATH);
    }

    private static void ensureDirectoryExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

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

    public static List<File> listUserFiles() {
        List<File> userList = new ArrayList<>();
        File pasta = new File(DIRECTORY_PATH);
        File[] users = pasta.listFiles();

        if (users != null) {
            for (File f : users) {
                String name = f.getName().toLowerCase();
                if (name.equals("formulario.txt") || name.endsWith("sequence.txt")) {
                    continue;
                }

                if (f.isFile()) userList.add(f);
            }
        }

        userList.sort(Comparator.comparingInt(f -> {
            String fileName = f.getName().split("-", 2)[0];
            try {
                return Integer.parseInt(fileName);
            } catch (NumberFormatException e) {
                return Integer.MAX_VALUE;
            }

        }));

        return userList;
    }

    private static int getQuestionCount() {
        int contador = 0;
        try {
            List<String> allQuestions = Files.readAllLines(Paths.get(FORMS_FILE));
            contador = allQuestions.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contador;
    }

    public static void addQuestion(String question) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FORMS_FILE, true))) {
            bw.newLine();
            bw.write((getQuestionCount() + 1) + " - " + Validator.formatQuestion(question));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List listQuestions() {
        List<String> questions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FORMS_FILE))) {
            String line;
            int index = 1;

            System.out.println("Perguntas adicionais cadastradas:\n");

            while ((line = reader.readLine()) != null) {
                if (index > 4) {
                    questions.add(line);
                    System.out.println(line);
                }
                index++;
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o formulário: " + e.getMessage());
        }


        return questions;
    }

    public static void deleteQuestion(int questionNumber) {
        File inputFile = new File(FORMS_FILE);
        File tempFile = new File(FORMS_FILE + "temp");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))

        ) {

            String line;
            int index = 1;

            while ((line = reader.readLine()) != null) {
                if (index <= 4) {
                    writer.write(line);
                    writer.newLine();
                } else {

                    String[] parts = line.split("-", 2);
                    if (parts.length < 2) continue; //ignora se a linha estiver quebrada

                    int currentNumber = Integer.parseInt(parts[0].trim());

                    if (currentNumber == questionNumber) {
                        index++;
                        continue;
                    }

                    //Atualiza o numero
                    if (currentNumber > questionNumber) {
                        currentNumber--;
                    }

                    //Reescreve no formato "n - texto"
                    writer.write(currentNumber + " -" + parts[1]);
                    writer.newLine();
                }
                index++;
            }

        } catch (IOException e) {
            System.err.println("Erro ao manipular o arquivo: " + e.getMessage());
            return;
        }

        //Substitui o original pelo novo
        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            System.err.println("Não foi possível deletar o arquivo original!");
        } else {
            System.out.println("Pergunta " + questionNumber + " excluída com sucesso!");
        }
    }

    public static User readUserFromFile(File file) {
        String name = null;
        String email = null;
        int age = 0;
        double height = 0.0;
        Map<String, String> extra = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.toLowerCase().startsWith("nome:")) {
                    name = line.substring(5).trim();
                } else if (line.toLowerCase().startsWith("email:")) {
                    email = line.substring(6).trim();
                } else if (line.toLowerCase().startsWith("idade:")) {
                    String num = line.replaceAll("[^0-9]", ""); //substitui "anos"
                    age = Integer.parseInt(num);
                } else if (line.toLowerCase().startsWith("altura:")) {
                    String num = line.replaceAll("[^0-9.,]", "").replace(",", "."); //substitui "m" do final da linha
                    height = Double.parseDouble(num);
                } else if (!line.isBlank()) {
                    // tudo que sobrar vira campo extra (Map)
                    int i = line.indexOf('?');
                    if (i != -1 && i < line.length() - 1) {
                        String question = line.substring(0, i + 1).trim();
                        String answer = line.substring(i + 1).trim();
                        extra.put(question, answer);
                    } else {
                        extra.put(line, ""); // caso raro: pergunta sem resposta
                    }
                }
            }


        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + file.getName());
        }

        //Criar o objeto User
        return new User(name, email, age, height, extra);
    }

    public static void searchUsers(String key) {
        List<File> users = listUserFiles();
        boolean found = false;
        String termo = key.toLowerCase().trim();

        System.out.println("\n--- Resultados da busca por: \"" + key + "\" ---");

        for (File f : users) {
            User u = readUserFromFile(f);

            if (u == null) continue;
            String nome = u.getName() != null ? u.getName().toLowerCase() : "";
            String email = u.getEmail() != null ? u.getEmail().toLowerCase() : "";
            String idade = String.valueOf(u.getAge());

            if (nome.contains(termo) || email.contains(termo) || idade.contains(termo)) {
                System.out.println("\nArquivo: " + f.getName());
                System.out.println("------------------------------");
                System.out.println(u); // toString() do User
                found = true;
            }
        }

        if (!found) {
            System.out.println("Nenhum usuário encontrado com o termo: " + key);
        }

        System.out.println("\n----------------------------------------\n");
    }

}
