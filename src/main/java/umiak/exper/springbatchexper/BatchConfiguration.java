package umiak.exper.springbatchexper;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import uiak.exper.batch.framework.CustomJobExecListener;
import uiak.exper.batch.framework.InvoiceDBStoreItemWriter;
import uiak.exper.batch.framework.InvoiceItemReaderFromYaml;
import uiak.exper.batch.framework.InvoiceSummaryReportTaskLet;
import uiak.exper.batch.model.Invoice;
import uiak.exper.batch.model.Product;
import uiak.exper.batch.store.InvoiceRepository;

@Configuration
@EnableBatchProcessing
@EnableJpaRepositories("uiak.exper.batch.store")
@EntityScan("uiak.exper.batch.model")
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private InvoiceRepository invoiceRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("file:/tmp/raw-*.csv")
    private Resource[] inputCSVresources;

    @Value("file:/tmp/clean-products.csv")
    private Resource outputCSVresources;


    @Bean
    ItemReader<Invoice> invoiceSourceDataReader() {
        return new InvoiceItemReaderFromYaml();
    }

    @Bean
    public InvoiceDBStoreItemWriter<Invoice> dbWriter() {
        return new InvoiceDBStoreItemWriter<Invoice>(invoiceRepo);
    }

    // try : Split, Decision, multistep, Flow
    // prevent restart : don't restart failed job where it failed. always start new instance
    //Job
    /*
    @Bean
    public Job fileLoadJob() {
        return jobBuilderFactory.get("invoiceLoadJob")
                .incrementer(new RunIdIncrementer())
                .listener(new CustomJobExecListener())
                .preventRestart()
                .flow(loadFileToDbStep())
                .end()
                .build();
    }
    */

    @Bean
    public Job invoiceLoadJob() {
        return jobBuilderFactory.get("invoiceLoadJob")
                .incrementer(new RunIdIncrementer())
                .listener(new CustomJobExecListener())
                .preventRestart()
                .start(invoiceLoadStep())
                .next(invoiceSummaryStep())
                .next(rawProductDataAggregatorStep())
                .build();
    }

    // Step

    @Bean
    public Step invoiceLoadStep() {
        return stepBuilderFactory.get("invoiceLoadStep")
                .<Invoice, Invoice> chunk(2)
                .reader(invoiceSourceDataReader())
                .processor(new PassThroughItemProcessor())
                .writer(dbWriter())
                .build();
    }

    @Bean
    public Step invoiceSummaryStep() {
        return this.stepBuilderFactory.get("invoiceSummaryStep")
                .tasklet(fileDeletingTasklet())
                .build();
    }

    @Bean
    public InvoiceSummaryReportTaskLet fileDeletingTasklet() {
        InvoiceSummaryReportTaskLet tasklet = new InvoiceSummaryReportTaskLet(jdbcTemplate);
        return tasklet;
    }

    @Bean
    public MultiResourceItemReader rawProductCSVReader() {
        return new MultiResourceItemReaderBuilder<Product>()
                .name("rawProductCSVReader")
                .delegate(flatFileItemReader())
                .resources(inputCSVresources )
                .build();
    }


    @Bean
    public FlatFileItemReader<Product> flatFileItemReader() {
        return new FlatFileItemReaderBuilder<Product>()
                .name("rawProductReader")
                .delimited()
                .names(new String[]{"id", "sku", "quantity", "description", "price"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Product>() {
                    {
                    setTargetType(Product.class);
                    }
                }
                )
                .build();
    }

    @Bean
    public ItemWriter<Product> cleanProductCSVWriter() {
        return new FlatFileItemWriterBuilder<Product>()
                .name("cleanProductCSVWriter")
                .resource(outputCSVresources)
                .lineAggregator(new DelimitedLineAggregator<Product>() {
                    {
                        setDelimiter("|");
                        setFieldExtractor(new BeanWrapperFieldExtractor<Product>() {
                            {
                                setNames(new String[] {"id", "sku", "quantity", "description", "price"});
                            }
                        });
                    }
                })
                .build();
    }

    @Bean
    public Step rawProductDataAggregatorStep() {
        return stepBuilderFactory.get("rawProductDataAggregatorStep")
                .<Invoice, Invoice> chunk(2)
                .reader(rawProductCSVReader())
                .processor(new PassThroughItemProcessor())
                .writer(cleanProductCSVWriter())
                .build();
    }

    /*
    @Bean
    public Step simpleStep() {
        return stepBuilderFactory.get("simpleStep")
                .<Invoice, Invoice> chunk(1)
                .reader(invoiceSourceDataReader())
                .processor(new PassThroughItemProcessor())
                .writer(new ConsoleItemWriter<Invoice>())
                .build();
    }

    // Removed after adding JPA
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