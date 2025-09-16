package voxeluniverse;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import textures.ModelTex;
import toolbox.Timer;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;
import org.lwjglx.util.vector.Matrix4f;
import org.lwjglx.util.vector.Vector3f;

import models.RawModel;
import renderer.Loader;
import renderer.Render;
import renderer.WindowManager;
import shaders.StaticShader;
import models.TexModel;
import entities.Entity;
import cosmos.*;
import entities.Camera;
import entities.*;
import renderer.FrustumCuller;
import cosmos.ChunkManager;
import renderer.MasterRender;

public class App {
	static IntBuffer width = MemoryUtil.memAllocInt(1);
	static IntBuffer height = MemoryUtil.memAllocInt(1);
	static Loader loader = new Loader();
	static StaticShader shader = new StaticShader();
	static Render render2 = new Render(shader);
	
	static MasterRender render = new MasterRender();

	public void run() {
		init();
		gameLoop();
		cleanup();
	}

	private void cleanup() {
		// TODO Auto-generated method stub
		shader.cleanUp();
		loader.cleanUp();
		WindowManager.windowDelete(width, height);
		GLFW.glfwTerminate();

	}

	private void gameLoop() {
		// TODO Auto-generated method stub
		float delta;
		float accumulator = 0f;
		float interval = 1 / 60f;
		float alpha;


		//Camera camera = new Camera();
		//camera.initMouseCallback();
		Player player = new Player();
		FrustumCuller frustum = new FrustumCuller();
		Matrix4f projectionMatrix = render2.projectionMatrix;

		//RawModel model = loader.loadToVAO(vertices, indices, texscoords);
		ModelTex tex = new ModelTex(loader.loadTex("image"));
		//TexModel texModel = new TexModel(model, tex);
		//Entity entity = new Entity(texModel, new Vector3f(0, 0, -1), 0, 0, 0, 1);

		//RawModel cube = loader.loadToVAO(vertices2, indices2, textureCoords2);
		//TexModel texcube = new TexModel(cube, tex);
		//Entity cubeEntity = new Entity(texcube, new Vector3f(0, 0, -1), 0, 0, 0, 0.2f);
		Entity[][][] blocks = new Entity[500][500][500];
		Entity[][] chunks = new Entity[50][50];

		ChunkManager chunkManager = new ChunkManager(loader, tex, 16);
		ChunkManager chunkManager2 = new ChunkManager(loader, tex, 16);

		//Chunk chunk = new Chunk(new Vector3f(0, 0, 0));


		// chunk.getBlock(1, 1, 1).setActive(true);
		// chunk.getBlock(2, 1, 1).setActive(true);
		// chunk.getBlock(15, 1, 0).setActive(true);

		//chunk.createMesh(loader, tex);

		//Entity chunkEntity = new Entity(chunk.getModel(), chunk.getPosition(), 0, 0, 0, 1);

		PlanetGenerator planetGen = new PlanetGenerator(chunkManager, new Vector3f(0, 0, 0), 10);
		
		//PlanetGenerator planetGen2 = new PlanetGenerator(chunkManager2);

		// Create a planet with center at (0, 0, 0), radius 5 chunks
		//planetGen.generatePlanet(new Vector3f(0, 0, -10), 15);
		//planetGen2.generatePlanet(new Vector3f(0, 0, -20), 1);
		
		//WorldGenerator worldgen = new WorldGenerator(chunkManager, 16, 16);
		

//		for (int x = 0; x < 15; x++) {
//			for (int y = 0; y < 15; y++) {
//				chunks[x][y] = new Entity(chunk3.getModel(), new Vector3f(x * 16, y * 16, 0), 0, 0, 0, 1);
//			}
//		}

//		for (int x = 0; x <= 2; x++) {
//		    for (int z = -2; z <= 2; z++) {
//		        Vector3f chunkPos = new Vector3f(
//		            x * Chunk.CHUNK_SIZE * Chunk.BLOCK_RENDER_SIZE * 2,
//		            0,
//		            z * Chunk.CHUNK_SIZE * Chunk.BLOCK_RENDER_SIZE * 2
//		        );
//		        Chunk chunk1 = new Chunk(chunkPos);
//		        chunkManager.addChunk(chunk1);
//		    }
//		}

		while (!glfwWindowShouldClose(WindowManager.getWindow())) {
			// input
			// update
			// render
		//	entity.increasePos(0, 0, -0.01f);
			// cubeEntity.increaseRot(0, 1, 1);
			delta = Timer.getDelta();
			accumulator += delta;
			/// handle input here
			//entity.increaseRot(0f, 0f, 1f);
			//camera.setWorldSize(10 * Chunk.CHUNK_SIZE * Chunk.BLOCK_RENDER_SIZE);
			player.getCamera().move();
			
			frustum.updateFrustum(projectionMatrix, player.getCamera());
			// chunkEntity.increaseRot(0, 0, 1);

			///

			/// update
			while (accumulator >= interval) {

				accumulator -= interval;
			}
			alpha = accumulator / interval;
			// update();
			// System.out.println("W key: " + InputManager.isKeyDown(GLFW.GLFW_KEY_W));
			// System.out.println("Player position: " + player.getPosition());
			// System.out.printf("Mouse DX: %.2f DY: %.2f%n",
			// InputManager.getMouseDX(), InputManager.getMouseDY());

			///

			/// render
			planetGen.updatePlanetGeneration(player.getCamera());
			//worldgen.updateWorldGeneration(camera);
			
			Runtime runtime = Runtime.getRuntime();
			long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
			System.out.println("Used memory: " + usedMemory + " MB");
			System.out.println("Remaining chunks: " + planetGen.generationQueue.size());
			System.out.println(player.getCamera().getPosition());
			render();
			shader.start();
			shader.loadViewMatrix(player.getCamera());
			
			chunkManager.update(player.getCamera().getPosition(), new Vector3f(0, 0, 0));
			//chunkManager2.update(camera.getPosition(), new Vector3f(5, 0, 0));
//			for (Chunk chunkss : chunkManager.getRenderList()) {
//			    if (chunk.shouldRender()) {
//			        // Render the chunk using the Render system
//			        Entity entitys = new Entity(
//			            chunkss.getModel(),
//			            chunkss.getPosition(),
//			            0, 0, 0, 1
//			        );
//			        render.render(entitys, shader);
//			    }
//			}

			for (Chunk chunkss : chunkManager.getRenderList()) {
				if (chunkss.shouldRender()) {
					Entity chunkEntitys = new Entity(chunkss.getModel(), chunkss.getPosition(), 0, 0, 0, 1);
					//render.render(chunkEntitys, shader);
					render.proccessEntity(chunkEntitys);

			}}
			
			render.render(player.getCamera());

			// render.render(entity, shader);
			// render.render(cubeEntity, shader);
			// for (int x = 0; x < 10; x++) {
			// for (int y = 0; y < 10; y++) {
			// for (int z = 0; z < 10; z++) {
			// blocks[x][y][z] = new Entity(texcube, new Vector3f(x,y,z), 0, 0f, 0, 1f); //
			// Render m_pBlocks[x][y][z]
			// render.render(blocks[x][y][z], shader);
			// }
			// }
			// }
			// render.render(blocks[1][1][1], shader);

			// main.testCube(WindowManager.getWindow(), width, height);

			// render.render(texturedCube);

			// render.render(chunkEntity, shader);
			// render.render(chunkEntity2, shader);
			// render.render(chunkEntity3, shader);
			// render.render(chunkEntity4, shader);
			// render.render(chunkEntity5, shader);

//			for(int x = 0; x < 0; x++) {
//				for(int y = 0; y < 0; y++) {
//					
//					render.render(chunks[x][y], shader);
//				}
//			}

//			for (int x = 0; x < 15; x++) {
//			    for (int y = 0; y < 15; y++) {
//			        Entity chunkEntity6 = chunks[x][y];
//			        Vector3f pos = chunkEntity6.getPos();
//			        
//			        if (FrustumCuller.isBoxInFrustum(pos.x + 8, pos.y + 8, pos.z + 8, 8)) { // +8 for center, 8 is half chunk size (16)
//			            render.render(chunkEntity6, shader);
//			        }
//			    }
//			}

			//shader.stop();
			render.CleanUp();

			/// window update
			/* Swap buffers and poll Events */
			glfwSwapBuffers(WindowManager.getWindow());
			glfwPollEvents();

			/* Flip buffers for next loop */
			width.flip();
			height.flip();
			Timer.update();

		}

	}

	private void render() {
		// TODO Auto-generated method stub
		//render.prepare();

		// render.render(model);
		// main.testCube(WindowManager.getWindow(), width, height);

		// render(alpha);
		Timer.updateFPS();
		GLFW.glfwSetWindowTitle(WindowManager.getWindow(), "FPS: " + Timer.getFPS());

		///

	}

	private void init() {
		// TODO Auto-generated method stub

		// create window
		Timer.init();
		
		

		// Set up input callbacks
		// GLFW.glfwSetKeyCallback(WindowManager.getWindow(), Input::keyCallback);
		// GLFW.glfwSetCursorPosCallback(WindowManager.getWindow(),
		// Input::cursorPosCallback);
		// GLFW.glfwSetInputMode(WindowManager.getWindow(), GLFW.GLFW_CURSOR,
		// GLFW.GLFW_CURSOR_DISABLED);

	}

}
