package ja3d.loaders.collada;

import org.w3c.dom.Element;

import utils.XmlPath;

public class DaeGeometry extends DaeElement {

	private DaeVertices vertices;
	
	public DaeGeometry(Element data, DaeDocument document) {
		super(data, document);
		constructVertices();
	}
	
	private void constructVertices() {
		Element e = XmlPath.element(data, ".mesh.vertices[0]");
		
		if (e != null) {
			vertices = new DaeVertices(e, document);
			document.vertices[vertices.id] = vertices;
		}
	}

}
