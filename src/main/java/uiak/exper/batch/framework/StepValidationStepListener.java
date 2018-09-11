package uiak.exper.batch.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.core.io.Resource;

public class StepValidationStepListener implements StepExecutionListener {

    private static final Logger LOG = LoggerFactory.getLogger(StepValidationStepListener.class);
    private Resource[] inputFileResource;

    public StepValidationStepListener(Resource[] resource) {
        inputFileResource = resource;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOG.info("StepValidationStepListener : beforeStep " + stepExecution);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOG.info("StepValidationStepListener : afterStep  "  + stepExecution);
        if (inputFileResource != null & inputFileResource.length > 0 )
            return ExitStatus.COMPLETED;
        else
            return ExitStatus.FAILED;
    }
}
