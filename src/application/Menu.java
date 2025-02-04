package application;

import services.UserService;

import java.util.Scanner;

public class Menu {
    private static final Scanner sc = new Scanner(System.in);

    public static void exibirMenu() {
        while (true) {
            System.out.println("----------MENU-----------");
            System.out.println("""
                    1 - Cadastrar o usuário
                    2 - Listar todos usuários cadastrados
                    3 - Cadastrar nova pergunta no formulário
                    4 - Deletar pergunta do formulário
                    5 - Pesquisar usuário por nome ou idade ou email
                    0 - Sair
                    """);
            System.out.print("Selecione uma opção: ");

            int opcao = sc.nextInt();
            sc.nextLine();

            try {
                switch (opcao) {
                    case 1 -> UserService.cadastrarUsuario();

                    case 2 -> {


                    }

                    case 3 -> {


                    }

                    case 4 -> {


                    }

                    case 5 -> {


                    }

                    case 0 -> {
                        System.out.println("Saindo do programa...");
                        sc.close();
                        return;
                    }
            }
            } catch (IllegalArgumentException e) {
                System.out.println("Valor inválido: " + e.getMessage());
            }

        }
    }
}
