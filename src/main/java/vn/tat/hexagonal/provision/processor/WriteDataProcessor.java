package vn.tat.hexagonal.provision.processor;

import vn.tat.hexagonal.provision.pipe.PipeOperator;
import vn.tat.hexagonal.provision.pipe.PostgresDatabasePipeOperator;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 11/21/21 09:49
 */
public class WriteDataProcessor implements DataProcessor {

    @Override
    public void process(PipeOperator pipeOperator) {
        if (pipeOperator instanceof PostgresDatabasePipeOperator) {
            //write data to stream
            CompletableFuture.supplyAsync(((PostgresDatabasePipeOperator) pipeOperator)::writeDataToStream, Executors.newFixedThreadPool(1));
            //write data from stream to database
            CompletableFuture.supplyAsync(((PostgresDatabasePipeOperator) pipeOperator)::copyIn, Executors.newFixedThreadPool(1));


        }
    }
}