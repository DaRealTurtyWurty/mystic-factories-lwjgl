package dev.turtywurty.mysticfactories.core;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

// TODO: Setters
public class Window {
    private long handle;
    private String title;
    private int width, height;
    private boolean resizable;
    private boolean visible;
    private boolean vsync;
    private boolean centered;
    private boolean fullscreen;
    private boolean decorated;
    private boolean resized;

    private Window(Builder builder) {
        this.title = builder.title;
        this.width = builder.width;
        this.height = builder.height;
        this.resizable = builder.resizable;
        this.visible = builder.visible;
        this.vsync = builder.vsync;
        this.centered = builder.centered;
        this.fullscreen = builder.fullscreen;
        this.decorated = builder.decorated;

        init();
    }

    public long getHandle() {
        return this.handle;
    }

    private void init() {
        glfwSetErrorCallback(Callbacks.CALLBACKS.errorCallback);

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, this.visible ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, this.resizable ? GLFW_TRUE : GLFW_FALSE);

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        this.handle = glfwCreateWindow(this.width, this.height, this.title,
                this.fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
        if (this.handle == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(this.handle, Callbacks.CALLBACKS.keyCallback);

        if (this.centered) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer pWidth = stack.mallocInt(1);
                IntBuffer pHeight = stack.mallocInt(1);

                // Get the window size passed to glfwCreateWindow
                glfwGetWindowSize(this.handle, pWidth, pHeight);

                // Get the resolution of the primary monitor
                GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
                if (vidMode == null) {
                    throw new RuntimeException("Failed to get video mode");
                }

                // Center the window
                glfwSetWindowPos(this.handle, (vidMode.width() - pWidth.get(0)) / 2,
                        (vidMode.height() - pHeight.get(0)) / 2);
            }
        }

        glfwMakeContextCurrent(this.handle);

        if (this.vsync) {
            glfwSwapInterval(1);
        }

        glfwShowWindow(this.handle);

        glfwSetFramebufferSizeCallback(this.handle, (window, width, height) -> {
            Window.this.width = width;
            Window.this.height = height;
            Window.this.setResized(true);
        });
    }

    public void setup() {
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(this.handle, keyCode) == GLFW_PRESS;
    }

    public boolean isKeyReleased(int keyCode) {
        return glfwGetKey(this.handle, keyCode) == GLFW_RELEASE;
    }

    public boolean isKeyHeld(int keyCode) {
        return glfwGetKey(this.handle, keyCode) == GLFW_REPEAT;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    private void cleanup() {
        glfwFreeCallbacks(this.handle);
        glfwDestroyWindow(this.handle);

        glfwTerminate();
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null) {
            callback.free();
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isResizable() {
        return this.resizable;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public boolean isVsync() {
        return this.vsync;
    }

    public boolean isCentered() {
        return this.centered;
    }

    public boolean isFullscreen() {
        return this.fullscreen;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean isDecorated() {
        return this.decorated;
    }

    public boolean isResized() {
        return this.resized;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(this.handle);
    }

    public void update() {
        glfwSwapBuffers(this.handle);
        glfwPollEvents();
    }

    public void setClearColor(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
    }

    public void handleResize() {
        if (isResized()) {
            glViewport(0, 0, this.width, this.height);
            setResized(false);
        }
    }

    public static class Builder {
        private String title;
        private int width, height;
        private boolean resizable;
        private boolean visible;
        private boolean vsync;
        private boolean centered;
        private boolean fullscreen;
        private boolean decorated;

        public Builder() {
            this.title = "";
            this.width = 1080;
            this.height = 720;
            this.resizable = false;
            this.visible = false;
            this.vsync = false;
            this.centered = false;
            this.fullscreen = false;
            this.decorated = false;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setResizable(boolean resizable) {
            this.resizable = resizable;
            return this;
        }

        public Builder setVisible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder setVSync(boolean vsync) {
            this.vsync = vsync;
            return this;
        }

        public Builder setCentered(boolean centered) {
            this.centered = centered;
            return this;
        }

        public Builder setFullscreen(boolean fullscreen) {
            this.fullscreen = fullscreen;
            return this;
        }

        public Builder setDecorated(boolean decorated) {
            this.decorated = decorated;
            return this;
        }

        public Window build() {
            return new Window(this);
        }
    }

    public static final class Callbacks {
        public static final Callbacks CALLBACKS = new Callbacks();

        private Callbacks() {
        }

        private final GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
        private final GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true);
            }
        };
    }
}
