package entities;

import org.lwjglx.util.vector.Vector3f;

import models.TexModel;

public class Entity {
	
	private TexModel model;
	private Vector3f pos;
	private float rotx, roty, rotz;
	private float scale;
	
	public Entity(TexModel model, Vector3f pos, float rotx, float roty, float rotz, float scale) {
		this.model = model;
		this.pos = pos;
		this.rotx = rotx;
		this.roty = roty;
		this.rotz = rotz;
		this.scale = scale;
	}
	
	public void increasePos(float dx, float dy, float dz) {
		
		this.pos.x += dx;
		this.pos.y += dy;
		this.pos.z += dz;
	}
	
	public void increaseRot(float dx, float dy, float dz) {
		
		this.rotx += dx;
		this.roty += dy;
		this.rotz += dz;
		
	}

	public TexModel getModel() {
		return model;
	}

	public void setModel(TexModel model) {
		this.model = model;
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public float getRotx() {
		return rotx;
	}

	public void setRotx(float rotx) {
		this.rotx = rotx;
	}

	public float getRoty() {
		return roty;
	}

	public void setRoty(float roty) {
		this.roty = roty;
	}

	public float getRotz() {
		return rotz;
	}

	public void setRotz(float rotz) {
		this.rotz = rotz;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	
	

}
