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
package net.ae97.notlet.server.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import net.ae97.notlet.server.CoreServer;
import net.ae97.notlet.server.crypto.HashGenerator;
import net.ae97.notlet.server.database.Database;

public class AuthenticationEngine {

    public static boolean validate(String username, String password) {
        String hashedPw = HashGenerator.hash(password);
        try {
            try (Connection conn = Database.openConnection()) {
                PreparedStatement stmt = conn.prepareStatement("SELECT password FROM auth WHERE username = ?");
                stmt.setString(1, username);
                ResultSet set = stmt.executeQuery();
                if (set.first()) {
                    String pw = set.getString("password");
                    return hashedPw.equals(pw);
                } else {
                    return false;
                }
            }
        } catch (SQLException ex) {
            CoreServer.getLogger().log(Level.SEVERE, "Error on database connection", ex);
            return false;
        }
    }

    public static boolean register(String username, String password) {
        String hashedPw = HashGenerator.hash(password);
        try {
            try (Connection conn = Database.openConnection()) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO AUTH (?, ?)");
                stmt.setString(1, username);
                stmt.setString(2, hashedPw);
                stmt.execute();
            }
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

}
