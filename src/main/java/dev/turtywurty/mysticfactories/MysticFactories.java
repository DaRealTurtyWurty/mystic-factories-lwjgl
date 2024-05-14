package dev.turtywurty.mysticfactories;

import dev.turtywurty.mysticfactories.core.GameLogic;
import dev.turtywurty.mysticfactories.core.Renderer;
import dev.turtywurty.mysticfactories.core.Window;

public class MysticFactories implements GameLogic {
    private final Renderer renderer;

    public MysticFactories() {
        this.renderer = new Renderer();
    }

    @Override
    public void init(Window window) {
        this.renderer.init(window);
    }

    @Override
    public void input(Window window) {

    }

    @Override
    public void update() {

    }

    @Override
    public void render(Window window) {
        window.handleResize();

        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.renderer.render();
    }

    @Override
    public void cleanup() {
        this.renderer.cleanup();
    }
}
