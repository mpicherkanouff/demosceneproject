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

public class Scene5 extends Scene {
	
	private Spirograph s1;
	private Spirograph s2;
	private ShaderProgram shaders;
	private double lastRuntime1 = 0;
	private double currentRuntime;
	private Vec2 startPoint, endPoint;
	
	private String vertShaderFile = "src/finalproject/scene1.vertex.glsl";
	private String fragShaderFile = "src/finalproject/scene5.fragment.glsl";
	
	private Mat4 projectionMatrix;
	private Mat4 modelViewMatrix;
	
	public Scene5(float startTime) {
		super(startTime);
		
	}

	@Override
	public void init(Window window) {
	
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
		
		
		s1 = new Spirograph(shaders);
		s1.populateSpirograph(-10f, 6f, 4.3f, 2.8f, .01f);
		s2 = new Spirograph(shaders);
		s2.populateSpirograph(-9f, 3f, .6f, 1.6f, .01f);
		
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
		
		if(currentRuntime - lastRuntime1 > 1.65 ) {
			lastRuntime1 = currentRuntime;
			s1.rotate(Math.PI/4);
			s2.rotate(-Math.PI/9);
		}
		
		glUniform2f(shaders.getUniformLocation("a"), 250, 300);
		glUniform2f(shaders.getUniformLocation("b"), 250, 150);
		glUniform4f(shaders.getUniformLocation("startColorL"), .980f, .969f, .718f, 1);
		glUniform4f(shaders.getUniformLocation("endColorL"), .408f, .702f, .525f, 1);
		s2.draw();
		
		glUniform2f(shaders.getUniformLocation("a"), 150, 150);
		glUniform2f(shaders.getUniformLocation("b"), 300, 300);
		glUniform4f(shaders.getUniformLocation("startColorL"), .772f, .208f, .055f, 1f);
		glUniform4f(shaders.getUniformLocation("endColorL"), .262f, .164f, .169f, 1f);
		s1.draw();
		
	}
	

	@Override
	public void destroy(Window window) {
		s2.destroy();
		s1.destroy();
	}

}
