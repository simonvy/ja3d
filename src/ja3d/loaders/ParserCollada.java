package ja3d.loaders;

import java.util.List;

import org.w3c.dom.Document;

import ja3d.core.Object3D;
import ja3d.loaders.collada.DaeDocument;
import ja3d.loaders.collada.DaeNode;
import ja3d.loaders.collada.DaeObject;

public class ParserCollada extends Parser {
	
	public void parse(Document data, String baseURL, Boolean trimPaths) {
		init();
		
		DaeDocument document = new DaeDocument(data, 0);
		if (document.scene() != null) {
			parseNodes(document.scene().nodes(), null, false);
			// parse materials
		}
	}
	
	private void parseNodes(List<DaeNode> nodes, Object3D parent, boolean skinsOnly) {
		
		for (DaeNode node : nodes) {
			node.parse();
			
			// Object to which child objects will be added.
			Object3D container = null;			
//			if (node.skins != null) {
//				container = addObjects(node.skins, parent, node.layer());
//			} else {
				if (!skinsOnly && !node.skinOrTopmostJoint) {
					if (node.objects() != null) {
						container = addObjects(node.objects(), parent, node.layer());
					} else {
						// Empty object3D
						container = new Object3D();
						container.setName(node.name());
						addObject(node.applyAnimation(node.applyTransformations(container)), parent, node.layer());
						container.calculateBoundBox();
					}
				} else {
					// Object or its parent is a skin or joint
					// Create an object only if there are a child skins
					// @never
//					if (hasSkinsInChildren(node)) {
//						container = new Object3D();
//						container.name = node.cloneString(node.name);
//						addObject(node.applyAnimation(node.applyTransformations(container)), parent, node.layer);
//						parseNodes(node.nodes, container, skinsOnly || node.skinOrTopmostJoint);
//						container.calculateBoundBox();
//					}
				}
//			}
			// Parse children
			if (container != null) {
				parseNodes(node.nodes(), container, skinsOnly || node.skinOrTopmostJoint);
			}
		}
	}

	private Object3D addObject(DaeObject animatedObject, Object3D parent, String layer) {
		Object3D object = animatedObject.object();
		objects.add(object);
		if (parent == null) {
			hierarchy.add(object);
		} else {
			parent.addChild(object);
		}
		return object;
	}

	private Object3D addObjects(List<DaeObject> animatedObjects, Object3D parent, String layer) {
		Object3D first = null;
		for (DaeObject animatedObject : animatedObjects) {
			Object3D o = addObject(animatedObject, parent, layer);
			if (first == null) {
				first = o;
			}
		}
		return first;
	}
}
