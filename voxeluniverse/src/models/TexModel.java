package models;

import textures.ModelTex;

public class TexModel {
	
	private RawModel rawModel;
	private ModelTex modelTex;
	
	public TexModel(RawModel model, ModelTex tex) {
		this.rawModel = model;
		this.modelTex = tex;
		
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTex getModelTex() {
		return modelTex;
	}
	

}
