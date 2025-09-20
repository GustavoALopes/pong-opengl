package dev.gustavo.components.entities;

import dev.gustavo.components.Program;
import dev.gustavo.components.Shader;
import org.lwjgl.opengl.GL40;

public class EntityProgram extends Program {

    private final int POSITION_VERTEX_LAYOUT = 0;
    private final int COLOR_VERTEX_LAYOUT = 1;

    public EntityProgram() {
        super();
    }

    public void loadEntity(final Entity entity) {
        this.use();

        final var vao = entity.getVao();
        vao.bind();

        final var vbo = entity.getVbo();
        vbo.bind();

        this.defineLocationProperty();
        this.defineColorProperty();

        vbo.updateData(entity.getData());
    }

    private void defineColorProperty() {
        GL40.glEnableVertexAttribArray(COLOR_VERTEX_LAYOUT);
        GL40.glVertexAttribPointer(
                COLOR_VERTEX_LAYOUT,
                3,
                GL40.GL_FLOAT,
                false,
                Float.BYTES * 6,
                Float.BYTES * 3
        );
    }

    private void defineLocationProperty() {
        GL40.glEnableVertexAttribArray(POSITION_VERTEX_LAYOUT);
        GL40.glVertexAttribPointer(
                POSITION_VERTEX_LAYOUT,
                3,
                GL40.GL_FLOAT,
                false,
                Float.BYTES * 6,
                0
        );
    }

    public static EntityProgram create(
            final Shader vertexShader,
            final Shader fragmentShader
    ) {
        final var program = new EntityProgram();
        program.addShader(vertexShader);
        program.addShader(fragmentShader);
        program.compile();

        return program;
    }
}
