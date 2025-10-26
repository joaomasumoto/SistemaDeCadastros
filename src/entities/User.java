package entities;
import java.util.LinkedHashMap;
import java.util.Map;

public class User {
    private String name;
    private String email;
    private int age;
    private double height;
    private Map<String, String> extraAnswers = new LinkedHashMap<>();

    public User(String name, String email, int age, double height, Map<String, String> extraAnswers) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.height = height;
        this.extraAnswers = extraAnswers != null ? extraAnswers : new LinkedHashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public Map<String, String> getExtraAnswers() {
        return extraAnswers;
    }

    public void setExtraAnswers(Map<String, String> extraAnswers) {
        this.extraAnswers = extraAnswers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        //Fixos
        sb.append(String.format(""" 
                Nome: %s
                Email: %s
                Idade: %d anos
                Altura: %.2f m
                """, name, email, age, height));

        //Variáveis (se existirem)
        if (extraAnswers != null && !extraAnswers.isEmpty()) {
            for (Map.Entry<String, String> entry : extraAnswers.entrySet()) {
                sb.append(entry.getKey()).append(" ").append(entry.getValue()).append(System.lineSeparator());
            }
        }

        return sb.toString().trim();
    }

}
