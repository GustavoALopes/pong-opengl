package dev.gustavo.components;

import org.lwjgl.glfw.GLFW;

public class Keyboard {

    private static final boolean[] state = new boolean[348];

    private Keyboard() {
    }

    public static void bind() {
        Window.bindKeyboard(Keyboard::callback);
    }

    private static void callback(
            final long windowId,
            final int key,
            final int scanCode,
            final int action,
            final int mods
    ) {
        if(GLFW.GLFW_PRESS == action) {
            state[key] = true;
        } else if(GLFW.GLFW_RELEASE == action){
            state[key] = false;
        }
    }

    public static boolean isDown(final Key key) {
        return state[key.value];
    }

    public enum Key {
        W(GLFW.GLFW_KEY_W),
        S(GLFW.GLFW_KEY_S),
        A(GLFW.GLFW_KEY_A),
        D(GLFW.GLFW_KEY_D);

        final int value;

        Key(final int value) {
            this.value = value;
        }
    }
}
