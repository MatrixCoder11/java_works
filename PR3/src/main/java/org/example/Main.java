package org.example;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;

/**
 * Консольна взаємодія з користувачем.
 * Програма циклічно запитує операцію та числа, обробляє виключення
 * і в блоці {@code finally} повідомляє про завершення обробки запиту.
 */
public class Main {

    private static final String OPERATIONS = "+, -, *, /, sqrt";
    private static final String GOODBYE = "До побачення!";
    private static final String REQUEST_FINISHED = "Завершення обробки запиту.";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            run(scanner, System.out);
        }
    }

    /**
     * Основний цикл обробки запитів. Виділений в окремий метод, щоб його можна було
     * перевірити в тестах без реальної консолі.
     *
     * @param scanner джерело вхідних даних
     * @param out     потік для виведення повідомлень
     */
    static void run(Scanner scanner, PrintStream out) {
        out.println("Калькулятор. Введіть 'q' для виходу.");
        while (true) {
            String operation = readOperation(scanner, out);
            if (operation == null) {
                return;
            }

            Double first = readNumber(scanner, out, "Введіть перше число: ");
            if (first == null) {
                return;
            }

            double second = 0.0;
            if (!"sqrt".equals(operation)) {
                Double value = readNumber(scanner, out, "Введіть друге число: ");
                if (value == null) {
                    return;
                }
                second = value;
            }

            try {
                double result = calculate(operation, first, second);
                out.printf(Locale.ROOT, "Результат: %s%n", format(result));
            } catch (ArithmeticException e) {
                out.println("Помилка: " + e.getMessage());
            } catch (InvalidInputException e) {
                out.println("Помилка вхідних даних: " + e.getMessage());
            } finally {
                out.println(REQUEST_FINISHED);
            }
        }
    }

    /**
     * Виконує обрану операцію.
     *
     * @throws InvalidInputException якщо операція невідома або дані некоректні
     * @throws ArithmeticException   якщо відбулося ділення на нуль
     */
    static double calculate(String operation, double first, double second)
            throws InvalidInputException {
        Calculator calculator = new Calculator();
        return switch (operation) {
            case "+" -> calculator.add(first, second);
            case "-" -> calculator.subtract(first, second);
            case "*" -> calculator.multiply(first, second);
            case "/" -> calculator.divide(first, second);
            case "sqrt" -> calculator.sqrt(first);
            default -> throw new InvalidInputException("Невідома операція: " + operation);
        };
    }

    private static String readOperation(Scanner scanner, PrintStream out) {
        while (true) {
            out.printf("Введіть операцію (%s): ", OPERATIONS);
            if (!scanner.hasNext()) {
                printInputFinished(out);
                return null;
            }
            String token = scanner.next().trim().toLowerCase(Locale.ROOT);
            if ("q".equals(token)) {
                out.println(GOODBYE);
                return null;
            }
            if (isSupported(token)) {
                return token;
            }
            out.printf("Помилка: невідома операція '%s'. Доступні операції: %s.%n",
                    token, OPERATIONS);
        }
    }

    /**
     * Читає число, доки користувач не введе коректне значення.
     *
     * @return число, або {@code null}, якщо користувач вийшов / вхідні дані закінчилися
     */
    private static Double readNumber(Scanner scanner, PrintStream out, String prompt) {
        while (true) {
            out.print(prompt);
            if (!scanner.hasNext()) {
                printInputFinished(out);
                return null;
            }
            String token = scanner.next().trim();
            if ("q".equalsIgnoreCase(token)) {
                out.println(GOODBYE);
                return null;
            }
            try {
                return Double.parseDouble(token);
            } catch (NumberFormatException e) {
                out.printf("Помилка: '%s' не є числом. Спробуйте ще раз.%n", token);
            }
        }
    }

    private static boolean isSupported(String operation) {
        return switch (operation) {
            case "+", "-", "*", "/", "sqrt" -> true;
            default -> false;
        };
    }

    private static void printInputFinished(PrintStream out) {
        out.println();
        out.println("Вхідні дані завершено. Робота програми зупинена.");
    }

    private static String format(double value) {
        if (value == Math.rint(value) && Math.abs(value) < 1e15) {
            return String.valueOf((long) value);
        }
        return String.format(Locale.ROOT, "%.4f", value);
    }
}
