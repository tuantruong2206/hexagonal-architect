package vn.tat.hexagonal.provision.processor;

import vn.tat.hexagonal.provision.pipe.PipeOperator;
import vn.tat.hexagonal.provision.pipe.PostgresDatabasePipeOperator;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 11/21/21 09:49
 */
public class ReadDataProcessor implements DataProcessor {
    @Override
    public void process(PipeOperator pipeOperator) {
        if (pipeOperator instanceof PostgresDatabasePipeOperator) {
            CompletableFuture.supplyAsync(((PostgresDatabasePipeOperator)pipeOperator)::copyOut, Executors.newFixedThreadPool(1));
        }
    }
}