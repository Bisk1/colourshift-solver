package colourshift.util;

import java.util.List;
import java.util.Set;

public class IterationUtils {
	public static <T> T getNextFromListIfInSet(T current, List<? extends T> all, Set<? extends T> available) {
		return getNextFromListIfInSet(all.indexOf(current), all, available);
	}
	
	public static <T> T getNextFromListIfInSet(int currentIndex, List<? extends T> all, Set<? extends T> available) {
		int nextIndex = (currentIndex + 1) % all.size();
		T nextCandidate = all.get(nextIndex);
		if (available.contains(nextCandidate)) {
			return nextCandidate;
		} else {
			return getNextFromListIfInSet(nextIndex, all, available);
		}
	}
}
