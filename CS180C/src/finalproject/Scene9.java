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

public class Scene9 extends Scene {
	
	private ShaderProgram shaders;
	private CantorSet set4;
	private CantorSet currentSet;
	private double currentRuntime;
	private double lastRuntime;
	
	private double currentDegree;
	private double nextDegree;
	private Vector<Vec4> protagCoords, protagColors;
	private Mesh protag;
	private float radius;
	
	private String vertShaderFile = "src/finalproject/scene1.vertex.glsl";
	private String fragShaderFile = "src/finalproject/scene9.fragment.glsl";
	
	private Mat4 projectionMatrix;
	private Mat4 modelViewMatrix;
	
	public Scene9(float startTime) {
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
		
		protagCoords = new Vector<Vec4>();
		protagColors = new Vector<Vec4>();
		
		protag = new Mesh(shaders);
		
		currentDegree = 0;
		nextDegree = 10;
		radius = 25;
		
		glUniform1f(shaders.getUniformLocation("radius"), 25);
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
		currentRuntime = window.getRuntime();
		
		modelViewMatrix.identity();
		//modelViewMatrix.rotate(0,0,0,1);
		updateMatrixUniforms();
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	
		

		glUniform1i(shaders.getUniformLocation("radialGradientEnabled"), 0);
		set4.draw();
		
		if(nextDegree <= 360) {
			protagCoords.add(new Vec4(0,0,-5, 1));
			protagCoords.add(new Vec4(Math.cos(Math.toRadians(currentDegree))/4, Math.sin(Math.toRadians(currentDegree))/4, -5, 1));
			protagCoords.add(new Vec4(Math.cos(Math.toRadians(nextDegree))/4, Math.sin(Math.toRadians(nextDegree))/4, -5, 1));
			currentDegree = nextDegree;
			nextDegree += 10;
			
			protagColors.add(new Vec4(1, .5, .4, 1));
			protagColors.add(new Vec4(.8,.2,.1, 1));
			protagColors.add(new Vec4(.8,.2,.1, 1));
		}
		
		if (nextDegree >= 360) {
			if(protagCoords.get(1).x < Math.cos(Math.toRadians(currentDegree))/2) {
				for(int i = 0; i < protagCoords.size(); i++ ) {
					float xTemp = protagCoords.get(i).x;
					float yTemp = protagCoords.get(i).y;
					protagCoords.set(i, new Vec4(xTemp * 1.001f, yTemp * 1.001f, -5, 1));
					
				}
				radius = radius * 1.001f;
				glUniform1f(shaders.getUniformLocation("radius"), radius);
			}
		}
		protag.updateData(protagCoords, protagColors);
		
		glUniform1i(shaders.getUniformLocation("radialGradientEnabled"), 1);
		protag.draw();
		
	}
	
	

	@Override
	public void destroy(Window window) {
		protag.destroy();
		set4.destroy();
	}

}
