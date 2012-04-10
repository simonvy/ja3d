package ja3d.loaders.collada;

import ja3d.core.VertexAttributes;
import ja3d.objects.Mesh;
import ja3d.resources.Geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.XmlPath;

public class DaeGeometry extends DaeElement {

	private List<DaeVertex> geometryVertices;
	private List<DaePrimitive> primitives;
	private Geometry geometry;

	private DaeVertices vertices;
	
	public DaeGeometry(Element data, DaeDocument document) {
		super(data, document);
		constructVertices();
	}
	
	private void constructVertices() {
		Element e = XmlPath.element(data, ".mesh.vertices[0]");
		
		if (e != null) {
			vertices = new DaeVertices(e, document);
			document.addVertices(vertices);
		}
	}

	@Override
	protected boolean parseImplementation() {
		if (vertices != null) {
			parsePrimitives();
			
			vertices.parse();
			int numVertices = vertices.positions.numbers.size()/vertices.positions.stride;
			geometry = new Geometry();
			geometryVertices = new ArrayList<DaeVertex>(numVertices);
			int channels = 0;
			for (DaePrimitive p : primitives) {
				p.parse();
				if (p.verticesEqual(vertices)) {
					numVertices = geometryVertices.size();
					channels |= p.fillGeometry(geometry, geometryVertices);
				} else {
					
				}
			}
			
			List<Integer> attributes = new ArrayList<Integer>();
			
			attributes.add(VertexAttributes.POSITION);
			attributes.add(VertexAttributes.POSITION);
			attributes.add(VertexAttributes.POSITION);
			
			if ((channels & DaePrimitive.NORMALS) != 0) {
				attributes.add(VertexAttributes.NORMAL);
				attributes.add(VertexAttributes.NORMAL);
				attributes.add(VertexAttributes.NORMAL);
			}
			
			if ((channels & DaePrimitive.TANGENT4) != 0) {
				attributes.add(VertexAttributes.TANGENT4);
				attributes.add(VertexAttributes.TANGENT4);
				attributes.add(VertexAttributes.TANGENT4);
				attributes.add(VertexAttributes.TANGENT4);
			}
			
			for (int i = 0; i < 8; i++) {
				if ((channels & DaePrimitive.TEXCOORDS[i]) != 0) {
					attributes.add(VertexAttributes.TEXCOORDS[i]);
					attributes.add(VertexAttributes.TEXCOORDS[i]);
				}
			}
			
			geometry.addVertexStream(attributes);
			
			numVertices = geometryVertices.size();
			
			// data
			
			return true;
		}
		return false;
	}
	
	private void parsePrimitives() {
		primitives = new ArrayList<DaePrimitive>();
		NodeList children = XmlPath.element(data, ".mesh[0]").getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				Element child = (Element) node;
				String localName = child.getTagName();
				if ("polygons".equals(localName)
						|| "polylist".equals(localName)
						|| "triangles".equals(localName)
						|| "trifans".equals(localName)
						|| "tristrips".equals(localName)) {
					DaePrimitive p = new DaePrimitive(child, document);
					primitives.add(p);
				}
			}
		}
	}

	public Mesh parseMesh(Map<String, Object> meterials) {
		return new Mesh();
	}
}
