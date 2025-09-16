package entities;

public class Player {
	private Camera camera;
	
	public Player() {
		camera = new Camera();
		camera.initMouseCallback();
	}
	
	public Camera getCamera() {
		return camera;
	}

}
