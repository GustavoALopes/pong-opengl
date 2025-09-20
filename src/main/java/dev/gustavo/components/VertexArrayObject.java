package dev.gustavo.components;

import org.lwjgl.opengl.GL40;

public class VertexArrayObject {

    private final int id;

    private VertexArrayObject() {
        this.id = GL40.glGenVertexArrays();
    }

    public void bind() {
        GL40.glBindVertexArray(this.id);
    }

    public static VertexArrayObject create() {
        return new VertexArrayObject();
    }
}
