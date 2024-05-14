package dev.turtywurty.mysticfactories.rendering.shapes;

import dev.turtywurty.mysticfactories.rendering.GameObject;
import dev.turtywurty.mysticfactories.rendering.Mesh;
import dev.turtywurty.mysticfactories.rendering.Texture;

public class Tile extends GameObject {
    private static final float[] POSITIONS = new float[]{-0.5f, 0.5f, -1.0f, -0.5f, -0.5f, -1.0f, 0.5f, -0.5f, -1.0f, 0.5f, 0.5f, -1.0f};
    private static final int[] INDICES = new int[]{0, 1, 3, 3, 1, 2};
    private static final float[] TEXTURE_COORDS = new float[]{0, 0, 1, 0, 1, 1, 0, 1};

    public Tile(String name) {
        super(new Mesh(POSITIONS, TEXTURE_COORDS, INDICES, new Texture(name)));
    }
}
