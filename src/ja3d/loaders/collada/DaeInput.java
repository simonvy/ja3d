package ja3d.loaders.collada;

import org.w3c.dom.Element;

import utils.XmlPath;

public class DaeInput extends DaeElement {

	public DaeInput(Element data, DaeDocument document) {
		super(data, document);
	}
	
	public String semantic() {
		return XmlPath.attribute(data, ".@semantic[0]");
	}
	
	public String source() {
		return XmlPath.attribute(data, ".@source[0]");
	}
	
	public int offset() {
		String o = XmlPath.attribute(data, ".@offset[0]");
		return o == null ? 0 : parseInt(o);
	}
	
	public int setNum() {
		String o = XmlPath.attribute(data, ".@set[0]");
		return o == null ? 0 : parseInt(o);
	}
	
	public DaeSource prepareSource(int minComponents) {
		DaeSource source = document.findSource(this.source());
		if (source != null) {
			source.parse();
			if (source.numbers != null && source.stride >= minComponents) {
				return source;
			}
		} else {
		}
		return null;
	}
}
