package ja3d.core;

import ja3d.collisions.EllipsoidCollider;

import java.util.Map;

public class Object3D {

	private String name;
	private boolean visible;
	
	private BoundBox boundBox;
	
	private double x;
	private double y;
	private double z;
	
	private double rotationX;
	private double rotationY;
	private double rotationZ;
	
	private double scaleX;
	private double scaleY;
	private double scaleZ;
	
	private Object3D parent;
	private Object3D childrenList;
	private Object3D next;
	
	public Transform3D transform = new Transform3D();
	public Transform3D inverseTransform = new Transform3D();
	private boolean transformChanged = true;
	
	public Transform3D localToGlobalTransform = new Transform3D();
	public Transform3D globalToLocalTransform = new Transform3D();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getX() {
		return x;
	}

	public void setX(double v) {
		if (this.x != v) {
			this.x = v;
			transformChanged = true;
		}
	}

	public double getY() {
		return y;
	}

	public void setY(double v) {
		if (this.y != v) {
			this.y = v;
			transformChanged = true;
		}
	}

	public double getZ() {
		return z;
	}

	public void setZ(double v) {
		if (this.z != v) {
			this.z = v;
			transformChanged = true;
		}
	}

	public double getRotationX() {
		return rotationX;
	}

	public void setRotationX(double v) {
		if (this.rotationX != v) {
			this.rotationX = v;
			transformChanged = true;
		}
	}

	public double getRotationY() {
		return rotationY;
	}

	public void setRotationY(double v) {
		if (this.rotationY != v) {
			this.rotationY = v;
			transformChanged = true;
		}
	}

	public double getRotationZ() {
		return rotationZ;
	}

	public void setRotationZ(double v) {
		if (this.rotationZ != v) {
			this.rotationZ = v;
			transformChanged = true;
		}
	}

	public double getScaleX() {
		return scaleX;
	}

	public void setScaleX(double v) {
		if (this.scaleX != v) {
			this.scaleX = v;
			transformChanged = true;
		}
	}

	public double getScaleY() {
		return scaleY;
	}

	public void setScaleY(double v) {
		if (this.scaleY != v) {
			this.scaleY = v;
			transformChanged = true;
		}
	}

	public double getScaleZ() {
		return scaleZ;
	}

	public void setScaleZ(double v) {
		if (this.scaleZ != v) {
			this.scaleZ = v;
			transformChanged = true;
		}
	}
	
	public void calculateBoundBox() {
		if (boundBox != null) {
			boundBox.reset();
		} else {
			boundBox = new BoundBox();
		}
		// Fill values of the boundBox
		updateBoundBox(boundBox, null);
	}
	
	protected void updateBoundBox(BoundBox boundBox, Transform3D transform) {
	}
	
	public BoundBox getBoundBox() {
		return boundBox;
	}
	
	public Object getParent() {
		return parent;
	}

	public Object3D addChild(Object3D child) {
		if (child == null || child == this) {
			throw new IllegalArgumentException();
		}
		
		for (Object3D container = parent; container != null; container = container.parent) {
			if (container == child) {
				throw new IllegalArgumentException("An object cannot be added as a child to one of it's children (or children's children, etc.).");
			}
		}
		
		// Adding
		if (child.parent != this) {
			// removing from old parent
			if (child.parent != null) {
				child.parent.removeChild(child);
			}
			// Adding
			addToList(child, null);
			child.parent = this;
		} else {
			child = removeFromList(child);
			if (child == null) {
				throw new IllegalStateException();
			}
			addToList(child, null);
		}
		
		return child;
	}
	
	public Object3D removeChild(Object3D child) {
		if (child == null || child.parent != this) {
			throw new IllegalArgumentException();
		}
		
		child = removeFromList(child);
		if (child == null) {
			throw new IllegalStateException();
		}
		child.parent = null;
		return child;
	}
	
	// add child right before the item in the list.
	private void addToList(Object3D child, Object3D item) {
		child.next = item;
		if (item == childrenList) {
			childrenList = child;
		} else {
			for (Object3D current = childrenList; current != null; current = current.next) {
				if (current.next == item) {
					current.next = child;
					break;
				}
			}
		}
	}
	
	private Object3D removeFromList(Object3D child) {
		Object3D prev = null;
		Object3D current = childrenList;
		while(current != null) {
			if (current == child) {
				if (prev != null) {
					prev.next = current.next;
				} else {
					childrenList = current.next;
				}
				current.next = null;
				return current;
			}
			prev = current;
			current = current.next;
		}
		return null;
	}

	public void composeTransforms() {
		if (transformChanged) {
			transform.compose(x, y, z, rotationX, rotationY, rotationZ, scaleX, scaleY, scaleZ);
			inverseTransform.copy(transform);
			inverseTransform.invert();
			transformChanged = false;
		}
		
	}

	public void collectGeometry(EllipsoidCollider ellipsoidCollider,
			Map<Object3D, Object3D> excludedObjects) {
	}
	
	public void collectionChildrenGeometry(EllipsoidCollider collidar,
			Map<Object3D, Object3D> excludedObjects) {
		
		for (Object3D child = childrenList; child != null; child = child.next) {
			if (excludedObjects == null || !excludedObjects.containsKey(child)) {
				child.composeTransforms();
				// Calculating matrix for converting from collider coordinates to local coordinates
				child.globalToLocalTransform.combine(child.inverseTransform, globalToLocalTransform);
				// Check boundbox intersecting
				boolean intersects = true;
				if (child.getBoundBox() != null) {
					collidar.calculateSphere(child.globalToLocalTransform);
					intersects = child.getBoundBox().checkSphere(collidar.getSphere());
				}
				// Adding the geometry of self content
				if (intersects) {
					child.localToGlobalTransform.combine(localToGlobalTransform, child.transform);
					child.collectGeometry(collidar, excludedObjects);
				}
				if (child.hasChildren()) {
					child.collectionChildrenGeometry(collidar, excludedObjects);
				}
			}
		}
	}
	
	public boolean hasChildren() {
		return childrenList != null;
	}
}
