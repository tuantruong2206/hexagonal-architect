package vn.tat.hexagonal.provision.processor;

import vn.tat.hexagonal.provision.pipe.PipeOperator;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 11/21/21 09:49
 */
public interface DataProcessor {

    public void process(PipeOperator pipeOperator);
}
