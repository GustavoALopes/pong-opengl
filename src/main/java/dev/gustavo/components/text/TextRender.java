package dev.gustavo.components.text;

import org.lwjgl.opengl.GL40;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.Objects;

public class TextRender {

    private final TextureAtlas atlas;

    private final TextProgram program;

    //25 quad
    private static int BATCH_SIZE = 100;

    private static int VERTEX_SIZE = 7;

    private float[] vertices = new float[BATCH_SIZE * VERTEX_SIZE];

    private int size = 0;

    private TextRender(final TextureAtlas atlas) throws URISyntaxException {
        this.atlas = atlas;
        this.program = TextProgram.create(
                (long) VERTEX_SIZE * BATCH_SIZE,
                BATCH_SIZE
        );

        GL40.glEnable(GL40.GL_BLEND);
        GL40.glBlendFunc(GL40.GL_SRC_ALPHA, GL40.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void drawText(
            final String content,
            final float x,
            final float y,
            final Color color
    ) {
        var currentX = x;
        var currentY = y;

        final var rgb = color.getRGB();//0xFF00AB0;
        final var r = color.getRed();//(float)((rgb >> 16) & 0xFF) / 255.0f;
        final var g = color.getGreen();//(float)((rgb >> 8) & 0xFF) / 255.0f;
        final var b = color.getBlue();//(float)(rgb & 0xFF) / 255.0f;

        for (int i = 0; i < content.length(); i++) {
            final var character = content.charAt(i);
            final var glyph = this.atlas.getGlyph(character);
            if(Objects.isNull(glyph)) {
                System.out.printf("Unknown characters %s\n", character);
                continue;
            }

            if(this.size == BATCH_SIZE - 4) {
                this.flush();
            }

            final var xPos = currentX;
            final var yPos = currentY;
            this.loadGlyph(glyph, xPos, yPos, r, g, b);

            currentX += glyph.width;
        }
        this.flush();
    }

    protected void loadGlyph(
            final TextureAtlas.Glyph glyph,
            final float xPos,
            final float yPos,
            final float r,
            final float g,
            final float b
    ) {
        final var bottomL = xPos;
        final var topL = yPos;
        final var bottomR = xPos + 1 * glyph.width;
        final var topR = yPos + 1 * glyph.height;

        final float textureBottomL = glyph.textureCoords[0].x;
        final float textureTopL = glyph.textureCoords[0].y;
        final float textureBottomR = glyph.textureCoords[1].x;
        final float textureTopR = glyph.textureCoords[1].y;

        var index = size * VERTEX_SIZE;
        this.vertices[index] = bottomR;
        this.vertices[index + 1] = topL;

        this.vertices[index + 2] = r;
        this.vertices[index + 3] = g;
        this.vertices[index + 4] = b;

        this.vertices[index + 5] = textureBottomR;
        this.vertices[index + 6] = textureTopL;

        index += 7;
        this.vertices[index] = bottomR;
        this.vertices[index + 1] = topR;

        this.vertices[index + 2] = r;
        this.vertices[index + 3] = g;
        this.vertices[index + 4] = b;

        this.vertices[index + 5] = textureBottomR;
        this.vertices[index + 6] = textureTopR;

        index += 7;
        this.vertices[index] = bottomL;
        this.vertices[index + 1] = topR;

        this.vertices[index + 2] = r;
        this.vertices[index + 3] = g;
        this.vertices[index + 4] = b;

        this.vertices[index + 5] = textureBottomL;
        this.vertices[index + 6] = textureTopR;

        index += 7;
        this.vertices[index] = bottomL;
        this.vertices[index + 1] = topL;

        this.vertices[index + 2] = r;
        this.vertices[index + 3] = g;
        this.vertices[index + 4] = b;

        this.vertices[index + 5] = textureBottomL;
        this.vertices[index + 6] = textureTopL;

        size += 4;
    }

    public void flush() {
        //Clear and update the buffer
        this.program.uploadData(
                VERTEX_SIZE * BATCH_SIZE,
                this.vertices
        );

        //Draw
        this.program.prepareDraw(this.atlas.texture);

        GL40.glDrawElements(GL40.GL_TRIANGLES, size * 6, GL40.GL_UNSIGNED_INT, 0);
        this.size = 0;
    }

    public static TextRender create(
        final TextureAtlas atlas
    ) throws URISyntaxException {
        return new TextRender(atlas);
    }
}
