package colourshift.model.blocks;

import java.util.HashSet;
import java.util.Set;

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
