package dev.turtywurty.mysticfactories.rendering;

import dev.turtywurty.mysticfactories.utils.ResourceUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {
    private final int textureId;
    private final String filename;

    public Texture(String filename) {
        this.filename = filename;
        this.textureId = loadTexture(this.filename);
    }

    private int loadTexture(String filename) {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            String texture = ResourceUtils.getTexture(filename);
            System.out.println(texture);
            ByteBuffer buffer = stbi_load(texture, width, height, channels, 4);
            if (buffer == null) {
                throw new RuntimeException("Failed to load texture: " + filename);
            }

            int textureId = generateTextureId(width.get(), height.get(), buffer);

            stbi_image_free(buffer);

            return textureId;
        }
    }

    private int generateTextureId(int width, int height, ByteBuffer buffer) {
        int textureId = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, textureId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);

        return textureId;
    }

    public void cleanup() {
        glDeleteTextures(textureId);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public int getTextureId() {
        return this.textureId;
    }

    public String getFilename() {
        return this.filename;
    }
}
