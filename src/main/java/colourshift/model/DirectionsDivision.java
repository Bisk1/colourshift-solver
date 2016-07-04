package colourshift.model;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

public class DirectionsDivision implements Iterable<DirectionSet> {
	private Set<DirectionSet> dirsets;

	public DirectionsDivision(DirectionSet... dirsets) {
		this.dirsets = Sets.newHashSet(dirsets);
	}
	
	public DirectionsDivision(Set<DirectionSet> dirsets) {
		this.dirsets = dirsets;
	}
	
	public Set<DirectionSet> getDirsets() {
		return dirsets;
	}

	@Override
	public Iterator<DirectionSet> iterator() {
		return dirsets.iterator();
	}
	
	public Stream<DirectionSet> stream() {
		return dirsets.stream();
	}
}
