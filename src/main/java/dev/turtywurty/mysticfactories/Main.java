package dev.turtywurty.mysticfactories;

import dev.turtywurty.mysticfactories.core.GameEngine;
import dev.turtywurty.mysticfactories.core.Window;

public class Main {
    public static void main(String[] args) {
        var window = new Window.Builder()
                .setWidth(1280)
                .setHeight(720)
                .setTitle("Mystic Factories")
                .setResizable(true)
                .setVSync(true)
                .setCentered(true)
                .setDecorated(true)
                .build();

        var gameLogic = new MysticFactories();
        var gameEngine = new GameEngine(window, gameLogic);
        gameEngine.start();
    }
}
