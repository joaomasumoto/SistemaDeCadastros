package application;

import entities.User;
import util.Validator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        String path = "C:\\Users\\jgabr\\OneDrive\\Sticky Notes 8\\Imagens\\Documentos\\T.i\\projeto sistema de cadastro\\formulario.txt";
        List<String> perguntas = new ArrayList<>();

        // Carregar as perguntas do formulario
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String linha = bufferedReader.readLine();
            while (linha != null) {
                perguntas.add(linha.substring(4)); // Remove os numeros e o " - "
                linha = bufferedReader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Instancia das variaveis temporarias
        String name = null;
        String email = null;
        int age = 0;
        double height = 0.0;
        String input;

        // Scanner para receber as respostas
        Scanner sc = new Scanner(System.in);

        System.out.println("Preencha o perfil de cadastro de usuário: ");


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

        // Exibir cadastro do 'User'
        System.out.println(user1);


    }
}
