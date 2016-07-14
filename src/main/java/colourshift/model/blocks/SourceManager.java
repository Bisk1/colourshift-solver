package colourshift.model.blocks;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SourceManager {
    private List<Source> sources = Lists.newArrayList();

    public void add(Source source) {
        sources.add(source);
    }

    public void remove(Source source) {
        sources.remove(source);
    }

    public void activateAll() {
        sources.stream()
                .forEach(Source::activate);
    }
}
