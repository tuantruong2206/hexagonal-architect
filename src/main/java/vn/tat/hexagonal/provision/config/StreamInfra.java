package vn.tat.hexagonal.provision.config;

import com.univocity.parsers.csv.CsvParser;
import lombok.Data;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Iterator;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 11/21/21 09:53
 */
@Data
public class StreamInfra {
    private CsvParser csvParser;

    //this point will be sticked into source data e.g: data, file etc.
    private PipedOutputStream dataOutStream;

    //this point will be get from outStream;
    private PipedInputStream dataInStream;

    //this is iterator for data traverse
    private Iterator<String[]> dataItor;

    public StreamInfra() throws IOException {
        csvParser = CsvConfig.getParser("");
        dataOutStream = new PipedOutputStream();
        dataInStream = new PipedInputStream(dataOutStream);
        dataItor = csvParser.iterate(this.dataInStream).iterator();
    }
}