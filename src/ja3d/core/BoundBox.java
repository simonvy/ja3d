package ja3d.core;

import utils.Vector3D;

public class BoundBox {

	public double minX = Double.MAX_VALUE;
	public double minY = Double.MAX_VALUE;
	public double minZ = Double.MAX_VALUE;
	public double maxX = Double.MIN_VALUE;
	public double maxY = Double.MIN_VALUE;
	public double maxZ = Double.MIN_VALUE;

	public void reset() {
		minX = Double.MAX_VALUE;
		minY = Double.MAX_VALUE;
		minZ = Double.MAX_VALUE;
		maxX = Double.MIN_VALUE;
		maxY = Double.MIN_VALUE;
		maxZ = Double.MIN_VALUE;
		
	}

	// if return true, then intersects. AABB collison detection.
	public boolean checkSphere(Vector3D sphere) {
		return sphere.x + sphere.w > minX && sphere.x - sphere.w < maxX 
				&& sphere.y + sphere.w > minY && sphere.y - sphere.w < maxY 
				&& sphere.z + sphere.w > minZ && sphere.z - sphere.w < maxZ;
	}

}
