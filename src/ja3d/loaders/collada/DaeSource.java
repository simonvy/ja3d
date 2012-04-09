package ja3d.loaders.collada;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.Numbers;
import utils.XmlPath;

public class DaeSource extends DaeElement {

	private static final String FLOAT_ARRAY = "float_array";
	private static final String INT_ARRAY = "int_array";
	private static final String NAME_ARRAY = "Name_array";
	
	List<Double> numbers;
	List<Integer> ints;
	List<String> names;
	int stride;
	
	public DaeSource(Element data, DaeDocument document) {
		super(data, document);
		constructArrays();
	}
	
	private void constructArrays() {
		NodeList children = data.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Element child = (Element)children.item(i);
			String name = child.getLocalName();
			if (FLOAT_ARRAY.equals(name) 
					|| INT_ARRAY.equals(name) 
					|| NAME_ARRAY.equals(name)) {
				DaeArray array = new DaeArray(child, document);
				if (array.id() != null) {
					document.arrays[array.id()] = array;
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
			DaeArray array = s != null ? null : document.findArray(s);
			if (array != null) {
				String countString = XmlPath.attribute(accessor, ".@count[0]");
				if (countString != null) {
					int count = Numbers.parseInt(countString);
					int offset = Numbers.parseInt(XmlPath.attribute(accessor, ".@offset[0]"));
					int stride = Numbers.parseInt(XmlPath.attribute(accessor, ".@stride[0]"));
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
	}
	
	private int numValidParams(NodeList params) {
		int res = 0;
		for (int i = 0; i < params.getLength(); i++) {
			Element item = (Element) params.item(i);
			String name = XmlPath.attribute(item, ".@name[0]");
			if (name != null) {
				res ++;
			}
		}
		return res;
	}
	
	private int parseArray(int offset, int count, int stride, String[] array, String type) {
		return 0;
	}

}
