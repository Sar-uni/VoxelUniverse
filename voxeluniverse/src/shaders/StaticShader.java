package shaders;

import org.lwjglx.util.vector.Matrix4f;
import org.lwjglx.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import toolbox.MathTime;

public class StaticShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "src\\shaders\\vertexShader";
	private static final String FRAG_FILE = "src\\shaders\\fragmentShaders";
	
	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	private int locationLightPosition;
	private int locationLightColor;
	int locationCameraPosition;
	int locationPlanetRadius;

	public StaticShader() {//String vertexFile, String fragmentFile
		super(VERTEX_FILE, FRAG_FILE);
		// TODO Auto-generated constructor stub
	}
 
	protected void bindAttribs() {
		super.bindAttrib(0, "pos");
		super.bindAttrib(1, "textureCoords");
		
		
	}

	@Override
	protected void getAllUniformLocations() {
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
		locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
		locationLightPosition = super.getUniformLocation("lightPosition");
		locationLightColor = super.getUniformLocation("lightColor");
		
		locationCameraPosition = super.getUniformLocation("cameraPosition");
		locationPlanetRadius = super.getUniformLocation("planetRadius");
		

	}
	
	public void loadTransformationmatrix(Matrix4f matrix) {
		super.loadMatrix(locationTransformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(locationProjectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = MathTime.createViewMatrix(camera);
		super.loadMatrix(locationViewMatrix, matrix);
	}
	
	public void loadstuff(Camera camera) {
		super.loadVector(locationCameraPosition, camera.getPosition());
		super.loadFloat(locationPlanetRadius, 250.0f); // try 200–500 range
	}
	
	public void loadLight(Light light) {
		super.loadVector(locationLightColor, light.getColor());
		super.loadVector(locationLightPosition, light.getPos());
		
	}
	
	

	public void loadCameraPosition(Vector3f camPos) {
	    loadVector(locationCameraPosition, camPos);
	}

	public void loadPlanetRadius(float radius) {
	    loadFloat(locationPlanetRadius, radius);
	}
	
	

}
