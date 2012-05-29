package test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ja3d.loaders.ParserCollada;
import ja3d.objects.Mesh;
import ja3d.collisions.EllipsoidCollider;
import ja3d.core.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import utils.Vector3D;

public class Main {
	
	private static Object3D _model;
	
	public static void main(String[] args) 
			throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(new File("src/test/XIANGZI.xml"));
		
		ParserCollada p = new ParserCollada();
		p.parse(document, "", false);
		
		initFromDae(p);
		
		
		EllipsoidCollider ellipsoid = new EllipsoidCollider(40, 40, 40);
		
		Vector3D src = new Vector3D(-10, -10, 40);
		Vector3D displ = new Vector3D(30, 30, 0);
		Vector3D collidePoint = new Vector3D();
		Vector3D collidePlane = new Vector3D();
		
		boolean collided = ellipsoid.getCollision(src, displ, collidePoint, collidePlane, _model, null);
		
		System.out.println(collided);
	}
	
	
	
	
	private static void initFromDae(ParserCollada daeParser) {
		_model = new Object3D();
		_model.setName("ground");
		
		for (Object3D o : daeParser.getHierarchy()) {
//			if (!(o instanceof Mesh)) {
//				switch(o.numChildren) {
//					case 1:
//						Object3D child = o.getChildAt(0);
//						if (child.getName().equals(o.getName() + "_PIVOT"))) { // add PIVOT node directly into the ground.
//							child.setName(o.getName());
//							child.matrix = child.concatenatedMatrix;
//							o = child;
//						} else {
//							trace(o.name + " has an unexpected child named " + child.name);
//							o = null;
//						}
//						break;
//					default:
//						trace(o.name + " has unexpected " + o.numChildren + " object");
//						o = null;
//						break;
//				}
//			}
			if (o != null) {
				// o now is a mesh
				Mesh oo = (Mesh)o;
				_model.addChild(oo);	
			}
		}
	}

}
