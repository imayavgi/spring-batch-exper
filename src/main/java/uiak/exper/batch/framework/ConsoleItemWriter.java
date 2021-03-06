package uiak.exper.batch.framework;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import java.util.List;


public class ConsoleItemWriter<T> implements ItemWriter<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ConsoleItemWriter.class);

    @Override
    public void write(List<? extends T> items) throws Exception {
        LOG.info("Console item writer starts");
        for (T item : items) {
            LOG.info("CONSOLE ITEM WRITER " + item);
        }
        LOG.info("Console item writer ends");

    }
}