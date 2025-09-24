package dev.gustavo.components;


import dev.gustavo.components.scenes.interfaces.IScene;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL40;

public class Engine {


    private final Renderer renderer;

    private IScene scene;

    public Engine() {
        Window.create(
                "Pong",
                1080,
                728
        );

        this.renderer = Renderer.create();
    }

    public static Engine create() {
        return new Engine();
    }

    public void setStartScene(final IScene scene) {
        this.scene = scene;
    }


    public void start() {
        while (!Window.shouldClose()) {
            GL40.glClearColor(0, 0, 0, 1);
            GL40.glClear(GL40.GL_COLOR_BUFFER_BIT | GL40.GL_DEPTH_BUFFER_BIT);
            Tick.update();

            this.renderer.render(this.scene);

            GLFW.glfwSwapBuffers(Window.getId());
            GLFW.glfwPollEvents();
        }
    }
}
