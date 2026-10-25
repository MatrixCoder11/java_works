package dut.pd3.practics.practic_2.financeanalyzer;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionReportGeneratorTest {

    private static String capture(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void buildBarUsesOneStarPerThousand() {
        assertEquals("***", TransactionReportGenerator.buildBar(3000));
        assertEquals("***", TransactionReportGenerator.buildBar(3200));
        assertEquals("****", TransactionReportGenerator.buildBar(3600));
    }

    @Test
    void buildBarGivesAtLeastOneStarForSmallAmounts() {
        assertEquals("*", TransactionReportGenerator.buildBar(120));
        assertEquals("", TransactionReportGenerator.buildBar(0));
    }

    @Test
    void categoryReportContainsBars() {
        Map<String, Double> data = new LinkedHashMap<>();
        data.put("Оренда", 3200.0);

        String out = capture(() -> TransactionReportGenerator.printCategoryReport(data));

        assertTrue(out.contains("Оренда"));
        assertTrue(out.contains("| ***"));
    }

    @Test
    void monthlyReportUsesMonthYearLabels() {
        Map<YearMonth, Double> data = Map.of(YearMonth.of(2024, 1), 2000.0);

        String out = capture(() -> TransactionReportGenerator.printMonthlyReport(data));

        assertTrue(out.contains("01-2024"));
        assertTrue(out.contains("| **"));
    }

    @Test
    void balanceReport() {
        String out = capture(() -> TransactionReportGenerator.printBalanceReport(200.0));
        assertTrue(out.contains("Загальний баланс: 200.0"));
    }
}
