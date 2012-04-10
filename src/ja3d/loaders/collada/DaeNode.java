package ja3d.loaders.collada;

import ja3d.core.Object3D;
import ja3d.objects.Mesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import utils.XmlPath;

public class DaeNode extends DaeElement {

	private DaeVisualScene scene;
	private DaeNode parent;
	
	public boolean skinOrTopmostJoint = false;
	
	private List<DaeNode> nodes;
	private List<DaeObject> objects;
	
	public List<DaeObject> skins = null;
	
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
	
	private Map<String, Object> parseInstanceMaterials(Element geometry) {
		Map<String, Object> instances = new HashMap<String, Object>();
		List<Element> list = XmlPath.list(geometry, ".bind_material.technique_common.instance_material");
		for (Element item : list) {
//			DaeInstanceMaterial instance = new DaeInstanceMaterial(item, document);
//			instances.put(instance.symbol(), instance);	
		}
		return instances;
	}
	
	
	public List<DaeObject> objects() {
		return objects;
	}
	
	public List<DaeNode> nodes() {
		return nodes;
	}
	
	public String layer() {
		return XmlPath.attribute(data, ".@layer[0]");
	}

	public DaeObject applyTransformations(Object3D container) {
		// TODO Auto-generated method stub
		return null;
	}

	public DaeObject applyAnimation(DaeObject applyTransformations) {
		// TODO Auto-generated method stub
		return null;
	}
}
