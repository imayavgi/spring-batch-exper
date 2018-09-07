package uiak.exper.batch.framework;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import uiak.exper.batch.model.Invoice;
import uiak.exper.batch.store.InvoiceRepository;

import java.util.List;


public class InvoiceDBStoreItemWriter<T> implements ItemWriter<T> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceDBStoreItemWriter.class);
    private InvoiceRepository repo;

    public InvoiceDBStoreItemWriter(InvoiceRepository repo) {
        this.repo = repo;
    }

    @Override
    public void write(List<? extends T> list) throws Exception {
        LOG.info("DB item writer starts");
        for (T item : list) {
            LOG.info("DB ITEM WRITER " + item);
            int invid = ((Invoice)item).invoice;
            if (repo.findById(invid) == null )
                repo.save((Invoice)item);
            else
                LOG.info(" FOUND Invoice with number " + invid + " Skipping ");
        }
        LOG.info("DB item writer ends");
    }


}