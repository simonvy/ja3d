package ja3d.core;

public class VertexAttributes {

	public static final int POSITION = 1;
	
	public static final int NORMAL = 2;
	
	public static final int TANGENT4 = 3;
	
	public static final int[] JOINTS = {4, 5, 6, 7};
	
	public static final int[] TEXCOORDS = {8, 9, 10, 11, 12, 13, 14, 15};
	
	public static final int NEXT_INDEX = 16;
	
	public static int getAttributeStride(int attribute) {
		if (attribute >= TEXCOORDS[0] && attribute <= TEXCOORDS[TEXCOORDS.length - 1]) {
			return 2;
		}
		if (attribute == POSITION || attribute == NORMAL) {
			return 3;
		}
		if (attribute >= JOINTS[0] && attribute <= JOINTS[JOINTS.length - 1]) {
			return 4;
		}
		if (attribute == TANGENT4) {
			return 4;
		}
		return 0;
	}
}
