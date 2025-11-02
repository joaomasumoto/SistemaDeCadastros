package services;

import entities.User;
import util.FileManager;
import util.Validator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static util.Validator.ensureEmailNotUsed;
import static util.Validator.isQuestionValid;

public class UserService {
    private static final Scanner sc = new Scanner(System.in);

    public static void cadastrarUsuario() {

        String path = "C:\\Users\\jgabr\\OneDrive\\Sticky Notes 8\\Imagens\\Documentos\\T.i\\projeto sistema de cadastro\\formulario.txt";
        List<String> perguntas = FileManager.readFileLines(path);

        String usersDir = FileManager.getUsersDirectory();

        // Evita erro caso formulario.txt esteja mal formatado
        for (int i = 0; i < perguntas.size(); i++) {
            if (perguntas.get(i).length() > 4) {
                perguntas.set(i, perguntas.get(i).substring(4)); // Remove "1 - ", "2 - ", etc.
            }
        }

        // Instancia das variáveis temporárias
        String name = null;
        String email = null;
        int age = 0;
        double height = 0.0;
        String input;
        Map<String, String> extras = new HashMap<>();

        System.out.println("Preencha o perfil de cadastro de usuário:");

        for (int i = 0; i < perguntas.size(); i++) {
            boolean valido = false;
            while (!valido) {
                System.out.print(perguntas.get(i) + ": ");
                input = sc.nextLine();

                // Validar e armazenar os dados com base no índice
                try {
                    switch (i) {
                        case 0 -> {
                            if (Validator.isNameValid(input)) {
                                name = Validator.formatName(input);
                                valido = true;
                            }
                        }

                        case 1 -> {

                            if (Validator.isEmailValid(input)) {
                                email = input.trim();
                                ensureEmailNotUsed(usersDir, email);
                                valido = true;
                            }
                        }

                        case 2 -> {
                            age = Integer.parseInt(input.trim());
                            valido = true;
                        }
                        case 3 -> {
                            height = Double.parseDouble(input.trim().replace(",", "."));
                            valido = true;
                        }
                        default -> {
                            extras.put(perguntas.get(i), input);
                            valido = true;
                        }

                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }
        }

        System.out.println("\nCadastro concluído com sucesso!");

        // Criar objeto `User`
        User user1 = new User(name, email, age, height, extras);

        // Obtém o número sequencial e salva o usuário no arquivo
        int sequence = FileManager.getSequence();
        FileManager.saveUserToFile(user1, sequence);

        // Ler e exibir o conteúdo do usuário salvo
        FileManager.readUserFromFile(sequence, user1.getName());
    }

    public static void listarUsuarios() {
        List<File> usuarios = FileManager.listUserFiles();
        List<String> nomes = new ArrayList<>();

        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        int indice = 1;
        for (File f : usuarios) {

            try (BufferedReader br = new BufferedReader(new FileReader(f))){
                String line = br.readLine();

                if (line.trim().toLowerCase().startsWith("nome:")) {
                    nomes.add(indice + " - " + line.split(":", 2) [1].trim());
                    indice++;
                }
            } catch (IOException e) {
                System.err.println("Erro ao ler arquivo: " + f.getName());            }
        }

        for (String nome : nomes) {
            System.out.println(nome);
        }
    }

    public static void adicionarPergunta() {
        System.out.print("Pergunta a ser adicionada no formulário: ");
        String pergunta = sc.nextLine();
        if (!isQuestionValid(pergunta)){
            return;
        }

        FileManager.addQuestion(pergunta);
        System.out.println("Pergunta adicionada com sucesso!");
    }

    public static void excluirPerguntaDoFormulario() {
        System.out.println("\n--- Excluir Pergunta do Formulário ---");
        FileManager.listQuestions();
        System.out.println("0 - Voltar ao menu principal.");

        System.out.print("\nSelecione o número da pergunta que deseja excluir: ");

        try {
            int pergunta = sc.nextInt();
            sc.nextLine();


            if (pergunta == 0) {
                System.out.println("Retornando...");
                return;
            }
            if (pergunta <= 4) {
                System.out.println("Não é permitido excluir as perguntas base.");
                return;
            }

            FileManager.deleteQuestion(pergunta);
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Digite apenas números.");
            sc.nextLine();
        }

        System.out.println("Operação concluída.\n");
    }

    public static void pesquisarUsuario() {

            System.out.println("\n--- Pesquisa de usuários ---");
            System.out.println("DIGITE \"0\" PARA RETORNAR AO MENU PRINCIPAL.");
            System.out.print("Buscar: ");
            String nameKey = sc.nextLine().trim();

            if (nameKey.isBlank()) {
                System.out.println("Digite algo para buscar.");
                return;
            }

            if (nameKey.equals("0")) {
                System.out.println("Retornando...");
                return;
            }

            FileManager.searchUsers(nameKey);
    }

}
