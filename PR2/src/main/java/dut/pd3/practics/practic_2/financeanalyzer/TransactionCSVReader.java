package dut.pd3.practics.practic_2.financeanalyzer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class TransactionCSVReader {

    public static List<Transaction> readTransactions(DataSource source) {
        List<String> lines;
        try {
            lines = source.readLines();
        } catch (IOException e) {
            throw new UncheckedIOException("Не вдалося прочитати дані: " + e.getMessage(), e);
        }

        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            Optional<Transaction> transaction = parseLine(line);
            if (transaction.isPresent()) {
                transactions.add(transaction.get());
            } else if (i > 0 && !line.isBlank()) {
                System.out.println("Пропущено рядок " + (i + 1) + ": " + line);
            }
        }
        return transactions;
    }

    public static Optional<Transaction> parseLine(String line) {
        String[] values = line.split(",", 3);
        if (values.length < 3) {
            return Optional.empty();
        }
        try {
            Transaction transaction = new Transaction(
                    values[0].trim(), Double.parseDouble(values[1].trim()), values[2].trim());
            transaction.toLocalDate();
            return Optional.of(transaction);
        } catch (NumberFormatException | DateTimeParseException e) {
            return Optional.empty();
        }
    }
}
