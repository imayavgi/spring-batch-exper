package uiak.exper.batch.store;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uiak.exper.batch.model.Invoice;

public interface InvoiceRepository extends CrudRepository<Invoice, Integer> {
}
