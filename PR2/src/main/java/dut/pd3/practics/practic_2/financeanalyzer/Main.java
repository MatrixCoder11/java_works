package dut.pd3.practics.practic_2.financeanalyzer;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class Main {

    private static final String SAMPLE_FILE = "data/sample.csv";

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        List<Transaction> transactions = TransactionCSVReader.readTransactions(
                new FileDataSource(Path.of(SAMPLE_FILE)));

        double totalBalance = TransactionAnalyzer.calculateTotalBalance(transactions);
        TransactionReportGenerator.printBalanceReport(totalBalance);

        String monthYear = "01-2024";
        int count = TransactionAnalyzer.countTransactionsByMonth(transactions, monthYear);
        TransactionReportGenerator.printTransactionsCountByMonth(monthYear, count);

        TransactionReportGenerator.printTopExpensesReport(TransactionAnalyzer.findTopExpenses(transactions));

        System.out.println();
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 1, 31);
        TransactionReportGenerator.printExtremeExpensesReport(
                from, to, TransactionAnalyzer.findExtremeExpenses(transactions, from, to));

        System.out.println();
        TransactionReportGenerator.printCategoryReport(TransactionAnalyzer.sumExpensesByCategory(transactions));

        System.out.println();
        TransactionReportGenerator.printMonthlyReport(TransactionAnalyzer.sumExpensesByMonth(transactions));
    }
}
