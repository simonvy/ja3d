package ja3d.loaders.collada;

import ja3d.resources.Geometry;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import utils.XmlPath;

class DaePrimitive extends DaeElement {

	public static final int NORMALS = 1;
	public static final int TANGENT4 = 2;
	public static final int[] TEXCOORDS ={8, 16, 32, 64, 128, 256, 512, 1024};
	
	private DaeInput verticesInput;
	private List<DaeInput> texCoordsInputs;
	private DaeInput normalsInput;
	private List<DaeInput> biNormalsInputs;
	private List<DaeInput> tangentsInputs;
	
	private List<Integer> indices;
	private int inputsStride;
	
	int indexBegin;
	int numTriangles;
	
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
		indices = new ArrayList<Integer>();
		List<Integer> vcount = new ArrayList<Integer>();
		
		String localName = data.getTagName();
		if ("polylist".equals(localName) || "polygons".equals(localName)) {
			Element element = XmlPath.element(data, ".vcount[0]");
			if (element != null) {
				String[] array = parseStringArray(element);
				for (String item : array) {
					vcount.add(parseInt(item));
				}
			}
			// we do not accept other formats.
			throw new IllegalStateException();
			
		} else if ("triangles".equals(localName)) {
			List<Element> pList = XmlPath.list(data, ".p");
			for (Element element : pList) {
				String[] array = parseStringArray(element);
				for (String item : array) {
					indices.add(parseInt(item));
				}
//				if (!vcount.isEmpty()) {
//					indices = triangulate(indices, vcount);
//				}
			}
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
		
		int numIndices = indices.size();
		
		DaeVertices daeVertices = document.findVertices(verticesInput.source());
		if (daeVertices == null) {
			return 0;
		}
		daeVertices.parse();
		
		DaeSource positionSource = daeVertices.positions;
		int vertexStride = 3; // XYZ
		
		DaeSource mainSource = positionSource;
		DaeInput mainInput = verticesInput;
		
		DaeSource tangentSource;
		DaeSource binormalSource;
		
		int channels = 0;
		DaeSource normalSource;
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
			channels |= TEXCOORDS[i];
		}
		
		int verticesLength = vertices.size();
		
		// Make geometry data
//		indexBegin = geometry._indices.length;
//		for (int i = 0; i < numIndices; i+= inputsStride) {
//			int index = indices.get(i + mainInput.offset());
//			DaeVertex vertex = vertices.get(index);
//			// TODO
//		}
//		
//		numTriangles = (geometry._indices.length - indexBegin)/3;
		return channels;
	}
}
