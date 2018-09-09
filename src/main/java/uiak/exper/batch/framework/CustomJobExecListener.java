package uiak.exper.batch.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class CustomJobExecListener implements JobExecutionListener {

    private static final Logger LOG = LoggerFactory.getLogger(CustomJobExecListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOG.info("JOB EXECUTION STATUS  BEFORE JOB ");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        LOG.info("JOB EXECUTION STATUS  AFTER JOB " + jobExecution + " at  " +  jobExecution.getEndTime());
    }
}