package ja3d.loaders.collada;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.Numbers;
import utils.XmlPath;


public class DaeDocument {
	
	private Document data;
	
	private DaeVisualScene scene;
	
	
	private float unitScaleFactor = 1;
	
	
	public DaeDocument(Document document, float units) {
		
		this.data = document;
		
		// :version
		
		float colladaUnit = Numbers.parseFloat(XmlPath.evaluate(data, "asset[0].unit[0].@meter"));
		
		if (units > 0) {
			unitScaleFactor = colladaUnit/units;
		} else {
			unitScaleFactor = 1;
		}
		
		constructStructures();
//		constructScenes();
//		registerInstanceControllers();
//		constructAnimations();
	}
	
	// Search for the declarations of items and fill the dictionaries.
	private void constructStructures() {
		Element elem = (Element) data.getElementsByTagName("COLLADA").item(0);
		
		// geometries
		NodeList gNodes = XmlPath.list(elem, ".library_geometries.geometry");
		
		for (int i = 0; i < gNodes.getLength(); i++) {
			Element element = (Element)gNodes.item(i);
			DaeGeometry geom = new DaeGeometry(element, this);
			if (geom.id() != null) {
				geometries[geom.id()] = geom;
			}
		}
	}
//			private void constructStructures() {
//				var element:XML;
//
//				sources = new Object();
//				arrays = new Object();
//
//				effects = new Object();
//
//				geometries = new Object();
//				vertices = new Object();
//				for each (element in data.library_geometries.geometry) {
//					// Collect all <geometry>. Dictionary <code>vertices</code> is filled at constructors.
//					var geom:DaeGeometry = new DaeGeometry(element, this);
//					if (geom.id != null) {
//						geometries[geom.id] = geom;
//					}
//				}
//
//				nodes = new Object();
//				for each (element in data.library_nodes.node) {
//					// Create only root nodes. Others are created recursively at constructors.
//					var node:DaeNode = new DaeNode(element, this);
//					if (node.id != null) {
//						nodes[node.id] = node;
//					}
//				}
//			}
	
}
