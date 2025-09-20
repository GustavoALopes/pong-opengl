package dev.gustavo.components.text;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL40;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TextureAtlas {

    private static final float SECURITY_HEIGHT_SPACE = 1.4f;

    private static final int ASCII_STARTER_CHARACTER = 32;

    private static final int ASCII_DEL_CHARACTER = 127;

    private static final int ASCII_LAST_CHARACTER = 256;

    public final Texture texture;

    private final Map<Character, Glyph> glyphs;

    public TextureAtlas(
            final Map<Character, Glyph> glyphs,
            final Texture texture
    ) {
        this.glyphs = glyphs;
        this.texture = texture;
    }

    public Glyph getGlyph(final char character) {
        return this.glyphs.get(character);
    }

    public static TextureAtlas create(final int fontSize) throws URISyntaxException {
        return create(Font.MONOSPACED, fontSize);
    }

    public static TextureAtlas create(final String fontChosen, final int fontSize) throws URISyntaxException {
        if(Objects.isNull(fontChosen) || fontChosen.isEmpty()) {
            return create(fontSize);
        }

        final var font = new Font(fontChosen, Font.PLAIN, fontSize);
        final var glyphs = new HashMap<Character, Glyph>(127);

        final var fontBitMapTest = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        final var fakeGraph = fontBitMapTest.createGraphics();
        fakeGraph.setFont(font);
        final var fontMetrics = fakeGraph.getFontMetrics();

        final var estimatedWidth = (int)Math.sqrt(font.getNumGlyphs()) * font.getSize() + 1;

        var width = 0;
        var height = fontMetrics.getHeight();
        var lineHeight = fontMetrics.getHeight();
        int x = 0;
        int y = (int)(fontMetrics.getHeight() * SECURITY_HEIGHT_SPACE);

        for (int i = ASCII_STARTER_CHARACTER; i < ASCII_LAST_CHARACTER; i++) {
             if(font.canDisplay(i)) {
                 final var glyph = Glyph.create(x, y, fontMetrics.charWidth(i), fontMetrics.getHeight());
                 glyphs.put((char)i, glyph);
                 width = Math.max(x + fontMetrics.charWidth(i), width);

                 x += glyph.width;
                 if(x > estimatedWidth) {
                     x = 0;
                     y += fontMetrics.getHeight() * SECURITY_HEIGHT_SPACE;
                     height += fontMetrics.getHeight() * SECURITY_HEIGHT_SPACE;
                 }
             }
        }
        height += fontMetrics.getHeight() * SECURITY_HEIGHT_SPACE;
        fakeGraph.dispose();

        final var realBitMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final var graphic = realBitMap.createGraphics();
        graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphic.setFont(font);
        graphic.setColor(Color.WHITE);
        for (int i = ASCII_STARTER_CHARACTER; i < ASCII_LAST_CHARACTER; i++) {
            if(font.canDisplay(i)) {
                final var glyph = glyphs.get((char)i);
                glyph.setTextureCoords(width, height);
                graphic.drawString("" + (char)i, glyph.x, glyph.y);
            }
        }
        graphic.dispose();

//        final var file = new File("temp-3.png");
//        try {
//            ImageIO.write(realBitMap, "png", file);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        final var texture = Texture.createFromBuffer(realBitMap);
        return new TextureAtlas(glyphs, texture);
    }

    public static class Glyph {

        public final float x;

        public final float y;

        public final float width;

        public final float height;

        public final Vector2f[] textureCoords;

        private Glyph(
                final float x,
                final float y,
                final float width,
                final float height
        ) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.textureCoords = new Vector2f[2];
        }

        public void setTextureCoords(
                final int fontWidth,
                final int fontHeight
        ) {
            final float x0 = (float)x/(float)fontWidth;
            final float x1 = (x + width) / (float) fontWidth;
            final float y0 = (y - height) / (float)fontHeight;
            final float y1 = (y)/(float)fontHeight;

            textureCoords[0] = new Vector2f(x0, y1);
            textureCoords[1] = new Vector2f(x1, y0);
        }

        public static Glyph create(
                final float x,
                final float y,
                final float width,
                final float height
        ) {
            return new Glyph(
                x,
                y,
                width,
                height
            );
        }
    }

    public static class Texture {

        public final int id;

        private Texture() {
            this.id = GL40.glGenTextures();
        }

        public void bind() {
            GL40.glBindTexture(GL40.GL_TEXTURE_2D, this.id);
        }

        private void setData(final int width, final int height, final ByteBuffer buffer) {
            GL40.glTexImage2D(GL40.GL_TEXTURE_2D, 0, GL40.GL_RGBA, width, height, 0, GL40.GL_RGBA, GL40.GL_UNSIGNED_BYTE, buffer);
        }

        public static Texture create(
                final int width,
                final int height,
                final ByteBuffer data
        ) {
            final var texture = new Texture();
            texture.bind();
            GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_WRAP_S, GL40.GL_REPEAT);
            GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_WRAP_T, GL40.GL_REPEAT);

            GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MAG_FILTER, GL40.GL_LINEAR);
            GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MIN_FILTER, GL40.GL_LINEAR);

            texture.setData(width, height, data);
            return texture;
        }

        public static Texture createFromBuffer(final BufferedImage data) {
            final var pixels = new int[data.getHeight() * data.getWidth()];
            data.getRGB(0, 0, data.getWidth(), data.getHeight(), pixels, 0, data.getWidth());

            final var buffer = BufferUtils.createByteBuffer(data.getWidth() * data.getHeight() * 4);
            for (int y = 0; y < data.getHeight(); y++) {
                for (int x = 0; x < data.getWidth(); x++) {
                    final var pixel = pixels[y * data.getWidth() + x];
                    final var alphaComponent = (byte)((pixel >> 24) & 0xFF);
                    buffer.put(alphaComponent);
                    buffer.put(alphaComponent);
                    buffer.put(alphaComponent);
                    buffer.put(alphaComponent);
                }
            }
            buffer.flip();

            final var texture = create(data.getWidth(), data.getHeight(), buffer);
            buffer.clear();
            return texture;
        }

        public static Texture createFromImage(final String path) {
            ByteBuffer image;
            int width, height;
            try (final var stack = MemoryStack.stackPush()) {
                /* Prepare image buffers */
                final var w = stack.mallocInt(1);
                final var h = stack.mallocInt(1);
                final var comp = stack.mallocInt(1);

                /* Load image */
                image = STBImage.stbi_load(
                        path,
                        w,
                        h,
                        comp,
                        4
                );

                if (image == null) {
                    throw new RuntimeException("Failed to load a texture file!"
                            + System.lineSeparator() + STBImage.stbi_failure_reason());
                }

                /* Get width and height of image */
                width = w.get();
                height = h.get();
            }

            return create(width, height, image);
        }
    }
}
