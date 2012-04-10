package ja3d.loaders.collada;

import ja3d.resources.Geometry;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import utils.XmlPath;

public class DaePrimitive extends DaeElement {

	public static final int NORMALS = 1;
	public static final int TANGENT4 = 2;
	public static final int[] TEXCOORDS ={8, 16, 32, 64, 128, 256, 512, 1024};
	
	
	private DaeInput verticesInput;
	private List<DaeInput> texCoordsInputs;
	private DaeInput normalsInput;
	private List<DaeInput> biNormalsInputs;
	private List<DaeInput> tangentsInputs;
	
	private List<Integer> indices;
	private Integer inputsStride;
	
	private Integer indexBegin;
	private Integer numTriangles;
	
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
		
		String localName = data.getLocalName();
		if ("polylist".equals(localName) || "polygons".equals(localName)) {
			Element element = XmlPath.element(data, ".vcount[0]");
			if (element != null) {
				String[] array = parseStringArray(element);
				for (String item : array) {
					vcount.add(parseInt(item));
				}
			}
		} else if ("triangles".equals(localName)) {
			List<Element> pList = XmlPath.list(data, ".p");
			for (Element element : pList) {
				String[] array = parseStringArray(element);
				for (String item : array) {
					indices.add(parseInt(item));
				}
				if (!vcount.isEmpty()) {
					indices = triangulate(indices, vcount);
				}
			}
		}
	}
	
	private List<Integer> triangulate(List<Integer> input, List<Integer> vcount) {
		List<Integer> res = new ArrayList<Integer>();
		int indexIn = 0;
		for (int i = 0; i < vcount.size(); i++) {
			int verticesCount = vcount.get(i);
			int attributesCount = verticesCount * inputsStride;
			if (verticesCount == 3) {
				for (int j = 0; j < attributesCount; j++, indexIn++) {
					res.add(input.get(indexIn));
				}
			} else {
				for (int j = 1; j < verticesCount - 1; j++) {
					// 0 - vertex
					for (int k = 0; k < inputsStride; k++) {
						res.add(input.get(indexIn + k));
					}
					// 1 - vertex
					for (int k = 0; k < inputsStride; k++) {
						res.add(input.get(indexIn + inputsStride * j + k));
					}
					// 2 - vertex
					for (int k = 0; k < inputsStride; k++) {
						res.add(input.get(indexIn + inputsStride * (j + 1) + k));
					}
				}
				indexIn += inputsStride * verticesCount;
			}
		}
		return res;
	}
	
	public boolean verticesEqual(DaeVertices otherVertices) {
		DaeVertices vertices = document.findVertices(verticesInput.source());
		if (vertices != null) {
			//
		}
		return vertices == otherVertices;
	}

	public int fillGeometry(Geometry geometry, List<DaeVertex> geometryVertices) {
		// TODO Auto-generated method stub
		return 0;
	}
}
