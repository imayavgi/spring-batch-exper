package uiak.exper.batch.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.yaml.snakeyaml.Yaml;
import uiak.exper.batch.model.Invoice;
import uiak.exper.batch.model.Invoices;

import java.io.InputStream;

public class InvoiceItemReaderFromYaml implements ItemReader<Invoice> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceItemReaderFromYaml.class);

    private Invoices invoices;
    private int nextInvoiceIndex;

    public InvoiceItemReaderFromYaml() {
        initialize();
    }

    private void initialize() {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = this.getClass()
                    .getClassLoader()
                    .getResourceAsStream("invoices-data.yml");
            invoices = yaml.loadAs(inputStream, Invoices.class);
            nextInvoiceIndex = 0;
        } catch (Exception e) {
           LOG.error("InvoiceItemReaderFromYaml : ERROR Loading Yaml source file ");
        }
    }

    @Override
    public Invoice read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        LOG.info(" InvoiceItemReaderFromYaml read START ");

        Invoice nextInvoice = null;

        if (invoices == null || invoices.invoices.size() == 0 ) {
            LOG.error(" InvoiceItemReaderFromYaml : DOES NOT HAVE DATA SEEMS ODD ");
            return null;
        }

        if (nextInvoiceIndex < invoices.invoices.size()) {
            nextInvoice = invoices.invoices.get(nextInvoiceIndex);
            nextInvoiceIndex++;
        }
        LOG.info(" InvoiceItemReaderFromYaml read END ");
        return nextInvoice;
    }
}
