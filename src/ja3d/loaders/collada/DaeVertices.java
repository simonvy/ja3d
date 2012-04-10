package ja3d.loaders.collada;

import java.util.List;

import org.w3c.dom.Element;

import utils.XmlPath;

public class DaeVertices extends DaeElement {

	DaeSource positions;
	
	public DaeVertices(Element data, DaeDocument document) {
		super(data, document);
	}

	@Override
	protected boolean parseImplementation() {
		Element e = null;
		
		List<Element> inputs = XmlPath.list(data, ".input");
		for (Element input : inputs) {
			if ("POSITION".equals(input.getAttribute("semantic"))) {
				e = input;
				break;
			}
		}
		
		if (e != null) {
			DaeInput input = new DaeInput(e, document);
			positions = input.prepareSource(3);
			if (positions != null) {
				return true;
			}
		}
		
		return false;
	}
}
