package dev.gustavo.components.text;

import dev.gustavo.components.*;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import java.io.File;
import java.net.URISyntaxException;

public class TextProgram extends Program {

    private final static int TEXT_POSITION_LOCATION = 0;

    private final static int COLOR_LOCATION = 1;

    private final static int TEXT_TEXTURE_COORDINATES_LOCATION = 2;

    public VertexArrayObject vao;

    public VertexBufferObject vbo;

    public final Matrix4f projection;

    private TextIndexBuffer ebo;

    private TextProgram(
        final long initialBufferSize,
        final int indexBufferSize
    ) {
        super();
        this.vao = VertexArrayObject.create();
        this.vao.bind();

        this.vbo = VertexBufferObject.create();
        this.vbo.allocateBuffer(initialBufferSize);

        this.ebo = TextIndexBuffer.create(indexBufferSize);
        this.ebo.bind();

        this.projection = new Matrix4f();
        this.projection.identity();
        this.projection.ortho(0, Window.getWidth(), Window.getHeight(), 0f, 1f, 100f);
    }

    public void uploadData(
            final int size,
            final float[] data
    ) {
        this.vbo.bind();
        this.vbo.updateDynamicData(Float.BYTES * size, data);
    }

    public void prepareDraw(final TextureAtlas.Texture texture) {
        this.use();

        GL40.glActiveTexture(GL40.GL_TEXTURE0);
        GL40.glBindTexture(GL40.GL_TEXTURE_BUFFER, texture.id);
        final var location = this.getUniformLocation("atlasTexture");
        this.updateUniformValue(location, 0);

        this.updateProjectionMatrix(this.projection);

        this.vao.bind();
    }

    @Override
    protected void afterCompile() {
        super.afterCompile();

        int stride = 7 * Float.BYTES;
        GL40.glVertexAttribPointer(TEXT_POSITION_LOCATION, 2, GL40.GL_FLOAT, false, stride, 0);
        GL40.glEnableVertexAttribArray(TEXT_POSITION_LOCATION);

        GL40.glVertexAttribPointer(COLOR_LOCATION, 3, GL40.GL_FLOAT, false, stride, 2 * Float.BYTES);
        GL40.glEnableVertexAttribArray(COLOR_LOCATION);

        GL40.glVertexAttribPointer(TEXT_TEXTURE_COORDINATES_LOCATION, 2, GL40.GL_FLOAT, false, stride, 5 * Float.BYTES);
        GL40.glEnableVertexAttribArray(TEXT_TEXTURE_COORDINATES_LOCATION);
    }

    public static TextProgram create(
        final long initialBufferSize,
        final int indexBufferSize
    ) throws URISyntaxException {
        final var program = new TextProgram(
                initialBufferSize,
                indexBufferSize
        );

        program.addShader(Shader.create(
                Shader.Type.VERTEX,
                new File(TextProgram.class.getResource("/shaders/text.vert").toURI())
        ));
        program.addShader(Shader.create(
                Shader.Type.FRAGMENT,
                new File(TextProgram.class.getResource("/shaders/text.frag").toURI())
        ));
        program.compile();
        program.use();

        return program;
    }
}
