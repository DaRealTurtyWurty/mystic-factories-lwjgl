package dev.turtywurty.mysticfactories.rendering;

import org.joml.Vector2f;

public class GameObject {
    private final Mesh mesh;
    private final Vector2f position;
    private final Vector2f rotation;
    private float scale;

    public GameObject(Mesh mesh) {
        this.mesh = mesh;
        this.position = new Vector2f();
        this.rotation = new Vector2f();
        this.scale = 1f;
    }

    public Vector2f getPosition() {
        return this.position;
    }

    public Vector2f getRotation() {
        return this.rotation;
    }

    public float getScale() {
        return this.scale;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public void setRotation(float x, float y) {
        this.rotation.set(x, y);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Mesh getMesh() {
        return this.mesh;
    }
}
