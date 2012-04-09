package ja3d.loaders.collada;

import org.w3c.dom.Element;

import utils.XmlPath;

public class DaeVertices extends DaeElement {

	private DaeSource positions;
	
	public DaeVertices(Element data, DaeDocument document) {
		super(data, document);
	}

	@Override
	protected boolean parseImplementation() {
		Element e = XmlPath.element(data, ".input.(@semantic == 'POSITION')[0]");
		
		if (e != null) {
			positions = new DaeInput(e, document).prepareSource(3);
			if (positions != null) {
				return true;
			}
		}
		
		return false;
	}
}
