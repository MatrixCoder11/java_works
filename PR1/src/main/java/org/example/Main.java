package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        Scanner scanner = new Scanner(System.in);

        Category electronics = new Category(1, "Електроніка");
        Category smartphones = new Category(2, "Смартфони");
        Category accessories = new Category(3, "Аксесуари");

        // Створення об'єктів класу Product
        Product product1 = new Product(1, "Ноутбук", 19999.99, "Високопродуктивний ноутбук для роботи та ігор", electronics);
        Product product2 = new Product(2, "Смартфон", 12999.50, "Смартфон з великим екраном та високою автономністю", smartphones);
        Product product3 = new Product(3, "Навушники", 2499.00, "Бездротові навушники з шумозаглушенням", accessories);

        List<Product> catalog = new ArrayList<>(List.of(product1, product2, product3));

        Cart cart = new Cart();

        List<Order> orderHistory = new ArrayList<>();

        while (true) {
            System.out.println("\nВиберіть опцію:");
            System.out.println("1 - Переглянути список товарів");
            System.out.println("2 - Додати товар до кошика");
            System.out.println("3 - Переглянути кошик");
            System.out.println("4 - Зробити замовлення");
            System.out.println("5 - Видалити товар з кошика");
            System.out.println("6 - Історія замовлень");
            System.out.println("7 - Пошук товарів");
            System.out.println("0 - Вийти");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    // Виведення інформації про товари
                    for (Product product : catalog) {
                        System.out.println(product);
                    }
                    break;
                case 2:
                    System.out.println("Введіть ID товару для додавання до кошика:");
                    int id = scanner.nextInt();
                    Product toAdd = findProductById(catalog, id);
                    if (toAdd != null) {
                        cart.addProduct(toAdd);
                        System.out.println("Товар \"" + toAdd.getName() + "\" додано до кошика");
                    } else {
                        System.out.println("Товар з таким ID не знайдено");
                    }
                    break;
                case 3:
                    System.out.println(cart);
                    break;
                case 4:
                    if (cart.getProducts().isEmpty()) {
                        System.out.println("Кошик порожній. Додайте товари перед оформленням замовлення.");
                    } else {
                        Order order = new Order(cart);
                        System.out.println("Замовлення оформлено:");
                        System.out.println(order);
                        orderHistory.add(order);
                        cart.clear();
                    }
                    break;
                case 5:
                    if (cart.getProducts().isEmpty()) {
                        System.out.println("Кошик порожній.");
                    } else {
                        System.out.println(cart);
                        System.out.println("Введіть ID товару для видалення з кошика:");
                        int removeId = scanner.nextInt();
                        Product toRemove = findProductById(cart.getProducts(), removeId);
                        if (toRemove != null) {
                            cart.removeProduct(toRemove);
                            System.out.println("Товар \"" + toRemove.getName() + "\" видалено з кошика");
                        } else {
                            System.out.println("Товар з таким ID не знайдено у кошику");
                        }
                    }
                    break;
                case 6:
                    if (orderHistory.isEmpty()) {
                        System.out.println("Історія замовлень порожня.");
                    } else {
                        for (int i = 0; i < orderHistory.size(); i++) {
                            System.out.println("\nЗамовлення №" + (i + 1) + ":");
                            System.out.println(orderHistory.get(i));
                        }
                    }
                    break;
                case 7:
                    System.out.println("Шукати за: 1 - назвою, 2 - категорією");
                    int searchType = scanner.nextInt();
                    scanner.nextLine();
                    if (searchType == 1) {
                        System.out.println("Введіть назву товару:");
                        String name = scanner.nextLine();
                        printSearchResults(catalog, name, true);
                    } else if (searchType == 2) {
                        System.out.println("Введіть назву категорії:");
                        String categoryName = scanner.nextLine();
                        printSearchResults(catalog, categoryName, false);
                    } else {
                        System.out.println("Невідома опція пошуку.");
                    }
                    break;
                case 0:
                    System.out.println("Дякуємо, що використовували наш магазин!");
                    return;
                default:
                    System.out.println("Невідома опція. Спробуйте ще раз.");
                    break;
            }
        }
    }

    static Product findProductById(List<Product> products, int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    static void printSearchResults(List<Product> catalog, String query, boolean byName) {
        boolean found = false;
        for (Product product : catalog) {
            String source = byName ? product.getName() : product.getCategory().getName();
            if (source.toLowerCase().contains(query.toLowerCase())) {
                System.out.println(product);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Товарів за таким запитом не знайдено.");
        }
    }
}
