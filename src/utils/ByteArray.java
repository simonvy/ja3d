package utils;

public class ByteArray {
	
	private double[] buffer;
	private int nextWriteIndex;
	private int nextReadIndex;
	
	public ByteArray(int size) {
		buffer = new double[size / 4];
		nextWriteIndex = 0;
		nextReadIndex = 0;
	}
	
	public void writeFloat(double value) {
		buffer[nextWriteIndex++] = value;
	}
	
	public void setPosition(int value) {
		nextReadIndex = value / 4;
	}
	
	public double readFloat() {
		return buffer[nextReadIndex++];
	}
}
