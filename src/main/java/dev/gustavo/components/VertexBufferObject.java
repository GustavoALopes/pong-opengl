package dev.gustavo.components;

import org.lwjgl.opengl.GL40;

import java.nio.FloatBuffer;

public class VertexBufferObject {

    private final int id;

    private VertexBufferObject() {
        this.id = GL40.glGenBuffers();
    }

    public void bind() {
        GL40.glBindBuffer(GL40.GL_ARRAY_BUFFER, this.id);
    }

    public void updateData(final FloatBuffer data) {
        GL40.glBufferData(GL40.GL_ARRAY_BUFFER, data, GL40.GL_STATIC_DRAW);
    }

    public void updateData(final float[] data) {
        GL40.glBufferData(GL40.GL_ARRAY_BUFFER, data, GL40.GL_STATIC_DRAW);
    }

    public void updateDynamicData(
            final int size,
            final float[] data
    ) {
        GL40.glBufferData(GL40.GL_ARRAY_BUFFER, size, GL40.GL_DYNAMIC_DRAW);
        GL40.glBufferSubData(GL40.GL_ARRAY_BUFFER, 0, data);
    }

    public static VertexBufferObject create() {
        return new VertexBufferObject();
    }

    public void allocateBuffer(final long initialBufferSize) {
        this.bind();
        GL40.glBufferData(GL40.GL_ARRAY_BUFFER, Float.BYTES * initialBufferSize, GL40.GL_DYNAMIC_DRAW);
    }
}
