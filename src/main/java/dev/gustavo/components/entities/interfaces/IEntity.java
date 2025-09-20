package dev.gustavo.components.entities.interfaces;

import org.joml.Matrix4f;

public interface IEntity {
    Matrix4f getModelMatrix();

    void update(final long delta);
}
