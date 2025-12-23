package app;

public class Customer {

    // Виправлена декларація змінних класу
    private final String name;
    private final String phone;

    // Виправлений конструктор
    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    // Геттери для змінних класу
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
