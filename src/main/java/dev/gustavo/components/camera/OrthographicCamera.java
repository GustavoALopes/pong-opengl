package dev.gustavo.components.camera;

import dev.gustavo.components.Window;
import dev.gustavo.components.camera.interfaces.ICamera;
import org.joml.Matrix4f;

public class OrthographicCamera implements ICamera {


    public OrthographicCamera() {
    }

    @Override
    public Matrix4f getProjection() {
        return new Matrix4f().ortho2D(0f, Window.getWidth(), Window.getHeight(), 0f);
    }

    public static OrthographicCamera create() {
        return new OrthographicCamera();
    }
}
