package uiak.exper.batch.framework;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.yaml.snakeyaml.Yaml;
import uiak.exper.batch.model.Invoice;
import uiak.exper.batch.model.Invoices;

import java.io.InputStream;

public class InvoiceItemReaderFromYaml implements ItemReader<Invoice> {

    private Invoices invoices;
    private int nextInvoiceIndex;

    public InvoiceItemReaderFromYaml() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("invoices-data.yml");
        invoices = yaml.loadAs(inputStream, Invoices.class);
        nextInvoiceIndex = 0;
    }

    @Override
    public Invoice read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Invoice nextInvoice = null;

        if (nextInvoiceIndex < invoices.invoices.size()) {
            nextInvoice = invoices.invoices.get(nextInvoiceIndex);
            nextInvoiceIndex++;
        }

        return nextInvoice;
    }
}
