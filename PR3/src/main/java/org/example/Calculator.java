package org.example;

/**
 * Калькулятор з базовими арифметичними операціями.
 * <p>
 * Усі методи перевіряють вхідні значення та кидають {@link InvalidInputException},
 * якщо число не є скінченним (NaN або нескінченність) чи є неприпустимим для операції.
 * Ділення на нуль призводить до {@link ArithmeticException}.
 */
public class Calculator {

    /**
     * Додає два числа.
     *
     * @param a перший доданок
     * @param b другий доданок
     * @return сума
     * @throws InvalidInputException якщо хоча б одне число не є скінченним
     */
    public double add(double a, double b) throws InvalidInputException {
        validateOperands(a, b);
        return a + b;
    }

    /**
     * Віднімає друге число від першого.
     *
     * @param a зменшуване
     * @param b від'ємник
     * @return різниця
     * @throws InvalidInputException якщо хоча б одне число не є скінченним
     */
    public double subtract(double a, double b) throws InvalidInputException {
        validateOperands(a, b);
        return a - b;
    }

    /**
     * Множить два числа.
     *
     * @param a перший множник
     * @param b другий множник
     * @return добуток
     * @throws InvalidInputException якщо хоча б одне число не є скінченним
     */
    public double multiply(double a, double b) throws InvalidInputException {
        validateOperands(a, b);
        return a * b;
    }

    /**
     * Ділить перше число на друге.
     *
     * @param a ділене
     * @param b дільник
     * @return частка
     * @throws InvalidInputException якщо хоча б одне число не є скінченним
     * @throws ArithmeticException   якщо дільник дорівнює нулю
     */
    public double divide(double a, double b) throws InvalidInputException {
        validateOperands(a, b);
        if (b == 0.0) {
            throw new ArithmeticException("Ділення на нуль неможливе");
        }
        return a / b;
    }

    /**
     * Обчислює квадратний корінь.
     *
     * @param a підкореневе значення (повинно бути невід'ємним)
     * @return квадратний корінь
     * @throws InvalidInputException якщо число не є скінченним або є від'ємним
     */
    public double sqrt(double a) throws InvalidInputException {
        validateNumber(a, "підкореневе");
        if (a < 0.0) {
            throw new InvalidInputException(
                    "Квадратний корінь з від'ємного числа не існує: " + a);
        }
        return Math.sqrt(a);
    }

    private static void validateOperands(double a, double b) throws InvalidInputException {
        validateNumber(a, "перше");
        validateNumber(b, "друге");
    }

    private static void validateNumber(double value, String name) throws InvalidInputException {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new InvalidInputException(
                    "Вхідне " + name + " число повинно бути скінченним, отримано: " + value);
        }
    }
}
