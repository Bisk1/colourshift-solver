package colourshift.model;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

public class DirectionsDivision implements Iterable<DirectionSet> {
	private Set<DirectionSet> dirsets;

	public DirectionsDivision(DirectionSet... dirsets) {
		this.dirsets = Sets.newHashSet(dirsets);
	}

	@Override
	public Iterator<DirectionSet> iterator() {
		return dirsets.iterator();
	}

	public Optional<DirectionSet> get(Direction direction) {
		for (DirectionSet directionSet : dirsets) {
			if (directionSet.contains(direction)) {
				return Optional.of(directionSet);
			}
		}
        return Optional.empty();
	}

	public Set<Direction> getDirections() {
		return dirsets.stream()
				.flatMap(DirectionSet::stream)
				.collect(Collectors.toSet());
	}
}
