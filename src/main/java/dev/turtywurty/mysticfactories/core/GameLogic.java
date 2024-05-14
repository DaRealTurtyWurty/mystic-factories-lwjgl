package dev.turtywurty.mysticfactories.core;

public interface GameLogic {
    void init(Window window) throws Exception;

    void input(Window window);

    void update();

    void render(Window window);

    void cleanup();
}
