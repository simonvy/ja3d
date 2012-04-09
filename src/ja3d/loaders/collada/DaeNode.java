package ja3d.loaders.collada;

import org.w3c.dom.Element;

public class DaeNode extends DaeElement {

	public DaeNode(Element data, DaeDocument document, DaeVisualScene scene, DaeNode parent) {
		super(data, document);
		// TODO Auto-generated constructor stub
	}
	
	public DaeNode(Element data, DaeDocument document, DaeVisualScene scene) {
		this(data, document, scene, null);
	}

	@Override
	protected boolean parseImplementation() {
		// TODO Auto-generated method stub
		return false;
	}

}
