package colourshift.model.border;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.blocks.Block;

public class BorderMap {
	private Map<Direction, BorderView> map;
	
	public static class Builder {
		private Map<Direction, Border> borderMap;
		
		public Builder() {
			this.borderMap = Maps.newHashMap();
		}

		public void setBorder(Direction direction, Border borderView) {
			borderMap.put(direction, borderView);
		}
		
		public Border getBorder(Direction direction) {
			return borderMap.get(direction);
		}
		
		public BorderMap build(Block block) {
			Map<Direction, BorderView> borderViewMap = borderMap.entrySet().stream()
			.collect(Collectors.toMap(
							e -> e.getKey(),
							e -> e.getValue().getView(block)
					));
			return new BorderMap(borderViewMap);
		}
	}
	
	private BorderMap(Map<Direction, BorderView> map) {
		this.map = map;
	}

	public void send(Direction toDirection, Colour colour) {
		if (map.containsKey(toDirection)) {
			map.get(toDirection).send(colour);
		}
	}
	
	/**
	 * For all directions that are in the direction set, get all the colours that are sent
	 * through them and mix the colours.
	 * 
	 * @param directionSet
	 * @return mixed colour
	 */
	public Colour getColourMix(DirectionSet directionSet) {
		return map.entrySet().stream()
		.filter(directionToBorderView -> directionSet.contains(directionToBorderView.getKey()))
		.map(Map.Entry::getValue)
		.map(BorderView::getIncomingColour)
		.filter(Optional::isPresent)
		.map(Optional::get)
		.reduce(Colour.GREY, (colour1, colour2) -> colour1.plus(colour2));
	}
	

}
