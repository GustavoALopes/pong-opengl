package dev.gustavo.components.entities;

import dev.gustavo.components.VertexArrayObject;
import dev.gustavo.components.VertexBufferObject;
import dev.gustavo.components.entities.interfaces.IEntity;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class Entity implements IEntity {

    protected final Matrix4f model;

    private final FloatBuffer data;

    private final VertexArrayObject vao;

    private final VertexBufferObject vbo;

    public Entity(final float[] data) {
        this.data = BufferUtils.createFloatBuffer(data.length).put(data).flip();

        this.model = new Matrix4f();
        this.vao = VertexArrayObject.create();
        this.vbo = VertexBufferObject.create();
    }

    public VertexArrayObject getVao() {
        return vao;
    }

    public VertexBufferObject getVbo() {
        return vbo;
    }

    public static Entity create(
            final float[] data
    ) {
        return new Entity(data);
    }

    public FloatBuffer getData() {
        return this.data;
    }

    @Override
    public Matrix4f getModelMatrix() {
        return this.model;
    }

    @Override
    public void update(final long delta) {

    }
}
