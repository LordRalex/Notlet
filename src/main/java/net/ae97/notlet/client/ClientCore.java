/*
 * The MIT License
 *
 * Copyright 2015 Joshua.
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

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ae97.notlet.client.frames.LoginFrame;
import net.ae97.notlet.logging.LoggerFactory;
import net.ae97.notlet.logging.LoggerStream;

public class ClientCore {

    private static final Logger logger = LoggerFactory.create("Core");

    public static void init() {
        LoggerStream out = new LoggerStream(System.out, LoggerFactory.create("STDOUT"), Level.INFO);
        LoggerStream err = new LoggerStream(System.err, LoggerFactory.create("STDERR"), Level.SEVERE);
        System.setOut(out);
        System.setErr(err);
    }

    public static void start() {
        EventQueue.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }

    public static Logger getLogger() {
        return logger;
    }

}
