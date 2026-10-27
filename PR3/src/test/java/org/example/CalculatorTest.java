package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    @DisplayName("Додавання: додатні, від'ємні та нульові числа")
    void addReturnsSum() throws InvalidInputException {
        assertEquals(5.0, calculator.add(2.0, 3.0), 1e-9);
        assertEquals(-5.0, calculator.add(-2.0, -3.0), 1e-9);
        assertEquals(0.0, calculator.add(0.0, 0.0), 1e-9);
        assertEquals(1.5, calculator.add(-2.5, 4.0), 1e-9);
    }

    @Test
    @DisplayName("Віднімання: додатні, від'ємні та нульові числа")
    void subtractReturnsDifference() throws InvalidInputException {
        assertEquals(2.0, calculator.subtract(5.0, 3.0), 1e-9);
        assertEquals(-8.0, calculator.subtract(-5.0, 3.0), 1e-9);
        assertEquals(0.0, calculator.subtract(4.2, 4.2), 1e-9);
    }

    @Test
    @DisplayName("Множення: додатні, від'ємні числа та нуль")
    void multiplyReturnsProduct() throws InvalidInputException {
        assertEquals(20.0, calculator.multiply(4.0, 5.0), 1e-9);
        assertEquals(-12.0, calculator.multiply(-3.0, 4.0), 1e-9);
        assertEquals(0.0, calculator.multiply(0.0, 100.5), 1e-9);
    }

    @Test
    @DisplayName("Ділення повертає частку")
    void divideReturnsQuotient() throws InvalidInputException {
        assertEquals(2.5, calculator.divide(10.0, 4.0), 1e-9);
        assertEquals(-4.0, calculator.divide(8.0, -2.0), 1e-9);
        assertEquals(0.0, calculator.divide(0.0, 7.0), 1e-9);
    }

    @Test
    @DisplayName("Ділення на нуль кидає ArithmeticException")
    void divideByZeroThrowsArithmeticException() {
        ArithmeticException exception = assertThrows(
                ArithmeticException.class,
                () -> calculator.divide(10.0, 0.0));

        assertTrue(exception.getMessage().contains("Ділення на нуль"));
    }

    @Test
    @DisplayName("Ділення на від'ємний нуль також кидає ArithmeticException")
    void divideByNegativeZeroThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(5.0, -0.0));
    }

    @Test
    @DisplayName("Квадратний корінь з невід'ємного числа")
    void sqrtReturnsRoot() throws InvalidInputException {
        assertEquals(3.0, calculator.sqrt(9.0), 1e-9);
        assertEquals(0.0, calculator.sqrt(0.0), 1e-9);
        assertEquals(1.4142, calculator.sqrt(2.0), 1e-4);
    }

    @Test
    @DisplayName("Квадратний корінь з від'ємного числа кидає InvalidInputException")
    void sqrtOfNegativeThrowsInvalidInputException() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> calculator.sqrt(-4.0));

        assertTrue(exception.getMessage().contains("від'ємного"));
    }

    @Test
    @DisplayName("NaN і нескінченність є некоректними вхідними даними")
    void nonFiniteValuesThrowInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> calculator.add(Double.NaN, 1.0));
        assertThrows(InvalidInputException.class,
                () -> calculator.subtract(1.0, Double.POSITIVE_INFINITY));
        assertThrows(InvalidInputException.class,
                () -> calculator.multiply(Double.NEGATIVE_INFINITY, 2.0));
        assertThrows(InvalidInputException.class,
                () -> calculator.divide(Double.NaN, 2.0));
        assertThrows(InvalidInputException.class,
                () -> calculator.sqrt(Double.NaN));
    }
}
