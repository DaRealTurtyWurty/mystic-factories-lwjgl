package dev.turtywurty.mysticfactories.world;

import dev.turtywurty.mysticfactories.rendering.GameObject;

import java.util.ArrayList;
import java.util.List;

public class World {
    private final List<GameObject> objects = new ArrayList<>();

    public List<GameObject> getObjects() {
        return this.objects;
    }
}
