package vn.tat.hexagonal.provision.pipe;

import vn.tat.hexagonal.provision.processor.DataProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 11/21/21 21:51
 */
public class IngestDataPipeOperator  extends PipeOperator {

    public IngestDataPipeOperator(Iterator<String[]> input, DataProcessor dataProcessor) throws IOException {
        super(input, dataProcessor);
    }

    @Override
    protected void execute() {
        this.dp.process(this);
    }

    public Void ingestEnrichData() {
        while (inputItor.hasNext()) {
            String[] data = inputItor.next();
            String[] result = Arrays.stream(data).map(x -> x + " ingrest addition").collect(Collectors.toList()).toArray(String[]::new);
            writer.writeRow(result);
        }
        writer.close();
        return null;
    }
}