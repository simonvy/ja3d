package utils;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlPath {
	
	public static List<Element> list(Element e, String path) {
		XmlPath p = new XmlPath(e, path);
		p.process();
		assert(p.list == null);
		return p.list;
	}
	
	// if the specified attribute is not set, return empty string instead of null pointer.
	public static String attribute(Element e, String path) {
		XmlPath p = new XmlPath(e, path);
		p.process();
		return p.attribute;
	}
	
	public static Element element(Element e, String path) {
		XmlPath p = new XmlPath(e, path);
		p.process();
		return p.e;
	}
	
	private static final int EOF = -1;
	
	private String s;
	private int next;
	
	private Element e;
	private String attribute;
	private List<Element> list;
	
	private XmlPath(Element e, String path) {
		this.e = e;
		this.s = path;
		this.next = 0;
	}
	
	private void process() {
		while (tryMatch('.')) {
			if (tryMatch('@')) { // attribute
				String child = word();
				attribute = e.getAttribute(child);
				if (LA() == '[') {
					index();
				}
				break;
			} else {
				String child = word();
				list = getChildrenByTagName(e, child);
				switch(LA()) {
				case '[':
					int idx = index();
					e = list.size() > idx ? list.get(idx) : null;
					break;
				case '.':
					e = list.size() > 0 ? list.get(0) : null;
					break;
				}
			}
		}
		if (LA() != EOF) {
			throw new IllegalStateException();
		}
	}
	
	private List<Element> getChildrenByTagName(Element parent, String tag) {
		List<Element> retval = new ArrayList<Element>();
		if (parent != null) {
			NodeList children = parent.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof Element) {
					Element elem = (Element) child;
					System.out.println(elem.getAttribute("id"));
					if (tag.equals(elem.getTagName())) {
						retval.add(elem);
					}
				}
			}
		}
		return retval;
	}
	
	private boolean tryMatch(char c) {
		if (LA() == c) {
			next++;
			return true;
		}
		return false;
	}
	
	private String word() {
		int j = next;
		while (j < s.length()) {
			char c = s.charAt(j);
			if (c == '_' || Character.isAlphabetic(c)) {
				j++;
			} else {
				break;
			}
		}
		String w = s.substring(next, j);
		next = j;
		return w;
	}
	
	private int index() {
		match('[');
		int j = next;
		while (j < s.length() && Character.isDigit(s.charAt(j))) {
			j++;
		}
		String w = s.substring(next, j);
		next = j;
		int r = Integer.parseInt(w);
		match(']');
		return r;
	}
	
	 private int LA() {
		 return next >= s.length() ? EOF : s.charAt(next);
	 }
	 
	 private void match(char c) {
		 if (LA() != c) {
			 throw new IllegalStateException();
		 }
		 next ++;
	 }
}
