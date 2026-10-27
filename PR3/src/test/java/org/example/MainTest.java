package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private static String runSession(String input) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);

        Main.run(new Scanner(input), out);
        out.flush();

        return buffer.toString(StandardCharsets.UTF_8);
    }

    private static int countOccurrences(String text, String part) {
        int count = 0;
        int index = text.indexOf(part);
        while (index >= 0) {
            count++;
            index = text.indexOf(part, index + part.length());
        }
        return count;
    }

    @Test
    @DisplayName("Успішне додавання двох чисел")
    void additionWorks() {
        String output = runSession("+\n2\n3\nq\n");

        assertTrue(output.contains("Результат: 5"), output);
        assertTrue(output.contains("Завершення обробки запиту."), output);
    }

    @Test
    @DisplayName("Ділення на нуль обробляється без аварійного завершення")
    void divisionByZeroIsHandled() {
        String output = runSession("/\n5\n0\nq\n");

        assertTrue(output.contains("Помилка: Ділення на нуль"), output);
        assertFalse(output.contains("Результат:"), output);
        // Блок finally виконується навіть після виключення.
        assertTrue(output.contains("Завершення обробки запиту."), output);
    }

    @Test
    @DisplayName("Рядок замість числа: програма просить повторити введення")
    void invalidNumberIsReportedAndRetried() {
        String output = runSession("+\nabc\n2\n3\nq\n");

        assertTrue(output.contains("'abc' не є числом"), output);
        assertTrue(output.contains("Результат: 5"), output);
    }

    @Test
    @DisplayName("Невідома операція: програма просить повторити введення")
    void unknownOperationIsReportedAndRetried() {
        String output = runSession("%\n+\n2\n2\nq\n");

        assertTrue(output.contains("невідома операція '%'"), output);
        assertTrue(output.contains("Результат: 4"), output);
    }

    @Test
    @DisplayName("Від'ємне число під коренем викликає InvalidInputException")
    void negativeSqrtIsHandled() {
        String output = runSession("sqrt\n-4\nq\n");

        assertTrue(output.contains("Помилка вхідних даних:"), output);
        assertTrue(output.contains("від'ємного"), output);
        assertTrue(output.contains("Завершення обробки запиту."), output);
    }

    @Test
    @DisplayName("Дробовий результат ділення виводиться коректно")
    void fractionalResultIsFormatted() {
        String output = runSession("/\n10\n4\nq\n");

        assertTrue(output.contains("Результат: 2.5000"), output);
    }

    @Test
    @DisplayName("Блок finally виконується для кожного запиту")
    void finallyRunsForEveryRequest() {
        String output = runSession("+\n1\n1\n-\n5\n2\nq\n");

        assertEquals(2, countOccurrences(output, "Завершення обробки запиту."), output);
    }

    @Test
    @DisplayName("Коректне завершення за командою q")
    void quitCommandStopsProgram() {
        String output = runSession("q\n");

        assertTrue(output.contains("До побачення!"), output);
        assertFalse(output.contains("Завершення обробки запиту."), output);
    }

    @Test
    @DisplayName("Завершення введення без команди q не призводить до зависання")
    void endOfInputStopsProgram() {
        String output = runSession("+\n1\n2\n");

        assertTrue(output.contains("Результат: 3"), output);
        assertTrue(output.contains("Вхідні дані завершено"), output);
        assertTrue(output.contains("Завершення обробки запиту."), output);
    }
}
