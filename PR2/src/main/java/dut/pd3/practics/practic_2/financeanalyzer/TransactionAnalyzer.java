package dut.pd3.practics.practic_2.financeanalyzer;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public abstract class TransactionAnalyzer {

    public static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MM-yyyy");

    public static double calculateTotalBalance(List<Transaction> transactions) {
        double balance = 0;
        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
        }
        return balance;
    }

    public static int countTransactionsByMonth(List<Transaction> transactions, String monthYear) {
        YearMonth month = YearMonth.parse(monthYear, MONTH_FORMAT);
        int count = 0;
        for (Transaction transaction : transactions) {
            if (YearMonth.from(transaction.toLocalDate()).equals(month)) {
                count++;
            }
        }
        return count;
    }

    public static List<Transaction> findTopExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparing(Transaction::getAmount))
                .limit(10)
                .collect(Collectors.toList());
    }

    public static Optional<ExpenseRange> findExtremeExpenses(List<Transaction> transactions,
                                                             LocalDate from, LocalDate to) {
        List<Transaction> expenses = transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .filter(t -> !t.toLocalDate().isBefore(from) && !t.toLocalDate().isAfter(to))
                .toList();
        if (expenses.isEmpty()) {
            return Optional.empty();
        }
        Comparator<Transaction> byAmount = Comparator.comparing(Transaction::getAmount);
        return Optional.of(new ExpenseRange(
                expenses.stream().min(byAmount).get(),
                expenses.stream().max(byAmount).get()));
    }

    public static Map<String, Double> sumExpensesByCategory(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(Transaction::getDescription,
                        Collectors.summingDouble(t -> -t.getAmount())))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new));
    }

    public static Map<YearMonth, Double> sumExpensesByMonth(List<Transaction> transactions) {
        Map<YearMonth, Double> sums = new TreeMap<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                sums.merge(YearMonth.from(transaction.toLocalDate()), -transaction.getAmount(), Double::sum);
            }
        }
        return sums;
    }
}
