package application;
import entities.User;
import util.FileManager;
import util.Validator;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        String path = "C:\\Users\\jgabr\\OneDrive\\Sticky Notes 8\\Imagens\\Documentos\\T.i\\projeto sistema de cadastro\\formulario.txt";
        List<String> perguntas = FileManager.readFileLines(path);

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

        // Scanner para receber as respostas
        Scanner sc = new Scanner(System.in);

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
        sc.close();

        System.out.println("\nCadastro concluído com sucesso!");

        // Criar objeto `User`
        User user1 = new User(name, email, age, height);

        // Obtém o número sequencial e salva o usuário no arquivo
        int sequence = FileManager.getSequence();
        FileManager.saveUserToFile(user1, sequence);

        // Ler e exibir o conteúdo do usuário salvo
        FileManager.readUserFromFile(sequence, user1.getName());
    }
}