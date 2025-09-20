package dev.gustavo;

import dev.gustavo.components.Engine;
import dev.gustavo.components.scenes.MainGameScene;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(final String[] args) throws URISyntaxException, IOException {
        final var engine = Engine.create();
        engine.setStartScene(MainGameScene.create());
        engine.start();
    }
}