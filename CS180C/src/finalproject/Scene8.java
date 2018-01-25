package finalproject;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.opengl.GL;

import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

import java.util.Stack;
import java.util.Vector;

public class Scene8 extends Scene {
	
	private ShaderProgram shaders;
	private CantorSet set0;
	private CantorSet set1;
	private CantorSet set2;
	private CantorSet set3;
	private CantorSet set4;
	private CantorSet currentSet;
	private double currentRuntime;
	private double lastRuntime;
	
	private String vertShaderFile = "src/finalproject/scene1.vertex.glsl";
	private String fragShaderFile = "src/finalproject/scene8.fragment.glsl";
	
	private Mat4 projectionMatrix;
	private Mat4 modelViewMatrix;
	
	public Scene8(float startTime) {
		super(startTime);
		
	}

	@Override
	public void init(Window window) {
		lastRuntime = window.getRuntime();
	
		GL.createCapabilities();
		shaders = new ShaderProgram();
		shaders.attachVertexShaderFile(vertShaderFile);
		shaders.attachFragmentShaderFile(fragShaderFile);
		shaders.link();
		shaders.bind();
		
		projectionMatrix = new Mat4().perspective(45, 1, 1, 1000);
		modelViewMatrix = new Mat4().identity();
		updateMatrixUniforms();
		
		glDepthFunc(GL_LESS);
		glEnable(GL_DEPTH_TEST);
		
		/*
		 * Specified: [(1,1), (-1,1), (-1,-1), (1, -1)]
		 * [ top left, top right, bottom right, bottom left ]
		 */
		Vec2[] vertices = {new Vec2(5,5), new Vec2(-5,5), new Vec2(-5,-5), new Vec2(5, -5)};
		
		set0 = new CantorSet(shaders);
		set0.generate(0, vertices, -10);
		set0.updateData();
		
		set1 = new CantorSet(shaders);
		set1.generate(1, vertices, -10);
		set1.updateData();
		
		set2 = new CantorSet(shaders);
		set2.generate(2, vertices, -10);
		set2.updateData();
		
		set3 = new CantorSet(shaders);
		set3.generate(3, vertices, -10);
		set3.updateData();
		
		set4 = new CantorSet(shaders);
		set4.generate(4, vertices, -10);
		set4.updateData();
		
		currentSet = set0;
	}
	
	private void updateMatrixUniforms() {
		glUniformMatrix4fv(shaders.getUniformLocation("projectionMatrix"), false, projectionMatrix.toDfb_());
		glUniformMatrix4fv(shaders.getUniformLocation("modelviewMatrix"), false, modelViewMatrix.toDfb_());
	}

	@Override
	public void renderFrame(Window window, float musicTime) {
		currentRuntime = window.getRuntime();
		
		modelViewMatrix.identity();
		//modelViewMatrix.rotate(0,0,0,1);
		updateMatrixUniforms();
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	
		
		if (currentRuntime - lastRuntime > 3.35) {
			lastRuntime = currentRuntime;
			changeSet();
		}
		
		currentSet.draw();
		
	}
	
	private void changeSet() {
		if (currentSet.equals(set0)) {
			currentSet = set1;
		} else if (currentSet.equals(set1)) {
			currentSet = set2;
		} else if (currentSet.equals(set2)) {
			currentSet = set3;
		} else if (currentSet.equals(set3)) {
			currentSet = set4;
		}
	}
	

	

	@Override
	public void destroy(Window window) {
		set0.destroy();
		set1.destroy();
		set2.destroy();
		set3.destroy();
		set4.destroy();
	}

}
