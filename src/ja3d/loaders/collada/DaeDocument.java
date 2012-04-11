package ja3d.loaders.collada;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.XmlPath;

// Done
public class DaeDocument {
	
	private DaeVisualScene scene;
	
	private Element data;
	
	private Map<String, DaeSource> sources;
	private Map<String, DaeArray> arrays;
	
	private Map<String, DaeVertices> vertices;
	private Map<String, DaeGeometry> geometries;
	private Map<String, DaeNode> nodes;
	
	public float unitScaleFactor = 1;
	
	public DaeDocument(Document document, float units) {
		
		this.data = (Element) document.getElementsByTagName("COLLADA").item(0);
		
		// :version
		
		String unitXML = XmlPath.attribute(data, ".asset[0].unit[0].@meter");
		float colladaUnit = Float.parseFloat(unitXML);
		
		if (units > 0) {
			unitScaleFactor = colladaUnit/units;
		} else {
			unitScaleFactor = 1;
		}
		
		constructStructures();
		constructScenes();
	}
	
	// Search for the declarations of items and fill the dictionaries.
	private void constructStructures() {
		sources = new HashMap<String, DaeSource>();
		arrays = new HashMap<String, DaeArray>();
		
		// sources
		NodeList sNodes = data.getElementsByTagName("source");
		for (int i = 0; i < sNodes.getLength(); i++) {
			Element element = (Element) sNodes.item(i);
			DaeSource source = new DaeSource(element, this);
			if (source.id().length() > 0) {
				addSource(source);
			}
		}
		
		// geometries
		geometries = new HashMap<String, DaeGeometry>();
		vertices = new HashMap<String, DaeVertices>();
		List<Element> gNodes = XmlPath.list(data, ".library_geometries.geometry");
		for (Element element : gNodes) {
			DaeGeometry geom = new DaeGeometry(element, this);
			if (geom.id().length() > 0) {
				addGeometry(geom);
			}
		}
		
		// nodes
		nodes = new HashMap<String, DaeNode>();
		List<Element> nNodes = XmlPath.list(data, ".library_nodes.node");
		for (Element element : nNodes) {
			DaeNode node = new DaeNode(element, this, null, null);
			if (node.id().length() > 0) {
				addNode(node);
			}
		}
	}
	
	private void constructScenes() {
		String vsceneURL = XmlPath.attribute(data, ".scene.instance_visual_scene.@url[0]");
		String vsceneID = getLocalID(vsceneURL);
		
		List<Element> sNodes = XmlPath.list(data, ".library_visual_scenes.visual_scene");
		for (Element element : sNodes) {
			DaeVisualScene vscene = new DaeVisualScene(element, this);
			if (vsceneID.equals(vscene.id())) {
				this.scene = vscene;
			}
		}
		if (vsceneID != null && scene == null) {
			throw new IllegalStateException("cannot find scene with name " + vsceneID);
		}
	}
	
	private String getLocalID(String url) {
		if (url.charAt(0) == '#') {
			return url.substring(1);
		} else {
			return null;
		}
	}
	
	public DaeVisualScene scene() {
		return this.scene;
	}
	
	// getter and setter
	DaeArray findArray(String url) {
		return arrays.get(getLocalID(url));
	}
	
	void addArray(DaeArray o) {
		arrays.put(o.id(), o);
	}
	
	DaeSource findSource(String url) {
		return sources.get(getLocalID(url));
	}
	
	void addSource(DaeSource o) {
		sources.put(o.id(), o);
	}
	
	DaeVertices findVertices(String url) {
		return vertices.get(getLocalID(url));
	}
	
	void addVertices(DaeVertices o) {
		vertices.put(o.id(), o);
	}
	
	DaeGeometry findGeometry(String url) {
		return geometries.get(getLocalID(url));
	}
	
	void addGeometry(DaeGeometry o) {
		geometries.put(o.id(), o);
	}
	
	DaeNode findNode(String url) {
		return nodes.get(getLocalID(url));
	}
	
	void addNode(DaeNode o) {
		nodes.put(o.id(), o);
	}
	//
}
