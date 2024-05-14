package dev.turtywurty.mysticfactories.rendering;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private final int vao;
    private final int posVbo, idxVbo, tCoordVbo;
    private final int vertexCount;
    private final Texture texture;

    public Mesh(float[] positions, float[] texCoords, int[] indices, Texture texture) {
        this.vertexCount = indices.length;
        this.texture = texture;

        FloatBuffer posBuffer = null;
        FloatBuffer tCoordBuffer = null;
        IntBuffer idxBuffer = null;
        try {
            this.vao = glGenVertexArrays();
            glBindVertexArray(this.vao);

            this.posVbo = glGenBuffers();
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, this.posVbo);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            this.tCoordVbo = glGenBuffers();
            tCoordBuffer = MemoryUtil.memAllocFloat(texCoords.length);
            tCoordBuffer.put(texCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, this.tCoordVbo);
            glBufferData(GL_ARRAY_BUFFER, tCoordBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            this.idxVbo = glGenBuffers();
            idxBuffer = MemoryUtil.memAllocInt(indices.length);
            idxBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.idxVbo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, idxBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if(posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }

            if(idxBuffer != null) {
                MemoryUtil.memFree(idxBuffer);
            }

            if(tCoordBuffer != null) {
                MemoryUtil.memFree(tCoordBuffer);
            }
        }
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(this.posVbo);
        glDeleteBuffers(this.idxVbo);
        glDeleteBuffers(this.tCoordVbo);

        glDeleteVertexArrays(this.vao);
    }

    public void render() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this.texture.getTextureId());

        glBindVertexArray(this.vao);
        glDrawElements(GL_TRIANGLES, this.vertexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }
}
