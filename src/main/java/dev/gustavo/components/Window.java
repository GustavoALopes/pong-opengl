package dev.gustavo.components;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

public class Window {

    private static long id;

    private static int width;

    private static int height;

    private static boolean initialized;

    private Window() {
        throw new RuntimeException("Cannot instantiate a Window.");
    }

    public static void create(
            final String title,
            final int width,
            final int height
    ) {
        init(
            title,
            width,
            height
        );
    }

    private static void init(
            final String title,
            final int width,
            final int height
    ) {
        if(!GLFW.glfwInit()) {
            throw new IllegalStateException("GLFW have some trouble to initialize.");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);

        final var windowId = GLFW.glfwCreateWindow(
                width,
                height,
                title,
                0,
                0
        );

        if(windowId <= 0) {
            throw new IllegalStateException("Window cannot be created");
        }

        id = windowId;
        Window.width = width;
        Window.height = height;
        initialized = true;

        GLFW.glfwMakeContextCurrent(id);
        GLFW.glfwSwapBuffers(id);

        GLFW.glfwShowWindow(id);

        GLFW.glfwSwapInterval(1);

        Keyboard.bind();
    }

    public static boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(Window.id);
    }

    public static long getId() {
        return Window.id;
    }

    public static float getWidth() {
        return Window.width;
    }

    public static float getHeight() {
        return Window.height;
    }

    public static void bindKeyboard(
            final GLFWKeyCallbackI keyboardCallback
    ) {
        GLFW.glfwSetKeyCallback(
                Window.id,
                keyboardCallback
        );
    }
}
