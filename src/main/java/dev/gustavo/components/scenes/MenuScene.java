package dev.gustavo.components.scenes;

import dev.gustavo.components.scenes.interfaces.IScene;
import dev.gustavo.components.text.TextRender;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class MenuScene implements IScene {

    private final TextRender render;

    public MenuScene(
    ) throws URISyntaxException, IOException {
        this.render = null;
//        this.render = TextRender.create();
    }

    @Override
    public void render() {
        this.render.drawText("Hello world", 10, 10, Color.RED);
    }

    public static MenuScene create() throws URISyntaxException, IOException {
        return new MenuScene();
    }
}
