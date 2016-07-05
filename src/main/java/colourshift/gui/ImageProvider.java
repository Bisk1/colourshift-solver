package colourshift.gui;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import com.google.common.base.Objects;

import colourshift.model.Colour;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.BlockFactory;
import colourshift.model.blocks.BlockFactory.BlockType;
import colourshift.model.blocks.SourceManager;
import colourshift.model.blocks.TargetManager;
import com.google.common.collect.Lists;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

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

	private static Map<BlockKey, Image> blockKeyToImage;
	
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
		String imagesRootDir = String.valueOf(ImageProvider.class.getClassLoader().getResource("blocks"));
		BlockFactory blockFactory = new BlockFactory(new SourceManager(), new TargetManager()); // TODO: inject
		for (BlockType blockType : BlockType.values()) {
			String blockImagePath = imagesRootDir + File.separator + blockType.getJavaClass().getName();
            Image blockImage = new Image(blockImagePath);
			Block block = blockFactory.createAndInitBlock(blockType, Optional.of(Colour.GREY));
            loadBlockImages(blockType, block, blockImage);
		}

	}

    private void loadBlockImages(BlockType blockType, Block block, Image blockImageTemplate) {
        Image blockImage =  blockImageTemplate;
        for (Angle angle : blockType.getInitialAngles()) {
            if (blockType.getColoursCount() == 0) {
                blockKeyToImage.put(new BlockKey(blockType, angle, Lists.newArrayList()), blockImage);
            } else {
                for (Colour firstColour : Colour.values()) {
                    if (blockType.getColoursCount() == 1) {

                    }
                }
            }
            blockImage = getRotatedImage(blockImage, 90);
        }
    }

    private static Image getRotatedImage(Image bufferedImage, int angle) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(angle);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        reutrn op.filter(bufferedImage, null);
        return bufferedImage;
    }

    private static Image bufferedImageToWriteableImage(BufferedImage bufferedImage) {
        WritableImage writeableImage = null;
        if (bufferedImage != null) {
            writeableImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
            PixelWriter pw = writeableImage.getPixelWriter();
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    pw.setArgb(x, y, bufferedImage.getRGB(x, y));
                }
            }
        }
        return writeableImage;
    }
}
