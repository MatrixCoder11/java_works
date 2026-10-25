package dut.pd3.practics.practic_2.financeanalyzer;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionAnalyzerTest {

    private static Transaction tx(String date, double amount, String description) {
        return new Transaction(date, amount, description);
    }

    @Test
    void testCalculateTotalBalance() {
        List<Transaction> transactions = List.of(
                tx("2023-01-01", 100.0, "Дохід"),
                tx("2023-01-02", -50.0, "Витрата"),
                tx("2023-01-03", 150.0, "Дохід"));

        assertEquals(200.0, TransactionAnalyzer.calculateTotalBalance(transactions));
        assertEquals(0.0, TransactionAnalyzer.calculateTotalBalance(List.of()));
    }

    @Test
    void testCountTransactionsByMonth() {
        List<Transaction> transactions = List.of(
                tx("01-02-2023", 50.0, "Дохід"),
                tx("15-02-2023", -20.0, "Витрата"),
                tx("05-03-2023", 100.0, "Дохід"));

        assertEquals(2, TransactionAnalyzer.countTransactionsByMonth(transactions, "02-2023"));
        assertEquals(1, TransactionAnalyzer.countTransactionsByMonth(transactions, "03-2023"));
        assertEquals(0, TransactionAnalyzer.countTransactionsByMonth(transactions, "02-2024"));
    }

    @Test
    void testFindTopExpensesReturnsTenLargest() {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            transactions.add(tx("01-01-2024", -100.0 * i, "Витрата " + i));
        }
        transactions.add(tx("02-01-2024", 99999.0, "Зарплата"));

        List<Transaction> top = TransactionAnalyzer.findTopExpenses(transactions);

        assertEquals(10, top.size());
        assertEquals(-1200.0, top.get(0).getAmount());
        assertEquals(-300.0, top.get(9).getAmount());
        assertTrue(top.stream().allMatch(t -> t.getAmount() < 0));
    }

    @Test
    void testFindTopExpensesWhenFewerThanTen() {
        List<Transaction> transactions = List.of(
                tx("01-01-2024", -10.0, "A"),
                tx("02-01-2024", 500.0, "B"),
                tx("03-01-2024", -30.0, "C"));

        List<Transaction> top = TransactionAnalyzer.findTopExpenses(transactions);

        assertEquals(List.of("C", "A"), top.stream().map(Transaction::getDescription).toList());
    }

    @Test
    void testFindExtremeExpenses() {
        List<Transaction> transactions = List.of(
                tx("10-01-2024", -50.0, "Кава"),
                tx("15-01-2024", -500.0, "Оренда"),
                tx("20-01-2024", -20.0, "Газета"),
                tx("16-01-2024", 1000.0, "Зарплата"),
                tx("01-02-2024", -900.0, "Поза періодом"));

        ExpenseRange range = TransactionAnalyzer.findExtremeExpenses(
                transactions, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)).get();

        assertEquals("Оренда", range.largest().getDescription());
        assertEquals("Газета", range.smallest().getDescription());
    }

    @Test
    void testFindExtremeExpensesIncludesBoundariesAndHandlesNoExpenses() {
        LocalDate day = LocalDate.of(2024, 1, 1);

        Optional<ExpenseRange> single = TransactionAnalyzer.findExtremeExpenses(
                List.of(tx("01-01-2024", -5.0, "Межа")), day, day);
        Optional<ExpenseRange> none = TransactionAnalyzer.findExtremeExpenses(
                List.of(tx("01-01-2024", 100.0, "Дохід")), day, day);

        assertTrue(single.isPresent());
        assertTrue(none.isEmpty());
    }

    @Test
    void testSumExpensesByCategory() {
        List<Transaction> transactions = List.of(
                tx("05-12-2023", -450.0, "Сільпо"),
                tx("14-12-2023", -3200.0, "Оренда"),
                tx("05-01-2024", -500.0, "Сільпо"),
                tx("10-12-2023", 8000.0, "Зарплата"));

        Map<String, Double> sums = TransactionAnalyzer.sumExpensesByCategory(transactions);

        assertEquals(List.of("Оренда", "Сільпо"), List.copyOf(sums.keySet()));
        assertEquals(950.0, sums.get("Сільпо"));
    }

    @Test
    void testSumExpensesByMonth() {
        List<Transaction> transactions = List.of(
                tx("05-01-2024", -500.0, "A"),
                tx("30-12-2023", -1200.0, "B"),
                tx("28-12-2023", -400.0, "C"));

        Map<YearMonth, Double> sums = TransactionAnalyzer.sumExpensesByMonth(transactions);

        assertEquals(List.of(YearMonth.of(2023, 12), YearMonth.of(2024, 1)), List.copyOf(sums.keySet()));
        assertEquals(1600.0, sums.get(YearMonth.of(2023, 12)));
        assertEquals(500.0, sums.get(YearMonth.of(2024, 1)));
    }
}
