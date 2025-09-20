package dev.gustavo.components;

import dev.gustavo.components.scenes.interfaces.IScene;
import org.lwjgl.opengl.GL;

public class Renderer {

    private Renderer() {
        GL.createCapabilities();
    }

    public static Renderer create() {
        return new Renderer();
    }

    public void render(
            final IScene scene
    ) {
        scene.render();
    }
}
