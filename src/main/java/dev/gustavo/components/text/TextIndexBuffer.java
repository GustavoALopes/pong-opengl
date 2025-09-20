package dev.gustavo.components.text;

import dev.gustavo.components.IndexBuffer;


public class TextIndexBuffer extends IndexBuffer {

    private static final int TRIANGLES_VERTICES = 3;

    private static final int[] indexesBaseRef = new int[]{
      0, 1, 3,
      1, 2, 3
    };

    private TextIndexBuffer(
        final int batchSize
    ) {
        super();
        this.bind();

        final var indexes = new int[batchSize * TRIANGLES_VERTICES];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = indexesBaseRef[(i % 6)] + ((i / 6) * 4);
        }
        this.updateDate(indexes);
    }


    public static TextIndexBuffer create(
            final int batchSize
    ) {
        return new TextIndexBuffer(batchSize);
    }
}
