package dev.gustavo.components.entities;

import dev.gustavo.components.Keyboard;
import dev.gustavo.components.Window;

import java.util.Objects;

public class Paddle extends Entity {

    private static final float HEIGHT = 100;

    private static final float WIDTH = 20;

    private final Position position;

    private final Side side;

    private final float aiSpeed;

    private Paddle(
            final float[] vertices,
            final Side side
    ) {
        super(vertices);

        this.position = Position.create(side);
        this.side = side;
        this.aiSpeed = 200f;
    }

    @Override
    public void update(
            final float delta
    ) {
        if(this.side.equals(Side.LEFT)) {
            if (Keyboard.isDown(Keyboard.Key.W) && (this.position.y - 70) > 0) {
                this.position.y -= delta * 500;
            } else if (Keyboard.isDown(Keyboard.Key.S) && (this.position.y + 100) < Window.getHeight()) {
                this.position.y += delta * 500;
            }
        }

        this.model
                .identity()
                .translate(this.position.x, this.position.y, 0)
                .scale(50, 400, 0);
    }

    public void selfPlayer(
            final float delta,
            final Ball ball
    ) {
        final var targetY = ball.getPosition().getY();

        final var paddleCenterY = (this.position.y + HEIGHT/ 2f);

        if(targetY > paddleCenterY) {
            this.position.y += delta * aiSpeed;
        } else if(targetY < paddleCenterY) {
            this.position.y -= delta * aiSpeed;
        }

        this.position.y = Math.max(0, Math.min(this.position.y, Window.getHeight() - HEIGHT));
    }

    public float getTotalWidth() {
        return Objects.equals(this.side, Side.LEFT) ? this.position.x + WIDTH : this.position.x - WIDTH;
    }

    public float getTotalHeight() {
        return this.position.y + HEIGHT;
    }

    public boolean checkCollision(
            final Ball ball
    ) {
//        final var shouldInvert = (
//                this.ball.getX() <= this.paddleLeft.getWidth() &&
//                        this.ball.getY() >= this.paddleLeft.getY() &&
//                        this.ball.getY() <= this.paddleLeft.getY() + this.paddleLeft.getHeight()) ||
//                (this.ball.getX() >= width - this.paddleLeft.getWidth() && this.ball.getY() >= this.paddleRight.getY() && this.ball.getY() <= this.paddleRight.getY() + this.paddleRight.getHeight());

        System.out.printf(
                "Ball position X: %s and Y: %s and Paddle is X: %s and Y: %s And Total Y %s\n",
                ball.getPosition().getX(),
                ball.getPosition().getY(),
                this.position.x,
                this.position.y,
                this.getTotalHeight()
        );

        if(this.side.equals(Side.LEFT)) {
            return ball.getPosition().getX() <= this.getTotalWidth() &&
                    ball.getPosition().getY() >= this.position.y &&
                    ball.getPosition().getY() <= this.getTotalHeight();
        } else {
            return ball.getPosition().x >= this.getTotalWidth() &&
                    ball.getPosition().getY() >= this.position.y &&
                    ball.getPosition().getY() <= this.getTotalHeight();
        }
    }

    public static Paddle create(
            final Side side
    ) {
        return new Paddle(new float[]{
                //Top
                -.2f, -.2f, 0, 1, 1, 1,
                -.2f, .2f, 0, 1, 1, 1,
                .2f, .2f, 0, 1, 1, 1,

                //Bottom
                .2f, .2f, 0, 1, 1, 1,
                .2f, -.2f, 0, 1, 1, 1,
                -.2f, -.2f, 0, 1, 1, 1,
        }, side);
    }

    static class Position {
        public float x;
        public float y;

        private Position(
            final Side side
        ) {
            if(side.equals(Side.LEFT)) {
                this.x += 50;
            } else {
                this.x += Window.getWidth() - 50;
            }

            this.y += 100;
        }

        static Position create(
            final Side side
        ) {
            return new Position(side);
        }
    }

    public enum Side {
        LEFT,
        RIGHT
    }
}
