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
package net.ae97.notlet.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    private static Database INSTANCE;

    public synchronized static void init(String host, int port, String database, String username, String password) {
        if (INSTANCE != null) {
            throw new IllegalStateException("Database has already been initialized");
        }
        INSTANCE = new Database(host, port, database, username, password);
    }

    public static Connection openConnection() throws SQLException {
        return INSTANCE.open();
    }

    public static void execute(String query, Object... params) throws SQLException {
        try (Connection conn = openConnection()) {
            PreparedStatement stmt = prepareStatement(conn, query, params);
            stmt.execute();
        }
    }

    public static ResultSet executeWithResults(String query, Object... params) throws SQLException {
        try (Connection conn = openConnection()) {
            PreparedStatement stmt = prepareStatement(conn, query, params);
            return stmt.executeQuery();
        }
    }

    private static PreparedStatement prepareStatement(Connection conn, String query, Object... params) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt;
    }

    private final String host, database, user, pass;
    private final int port;

    private Database(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = username;
        this.pass = password;
    }

    private Connection open() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, pass);
    }

}
