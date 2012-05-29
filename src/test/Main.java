package test;

import java.io.File;
import java.io.IOException;

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
		
		
		EllipsoidCollider ellipsoid = new EllipsoidCollider(62, 37, 84);
		
		Vector3D src = new Vector3D();
		Vector3D displ = new Vector3D();
		
		src.x = 60.591072778383385;
		src.y = 90.35632693575246;
		src.z = 84;
		
		displ.x = -4.974067839938843;
		displ.y = -4.338046694502737;
		displ.z = 0;
		
		Vector3D collidePoint = new Vector3D();
		Vector3D collidePlane = new Vector3D();
		
		boolean collided = ellipsoid.getCollision(src, displ, collidePoint, collidePlane, _model, null);

		System.out.println(collided);
		
		if (collided) {
			Vector3D des = ellipsoid.calculateDestination(src, displ, _model, null);
			System.out.println(des.x + " " + des.y + " " + des.z + " " + des.w);
		}
	}
	
	private static void initFromDae(ParserCollada daeParser) {
		_model = new Object3D();
		_model.setName("ground");
		
		for (Object3D o : daeParser.getHierarchy()) {
			if (o != null) {
				// o now is a mesh
				Mesh oo = (Mesh)o;
				_model.addChild(oo);	
			}
		}
	}
}
