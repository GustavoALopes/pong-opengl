package dev.gustavo.components;

import org.lwjgl.glfw.GLFW;

public class Tick {

    private static float delta;

    private static float lastFrame;

    private Tick() {}

    public static void update() {
        final var now = GLFW.glfwGetTime();
        delta = (float)(now - lastFrame);
        lastFrame = (float)now;
    }

    public static float getDelta() {
        return delta;
    }
}
