package dev.gustavo.components;

import org.lwjgl.opengl.GL40;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Shader {

    private final int id;

    private final Type type;

    public Shader(final Type type) {
        this.id = GL40.glCreateShader(type.value);
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    private void compile(
        final File source
    ) {
        try(final var inputSource = new FileInputStream(source)) {
            final var code = new String(inputSource.readAllBytes(), StandardCharsets.UTF_8);
            GL40.glShaderSource(this.id, code);
            GL40.glCompileShader(this.id);

            if(GL40.glGetShaderi(this.id, GL40.GL_COMPILE_STATUS) != GL40.GL_TRUE) {
                throw new IllegalStateException(String.format("Shader: cannot compile shader cause: %s", GL40.glGetShaderInfoLog(this.id)));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Shader create(
            final Type type,
            final File source
    ) {
        final var shader = new Shader(type);
        shader.compile(source);
        return shader;
    }

    public enum Type {
        VERTEX(GL40.GL_VERTEX_SHADER),
        FRAGMENT(GL40.GL_FRAGMENT_SHADER);

        private final int value;

        Type(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
