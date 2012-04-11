package ja3d.loaders.collada;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.XmlPath;

// Done
class DaeSource extends DaeElement {

	private static final String FLOAT_ARRAY = "float_array";
//	private static final String INT_ARRAY = "int_array";
//	private static final String NAME_ARRAY = "Name_array";
	
	// union {
	List<Float> numbers;
//	List<Integer> ints;
//	List<String> names;
	// }
	
	int stride;
	
	public DaeSource(Element data, DaeDocument document) {
		super(data, document);
		constructArrays();
	}
	
	private void constructArrays() {
		NodeList children = data.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node instanceof Element) {
				Element child = (Element)node;
				String name = child.getTagName();
				if (FLOAT_ARRAY.equals(name)) {
					DaeArray array = new DaeArray(child, document);
					if (array.id().length() > 0) {
						document.addArray(array);
					}
				} else if ("technique_common".equals(name)) {
					
				} else {
					throw new IllegalStateException();
				}
			}
		}
	}
	
	private Element accessor() {
		return XmlPath.element(data, ".technique_common.accessor[0]");
	}

	@Override
	protected boolean parseImplementation() {
		Element accessor = this.accessor();
		if (accessor != null) {
			String s = XmlPath.attribute(accessor, ".@source[0]");
			DaeArray array = document.findArray(s);
			if (array != null) {
				String countString = XmlPath.attribute(accessor, ".@count[0]");
				if (countString.length() > 0) {
					int count = parseInt(countString);
					String offsetString = XmlPath.attribute(accessor, ".@offset[0]");
					String strideString = XmlPath.attribute(accessor, ".@stride[0]");
					int offset = offsetString.length() == 0 ? 0 : parseInt(offsetString);
					int stride = strideString.length() == 0 ? 1 : parseInt(strideString);
					array.parse();
					if (array.array.length < offset + count * stride) {
						return false;
					}
					this.stride = parseArray(offset, count, stride, array.array, array.type());
					return true;
				}
			} else {
				
			}
		}
		return false;
	}
	
	private int numValidParams(List<Element> params) {
		int res = 0;
		for (Element param : params) {
			String name = XmlPath.attribute(param, ".name[0]");
			if (name.length() > 0) {
				res ++;
			}
		}
		return res;
	}
	
	// This method removes from the array the items whose related parameter has no name.
	// Assumed no such items, the method just parse the array and output the items in float type.
	private int parseArray(int offset, int count, int stride, String[] array, String type) {
		List<Element> params = XmlPath.list(this.accessor(), ".param");
		assert(numValidParams(params) != stride);
		
		if (FLOAT_ARRAY.equals(type)) {
			numbers = new ArrayList<Float>(stride * count);
			for (String item : array) {
				float v = parseFloat(item);
				numbers.add(v);
			}
		} else {
			throw new IllegalStateException();
		}
		
		return stride;
	}
}
