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

public class Scene7 extends Scene {
	
	//private LSystem dragon;
	private ShaderProgram shaders;
	
	private String vertShaderFile = "src/finalproject/scene7.vertex.glsl";
	private String fragShaderFile = "src/finalproject/scene7.fragment.glsl";
	
	private Mat4 projectionMatrix;
	private Mat4 modelViewMatrix;
	
	private double currentDegree;
	private double nextDegree;
	private Vector<Vec4> protagCoords, protagColors;
	private Mesh protag;
	
	public Scene7(float startTime) {
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
		
		protagCoords = new Vector<Vec4>();
		protagColors = new Vector<Vec4>();
		
		glDepthFunc(GL_LESS);
		glEnable(GL_DEPTH_TEST);
		
		protag = new Mesh(shaders);
		currentDegree = 0;
		nextDegree = 10;
		while(nextDegree <= 360) {
			protagCoords.add(new Vec4(0,0,-10, 1));
			protagCoords.add(new Vec4(Math.cos(Math.toRadians(currentDegree)), Math.sin(Math.toRadians(currentDegree)), -10, 1));
			protagCoords.add(new Vec4(Math.cos(Math.toRadians(nextDegree)), Math.sin(Math.toRadians(nextDegree)), -10, 1));
			currentDegree = nextDegree;
			nextDegree += 10;
			
			protagColors.add(new Vec4(1, 1, 1, 1));
			protagColors.add(new Vec4(1, 1, 1, 1));
			protagColors.add(new Vec4(1, 1, 1, 1));
		}
		protag.updateData(protagCoords, protagColors);
		
		glUniform1f(shaders.getUniformLocation("radius"), 50.026913f);
		glUniform2f(shaders.getUniformLocation("center"), 250, 250);
		glUniform4f(shaders.getUniformLocation("startColor"), 0.98f, 0.969f, .718f, 1);
		glUniform4f(shaders.getUniformLocation("endColor"), .772f, 0.208f, 0.055f, 1);
		
	}
	
	private void updateMatrixUniforms() {
		glUniformMatrix4fv(shaders.getUniformLocation("projectionMatrix"), false, projectionMatrix.toDfb_());
		glUniformMatrix4fv(shaders.getUniformLocation("modelviewMatrix"), false, modelViewMatrix.toDfb_());
	}

	@Override
	public void renderFrame(Window window, float musicTime) {
		modelViewMatrix.identity();
		//modelViewMatrix.rotate(0,0,0,1);
		updateMatrixUniforms();
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glUniform1f(shaders.getUniformLocation("t"), (float) (2.8 * Math.cos(window.getRuntime()*1.6)));
		protag.draw();
	}

	@Override
	public void destroy(Window window) {
		
	}

}

