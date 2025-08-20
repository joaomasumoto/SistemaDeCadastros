package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {


    // Validações

    public static boolean isNameValid(String name) {
        Matcher nameMatcher = namePatttern.matcher(name.trim());
        if (!nameMatcher.matches()) {
            throw new IllegalArgumentException("Insira um nome válido.");
        }
        return nameMatcher.matches();

    }

    public static boolean isEmailValid(String email) {
        Matcher emailMatcher = emailPattern.matcher(email.trim());
        if (!emailMatcher.matches()) {
            throw new IllegalArgumentException("Insira um endereço de email válido.");
        }
        return emailMatcher.matches();

    }

    public static void ensureEmailNotUsed(String dirPath, String email) {
        File dir = new File(dirPath);
        File[] archives = dir.listFiles();
        if (archives == null) return; // nada pra checar

        String target = email.trim().toLowerCase();

        for (File f  : archives) {
            if (f.isDirectory()) continue;

            String name = f.getName().toLowerCase();

            if (name.equals("formulario.txt") || name.endsWith("sequence.txt")) continue;

            try (BufferedReader br = new BufferedReader((new FileReader(f)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.toLowerCase().contains(target)) { // Utilizar 'contains' pois User registra os dados em uma linha só, entao os emails se comparados com .equals nunca vao apontar duplicidade!
                        throw new IllegalArgumentException("Email já consta na base.");
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static boolean isAgeValid(int age) {
        return age > 0;
    }

    public static boolean isHeightValid(double height) {
        return height > 0.0;
    }

    //formatações

    public static String formatName(String name) {

        String[] palavras = name.trim().toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();

        for (String palavra : palavras) {
            if (!palavra.isBlank()) {
                sb.append(Character.toUpperCase(palavra.charAt(0)))
                        .append(palavra.substring(1))
                        .append(" ");
            }
        }

        return sb.toString().trim();
    }

    // Metodos auxiliares regex name

    private static final String NAME_PATTERN = "^[A-Za-zÀ-ÖØ-öø-ÿ]+(?: [A-Za-zÀ-ÖØ-öø-ÿ'-]+)+$";

    private static final Pattern namePatttern = Pattern.compile(NAME_PATTERN);

    // Metodos auxiliares regex email
    private static final String EMAIL_PATTERN =                         // Regex emailPattern estático e compilado é bom pois não é criado um novo a cada validação, ganhando em performance
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

}
