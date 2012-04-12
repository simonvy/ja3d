package ja3d.loaders.collada;

import java.util.List;

import org.w3c.dom.Element;

import utils.XmlPath;

// Done
class DaeVertices extends DaeElement {

	DaeSource positions;
	
	public DaeVertices(Element data, DaeDocument document) {
		super(data, document);
	}

	@Override
	protected boolean parseImplementation() {
		Element inputXML = null;
		
		List<Element> inputs = XmlPath.list(data, ".input");
		for (Element input : inputs) {
			if ("POSITION".equals(input.getAttribute("semantic"))) {
				inputXML = input;
				break;
			}
		}
		
		if (inputXML != null) {
			DaeInput input = new DaeInput(inputXML, document);
			positions = input.prepareSource(3);
			if (positions != null) {
				return true;
			}
		}
		
		return false;
	}
	
	// How many vertices are in this pool?
	public int getNumVertices() {
		if (positions != null) {
			return positions.numbers.length / positions.stride;
		}
		return 0;
	}
}
