package dev.gustavo.components.entities;

import dev.gustavo.components.Keyboard;
import dev.gustavo.components.Window;

public class Paddle extends Entity {

    private static final float HEIGHT = 100;

    private final Position position;

    private final Side side;

    private final float aiSpeed;

    public Paddle(
            final float[] data,
            final Side side
    ) {
        super(data);
        this.position = Position.create(side);
        this.side = side;
        this.aiSpeed = 200f;
    }

    @Override
    public void update(
            final long delta
    ) {
        if(this.side.equals(Side.RIGHT)) {
            if (Keyboard.isDown(Keyboard.Key.W) && (this.position.y - 70) > 0) {
                this.position.y -= 10;
            } else if (Keyboard.isDown(Keyboard.Key.S) && (this.position.y + 100) < Window.getHeight()) {
                this.position.y += 10;
            }
        }


        this.model
                .identity()
                .translate(this.position.x, this.position.y, 0)
                .scale(50, 400, 0);
    }

    public void selfPlayer(final Ball ball) {
        final var targetY = ball.getPosition().getY();

        final var paddleCenterY = this.position.y + HEIGHT/ 2f;

        if(targetY > paddleCenterY) {
            this.position.y += aiSpeed;
        } else if(targetY < paddleCenterY) {
            this.position.y -= aiSpeed;
        }

        this.position.y = Math.max(0, Math.min(this.position.y, Window.getHeight() - HEIGHT));
    }

    public void checkCollision(
            final Ball ball
    ) {
//        System.out.printf("Ball position X: %s and Y: %s and Paddle is X: %s and Y: %s\n", ball.getPosition().getX(), ball.getPosition().getY(), this.position.x, this.position.y);

        if(this.side.equals(Side.RIGHT)) {
            if (ball.getPosition().getX() <= (this.position.x + 20) &&
                    ((this.position.y - 100) <= ball.getPosition().getY() && ball.getPosition().getY() <= (this.position.y + 400))
            ) {
                System.out.println("Collision detected");
                ball.invertAcceleration();
            }
        } else {
            if (ball.getPosition().x >= this.position.x - 20 &&
                    (this.position.y - 100) <= ball.getPosition().getY() && ball.getPosition().getY() <= (this.position.y + 400)) {
                ball.invertAcceleration();
            }
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
            if(side.equals(Side.RIGHT)) {
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
