package dut.pd3.practics.practic_2.financeanalyzer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionCSVReaderTest {

    @Test
    void readsLinesAndSkipsHeader() {
        DataSource source = () -> List.of(
                "Дата,Сума,Категорія",
                "05-12-2023,-450,Сільпо",
                "10-12-2023,8000,Зарплата");

        List<Transaction> result = TransactionCSVReader.readTransactions(source);

        assertEquals(2, result.size());
        assertEquals(new Transaction("05-12-2023", -450.0, "Сільпо"), result.get(0));
        assertEquals(new Transaction("10-12-2023", 8000.0, "Зарплата"), result.get(1));
    }

    @Test
    void skipsBlankAndMalformedLines() {
        DataSource source = () -> List.of(
                "Дата,Сума,Категорія",
                "",
                "05-12-2023,abc,Сільпо",
                "05-12-2023,-10",
                "2023-12-05,-10,Аптека",
                "07-12-2023,-120,Аптека");

        List<Transaction> result = TransactionCSVReader.readTransactions(source);

        assertEquals(1, result.size());
        assertEquals("Аптека", result.get(0).getDescription());
    }

    @Test
    void keepsCommaInsideDescription() {
        Transaction result = TransactionCSVReader.parseLine("05-12-2023,-10,Кава, цукор").get();
        assertEquals("Кава, цукор", result.getDescription());
    }

    @Test
    void readsLocalFile(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("t.csv");
        Files.writeString(file, "Дата,Сума,Категорія\n01-02-2023,50,Дохід\n15-02-2023,-20,Витрата\n");

        assertEquals(2, TransactionCSVReader.readTransactions(new FileDataSource(file)).size());
    }

    @Test
    void readsSampleFile() {
        List<Transaction> result = TransactionCSVReader.readTransactions(
                new FileDataSource(Path.of("data/sample.csv")));

        assertEquals(26, result.size());
        assertEquals(13, TransactionAnalyzer.countTransactionsByMonth(result, "01-2024"));
    }

    @Test
    void throwsWhenSourceIsUnavailable() {
        DataSource broken = () -> {
            throw new IOException("немає з'єднання");
        };
        assertThrows(UncheckedIOException.class, () -> TransactionCSVReader.readTransactions(broken));
    }
}
