package utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class ByteArray {
	private ByteBuffer buffer;
	
	public void writeFloat(float v) {
//		if (buffer.remaining() < 4) { // float has 4 bytes
//			ByteBuffer nbuffer = ByteBuffer.allocate(buffer.capacity() * 2);
//			nbuffer.order(buffer.order());
//			nbuffer.put(buffer.array());
//			buffer = nbuffer;
//		}
		buffer.putFloat(v);
	}
	
	public void setLength(int i) {
		buffer = ByteBuffer.allocate(i);
	}
	
	public void setEndian(ByteOrder bo) {
		buffer.order(bo);
	}
	
	public byte[] bytes() {
		buffer.flip();
		return Arrays.copyOfRange(buffer.array(), 0, buffer.limit());
	}
}
