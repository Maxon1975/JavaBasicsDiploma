package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {
    protected int maxWidth = -1;
    protected int maxHeight = -1;
    protected double maxRatio = -1.0;
    TextColorSchema schema = new TextColorSchemaImpl();

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        double imgRatio = (double) img.getWidth() / img.getHeight();
        if (imgRatio > maxRatio && maxRatio != -1.0) {
            throw new BadImageSizeException(imgRatio, maxRatio);
        }

        int heightRatio = 1;
        int widthRatio = 1;
        if (img.getWidth() > maxWidth && maxWidth != -1) {
            widthRatio = img.getHeight() / maxHeight;
        }
        if (img.getHeight() > maxHeight && maxHeight != -1) {
            heightRatio = img.getWidth() / maxWidth;
        }
        int ratio = Integer.max(heightRatio, widthRatio);

        int newWidth = img.getWidth() / ratio;
        int newHeight = img.getHeight() / ratio;

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();

        StringBuilder result = new StringBuilder();
        int[] arrayRGB = new int[3];
        for (int h = 0; h < bwImg.getHeight(); h++) {
            for (int w = 0; w < bwImg.getWidth(); w++) {
                int color = bwRaster.getPixel(w, h, arrayRGB)[0];
                char c = schema.convert(color);
                result.append(c)
                        .append(c);
            }
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
