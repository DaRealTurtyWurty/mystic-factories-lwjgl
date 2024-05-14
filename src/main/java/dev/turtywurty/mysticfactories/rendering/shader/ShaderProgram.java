package dev.turtywurty.mysticfactories.rendering.shader;

import dev.turtywurty.mysticfactories.utils.ResourceUtils;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public ShaderProgram(){
        this.programId = glCreateProgram();
        if(this.programId == 0) {
            throw new RuntimeException("Could not create Shader");
        }
    }

    public void createVertexShader(String name) {
        String path = "/assets/mysticfactories/shaders/vertex/" + name + ".vert";
        this.vertexShaderId = createShader(ResourceUtils.loadResource(path), GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String name) {
        String path = "/assets/mysticfactories/shaders/fragment/" + name + ".frag";
        this.fragmentShaderId = createShader(ResourceUtils.loadResource(path), GL_FRAGMENT_SHADER);
    }

    public void createShaders(String name) {
        createVertexShader(name);
        createFragmentShader(name);
    }

    protected int createShader(String name, int type) {
        int shaderId = glCreateShader(type);
        if(shaderId == 0) {
            throw new RuntimeException("Error creating shader. Type: " + type);
        }

        glShaderSource(shaderId, name);
        glCompileShader(shaderId);

        if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(this.programId, shaderId);

        return shaderId;
    }

    public void link() {
        glLinkProgram(this.programId);
        if(glGetProgrami(this.programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking Shader code: " + glGetProgramInfoLog(this.programId, 1024));
        }

        if(this.vertexShaderId != 0) {
            glDetachShader(this.programId, this.vertexShaderId);
        }

        if(this.fragmentShaderId != 0) {
            glDetachShader(this.programId, this.fragmentShaderId);
        }

        glValidateProgram(this.programId);
        if(glGetProgrami(this.programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(this.programId, 1024));
        }
    }

    public void bind() {
        glUseProgram(this.programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if(this.programId != 0) {
            glDeleteProgram(this.programId);
        }
    }

    public void setUniform(String name, Matrix4f value) {
        int location = glGetUniformLocation(this.programId, name);
        if(location != -1) {
            try(MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer buffer = stack.mallocFloat(16);
                value.get(buffer);
                glUniformMatrix4fv(location, false, buffer);
            }
        }
    }

    public void setUniform(String name, int value) {
        int location = glGetUniformLocation(this.programId, name);
        if(location != -1) {
            glUniform1i(location, value);
        }
    }
}
