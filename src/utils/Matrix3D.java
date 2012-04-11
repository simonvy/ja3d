package utils;

public class Matrix3D {

	public Matrix3D(Float[] components) {
		// 0 4  8 12 
		// 1 5  9 13
		// 2 6 10 14
		// 3 7 11 15
		// TODO Auto-generated constructor stub
	}

	public Matrix3D() {
		// TODO Auto-generated constructor stub
	}

	public void appendScale(Float sx, Float sy, Float sz) {
		// TODO Auto-generated method stub
		
	}

	public void appendRotation(Float angle, Vector3D axis) {
		// TODO Auto-generated method stub
		
	}

	public void appendTranslation(float f, float g, float h) {
		// TODO Auto-generated method stub
		
	}

	public void append(Matrix3D matrix3d) {
		// TODO Auto-generated method stub
		
	}

	public Vector3D[] decompose() {
		return new Vector3D[] {
				new Vector3D(),
				new Vector3D(),
				new Vector3D()
		};
	}

}
