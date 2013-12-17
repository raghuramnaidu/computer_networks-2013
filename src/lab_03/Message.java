package lab_03;

import java.io.*;

/**
 * @author adkozlov
 */
public abstract class Message {

    protected static DataInputStream createInputStream(byte[] bytes) {
        return new DataInputStream(new ByteArrayInputStream(bytes));
    }

    public final byte[] toByteArray() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        writeToStream(dataOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    protected abstract void writeToStream(DataOutputStream dos);
}
