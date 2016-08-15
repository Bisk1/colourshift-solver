package colourshift.model.blocks;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
public class TargetManager implements Serializable {

	private List<Target> targets = Lists.newArrayList();

    public void add(Target target) {
		targets.add(target);
	}

    public void remove(Target target) {
        targets.remove(target);
    }
	
	public boolean areAllActive() {
		return targets.stream()
				.allMatch(Target::isActive);
	}
}
