package umiak.exper.springbatchexper;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import uiak.exper.batch.framework.ConsoleItemWriter;
import uiak.exper.batch.framework.CustomJobExecListener;
import uiak.exper.batch.framework.InvoiceItemReaderFromYaml;
import uiak.exper.batch.model.Invoice;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    ItemReader<Invoice> invoiceSourceDataReader() {
        return new InvoiceItemReaderFromYaml();
    }

    // try : Split, Decision, multistep, Flow
    // prevent restart : don't restart failed job where it failed. always start new instance
    //Job
    @Bean
    public Job simpleJob(Step simpleStep) {
        return jobBuilderFactory.get("simpleJob")
                .incrementer(new RunIdIncrementer())
                .listener(new CustomJobExecListener())
                .preventRestart()
                .flow(simpleStep)
                .end()
                .build();
    }

    // Step
    @Bean
    public Step simpleStep() {
        return stepBuilderFactory.get("simpleStep")
                .<Invoice, Invoice> chunk(1)
                .reader(invoiceSourceDataReader())
                .processor(new PassThroughItemProcessor())
                .writer(new ConsoleItemWriter<Invoice>())
                .build();
    }

    /*
    @Bean(name="dataSource")
    public DriverManagerDataSource dataSource(Environment env){
        DriverManagerDataSource dataSource= new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("batch.jdbc.driver"));
        dataSource.setUrl(env.getProperty("batch.jdbc.url"));
        dataSource.setUsername(env.getProperty("batch.jdbc.user"));
        dataSource.setPassword(env.getProperty("batch.jdbc.password"));
        return dataSource;
    }
    */

}