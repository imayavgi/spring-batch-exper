package uiak.exper.batch.framework;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import uiak.exper.batch.store.InvoiceRepository;

import java.util.List;


public class InvoiceDBStoreItemWriter<Invoice> implements ItemWriter<Invoice> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceDBStoreItemWriter.class);

    public InvoiceDBStoreItemWriter(InvoiceRepository repo) {
    }

    @Override
    public void write(List<? extends Invoice> list) throws Exception {
        LOG.trace("Console item writer starts");
        for (Invoice item : list) {
            LOG.info("CONSOLE ITEM WRITER " + item);
        }
        LOG.trace("Console item writer ends");

    }

}