package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.pdftest.matchers.ContainsExactText;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

public class FileTest {

    ClassLoader cl = FileTest.class.getClassLoader();

    @Test
    void zipTest() throws Exception {
        ZipFile zf = new ZipFile("src/test/resources/files/test-files.zip");
        ZipInputStream zis = new ZipInputStream(requireNonNull(cl.getResourceAsStream("files/test-files.zip")));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.getName().endsWith(".pdf") && !entry.getName().startsWith("__")) {
                try (InputStream is = zf.getInputStream(entry)) {
                    PDF pdf = new PDF(is);
                    Assertions.assertEquals(3, pdf.numberOfPages);
                    assertThat(pdf, new ContainsExactText("This is page"));
                    assertThat(pdf, new ContainsExactText("PDF test document"));
                }
            } else if (entry.getName().endsWith(".xlsx") && !entry.getName().startsWith("__")) {
                try (InputStream is = zf.getInputStream(entry)) {
                    XLS xls = new XLS(is);
                    Assertions.assertEquals(1, xls.excel.getNumberOfSheets());
                    String cellValue = xls.excel.getSheetAt(0).getRow(11).getCell(2).getStringCellValue();
                    assertThat(cellValue).contains("Ноль");
                }
            } else if (entry.getName().endsWith(".csv") && !entry.getName().startsWith("__")) {
                try (InputStream is = zf.getInputStream(entry);
                     CSVReader reader = new CSVReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    List<String[]> content = reader.readAll();
                    assertThat(content).contains(
                            new String[]{"1", "One", "Один"},
                            new String[]{"0", "Zero", "Ноль"}
                    );
                }
            }
        }
    }
}
