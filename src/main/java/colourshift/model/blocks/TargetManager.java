package colourshift.model.blocks;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TargetManager {

	private Set<Target> targets = new HashSet<>();
	
	public void add(Target target) {
		targets.add(target);
	}
	
	public boolean areAllActive() {
		return targets.stream()
		.allMatch(target -> target.isActive());
	}
}
