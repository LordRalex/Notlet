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
package net.ae97.notlet.network.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import net.ae97.notlet.network.packets.Packet;
import org.apache.commons.lang3.SerializationUtils;

public class EncryptedPacketOutputStream implements AutoCloseable {

    private final OutputStream parent;
    private final Cipher cipher;

    public EncryptedPacketOutputStream(OutputStream parent, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        this.parent = parent;
        //Encryption will be AES with a CBC padding
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
    }

    @Override
    public void close() throws IOException {
        parent.close();
    }

    public void sendPacket(Packet packet) throws IOException {

        //serialize packet for sending
        byte[] data = SerializationUtils.serialize(packet);

        //encrypt packet
        byte[] result;
        synchronized (cipher) {
            try {
                result = cipher.doFinal(data);
            } catch (IllegalBlockSizeException | BadPaddingException ex) {
                throw new IOException(ex);
            }
        }

        //transmit packet + terminating byte
        for (byte b : result) {
            parent.write(b);
        }
        parent.write(-1);

    }

}
