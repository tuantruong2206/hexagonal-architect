package vn.tat.hexagonal.provision.pipe;

import com.univocity.parsers.csv.CsvWriter;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.tat.hexagonal.provision.config.CsvConfig;
import vn.tat.hexagonal.provision.config.StreamInfra;
import vn.tat.hexagonal.provision.processor.DataProcessor;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 11/21/21 09:51
 */
@Data
public abstract class PipeOperator implements Closeable {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected StreamInfra streamInfra;

    protected Iterator<String []> inputItor;

    protected CsvWriter writer;

    protected DataProcessor dp;

    private PipeOperator nextPipeOperator;

    public PipeOperator() throws IOException {
        streamInfra = new StreamInfra();
    }

    public PipeOperator(Iterator<String[]> inputItor) throws IOException {
        this();
        this.inputItor = inputItor;
        this.writer = CsvConfig.createWriter(this.streamInfra.getDataOutStream());
    }

    public PipeOperator(Iterator<String[]> inputItor, DataProcessor dp) throws IOException {
        this(inputItor);
        this.dp = dp;
    }

    public void processData() {
        execute();
        if (nextPipeOperator != null) {
            nextPipeOperator.processData();
        }
    }

    protected abstract void execute();

    @Override
    public void close() {
        try {
            if (this.streamInfra.getDataInStream() != null) {
                this.streamInfra.getDataInStream().close();
            }
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }
}