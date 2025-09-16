package renderer;

import shaders.StaticShader;
import entities.Camera;
import entities.Entity;
import models.TexModel;

import java.util.*;

public class MasterRender {
	
	private StaticShader shader = new StaticShader();
	private Render render = new Render(shader);
	
	private Map<TexModel, List<Entity>> entities = new HashMap<TexModel, List<Entity>>();
	
	public void render(Camera camera) {
		render.prepare();
		shader.start();
		//light
		shader.loadViewMatrix(camera);
		
		
		render.render(entities);
		shader.stop();
		entities.clear();
		
	}
	
	public void proccessEntity(Entity entity) {
		TexModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch != null) {
			batch.add(entity);
		}else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	
	
	
	
	public void CleanUp() {
		shader.cleanUp();
	}

}
