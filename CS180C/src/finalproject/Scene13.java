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

public class Scene13 extends Scene {
	
	private ShaderProgram shaders;
	private CantorSet set4;
	private double currentRuntime;
	private double lastRuntime;
	private float alpha;
	
	private Spirograph s;
	
	private String vertShaderFile = "src/finalproject/scene1.vertex.glsl";
	private String fragShaderFile = "src/finalproject/scene13.fragment.glsl";
	
	private Mat4 projectionMatrix;
	private Mat4 modelViewMatrix;
	
	public Scene13(float startTime) {
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
		
		set4 = new CantorSet(shaders);
		set4.generate(4, vertices, -10);
		set4.updateData();
		
		s = new Spirograph(shaders);
		s.populateSpirograph(-8f, 5f, 3.1f, 2.4f, .01f);
		
		alpha = 1;
		glUniform1f(shaders.getUniformLocation("alpha"), 1);
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
		
		if (alpha > 0 ) {
			alpha -= 0.01f;
			
		}
		glUniform1f(shaders.getUniformLocation("alpha"), alpha);
		set4.draw();
		
		if(currentRuntime - lastRuntime > 1.65 ) {
			lastRuntime = currentRuntime;
			s.rotate(Math.PI/3);
		}
		
		glUniform1f(shaders.getUniformLocation("alpha"), 1);
		s.draw();
		
	}

	@Override
	public void destroy(Window window) {
		s.destroy();
		set4.destroy();
	}

}
