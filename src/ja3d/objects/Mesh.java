package ja3d.objects;

import java.util.Map;

import ja3d.collisions.EllipsoidCollider;
import ja3d.core.BoundBox;
import ja3d.core.Object3D;
import ja3d.core.Transform3D;
import ja3d.resources.Geometry;

public class Mesh extends Object3D {

	private Geometry geometry;
	
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;	
	}

	@Override
	public void collectGeometry(EllipsoidCollider collider, Map<Object3D, Object3D> excludedObjets) {
		collider.addGeometry(geometry, localToGlobalTransform);
	}
	
	@Override
	protected void updateBoundBox(BoundBox boundBox, Transform3D transform) {
		if (geometry != null) {
			geometry.updateBoundBox(boundBox, transform);
		}
	}
}
