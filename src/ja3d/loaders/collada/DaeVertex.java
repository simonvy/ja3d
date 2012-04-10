package ja3d.loaders.collada;

import java.util.ArrayList;
import java.util.List;

import utils.Vector3D;

public class DaeVertex {

//	private int vertexInIndex;
//	private int vertexOutIndex;
	
	private List<Integer> indices = new ArrayList<Integer>();
	
	private double x;
	private double y;
	private double z;
	
	private List<Double> uvs = new ArrayList<Double>();
	
	private Vector3D normal;
	private Vector3D tangent;
	
	public void addPosition(List<Double> data, int dataIndex, int stride, double unitScaleFactor) {
		indices.add(dataIndex);
		int offset = stride * dataIndex;
		x = data.get(offset) * unitScaleFactor;
		y = data.get(offset + 1) * unitScaleFactor;
		z = data.get(offset + 2) * unitScaleFactor;
	}
	
	public void addNormalList(List<Double> data, int dataIndex, int stride) {
		indices.add(dataIndex);
		int offset = stride * dataIndex;
		normal = new Vector3D();
		normal.x = data.get(offset);
		normal.y = data.get(offset + 1);
		normal.z = data.get(offset + 2);
	}
	
	public void addTangentBiDirection(List<Double> tangentData, int tangentDataIndex, int tangentStride, List<Double> biNormalData, int biNormalDataIndex, int biNormalStride) {
		indices.add(tangentDataIndex);
		indices.add(biNormalDataIndex);
		int tangentOffset = tangentStride * tangentDataIndex;
		int biNormalOffset = biNormalStride * biNormalDataIndex;
		
		double biNormalX = biNormalData.get(biNormalOffset++);
		double biNormalY = biNormalData.get(biNormalOffset++);
		double biNormalZ = biNormalData.get(biNormalOffset++);
		
		tangent = new Vector3D();
		tangent.x = tangentData.get(tangentOffset++);
		tangent.y = tangentData.get(tangentOffset++);
		tangent.z = tangentData.get(tangentOffset++);
		
		double crossX = normal.y * tangent.z - normal.z * tangent.y;
		double crossY = normal.z * tangent.x - normal.x * tangent.z;
		double crossZ = normal.x * tangent.y - normal.y * tangent.x;
		double dot = crossX * biNormalX + crossY * biNormalY + crossZ * biNormalZ;
		tangent.w = dot < 0 ? -1 : 1;
	}
	
	public void appendUV(List<Double> data, int dataIndex, int stride) {
		indices.add(dataIndex);
		uvs.add(data.get(dataIndex * stride));
		uvs.add(1 - data.get(dataIndex * stride + 1));
	}
}
