package ja3d.loaders.collada;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.XmlPath;

public class DaeVisualScene extends DaeElement {

	private List<DaeNode> nodes;
	
	public DaeVisualScene(Element data, DaeDocument document) {
		super(data, document);
		constructNodes();
	}

	public void constructNodes() {
		NodeList nodesList = XmlPath.list(data, ".node");
		int count = nodesList.getLength();
		nodes = new ArrayList<DaeNode>();
		for (int i = 0; i < nodesList.getLength(); i++) {
			DaeNode node = new DaeNode((Element)nodesList.item(i), document, this);
			if (node.id() != null) {
				document.nodes[node.id()] = node;
			}
			nodes.add(node);
		}
	}
}
