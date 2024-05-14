package dev.turtywurty.mysticfactories.core;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class GameEngine {
    private final Window window;
    private final GameLogic gameLogic;


    public GameEngine(Window window, GameLogic gameLogic) {
        this.window = window;
        this.gameLogic = gameLogic;
    }

    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            cleanup();
        }
    }

    public void start() {
        run();
    }

    protected void input() {
        this.gameLogic.input(window);
    }

    protected void update() {
        this.gameLogic.update();
    }

    protected void render() {
        this.gameLogic.render(window);
        this.window.update();
    }

    protected void init() throws Exception {
        this.window.setup();
        this.gameLogic.init(this.window);
    }

    protected void cleanup() {
        this.gameLogic.cleanup();
    }

    private void gameLoop() {
        double secsPerUpdate = 1.0 / 30.0;
        double previousTime = glfwGetTime();
        double steps = 0.0;

        while(!this.window.shouldClose()) {
            double startTime = glfwGetTime();
            double elapsed = startTime - previousTime;
            previousTime = startTime;
            steps += elapsed;

            input();

            while(steps >= secsPerUpdate) {
                update();
                steps -= secsPerUpdate;
            }

            render();
            sync(startTime);
        }
    }

    private void sync(double startTime) {
        float loopSlot = 1f / 50;
        double endTime = startTime + loopSlot;
        while (glfwGetTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
