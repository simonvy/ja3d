package ja3d.loaders.collada;

import org.w3c.dom.Element;

import utils.XmlPath;

// Done
class DaeInput extends DaeElement {

	private String _semantic;
	private String _source;
	private	int _offset = -1;
//	private int _set = -1;
	
	public DaeInput(Element data, DaeDocument document) {
		super(data, document);
	}
	
	public String semantic() {
		if (_semantic == null) {
			_semantic = XmlPath.attribute(data, ".@semantic[0]");
		}
		return _semantic;
	}
	
	public String source() {
		if (_source == null) {
			_source = XmlPath.attribute(data, ".@source[0]");
		}
		return _source;
	}
	
	public int offset() {
		if (_offset < 0) {
			String o = XmlPath.attribute(data, ".@offset[0]");
			_offset = o.length() > 0 ? parseInt(o) : 0;
		}
		return _offset;
	}
	
//	public int setNum() {
//		if (_set < 0) {
//			String o = XmlPath.attribute(data, ".@set[0]");
//			_set = o.length() > 0 ? parseInt(o) : 0;
//		}
//		return _set;
//	}
	
	public DaeSource prepareSource(int minComponents) {
		DaeSource source = document.findSource(this.source());
		if (source != null) {
			source.parse();
			if (source.numbers != null && source.stride >= minComponents) {
				return source;
			} else {
			}
		} else {
		}
		return null;
	}
}
