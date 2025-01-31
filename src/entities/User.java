package entities;

public class User {
    private String name;
    private String email;
    private int age;
    private double height;

    public User(String name, String email, int age, double height) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.height = height;
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

    @Override
    public String toString() {
        return String.format(""" 
                Nome: %s
                Email: %s
                Idade: %d anos
                Altura: %.2f m
                """, name, email, age, height);
    }

}
