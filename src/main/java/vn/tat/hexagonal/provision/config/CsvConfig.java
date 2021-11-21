package vn.tat.hexagonal.provision.config;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.OutputStream;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 11/21/21 09:51
 */
public class CsvConfig {

    public static CsvParser getParser(String config) {
        CsvParserSettings rowSettings = new CsvParserSettings();
        rowSettings.setDelimiterDetectionEnabled(true, ',');
        rowSettings.setQuoteDetectionEnabled(true);
        rowSettings.setEscapeUnquotedValues(true);
        rowSettings.setHeaderExtractionEnabled(false);
        rowSettings.trimValues(false);
        rowSettings.setEmptyValue("");
        rowSettings.setMaxCharsPerColumn(10000000);
        rowSettings.getFormat().setLineSeparator("\n");
        return new CsvParser(rowSettings);
    }

    public static CsvWriter createWriter(OutputStream outputStream) {
        CsvWriterSettings writerSettings = new CsvWriterSettings();
        writerSettings.trimValues(false);
        writerSettings.setEmptyValue("");
        writerSettings.setHeaderWritingEnabled(true);
        writerSettings.setQuoteAllFields(false);
        writerSettings.getFormat().setLineSeparator("\n");
        return new CsvWriter(outputStream, writerSettings);
    }
}