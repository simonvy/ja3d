package ja3d.loaders.collada;

import ja3d.core.VertexAttributes;
import ja3d.objects.Mesh;
import ja3d.resources.Geometry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.ByteArray;
import utils.XmlPath;

class DaeGeometry extends DaeElement {

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
			if (vertices.id().length() > 0) {
				document.addVertices(vertices);
			}
		}
	}

	@Override
	protected boolean parseImplementation() {
		if (vertices != null) {
			parsePrimitives();
			
			vertices.parse();
			int numVertices = vertices.positions.numbers.size() / vertices.positions.stride;
			geometry = new Geometry();
			geometryVertices = new ArrayList<DaeVertex>(numVertices);
			int channels = 0;
			for (DaePrimitive p : primitives) {
				p.parse();
				if (p.verticesEqual(vertices)) {
					channels |= p.fillGeometry(geometry, geometryVertices);
				} else {
					// Error, Vertices of another geometry can not be used
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
			
			ByteArray data = new ByteArray();
			
			// every attribute is a float of 4 bytes.
			int numMappings = attributes.size();
			data.setLength(4 * numMappings * numVertices);
			data.setEndian(ByteOrder.LITTLE_ENDIAN);
			
			for (DaeVertex vertex : geometryVertices) {
				data.writeFloat(vertex.x);
				data.writeFloat(vertex.y);
				data.writeFloat(vertex.z);
				if (vertex.normal != null) {
					data.writeFloat(vertex.normal.x);
					data.writeFloat(vertex.normal.y);
					data.writeFloat(vertex.normal.z);
				}
				if (vertex.tangent != null) {
					data.writeFloat(vertex.tangent.x);
					data.writeFloat(vertex.tangent.y);
					data.writeFloat(vertex.tangent.z);
					data.writeFloat(vertex.tangent.w);
				}
				for (int j = 0; j < vertex.uvs.size(); j++) {
					data.writeFloat(vertex.uvs.get(j));
				}
			}
			
			geometry._vertexStreams[0].data = data;
			geometry._numVertices = numVertices;
			return true;
		}
		return false;
	}
	
	private void parsePrimitives() {
		primitives = new ArrayList<DaePrimitive>();
		Element mesh = XmlPath.element(data, ".mesh");
		if (mesh != null) {
			NodeList children = mesh.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node node = children.item(i);
				if (!(node instanceof Element)) {
					continue;
				}
				
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
