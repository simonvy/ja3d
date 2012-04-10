package ja3d.loaders.collada;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import utils.XmlPath;

public class DaeVisualScene extends DaeElement {

	private List<DaeNode> nodes;
	
	public DaeVisualScene(Element data, DaeDocument document) {
		super(data, document);
		constructNodes();
	}

	public void constructNodes() {
		List<Element> nodesList = XmlPath.list(data, ".node");
		nodes = new ArrayList<DaeNode>();
		for (Element element : nodesList) {
			DaeNode node = new DaeNode(element, document, this, null);
			if (node.id().length() > 0) {
				document.addNode(node);
			}
			nodes.add(node);
		}
	}
	
	public List<DaeNode> nodes() {
		return this.nodes;
	}
}
