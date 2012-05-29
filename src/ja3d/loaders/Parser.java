package ja3d.loaders;

import ja3d.core.Object3D;

import java.util.ArrayList;
import java.util.List;

public class Parser {

	protected List<Object3D> hierarchy;
	protected List<Object3D> objects;
	
	protected void init() {
		hierarchy = new ArrayList<Object3D>();
		objects = new ArrayList<Object3D>();
	}
	
	public List<Object3D> getHierarchy() {
		return hierarchy;
	}
	
}
