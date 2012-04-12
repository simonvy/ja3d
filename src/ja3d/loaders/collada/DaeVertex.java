package ja3d.loaders.collada;

import java.util.ArrayList;
import java.util.List;

import utils.Vector3D;

class DaeVertex {

	int vertexInIndex;
	int vertexOutIndex;
	
	List<Integer> indices = new ArrayList<Integer>();
	
	double x;
	double y;
	double z;
	
	List<Double> uvs = new ArrayList<Double>();
	
	Vector3D normal;
	Vector3D tangent;
	
	public void addPosition(List<Double> data, int dataIndex, int stride, float unitScaleFactor) {
		assert(stride == 3);
		indices.add(dataIndex);
		int offset = stride * dataIndex;
		x = data.get(offset) * unitScaleFactor;
		y = data.get(offset + 1) * unitScaleFactor;
		z = data.get(offset + 2) * unitScaleFactor;
	}
	
	public void addNormal(List<Double> data, int dataIndex, int stride) {
		assert(stride == 3);
		indices.add(dataIndex);
		int offset = stride * dataIndex;
		normal = new Vector3D(data.get(offset), data.get(offset + 1), data.get(offset + 2));
	}
	
	public void addTangentBiDirection(List<Double> tangentData, int tangentDataIndex, int tangentStride, 
			List<Double> biNormalData, int biNormalDataIndex, int biNormalStride) {
		
		indices.add(tangentDataIndex);
		indices.add(biNormalDataIndex);
		
		int tangentOffset = tangentStride * tangentDataIndex;
		int biNormalOffset = biNormalStride * biNormalDataIndex;
		
		Vector3D biNormal = new Vector3D(
				biNormalData.get(biNormalOffset), 
				biNormalData.get(biNormalOffset + 1), 
				biNormalData.get(biNormalOffset + 2)
		);
		
		tangent = new Vector3D(
				tangentData.get(tangentOffset), 
				tangentData.get(tangentOffset + 1),
				tangentData.get(tangentOffset + 2)
		);
		
		Vector3D cross = new Vector3D();
		Vector3D.crossProduct(normal, tangent, cross);
		double dot = Vector3D.dotProduct(cross, biNormal);
		
		tangent.w = dot < 0 ? -1 : 1;
	}
	
	public void appendUV(List<Double> data, int dataIndex, int stride) {
		indices.add(dataIndex);
		int offset = stride * dataIndex;
		uvs.add(data.get(offset));
		uvs.add(1 - data.get(offset + 1));
	}
}
