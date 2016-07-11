package colourshift.model.blocks;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SourceManager {
	private Set<Source> sources = new HashSet<>();
	
	public void add(Source source) {
		sources.add(source);
	}
	
	public void activateAll() {
		sources.stream()
		.forEach(source -> source.activate());
	}
}
