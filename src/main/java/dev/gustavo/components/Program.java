package dev.gustavo.components;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL40;

public class Program {

    public final int id;

    private static int MODEL_MATRIX_LOCATION;
    private static int PROJECTION_MATRIX_LOCATION;

    protected Program() {
        this.id = GL40.glCreateProgram();
    }

    public void use() {
        GL40.glUseProgram(this.id);
    }

    protected void addShader(final Shader shader) {
        GL40.glAttachShader(this.id, shader.getId());
    }

    protected void compile() {
        GL40.glLinkProgram(this.id);
        GL40.glValidateProgram(this.id);

        if(GL40.glGetProgrami(this.id, GL40.GL_LINK_STATUS) != GL40.GL_TRUE) {
            throw new IllegalStateException(String.format("Program cannot link: %s", GL40.glGetProgramInfoLog(this.id)));
        }

        this.afterCompile();
    }

    protected void updateUniformValue(
            final int location,
            final int value
    ) {
        GL40.glUniform1i(location, value);
    }

    protected void updateUniformValue(
            final int location,
            final Matrix4f value
    ) {
        final var buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        GL40.glUniformMatrix4fv(location, false, buffer);
    }

    public void updateProjectionMatrix(final Matrix4f projection) {
        this.updateUniformValue(PROJECTION_MATRIX_LOCATION, projection);
    }


    public void updateModelMatrix(final Matrix4f modelMatrix) {
        this.updateUniformValue(MODEL_MATRIX_LOCATION, modelMatrix);
    }

    protected void afterCompile() {
//        MODEL_MATRIX_LOCATION = this.getUniformLocation("model");
        PROJECTION_MATRIX_LOCATION = this.getUniformLocation("projection");
    }

    protected int getUniformLocation(final String name) {
        this.use();
        final var location = GL40.glGetUniformLocation(this.id, name);
        if(location < 0) {
            throw new IllegalArgumentException(String.format("You try to get %s uniform location, but it doesn't exists", name));
        }
        return location;
    }

    public static Program create(
            final Shader vertexShader,
            final Shader fragmentShader
    ) {
        final var program = new Program();
        program.addShader(vertexShader);
        program.addShader(fragmentShader);
        program.compile();

        return program;
    }

    public int getId() {
        return this.id;
    }
}
