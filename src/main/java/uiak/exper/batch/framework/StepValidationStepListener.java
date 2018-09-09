package uiak.exper.batch.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class StepValidationStepListener implements StepExecutionListener {

    private static final Logger LOG = LoggerFactory.getLogger(StepValidationStepListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOG.info("StepValidationStepListener : beforeStep " + stepExecution);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOG.info("StepValidationStepListener : afterStep  "  + stepExecution);
        return ExitStatus.FAILED;
    }
}
