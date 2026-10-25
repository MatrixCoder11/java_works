package dut.pd3.practics.practic_2.financeanalyzer;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public abstract class TransactionReportGenerator {

    public static final double UNIT = 1000;

    public static void printBalanceReport(double totalBalance) {
        System.out.println("Загальний баланс: " + totalBalance);
    }

    public static void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.println("Кількість транзакцій за " + monthYear + ": " + count);
    }

    public static void printTopExpensesReport(List<Transaction> topExpenses) {
        System.out.println("10 найбільших витрат:");
        for (Transaction expense : topExpenses) {
            System.out.println(expense.getDescription() + ": " + expense.getAmount());
        }
    }

    public static void printExtremeExpensesReport(LocalDate from, LocalDate to, Optional<ExpenseRange> range) {
        System.out.println("Найбільша і найменша витрати за період "
                + from.format(Transaction.DATE_FORMAT) + " — " + to.format(Transaction.DATE_FORMAT) + ":");
        if (range.isEmpty()) {
            System.out.println("  витрат за цей період немає");
            return;
        }
        System.out.println("  Найбільша: " + describe(range.get().largest()));
        System.out.println("  Найменша:  " + describe(range.get().smallest()));
    }

    public static void printCategoryReport(Map<String, Double> byCategory) {
        System.out.println("Витрати по категоріях (1 * = " + (int) UNIT + " грн):");
        byCategory.forEach((category, sum) -> System.out.println(formatLine(category, sum)));
    }

    public static void printMonthlyReport(Map<YearMonth, Double> byMonth) {
        System.out.println("Витрати по місяцях (1 * = " + (int) UNIT + " грн):");
        byMonth.forEach((month, sum) ->
                System.out.println(formatLine(month.format(TransactionAnalyzer.MONTH_FORMAT), sum)));
    }

    public static String buildBar(double amount) {
        int stars = (int) Math.round(amount / UNIT);
        if (amount > 0 && stars == 0) {
            stars = 1;
        }
        return "*".repeat(stars);
    }

    private static String describe(Transaction t) {
        return t.getDescription() + " " + t.getAmount() + " (" + t.getDate() + ")";
    }

    private static String formatLine(String label, double sum) {
        return String.format(Locale.ROOT, "%-22s %10.2f грн | %s", label, sum, buildBar(sum));
    }
}
