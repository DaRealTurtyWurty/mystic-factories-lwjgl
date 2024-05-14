package dev.turtywurty.mysticfactories.utils;

import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class ResourceUtils {
    public static String loadResource(String fileName) {
        String result = "";
        try (InputStream in = ResourceUtils.class.getResourceAsStream(fileName)) {
            if (in != null) {
                result = IOUtils.toString(in, StandardCharsets.UTF_8);
            }
        } catch (IOException exception) {
            throw new RuntimeException("Could not load resource file: " + fileName, exception);
        }

        return result;
    }

    public static ByteBuffer loadResourceAsByteBuffer(String fileName) {
        ByteBuffer result = null;
        try (InputStream in = ResourceUtils.class.getResourceAsStream(fileName)) {
            if (in != null) {
                byte[] bytes = IOUtils.toByteArray(in);
                result = BufferUtils.createByteBuffer(bytes.length);
                result.put(bytes).flip();
            }
        } catch (IOException exception) {
            throw new RuntimeException("Could not load resource file: " + fileName, exception);
        }

        return result;
    }

    public static String getTexture(String name) {
        final Path textureFile = Paths.get(System.getProperty("java.io.tmpdir"), "mysticfactories")
                .resolve(name + ".png");

        try {
            if (Files.notExists(textureFile)) {
                final Path outputFolder = textureFile.getParent();

                if (Files.notExists(outputFolder)) {
                    System.out.println("Creating cached textures folder located at " + outputFolder + ".");
                }

                Files.createDirectories(outputFolder);

                try (final InputStream inputStream = Objects.requireNonNull(
                        ResourceUtils.class.getResourceAsStream("/assets/mysticfactories/textures/" + name + ".png"))) {
                    Files.copy(inputStream, textureFile, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Creating cached texture file located at " + textureFile + ".");
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException("Could not load texture file: " + name, exception);
        }

        return textureFile.toString();
    }
}
