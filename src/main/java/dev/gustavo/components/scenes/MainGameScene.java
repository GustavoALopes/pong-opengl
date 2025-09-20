package dev.gustavo.components.scenes;

import dev.gustavo.components.entities.Entity;
import dev.gustavo.components.entities.EntityProgram;
import dev.gustavo.components.Shader;
import dev.gustavo.components.camera.OrthographicCamera;
import dev.gustavo.components.entities.Ball;
import dev.gustavo.components.entities.Paddle;
import dev.gustavo.components.scenes.interfaces.IScene;
import dev.gustavo.components.text.TextRender;
import dev.gustavo.components.text.TextureAtlas;
import org.lwjgl.opengl.GL40;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MainGameScene implements IScene {

    private final List<Entity> entities;

    private final EntityProgram program;

    private final Paddle leftPaddle;

    private final Paddle rightPaddle;

    private final Ball ball;

    private final OrthographicCamera camera;

    private final TextRender textRender;

    private int[] scores;

    private MainGameScene(
            final List<Entity> entities,
            final EntityProgram program,
            final Paddle leftPaddle,
            final Paddle rightPaddle,
            final Ball ball,
            final TextRender textRender
    ) {
        this.scores = new int[2];
        this.ball = ball;
        this.leftPaddle = leftPaddle;
        this.rightPaddle = rightPaddle;
        this.entities = entities;
        this.program = program;
        this.camera = new OrthographicCamera();
        this.textRender = textRender;

        entities.forEach(program::loadEntity);
    }

    @Override
    public void render() {
        this.textRender.drawText(String.valueOf(this.scores[0]), 350, 50, Color.WHITE);
        this.textRender.drawText(String.valueOf(this.scores[1]), 650, 50, Color.WHITE);

        this.program.use();

        this.update();
        this.program.updateProjectionMatrix(this.camera.getProjection());

        for (final var entity : this.entities) {
            entity.getVao().bind();
            this.program.updateModelMatrix(entity.getModelMatrix());
            GL40.glDrawArrays(GL40.GL_TRIANGLES, 0 , 12);
        }
    }

    private void update() {
        this.leftPaddle.update(0);
        this.rightPaddle.update(0);
        this.leftPaddle.selfPlayer(this.ball);
        this.ball.update(0);

        this.leftPaddle.checkCollision(this.ball);
        this.rightPaddle.checkCollision(this.ball);
    }

    public static MainGameScene create() throws URISyntaxException, IOException {
        final var program = EntityProgram.create(
                Shader.create(
                        Shader.Type.VERTEX,
                        new File(MainGameScene.class.getResource("/shaders/default.vert").toURI())
                ),
                Shader.create(
                        Shader.Type.FRAGMENT,
                        new File(MainGameScene.class.getResource("/shaders/default.frag").toURI())
                )
        );

        final var atlas = TextureAtlas.create(64);
        final var textRender = TextRender.create(atlas);

        final var entities = new ArrayList<Entity>(3);

        final var ball = Ball.create();
        final var lefPaddle = Paddle.create(Paddle.Side.LEFT);
        final var rightPaddle = Paddle.create(Paddle.Side.RIGHT);

        entities.add(ball);
        entities.add(lefPaddle);
        entities.add(rightPaddle);

        return new MainGameScene(
                entities,
                program,
                lefPaddle,
                rightPaddle,
                ball,
                textRender
        );
    }
}
