package uiak.exper.batch.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class InvoiceSummaryReportTaskLet implements Tasklet {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceSummaryReportTaskLet.class);

    private JdbcTemplate queryTemplate;

    public InvoiceSummaryReportTaskLet(JdbcTemplate queryTemplate) {
        this.queryTemplate = queryTemplate;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        LOG.info(" Running summary step " + stepContribution + chunkContext);

        int rowCount = this.queryTemplate.queryForObject("select count(*) from spring_batch.invoice", Integer.class);

        LOG.info(" Invoice Row Count is " + rowCount);

        return null;
    }
}
