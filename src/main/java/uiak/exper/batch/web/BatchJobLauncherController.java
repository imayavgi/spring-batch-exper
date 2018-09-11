package uiak.exper.batch.web;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
public class BatchJobLauncherController {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobRegistry jobRegistry;


    @GetMapping("/launchLoadInvoices")
    public String loadInvoice(
            @RequestParam(value="job_name", defaultValue="invoiceLoadJob") String jobName,
            @RequestParam(value="debt_name", defaultValue="IT") String deptName,
            @RequestParam(value="run_on_date") String runOnDate
    )  {
        try {
            Properties props = new Properties();
            props.put("DEPARTMENT_NAME", deptName);
            props.put("RUN_ON_DATE", runOnDate);
            DefaultJobParametersConverter jparmcoverter = new DefaultJobParametersConverter();
            JobParameters jobParameters = jparmcoverter.getJobParameters(props);

            Job job = jobRegistry.getJob(jobName);
            if (job == null)
                return "ERROR : Request job " + jobName + " is not registered ...";

            JobExecution execution = jobLauncher.run(job, jobParameters);

            return "Just launched Job " + job.getName() + " with execution id of " + execution.getJobId();


        } catch (Exception e) {
            return "ERROR : Failed to launch job due to " + e.getMessage() ;
        }

    }

}
