package dut.pd3.practics.practic_2.financeanalyzer;

import java.io.IOException;
import java.util.List;

@FunctionalInterface
public interface DataSource {
    List<String> readLines() throws IOException;
}
