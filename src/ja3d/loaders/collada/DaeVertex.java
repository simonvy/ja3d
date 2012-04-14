package ja3d.loaders.collada;

import java.util.ArrayList;
import java.util.List;

import utils.Vector3D;

class DaeVertex {

	int vertexInIndex;
	int vertexOutIndex;
	
	List<Integer> indices = new ArrayList<Integer>();
	
	Vector3D position;
	
	List<Double> uvs = new ArrayList<Double>();
	
	Vector3D normal;
	Vector3D tangent;
	
	public void addPosition(double[] data, int dataIndex, int stride, double unitScaleFactor) {
		assert(stride == 3);
		indices.add(dataIndex);
		int offset = stride * dataIndex;
		position = new Vector3D();
		position.x = data[offset + 0] * unitScaleFactor;
		position.y = data[offset + 1] * unitScaleFactor;
		position.z = data[offset + 2] * unitScaleFactor;
	}
	
	public void addNormal(double[] data, int dataIndex, int stride) {
		assert(stride == 3);
		indices.add(dataIndex);
		int offset = stride * dataIndex;
		normal = new Vector3D(data[offset], data[offset + 1], data[offset + 2]);
	}
	
	public void addTangentBiDirection(double[] tangentData, int tangentDataIndex, int tangentStride, 
			double[] biNormalData, int biNormalDataIndex, int biNormalStride) {
		
		indices.add(tangentDataIndex);
		indices.add(biNormalDataIndex);
		
		int tangentOffset = tangentStride * tangentDataIndex;
		int biNormalOffset = biNormalStride * biNormalDataIndex;
		
		Vector3D biNormal = new Vector3D(
				biNormalData[biNormalOffset + 0], 
				biNormalData[biNormalOffset + 1], 
				biNormalData[biNormalOffset + 2]
		);
		
		tangent = new Vector3D(
				tangentData[tangentOffset + 0], 
				tangentData[tangentOffset + 1],
				tangentData[tangentOffset + 2]
		);
		
		Vector3D cross = new Vector3D();
		Vector3D.crossProduct(normal, tangent, cross);
		double dot = Vector3D.dotProduct(cross, biNormal);
		
		tangent.w = dot < 0 ? -1 : 1;
	}
	
	public void appendUV(double[] data, int dataIndex, int stride) {
		indices.add(dataIndex);
		int offset = stride * dataIndex;
		uvs.add(data[offset]);
		uvs.add(1 - data[offset + 1]);
	}
}
