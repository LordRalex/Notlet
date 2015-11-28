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
package net.ae97.notlet.server;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import net.ae97.notlet.config.Configuration;
import net.ae97.notlet.config.JsonConfiguration;
import net.ae97.notlet.logging.LoggerFactory;
import net.ae97.notlet.logging.LoggerStream;
import net.ae97.notlet.server.database.Database;

public class Main {

    static {
        LoggerStream out = new LoggerStream(System.out, LoggerFactory.create("STDOUT"), Level.INFO);
        LoggerStream err = new LoggerStream(System.err, LoggerFactory.create("STDERR"), Level.SEVERE);
        System.setOut(out);
        System.setErr(err);
    }

    public static void main(String[] args) throws IOException {

        //load a configuration containing the server information
        Configuration config = new JsonConfiguration();
        config.load(new File("config.json"));

        //initialize database connection
        String dbHost = config.getString("database.host", "localhost");
        int dbPort = config.getInt("database.port", 3306);
        String dbUser = config.getString("database.user", "notlet");
        String dbPass = config.getString("database.pass", "notlet");
        String dbDb = config.getString("database.database", "notlet");
        Database.init(dbHost, dbPort, dbDb, dbUser, dbPass);

        //test database connection
        try {
            Database.openConnection();
        } catch (SQLException ex) {
            ServerCore.getLogger().log(Level.SEVERE, "Could not connect to database", ex);
            return;
        }

        System.setProperty("javax.net.ssl.keyStore", config.getString("certificate.file", "server.jks"));
        System.setProperty("javax.net.ssl.keyStorePassword", config.getString("certificate.pass", "notlet"));

        String bindHost = config.getString("bind.host", "0.0.0.0");
        int bindPort = config.getInt("bind.port", 9687);
        ServerCore server = new ServerCore(bindHost, bindPort);

        server.start();
    }

}
