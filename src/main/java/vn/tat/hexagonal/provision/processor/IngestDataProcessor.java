package vn.tat.hexagonal.provision.processor;

import vn.tat.hexagonal.provision.pipe.IngestDataPipeOperator;
import vn.tat.hexagonal.provision.pipe.PipeOperator;
import vn.tat.hexagonal.provision.pipe.PostgresDatabasePipeOperator;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 11/21/21 21:49
 */

public class IngestDataProcessor implements DataProcessor{

    @Override
    public void process(PipeOperator pipeOperator) {
        if (pipeOperator instanceof IngestDataPipeOperator) {
            IngestDataPipeOperator operator = (IngestDataPipeOperator) pipeOperator;
            CompletableFuture.supplyAsync(operator::ingestEnrichData, Executors.newFixedThreadPool(1));
        }
    }
}
