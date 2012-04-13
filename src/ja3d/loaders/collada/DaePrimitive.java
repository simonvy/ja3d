package ja3d.loaders.collada;

import ja3d.resources.Geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;

import utils.XmlPath;

class DaePrimitive extends DaeElement {

	public static final int NORMALS = 1;
	public static final int TANGENT4 = 2;
	public static final int[] TEXCOORDS = {8, 16, 32, 64, 128, 256, 512, 1024};
	
	private DaeInput verticesInput;
	private List<DaeInput> texCoordsInputs;
	private DaeInput normalsInput;
	private List<DaeInput> biNormalsInputs;
	private List<DaeInput> tangentsInputs;
	
	private int[] indices;
	// Indices are organized in groups, each group has inputsStride items.
	private int inputsStride;
	
	private int indexBegin;
	private int numTriangles;
	
	public DaePrimitive(Element data, DaeDocument document) {
		super(data, document);
	}

	@Override
	protected boolean parseImplementation() {
		parseInputs();
		parseIndices();
		return true;
	}

	private void parseInputs() {
		texCoordsInputs = new ArrayList<DaeInput>();
		tangentsInputs = new ArrayList<DaeInput>();
		biNormalsInputs = new ArrayList<DaeInput>();
		
		List<Element> inputsList = XmlPath.list(data, ".input");
		int maxInputOffset = 0;
		for (Element element : inputsList) {
			DaeInput input = new DaeInput(element, document);
			String semantic = input.semantic();
			if ("VERTEX".equals(semantic)) {
				if (verticesInput == null) {
					verticesInput = input;
				}
			} else if ("TEXCOORD".equals(semantic)) {
				texCoordsInputs.add(input);
			} else if ("NORMAL".equals(semantic)) {
				if (normalsInput == null) {
					normalsInput = input;
				}
			} else if ("TEXTANGENT".equals(semantic)) {
				tangentsInputs.add(input);
			} else if ("TEXBINORMAL".equals(semantic)) {
				biNormalsInputs.add(input);
			}
			int offset = input.offset();
			if (offset > maxInputOffset) {
				maxInputOffset = offset;
			}
		}
		inputsStride = maxInputOffset + 1;
	}
	
	private void parseIndices() {
		List<Integer> vcount = new ArrayList<Integer>();
		
		String localName = data.getTagName();
		if ("polylist".equals(localName) || "polygons".equals(localName)) {
			Element element = XmlPath.element(data, ".vcount");
			if (element != null) {
				String[] array = parseStringArray(element);
				for (String item : array) {
					vcount.add(parseInt(item));
				}
			}
			// we do not accept other formats.
			throw new IllegalStateException();
			
		} else
			
		if ("triangles".equals(localName)) {
			List<Element> pList = XmlPath.list(data, ".p");
			for (Element element : pList) {
				String[] array = parseStringArray(element);
				indices = new int[array.length];
				for (int i = 0; i < array.length; i++) {
					indices[i] = parseInt(array[i]);
				}
//				if (!vcount.isEmpty()) {
//					indices = triangulate(indices, vcount);
//				}
			}
		}
		
		// This should not happen
		if (indices == null) {
			indices = new int[0];
		}
	}
	
//	private List<Integer> triangulate(List<Integer> input, List<Integer> vcount) {
//		List<Integer> res = new ArrayList<Integer>();
//		int indexIn = 0;
//		for (int i = 0; i < vcount.size(); i++) {
//			int verticesCount = vcount.get(i);
//			int attributesCount = verticesCount * inputsStride;
//			if (verticesCount == 3) {
//				for (int j = 0; j < attributesCount; j++, indexIn++) {
//					res.add(input.get(indexIn));
//				}
//			} else {
//				for (int j = 1; j < verticesCount - 1; j++) {
//					// 0 - vertex
//					for (int k = 0; k < inputsStride; k++) {
//						res.add(input.get(indexIn + k));
//					}
//					// 1 - vertex
//					for (int k = 0; k < inputsStride; k++) {
//						res.add(input.get(indexIn + inputsStride * j + k));
//					}
//					// 2 - vertex
//					for (int k = 0; k < inputsStride; k++) {
//						res.add(input.get(indexIn + inputsStride * (j + 1) + k));
//					}
//				}
//				indexIn += inputsStride * verticesCount;
//			}
//		}
//		return res;
//	}
	
	public boolean verticesEqual(DaeVertices otherVertices) {
		DaeVertices vertices = document.findVertices(verticesInput.source());
		if (vertices != null) {
			//
		}
		return vertices == otherVertices;
	}

	public int fillGeometry(Geometry geometry, List<DaeVertex> vertices) {
		if (verticesInput == null) {
			return 0;
		}
		verticesInput.parse();
		
		int numIndices = indices.length;
		
		DaeVertices daeVertices = document.findVertices(verticesInput.source());
		if (daeVertices == null) {
			return 0;
		}
		daeVertices.parse();
		
		DaeSource positionSource = daeVertices.positions;
		int vertexStride = 3; // XYZ
		
//		DaeSource mainSource = positionSource;
		DaeInput mainInput = verticesInput;
		
		DaeSource tangentSource = null;
		DaeSource binormalSource = null;
		
		int channels = 0;
		DaeSource normalSource = null;
		
		// inputOffsets is where we can find the value of the index in the related inputs
		// for example, if we have inputOffsets [position, normal] is [0, 1].
		// Then the vertex.indices[0] is the index of the related inputs for the position of the vertex.
		// vertex.indices[1] is for normal.
		// 
		List<Integer> inputOffsets = new ArrayList<Integer>();
		inputOffsets.add(verticesInput.offset());
		if (normalsInput != null) {
			normalSource = normalsInput.prepareSource(3);
			inputOffsets.add(normalsInput.offset());
			vertexStride += 3; // NORMAL XYZ
			channels |= NORMALS;
			if (tangentsInputs.size() > 0 && biNormalsInputs.size() > 0) {
				tangentSource = tangentsInputs.get(0).prepareSource(3);
				inputOffsets.add(tangentsInputs.get(0).offset());
				binormalSource = biNormalsInputs.get(0).prepareSource(3);
				inputOffsets.add(biNormalsInputs.get(0).offset());
				vertexStride += 4;
				channels |= TANGENT4;
			}
		}
		
		List<DaeSource> textureSources = new ArrayList<DaeSource>();
		int numTexCoordsInputs = texCoordsInputs.size();
		if (numTexCoordsInputs > 8) {
			// TODO: warning
			numTexCoordsInputs = 8;
		}
		for (int i = 0; i < numTexCoordsInputs; i++) {
			DaeSource s = texCoordsInputs.get(i).prepareSource(2); // UV
			textureSources.add(s);
			inputOffsets.add(texCoordsInputs.get(i).offset());
			vertexStride += 2;
			channels |= TEXCOORDS[i];
		}
		//
		
		// Make geometry data
		indexBegin = geometry._indices.size();
		for (int i = 0; i < numIndices; i+= inputsStride) {
			int index = indices[i + mainInput.offset()];
			
			DaeVertex vertex = index < vertices.size() ? vertices.get(index) : null;
			// not exist
			if (vertex == null || !isEqual(vertex, indices, i, inputOffsets)) {
				vertex = new DaeVertex();
				// replace or add to end
				if (index < vertices.size()) {
					vertices.set(index, vertex);
				} else {
					vertices.add(vertex);
					index = vertices.size() - 1;
				}
				
				vertex.vertexInIndex = indices[i + verticesInput.offset()];
				vertex.addPosition(positionSource.numbers, vertex.vertexInIndex, positionSource.stride, document.unitScaleFactor);
				
				if (normalSource != null) {
					vertex.addNormal(normalSource.numbers, indices[i + normalsInput.offset()], normalSource.stride);
				}
				
				if (tangentSource != null) {
					vertex.addTangentBiDirection(
							tangentSource.numbers, indices[i + tangentsInputs.get(0).offset()], tangentSource.stride, 
							binormalSource.numbers, indices[i + biNormalsInputs.get(0).offset()], binormalSource.stride
					);
				}
				
				for (int j = 0; j < textureSources.size(); j++) {
					vertex.appendUV(textureSources.get(j).numbers, indices[i + texCoordsInputs.get(j).offset()], textureSources.get(j).stride);
				}
			}
			
			vertex.vertexOutIndex = index;
			geometry._indices.add(index);
		}
		
		numTriangles = (geometry._indices.size() - indexBegin)/3;
		return channels;
	}
	
	// check whether the given vertex is equal to the vertex starting from index in indices/
	// Assume both vertices have the same offsets.
	private boolean isEqual(DaeVertex vertex, int[] indices, int index, List<Integer> offsets) {
		for (int j = 0; j < offsets.size(); j++) {
			if (vertex.indices.get(j) != indices[index + offsets.get(j)]) {
				return false;
			}
		}
		return true;
	}
}
