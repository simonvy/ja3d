package ja3d.loaders.collada;

import ja3d.core.Object3D;

// Done
public class DaeObject {

	private Object3D object;
	
	public DaeObject(Object3D object) {
		this.object = object;
	}
	
	public Object3D object() {
		return object;
	}
}
