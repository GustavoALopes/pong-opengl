package dev.gustavo.components;

import org.lwjgl.opengl.GL40;

public class IndexBuffer {

    protected final int id;

    protected IndexBuffer() {
        this.id = GL40.glGenBuffers();
    }

    public void bind() {
        GL40.glBindBuffer(GL40.GL_ELEMENT_ARRAY_BUFFER, this.id);
    }

    public void updateDate(final int[] indexes) {
        GL40.glBufferData(GL40.GL_ELEMENT_ARRAY_BUFFER, indexes, GL40.GL_STATIC_DRAW);
    }

    public static IndexBuffer create() {
        return new IndexBuffer();
    }
}
