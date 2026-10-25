package dut.pd3.practics.practic_2.financeanalyzer;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
public class FileDataSource implements DataSource {

    private final Path path;

    @Override
    public List<String> readLines() throws IOException {
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }
}
