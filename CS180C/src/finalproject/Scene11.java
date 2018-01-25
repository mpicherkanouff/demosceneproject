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

public class Scene11 extends Scene {
	
	private double currentDegree;
	private double nextDegree;
	private Vector<Vec4> protagCoords, protagColors;
	private Mesh protag;
	private Spirograph s;
	private ShaderProgram shaders;
	private float alpha;
	private double lastRuntime = 0;
	private double currentRuntime;
	private Vec2 startPoint, endPoint;
	
	private String vertShaderFile = "src/finalproject/scene1.vertex.glsl";
	private String fragShaderFile = "src/finalproject/scene3.fragment.glsl";
	
	private Mat4 projectionMatrix;
	private Mat4 modelViewMatrix;
	
	public Scene11(float startTime) {
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
			
			protagColors.add(new Vec4(1, .5, .4, 1));
			protagColors.add(new Vec4(.8,.2,.1, 1));
			protagColors.add(new Vec4(.8,.2,.1, 1));
		}
		protag.updateData(protagCoords, protagColors);
		s = new Spirograph(shaders);
		s.populateSpirograph(-10f, 7f, 4.5f, 2f, .01f);
		
		startPoint = new Vec2(150, 100);
		endPoint = new Vec2(300, 300);
		
		glUniform2f(shaders.getUniformLocation("a"), startPoint.x, startPoint.y);
		glUniform2f(shaders.getUniformLocation("b"), endPoint.x, endPoint.y);
		glUniform4f(shaders.getUniformLocation("startColorL"), .980f, .969f, .718f, 1);
		glUniform4f(shaders.getUniformLocation("endColorL"), .255f, .094f, .125f, 1);
		glUniform1f(shaders.getUniformLocation("alpha"),1);
		alpha = 0;
		
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
		glUniform1i(shaders.getUniformLocation("linearGradientEnabled"), 0);
		protag.draw();
		
		glUniform1i(shaders.getUniformLocation("linearGradientEnabled"), 1);
		if (alpha < 1) {
			alpha += .0005;
			glUniform1f(shaders.getUniformLocation("alpha"), alpha);
		}
		
		if(currentRuntime - lastRuntime > 1.65 ) {
			lastRuntime = currentRuntime;
			s.rotate(Math.PI/6);
			//updateGradient(Math.PI/4);
		}
		
		s.draw();
		
	}
	
	private void updateGradient(double degree) {
		float[] rotationMatrix = { (float) Math.cos(degree), (float)-Math.sin(degree),
								   (float) Math.sin(degree), (float) Math.cos(degree)};
		
		Vec2 temp = new Vec2(startPoint.x - 175, startPoint.y - 175);
		startPoint.x = rotationMatrix[0] * temp.x + rotationMatrix[1] * temp.y;
		startPoint.y = rotationMatrix[2] * temp.x + rotationMatrix[3] * temp.y;
		
		temp = new Vec2(endPoint.x, endPoint.y);
		endPoint.x = rotationMatrix[0] * temp.x + rotationMatrix[1] * temp.y;
		endPoint.y = rotationMatrix[2] * temp.x + rotationMatrix[3] * temp.y;
		
		glUniform2f(shaders.getUniformLocation("a"), startPoint.x + 175, startPoint.y + 175);
		glUniform2f(shaders.getUniformLocation("b"), endPoint.x + 175, endPoint.y + 175);
	}
	

	@Override
	public void destroy(Window window) {
		protag.destroy();
		s.destroy();
	}

}
