package colourshift.gui;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Objects;

import colourshift.model.Colour;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.BlockFactory;
import colourshift.model.blocks.BlockFactory.BlockType;
import colourshift.model.blocks.SourceManager;
import colourshift.model.blocks.TargetManager;
import javafx.scene.image.Image;

public class ImageProvider {

	/**
	 * Compound key which uniquely identifies image for a block.
	 */
	private static class BlockKey {
		private BlockType blockType;
		private Angle angle;
		private List<Colour> colours;

		public BlockKey(BlockType blockType, Angle angle, List<Colour> colours) {
			this.blockType = blockType;
			this.angle = angle;
			this.colours = colours;
		}

		@Override
		public boolean equals(Object object) {
			if (object instanceof BlockKey) {
				BlockKey k = (BlockKey) object;
				return Objects.equal(blockType, k.blockType) && Objects.equal(angle, k.angle)
						&& Objects.equal(colours, k.colours);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(blockType, angle, colours);
		}
	}

	private Map<BlockKey, Image> blockKeyToImage;
	
	private ImageProvider() {
		this.loadImages();
	}
		
	public Image getImage(Block block) {
		BlockType type = BlockType.fromJavaClass(block.getClass());
		Angle angle = block.getAngle();
		List<Colour> colours = block.getPower().toColoursList();
		BlockKey blockKey = new BlockKey(type, angle, colours);
		return blockKeyToImage.get(blockKey);
	}
	
	private void loadImages() {
		BlockFactory blockFactory = new BlockFactory(new SourceManager(), new TargetManager()); // TODO: inject
		for (BlockType blockType : BlockType.values()) {
			Block example = blockFactory.createAndInitBlock(blockType, Optional.of(Colour.GREY));
		}
	}
}
