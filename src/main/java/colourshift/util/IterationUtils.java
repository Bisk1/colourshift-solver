package colourshift.util;

import java.util.List;
import java.util.Set;

public class IterationUtils {
	public static <T> T getNextFromList(T current, List<? extends T> all) {
		return getNextFromList(all.indexOf(current), all);
	}

	private static <T> T getNextFromList(int currentIndex, List<? extends T> all) {
		int nextIndex = (currentIndex + 1) % all.size();
		return all.get(nextIndex);
	}

	public static <T> T getNextFromListIfInSet(T current, List<? extends T> all, Set<? extends T> available) {
		return getNextFromListIfInSet(all.indexOf(current), all, available);
	}
	
	private static <T> T getNextFromListIfInSet(int currentIndex, List<? extends T> all, Set<? extends T> available) {
		int nextIndex = (currentIndex + 1) % all.size();
		T nextCandidate = all.get(nextIndex);
		if (available.contains(nextCandidate)) {
			return nextCandidate;
		} else {
			return getNextFromListIfInSet(nextIndex, all, available);
		}
	}
}
