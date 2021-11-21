package vn.tat.hexagonal.provision.pipe;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 11/21/21 09:52
 */
public class DataPipeLineManager {

    private PipeOperator headOperator;

    private PipeOperator tailOperator;

    public void addOperator(PipeOperator pipeOperator) {
        if (headOperator == null) {
            headOperator = pipeOperator;
            tailOperator = pipeOperator;
        } else {
            tailOperator.setNextPipeOperator(pipeOperator);
            tailOperator = pipeOperator;
        }
    }

    public void runPipeLine() {
        headOperator.processData();
    }
}
