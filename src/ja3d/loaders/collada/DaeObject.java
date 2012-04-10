package ja3d.loaders.collada;

import ja3d.core.Object3D;

public class DaeObject {

	private Object3D object;
	private DaeNode jointNode;
	
	public DaeObject(Object3D object) {
		this.object = object;
	}
	
	public Object3D object() {
		return object;
	}
}
