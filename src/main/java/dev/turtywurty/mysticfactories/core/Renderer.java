package dev.turtywurty.mysticfactories.core;

import dev.turtywurty.mysticfactories.rendering.GameObject;
import dev.turtywurty.mysticfactories.rendering.Mesh;
import dev.turtywurty.mysticfactories.rendering.Transformation;
import dev.turtywurty.mysticfactories.rendering.shader.ShaderProgram;
import dev.turtywurty.mysticfactories.rendering.shapes.Tile;
import dev.turtywurty.mysticfactories.world.World;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;


    private ShaderProgram defaultShader;
    private Transformation transformation;
    private Window window;

    private final World world = new World();

    public void init(Window window) {
        this.window = window;

        if (this.defaultShader != null) throw new IllegalStateException("Default shader already initialized!");

        this.defaultShader = new ShaderProgram();
        this.defaultShader.createShaders("default");
        this.defaultShader.link();

        this.world.getObjects().add(new Tile("bread"));

        this.transformation = new Transformation();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render() {
        clear();

        this.defaultShader.bind();
        this.defaultShader.setUniform("projectionMatrix",
                this.transformation.getProjectionMatrix(FOV, this.window.getWidth(), this.window.getHeight(), Z_NEAR,
                        Z_FAR));

        for (GameObject object : this.world.getObjects()) {
            Matrix4f worldMatrix = this.transformation.getWorldMatrix(object.getPosition(), object.getRotation(),
                    object.getScale());
            this.defaultShader.setUniform("worldMatrix", worldMatrix);
            this.defaultShader.setUniform("sampler", 0);
            object.getMesh().render();
        }

        this.defaultShader.unbind();
    }

    public void cleanup() {
        if (this.defaultShader != null) this.defaultShader.cleanup();

        this.world.getObjects().stream().map(GameObject::getMesh).forEach(Mesh::cleanup);
    }
}
