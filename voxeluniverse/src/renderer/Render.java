package renderer;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjglx.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexModel;
import shaders.StaticShader;
import toolbox.MathTime;

public class Render {
	
	private static final float FOV = 70;
	private static final float NEARPLANE = 0.1f;
	private static final float FARPLANE = 5000;
	
	public Matrix4f projectionMatrix;
	private StaticShader shader = new StaticShader();
	
	public Render(StaticShader shader) {
		this.shader = shader;
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.5f, 0.7f, 0.8f, 1);
	}
	
	public void render(Map<TexModel, List<Entity>> entities) {
		for(TexModel model:entities.keySet()) {
			prepareTexModels(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity: batch) {
				prepareInstance(entity);
				GL11.nglDrawElements(GL11.GL_TRIANGLES, 
						model.getRawModel().getVertexCount(), 
						GL11.GL_UNSIGNED_INT, 0);
			}
			
			unbindTexModel();
		}
		
		
		
	}
	
	
	//helpers
	private void prepareTexModels(TexModel model) {
		RawModel rawmodel = model.getRawModel();
		GL30.glBindVertexArray(rawmodel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getModelTex().getID());
	}
	
	private void unbindTexModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		
	}
	
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = MathTime.createTransformationMatrix(entity.getPos(), entity.getRotx(), entity.getRoty(), entity.getRotz(), entity.getScale()); 
		shader.loadTransformationmatrix(transformationMatrix);
		
	}
	
	public void render2(Entity entity, StaticShader shader) {
		TexModel texmodel = entity.getModel();
		RawModel model = texmodel.getRawModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		Matrix4f transformationMatrix = MathTime.createTransformationMatrix(entity.getPos(), entity.getRotx(), entity.getRoty(), entity.getRotz(), entity.getScale()); 
		shader.loadTransformationmatrix(transformationMatrix);
		
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texmodel.getModelTex().getID());
		
		GL11.nglDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);

	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) WindowManager.WIDTH / (float) WindowManager.HEIGHT;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FARPLANE - NEARPLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FARPLANE + NEARPLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEARPLANE * FARPLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		
		//System.out.println(projectionMatrix);

	}

}
