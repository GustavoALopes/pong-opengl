package dev.gustavo.components.entities;

import dev.gustavo.components.Window;
import org.joml.Matrix4f;

public class Ball extends Entity {

    private final Position position;

    private Position acceleration;

    public Ball(final float[] data) {
        super(data);
        this.position = Position.create();
        this.acceleration = Position.create();
        this.acceleration.y = 1;
        this.acceleration.x = 1;
    }

    public static Ball create(
    ) {
        return new Ball(new float[] {
                //Bottom
                -.1f, .1f, 0f, 0f, 1f, 0f,
                -.1f, -.1f, 0f, 0f, 1f, 0f,
                .1f, -.1f, 0f, 0f, 1f, 0f,
                //Top
                .1f, -.1f, 0f, 0f, 1f, 0f,
                .1f, .1f, 0f, 0f, 1f, 0f,
                -.1f, .1f, 0f, 0f, 1f, 0f
        });
    }

    @Override
    public void update(final long delta) {
        if(position.y > Window.getHeight() || position.y < 0) {
            this.acceleration.y *= -1;
        }

        position.x += (float) (this.acceleration.x * 1.5);
        position.y += (float) (this.acceleration.y * 1.5);

        if(position.x > Window.getWidth() || position.x < 0) {
            this.reset();
        }

//        System.out.printf("Current ball position x: %s and y: %s\n", position.x, position.y);


        this.model
                .identity()
                .translate(this.position.x, this.position.y, 0)
                .scale(100f, 100f, 1f);
    }

    private void reset() {
        this.position.x = Window.getWidth()/2;
        this.position.y = Window.getHeight()/2;
        this.acceleration.x *= -1;
    }

    @Override
    public Matrix4f getModelMatrix() {
        return this.model;
    }

    public Position getPosition() {
        return this.position;
    }

    public void invertAcceleration() {
        this.acceleration.x *= -1;
    }


    static class Position {
        protected float x;
        protected float y;

        private Position(
                final float x,
                final float y
        ) {
            this.x = x;
            this.y = y;
        }

        float getX() {
            return x;
        }

        float getY() {
            return y;
        }

        public static Position create() {
            return new Position(Window.getWidth()/2, Window.getHeight()/2);
        }
    }
}
