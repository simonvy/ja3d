package ja3d.loaders.collada;

import java.util.Arrays;

import org.w3c.dom.Element;

import utils.XmlPath;

// Done
public class DaeArray extends DaeElement {

	String[] array;
	
	public DaeArray(Element data, DaeDocument document) {
		super(data, document);
	}
	
	public String type() {
		return data.getLocalName();
	}

	@Override
	protected boolean parseImplementation() {
		array = parseStringArray(data);
		String countString = XmlPath.attribute(data, ".@count[0]");
		if (countString != null) {
			// why it is parse integer?
			int count = parseInt(countString);
			if (array.length < count) {
				return false;
			} else {
				if (array.length > count) {
					array = Arrays.copyOfRange(array, 0, count);
				}
				return true;
			}
		}
		return false;
	}
}
