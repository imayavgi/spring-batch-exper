package umiak.exper.springbatchexper;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import uiak.exper.batch.model.Invoice;
import uiak.exper.batch.model.Invoices;

import java.io.InputStream;


public class YamlLoaderTest {

    @Test
    public void loadSampleInvoiceFile() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("invoices-data.yml");
        Invoices invoices = yaml.loadAs(inputStream, Invoices.class);
        System.out.println(invoices);
    }
}
