package ja3d.loaders.collada;

import ja3d.core.Object3D;
import ja3d.objects.Mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.Matrix3D;
import utils.Vector3D;
import utils.XmlPath;

// Done
public class DaeNode extends DaeElement {

	private DaeVisualScene scene;
	private DaeNode parent;
	
	public final boolean skinOrTopmostJoint = false;
	
	private List<DaeNode> nodes;
	private List<DaeObject> objects;
	
	public final List<DaeObject> skins = null;
	
	public DaeNode(Element data, DaeDocument document, DaeVisualScene scene, DaeNode parent) {
		super(data, document);
		
		this.scene = scene;
		this.parent = parent;
		
		// construct children nodes.
		constructNodes();
	}
	
	private void constructNodes() {
		List<Element> nodesList = XmlPath.list(data, ".node");
		nodes = new ArrayList<DaeNode>();
		for (Element element : nodesList) {
			DaeNode node = new DaeNode(element, document, scene, this);
			if (node.id().length() > 0) {
				document.addNode(node);
			}
			nodes.add(node);
		}
	}

	@Override
	protected boolean parseImplementation() {
		this.objects = parseObjects();
		return true;
	}
	
	// materials are not used.
	private Map<String, Object> parseInstanceMaterials(Element geometry) {
//		Map<String, Object> instances = new HashMap<String, Object>();
//		List<Element> list = XmlPath.list(geometry, ".bind_material.technique_common.instance_material");
//		for (Element item : list) {
//			DaeInstanceMaterial instance = new DaeInstanceMaterial(item, document);
//			instances.put(instance.symbol(), instance);	
//		}
//		return instances;
		return null;
	}
	
	private List<DaeObject> parseObjects() {
		List<DaeObject> objects = new ArrayList<DaeObject>();
		
		List<Element> children = XmlPath.list(data, ".instance_geometry");
		for (Element child : children) {
			DaeGeometry geom = document.findGeometry(XmlPath.attribute(child, ".@url[0]"));
			if (geom != null) {
				geom.parse();
				Mesh mesh = geom.parseMesh(parseInstanceMaterials(child));
				if (mesh != null) {
					mesh.setName(name());
					objects.add(applyAnimation(applyTransformations(mesh)));
				}
			} else {
				// error cannot find the geom in the library.
			}
		}
		
		return objects.size() > 0 ? objects : null;
	}
	
	// return transformation of node as a matrix.
	// This matrix transformation will be appended t o initialMatrix if not null.
	private Matrix3D getMatrix(Matrix3D initialMatrix) {
		Matrix3D matrix = initialMatrix == null ? new Matrix3D() : initialMatrix;
		
		NodeList children = data.getChildNodes();
		for (int i = children.getLength() - 1; i >= 0; i--) {
			// Transformations are append from the end to begin
			Node n = children.item(i);
			if (!(n instanceof Element)) {
				continue;
			}
			
			Element child = (Element)n;
			String sid = XmlPath.attribute(child, ".@sid[0]");
			if ("post-rotationY".equals(sid.length())) {
				// Default 3dsmax exporter writes some trash which ignores
				continue;
			}
			
			String localName = child.getTagName();
			if ("scale".equals(localName)) {
				double[] components = parseNumbersArray(child);
				matrix.appendScale(components[0], components[1], components[2]);
			} else if ("rotate".equals(localName)) {
				double[] components = parseNumbersArray(child);
				matrix.appendRotation(components[3], new Vector3D(components[0], components[1], components[2]));
			} else if ("translate".equals(localName)) {
				double[] components = parseNumbersArray(child);
				matrix.appendTranslation(components[0] * document.unitScaleFactor,
						components[1] * document.unitScaleFactor,
						components[2]*document.unitScaleFactor);
			} else if ("matrix".equals(localName)) {
				double[] components = parseNumbersArray(child);
				components[3] = components[3] * document.unitScaleFactor;
				components[7] = components[7] * document.unitScaleFactor;
				components[11] = components[11] * document.unitScaleFactor;
				matrix.append(new Matrix3D(components));
			} else {
				// ignored
			}
		}
		return matrix;
	}
	
	public Object3D applyTransformations(Object3D object) {
		return applyTransformations(object, null, null);
	}
	
	private Object3D applyTransformations(Object3D object, Matrix3D prepend, Matrix3D append) {
		Matrix3D matrix = getMatrix(prepend);
		if (append != null) {
			matrix.append(append);
		}
		Vector3D[] vs = matrix.decompose();
		
		Vector3D t = vs[0];
		Vector3D r = vs[1];
		Vector3D s = vs[2];
		object.setX(t.x);
		object.setY(t.y);
		object.setZ(t.z);
		object.setRotationX(r.x);
		object.setRotationY(r.y);
		object.setRotationZ(r.z);
		object.setScaleX(s.x);
		object.setScaleY(s.y);
		object.setScaleZ(s.z);
		return object;
	}
	
	public DaeObject applyAnimation(Object3D object) {
		// no animation is used.
		return new DaeObject(object);
	}
	
	public String layer() {
		return XmlPath.attribute(data, ".@layer[0]");
	}
	
	public List<DaeObject> objects() {
		return objects;
	}
	
	public List<DaeNode> nodes() {
		return nodes;
	}
}
