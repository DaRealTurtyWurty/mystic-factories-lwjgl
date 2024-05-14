package dev.turtywurty.mysticfactories.rendering;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Transformation {
    private final Matrix4f projectionMatrix;
    private final Matrix4f worldMatrix;

    public Transformation() {
        this.projectionMatrix = new Matrix4f();
        this.worldMatrix = new Matrix4f();
    }

    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        this.projectionMatrix.identity();
        this.projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return this.projectionMatrix;
    }

    public Matrix4f getWorldMatrix(Vector2f offset, Vector2f rotation, float scale) {
        Matrix4f identity = this.worldMatrix.identity();
        identity.translate(new Vector3f(offset.x(), offset.y(), -1.0f));
        identity.rotateX((float) Math.toRadians(rotation.x()));
        identity.rotateY((float) Math.toRadians(rotation.y()));
        identity.scale(scale);

        return this.worldMatrix;
    }
}
