/*
 * The MIT License
 *
 * Copyright 2015 AE97
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.ae97.notlet.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import net.ae97.notlet.client.frames.ExtractFrame;
import org.newdawn.slick.util.ResourceLoader;

public class Main {

    public static void main(String[] args) {
        System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
        ClientCore.init();
        try {
            extractLWJGL();
        } catch (IOException ex) {
            ClientCore.getLogger().log(Level.SEVERE, "Error extracting LWJGL", ex);
            JOptionPane.showMessageDialog(null, "Error extracting LWJGL");
            System.exit(1);
        }
        ClientCore.start();
    }

    private static void extractLWJGL() throws IOException {
        ExtractFrame frame = new ExtractFrame();
        frame.getProgressBar().setMaximum(nativeFiles.length);
        frame.setVisible(true);
        if (Files.exists(Paths.get("natives"))) {
            ClientCore.getLogger().warning("Natives folder exists, assuming extracted");
            frame.dispose();
            return;
        }
        Files.createDirectory(Paths.get("natives"));
        byte[] buffer = new byte[64];
        int read;
        for (String nativeFile : nativeFiles) {
            frame.getLabel().setText("Extracting " + nativeFile);
            try (InputStream resource = ResourceLoader.getResourceAsStream(nativeFile)) {
                try (FileOutputStream out = new FileOutputStream(Paths.get("natives", nativeFile).toFile())) {
                    while ((read = resource.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                        frame.getProgressBar().setValue(frame.getProgressBar().getValue() + 1);
                    }
                }
            }
        }
        frame.dispose();
    }

    private static final String[] nativeFiles = new String[]{
        "OpenAL32.dll",
        "OpenAL64.dll",
        "lwjgl.dll",
        "lwjgl64.dll",
        "liblwjgl.so",
        "liblwjgl64.so",
        "libopenal.so",
        "libopenal64.so",
        "liblwjgl.dylib",
        "openal.dylib",
        "libjinput-linux.so",
        "libjinput-linux64.so",
        "jinput-dx8.dll",
        "jinput-dx8_64.dll",
        "jinput-raw.dll",
        "jinput-raw_64.dll",
        "jinput-wintab.dll",
        "libjinput-osx.jnilib"
    };
}
