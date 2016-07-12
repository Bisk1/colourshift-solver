package colourshift.gui;

import colourshift.model.Colour;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.BlockType;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;

@Component
public class ImageProvider {

    private static final String BLOCKS_IMAGES_DIRECTORY = "blocks";
    private static final String IMAGE_TEMPLATE_EXTENSION = ".png";
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

    private static final int ORANGE_RGB = new Color(255, 128, 0).getRGB();
    private static final int PINK_RGB = new Color(255, 0, 220).getRGB();

    private static Map<BlockKey, Image> blockKeyToImage = Maps.newHashMap();

    /**
     * Must be called after internal graphics initialized (stage shown) and before images are requested
     */
    public void init() {
        loadImages();
    }

    public Image getImage(Block block) {
        BlockType type = BlockType.fromJavaClass(block.getClass());
        Angle angle = block.getAngle();
        List<Colour> colours = block.getPower().toColoursList();
        BlockKey blockKey = new BlockKey(type, angle, colours);
        return blockKeyToImage.get(blockKey);
    }

    private void loadImages() {
        String imagesRootDir = String.valueOf(ImageProvider.class.getClassLoader().getResource(BLOCKS_IMAGES_DIRECTORY));
        for (BlockType blockType : BlockType.values()) {
            String blockImagePath = imagesRootDir + File.separator + blockType.getJavaClass().getSimpleName() + IMAGE_TEMPLATE_EXTENSION;
            Image blockImage = new Image(blockImagePath);
            loadBlockImages(blockType, blockImage);
        }

    }

    private void loadBlockImages(BlockType blockType, Image blockImageTemplate) {
        BufferedImage blockImage = SwingFXUtils.fromFXImage(blockImageTemplate, null);
        for (Angle angle : blockType.getInitialAngles()) {
            if (blockType.getColoursCount() == 0) {
                addImageEntry(blockType, angle, Lists.newArrayList(), blockImage);
            } else {
                for (Colour firstColour : Colour.values()) {
                    BufferedImage imageColoredOnce = mask(blockImage, ORANGE_RGB, firstColour.getAwtColor().getRGB());
                    if (blockType.getColoursCount() == 1) {
                        addImageEntry(blockType, angle, Lists.newArrayList(firstColour), imageColoredOnce);
                    } else {
                        for (Colour secondColour : Colour.values()) {
                            BufferedImage imageColoredTwice = mask(imageColoredOnce, PINK_RGB, secondColour.getAwtColor().getRGB());
                            addImageEntry(blockType, angle, Lists.newArrayList(firstColour, secondColour), imageColoredTwice);
                        }
                    }
                }
            }
            blockImage = getRotatedImage(blockImage, Math.PI / 2);
        }
    }

    private void addImageEntry(BlockType blockType, Angle angle, List<Colour> colours, BufferedImage blockImage) {
        Image image = SwingFXUtils.toFXImage(blockImage, null);
        BlockKey blockKey = new BlockKey(blockType, angle, colours);
        blockKeyToImage.put(blockKey, image);
    }

    private static BufferedImage getRotatedImage(BufferedImage image, double angle) {
        AffineTransform transform = new AffineTransform();
        transform.translate(image.getWidth(), 0);
        transform.rotate(angle);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }

    private BufferedImage mask(BufferedImage image, int mask, int desired) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, image.getType());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int color = image.getRGB(i, j);
                if (color == mask) {
                    newImage.setRGB(i, j, desired);
                } else {
                    newImage.setRGB(i, j, color);
                }
            }
        }

        return newImage;
    }
}
