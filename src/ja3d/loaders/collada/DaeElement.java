package ja3d.loaders.collada;

import org.w3c.dom.Element;

import utils.XmlPath;

// Done
abstract class DaeElement {
	
	protected DaeDocument document;
	protected Element data;
	
	// cached attributes, if not set in the element, the value is empty instead of null.
	private String _id;
	private String _sid;
	private String _name;
	
	// -1 - not parsed, 0 - parsed with error, 1 - parsed without error.
	private int _parsed = -1;
	
	public DaeElement(Element data, DaeDocument document) {
		this.document = document;
		this.data = data;
	}
	
	public boolean parse() {
		if (_parsed < 0) {
			_parsed = parseImplementation() ? 1 : 0;
		}
		return _parsed != 0;
	}
	
	protected boolean parseImplementation() {
		return true;
	}
	
	protected final static String[] parseStringArray(Element element) {
		return element.getTextContent().split(" ");
	}
	
	protected final static float parseFloat(String o) {
		return Float.parseFloat(o);
	}
	
	protected final static int parseInt(String o) {
		return Integer.parseInt(o);
	}
	
	public String id() {
		if (_id == null) {
			_id = XmlPath.attribute(data, ".@id[0]");
		}
		return _id;
	}
	
	public String sid() {
		if (_sid == null) {
			_sid = XmlPath.attribute(data, ".@sid[0]");
		}
		return _sid;
	}
	
	public String name() {
		if (_name == null) {
			_name = XmlPath.attribute(data, ".@name[0]");
		}
		return _name;
	}
}
