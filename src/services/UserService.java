package services;

import entities.User;
import util.FileManager;
import util.Validator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static util.Validator.ensureEmailNotUsed;

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

                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }
        }

        System.out.println("\nCadastro concluído com sucesso!");

        // Criar objeto `User`
        User user1 = new User(name, email, age, height);

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

}
