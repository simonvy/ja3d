package ja3d.collisions;

import ja3d.core.Object3D;
import ja3d.core.Transform3D;
import ja3d.resources.Geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.Vector3D;
import static utils.Vector3D.*;

public class EllipsoidCollider {
	
	private float radiusX;
	private float radiusY;
	private float radiusZ;
	
	private float threshold = 0.001f;
	
	private Transform3D matrix = new Transform3D();
	private Transform3D inverseMatrix = new Transform3D();
	
	private List<Geometry> geometries = new ArrayList<Geometry>();
	private List<Transform3D> transforms = new ArrayList<Transform3D>();
	
	private List<Double> vertices = new ArrayList<Double>();
	private List<Double> normals = new ArrayList<Double>();
	private List<Integer> indices = new ArrayList<Integer>();
	
	private int numTriangles;
	
	// source, displacement and destination in the local space.
	private float radius;
	private Vector3D src = new Vector3D();
	private Vector3D displ = new Vector3D();
	private Vector3D dest = new Vector3D();
	//
	
	private Vector3D collisionPoint = new Vector3D();
	private Vector3D collisionPlane = new Vector3D();
	
	// the bound of the movement base.
	private Vector3D sphere = new Vector3D();
	private Vector3D cornerA = new Vector3D();
	private Vector3D cornerB = new Vector3D();
	private Vector3D cornerC = new Vector3D();
	private Vector3D cornerD = new Vector3D();
	//
	
	public EllipsoidCollider(float radiusX, float radiusY, float radiusZ) {
		this.radiusX = radiusX;
		this.radiusX = radiusY;
		this.radiusX = radiusZ;
	}
	
	public void calculateSphere(Transform3D transform) {
		// place the sphere at the transform translation.
		sphere.x = transform.d;
		sphere.y = transform.h;
		sphere.z = transform.l;
		sphere.w = 0;
		
		// sphere.w is the max distance from the sphere to the four corners.
		Vector3D s = new Vector3D();
		Vector3D d = new Vector3D();
		Vector3D[] corners = {cornerA, cornerD, cornerD, cornerD};
		for (Vector3D corner : corners) {
			transform.transform(corner, s);
			substract(s, sphere, d);
			double dxyz = d.getLength();
			if (dxyz > sphere.w) {
				sphere.w = dxyz;
			}
		}
	}
	
	private void prepare(Vector3D source, Vector3D displacement, 
			Object3D object, Map<Object3D, Object3D> excludedObjects) {
		
		// Radius of the sphere
		radius = Math.max(Math.max(radiusX, radiusY), radiusZ);
		
		// use the virtual sphere to do collision detection
		// Using the status of this ellipsoid as control,
		// transform the specified object in the global world into the local world of the virtual sphere.
		// This is the transform matrix with the ellipsoid as control.
		// We call this coordinates space as the collidar space or the collision space.  
		// TODO: can we deal with rotation here?
		matrix.compose(source.x, source.y, source.z, 0, 0, 0, radiusX / radius, radiusY / radius, radiusZ / radius);
		inverseMatrix.copy(matrix);
		inverseMatrix.invert();
		
		// Local source - src
		src.clear();
		// Local offset - displ
		substract(matrix.transform(displacement, new Vector3D()), source, displ);
		// Local destination point - dest
		add(src, displ, dest);
		
		// Bound defined by movement of the sphere
		double rad = radius + displ.getLength();
		cornerA.x = -rad;
		cornerA.y = -rad;
		cornerA.z = -rad;
		cornerB.x = rad;
		cornerB.y = -rad;
		cornerB.z = -rad;
		cornerC.x = rad;
		cornerC.y = rad;
		cornerC.z = -rad;
		cornerD.x = -rad;
		cornerD.y = rad;
		cornerD.z = -rad;
		
		if (excludedObjects == null || !excludedObjects.containsKey(object)) {
			object.composeTransforms();
			// inverseTransform transforms the object coordinates into the global world.
			// matrix transforms the global world coordinates into the sphere local coordinates.
			// now the globalToLocalTransform of the object has the value as the combine.
			object.globalToLocalTransform.combine(object.inverseTransform, matrix);
			
			// Check collision with the bound
			boolean intersects = true;
			if (object.getBoundBox() != null) {
				calculateSphere(object.globalToLocalTransform);
				intersects = object.getBoundBox().checkSphere(sphere);
			}
			if (intersects) {
				object.localToGlobalTransform.combine(inverseMatrix, object.transform);
				object.collectGeometry(this, excludedObjects);
			}
			// Check children
			if (object.hasChildren()) {
				object.collectionChildrenGeometry(this, excludedObjects);
			}
		}
		
		numTriangles = 0;
		int indicesLenght = 0;
		int normalsLength = 0;
		
		// TODO
		// Loop geometries
		for (int i = 0; i < geometries.size(); i++) {
			Geometry geometry = geometries.get(i);
			Transform3D transform = transforms.get(i);
		}
	}
	
	public boolean getCollision(Vector3D source, Vector3D displacement, 
			Vector3D resCollisionPoint, Vector3D resCollisionPlane,
			Object3D object, Map<Object3D, Object3D> excludedObjects) {
		
		if (displacement.getLength() < threshold) {
			return false;
		}
		
		prepare(source, displacement, object, excludedObjects);
		
		if (numTriangles > 0) {
			int limit = 50;
			for (int i = 0; i < limit; i++) {
				if (checkCollision()) {
					// Transform the point to the global space
					matrix.transform(collisionPoint, resCollisionPoint);
					
					// Transform the plane to the global space
					Vector3D ab = new Vector3D();
					if (collisionPlane.x < collisionPlane.y) {
						if (collisionPlane.x < collisionPlane.z) {
							ab.x = 0;
							ab.y = -collisionPlane.z;
							ab.z = collisionPlane.y;
						} else {
							ab.x = -collisionPlane.y;
							ab.y = collisionPlane.x;
							ab.z = 0;
						}
					} else {
						if (collisionPlane.y < collisionPlane.z) {
							ab.x = collisionPlane.z;
							ab.y = 0;
							ab.z = -collisionPlane.x;
						} else {
							ab.x = -collisionPlane.y;
							ab.y = collisionPlane.x;
							ab.z = 0;
						}
					}
				}
			}
		}
		
		return false;
	}

	private boolean checkCollision() {					
		double minTime = 1;
		double displacementLength = displ.getLength();
		
		// Loop triangles
		int indicesLength = numTriangles * 3;
		for (int i = 0, j = 0; i < indicesLength;) {
			// three Points for the triangle
			int indexA = indices.get(i) * 3;
			int indexB = indices.get(i + 1) * 3;
			int indexC = indices.get(i + 2) * 3;
			i += 3;
			
			Vector3D a = new Vector3D(vertices.get(indexA), vertices.get(indexA + 1), vertices.get(indexA + 2));
			Vector3D b = new Vector3D(vertices.get(indexB), vertices.get(indexB + 1), vertices.get(indexB + 2));
			Vector3D c = new Vector3D(vertices.get(indexC), vertices.get(indexC + 1), vertices.get(indexC + 2));
			
			// Normal
			Vector3D n = new Vector3D(normals.get(j), normals.get(j + 1), normals.get(j + 2));
			double offset = normals.get(j + 3);
			j += 4;
			
			// distance from src to the triangle
			double distance = dotProduct(src, n) - offset; 
			
			// The intersection of plane and sphere
			Vector3D point = new Vector3D();
			if (distance < radius) {
				ma(src, n, -distance, point);
			} else {
				double t = (distance - radius) / (distance - dotProduct(dest, n) + offset);
				ma(ma(src, displ, t, point), n, -radius, point);
			}
			
			// Now to calculate Closest polygon vertex
			Vector3D face = null;
			double min = Double.MAX_VALUE;
			boolean inside = true;
			Vector3D[] ps = {a, b, c};
			for (int k = 0; k < ps.length; k++) {
				Vector3D p1 = ps[k];
				Vector3D p2 = ps[(k + 1) % ps.length];
				Vector3D ab = substract(p2, p1, new Vector3D());
				Vector3D ac = substract(point, p1, new Vector3D());
				Vector3D cr = crossProduct(ab, ac, new Vector3D());
				// Case of the point is outside of the polygon
				if (dotProduct(cr, n) < 0) {
					double edgeLength = ab.getLengthSquare();
					double edgeDistanceSqr = cr.getLengthSquare() / edgeLength;
					if (edgeDistanceSqr < min) {
						// Edge normalization
						ab.normalize();
						// Distance to intersection of normal along the edge
						double t = dotProduct(ab, ac);
						if (t < 0) {
							// Closest point is the first one
							double acLen = ac.getLengthSquare();
							if (acLen < min) {
								min = acLen;
								face = p1;
							}
						} else if (t > edgeLength) {
							// Closest point is the second one
							substract(point, p2, ac);
							double acLen = ac.getLengthSquare();
							if (acLen < min) {
								min = acLen;
								face = p2;
							}
						} else {
							// Closest point is on edge
							min = edgeDistanceSqr;
							face = ma(p1, ab, t, new Vector3D());
						}
					}
					inside = false;
				}
			}
			
			// Case of point is inside polygon
			if (inside) {
				face = point;
			}
			
			// Vector pointed from closest point to the center of sphere
			Vector3D delta = substract(src, face, new Vector3D());
			
			// If move directed to point
			if (dotProduct(delta, displ) <= 0) {
				// reserved vector
				Vector3D back = scale(displ, -1 / displacementLength, new Vector3D());
				// Length of Vector pointed from closest point to the center of sphere
				double deltaLength = delta.getLengthSquare();
				double projectionLength = dotProduct(delta, back);
				double projectionInsideLength = radius * radius - deltaLength - projectionLength * projectionLength;
				if (projectionInsideLength > 0) {
					// Time of the intersection
					double time = (projectionLength - Math.sqrt(projectionInsideLength)) / displacementLength;
					// Collision with closest point occurs
					if (time < minTime) {
						minTime = time;
						collisionPoint = face;
						if (inside) {
							collisionPlane = n;
							collisionPlane.w = offset;
						} else {
							deltaLength = Math.sqrt(deltaLength);
							collisionPlane = delta;
							collisionPlane.normalize();
							collisionPlane.w = 1;
						}
					}
				}
			}
			
			return minTime < 1;
		}
		
		// TODO Auto-generated method stub
		return false;
	}

	public Vector3D getSphere() {
		return sphere;
	}
	
}
