package uiak.exper.batch.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class NoOpTasklet implements Tasklet {

    private static final Logger LOG = LoggerFactory.getLogger(NoOpTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        LOG.info(" NoOpTasklet : execute " + stepContribution + "  " + chunkContext);
        return RepeatStatus.FINISHED;
    }
}
