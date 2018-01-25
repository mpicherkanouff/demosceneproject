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

public class Scene4andahalf extends Scene {
	
	private Spirograph s;
	private ShaderProgram shaders;
	private double lastRuntime = 0;
	private double currentRuntime;
	private Vec2 startPoint, endPoint;
	private Vec4 startColor1, endColor1, startColor2, endColor2, midColor1, midColor2;
	private float colorLerpT;
	
	private String vertShaderFile = "src/finalproject/scene1.vertex.glsl";
	private String fragShaderFile = "src/finalproject/scene4.fragment.glsl";
	
	private Mat4 projectionMatrix;
	private Mat4 modelViewMatrix;
	
	public Scene4andahalf(float startTime) {
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
		
		s = new Spirograph(shaders);
		s.populateSpirograph(-10f, 6f, 4.3f, 2.8f, .01f);
		
		startPoint = new Vec2(150, 150);
		endPoint = new Vec2(300, 300);
		
		startColor1 = new Vec4(.772f, .208f, .055f, 1);
		endColor1 = new Vec4(.262f, .164f, .169f, 1);
		startColor2 = new Vec4(1f, 1f, 1f, 1f);
		endColor2 = new Vec4(1f, 1f, 1f, 1f);
		
		midColor1 = new Vec4();
		midColor2 = new Vec4();
		colorLerpT = 0;
		
		glUniform2f(shaders.getUniformLocation("a"), startPoint.x, startPoint.y);
		glUniform2f(shaders.getUniformLocation("b"), endPoint.x, endPoint.y);
		glUniform4f(shaders.getUniformLocation("startColorL"), startColor1.x, startColor1.y, startColor1.z, startColor1.w);
		glUniform4f(shaders.getUniformLocation("endColorL"), endColor1.x, endColor1.y, endColor1.z, endColor1.w);
		glUniform1f(shaders.getUniformLocation("alpha"),1);
		
		glUniform1f(shaders.getUniformLocation("radius"), 50.026913f);
		glUniform2f(shaders.getUniformLocation("center"), 250, 250);
		glUniform4f(shaders.getUniformLocation("startColorR"), 0.98f, 0.969f, .718f, 1);
		glUniform4f(shaders.getUniformLocation("endColorR"), .772f, 0.208f, 0.055f, 1);
		
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
		
		glUniform1i(shaders.getUniformLocation("linearGradientEnabled"), 1);
		
		if(currentRuntime - lastRuntime > 1.65 ) {
			lastRuntime = currentRuntime;
			s.rotate(Math.PI/4);
		}
		if (colorLerpT < 1) {
			colorLerpT += 0.001;
			alterGradient(colorLerpT);
		}
		
		s.draw();
		
	}
	
	private void alterGradient(float t) {
		midColor1.x = ((1 - t) * startColor1.x) + (t * startColor2.x);
		midColor1.y = ((1 - t) * startColor1.y) + (t * startColor2.y);
		midColor1.z = ((1 - t) * startColor1.z) + (t * startColor2.z);
		midColor1.w = 1;
		
		midColor2.x = ((1 - t) * endColor1.x) + (t * endColor2.x);
		midColor2.y = ((1 - t) * endColor1.y) + (t * endColor2.y);
		midColor2.z = ((1 - t) * endColor1.z) + (t * endColor2.z);
		midColor2.w = 1;
		
		glUniform4f(shaders.getUniformLocation("startColorL"), midColor1.x, midColor1.y, midColor1.z, midColor1.w);
		glUniform4f(shaders.getUniformLocation("endColorL"), midColor2.x, midColor2.y, midColor2.z, midColor2.w);

	}
	

	@Override
	public void destroy(Window window) {
		s.destroy();
	}

}
