package coursework.common.messages;

import coursework.common.FileWrapper;
import coursework.common.Utils;

import java.io.*;

/**
 * @author adkozlov
 */
public abstract class AbstractMessage implements IMessage {

    @Override
    public final byte[] toByteArray() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        writeMessage(dataOutputStream);

        ByteArrayOutputStream resultByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream resultDataOutputStream = new DataOutputStream(resultByteArrayOutputStream);

        resultDataOutputStream.writeInt(byteArrayOutputStream.size());
        resultDataOutputStream.write(byteArrayOutputStream.toByteArray());

        return resultByteArrayOutputStream.toByteArray();
    }

    protected static byte type;

    @Override
    public byte getType() {
        return type;
    }

    protected final DataInputStream dataInputStream;

    protected AbstractMessage() {
        dataInputStream = null;
    }

    protected AbstractMessage(byte[] bytes) throws IOException {
        dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));

        dataInputStream.readInt();

        byte type = dataInputStream.readByte();
        if (type != getType()) {
            throw new MessageTypeRecognizingException(type);
        }
    }

    public static class MessageTypeRecognizingException extends IOException {

        public MessageTypeRecognizingException(byte b) {
            super("There is no such type of message: " + b);
        }
    }

    protected void writeMessage(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(getType());
    }

    protected static byte[] readBytes(DataInputStream dataInputStream) throws IOException {
        int length = dataInputStream.readInt();
        byte[] bytes = new byte[length];
        dataInputStream.read(bytes);

        return bytes;
    }

    protected static void writeBytes(DataOutputStream dataOutputStream, byte[] bytes) throws IOException {
        dataOutputStream.writeInt(bytes.length);
        dataOutputStream.write(bytes);
    }

    protected static String readString(DataInputStream dataInputStream) throws IOException {
        return Utils.fromBytes(readBytes(dataInputStream));
    }

    protected static void writeString(DataOutputStream dataOutputStream, String s) throws IOException {
        writeBytes(dataOutputStream, Utils.getBytes(s));
    }

    protected static FileWrapper readFile(DataInputStream dataInputStream) throws IOException {
        return new FileWrapper(readString(dataInputStream), readBytes(dataInputStream));
    }

    protected static void writeFileWrapper(DataOutputStream dataOutputStream, FileWrapper fileWrapper) throws IOException {
        writeString(dataOutputStream, fileWrapper.getFileName());
        writeBytes(dataOutputStream, fileWrapper.getContent());
    }
}
