package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Button.ClickListener;
import com.mygdx.game.Button.MyButton;
import com.mygdx.game.Component.Console;
import com.mygdx.game.Component.Dialog;
import com.mygdx.game.Component.EditBoard;
import com.mygdx.game.Component.EditText;
import com.mygdx.game.Entity.BodyInfo;
import com.mygdx.game.Entity.Config;
import com.mygdx.game.Entity.NN;
import com.mygdx.game.Entity.Population;
import com.mygdx.game.Entity.Trace;
import com.mygdx.game.Util.Drawer;
import com.mygdx.game.Util.FileHelper;
import com.mygdx.game.Util.Line;
import com.mygdx.game.Util.MyStage;
import com.mygdx.game.Util.MyUtil;


import com.mygdx.game.Entity.Robot;
import com.sun.org.apache.bcel.internal.generic.POP;

import java.awt.geom.Line2D;
import java.security.Key;
import java.util.ArrayList;

import org.luaj.vm2.Globals;
import org.luaj.vm2.Lua;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import javax.rmi.CORBA.Util;

import static com.badlogic.gdx.Gdx.gl;
import static com.mygdx.game.Entity.Population.selectGene;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor, Input.TextInputListener {

	Console console;
	public static String text = "text";
	static public int Count = 111;
	int recordSize = 0;
	int count = 0;
	boolean isRecord = false;
	int addCount = 0;
	Vector2 start,end;
	SpriteBatch batch;
	World world;
	Box2DDebugRenderer debug;
	Camera camera;
	ShapeRenderer shapeRenderer;
	final int P2M = 10;
	int width,height;
	float interval = 0;
	MyButton myButton;
	MyButton myButton1;
	MyButton myButton2;
	MyButton myButton3;
	MyButton myButton4;
	float mouse_x, mouse_y;
	EditBoard editBoard;
	Dialog dialog;
	ArrayList<Vector2> points;
	ArrayList<Line> lines;
	BitmapFont bitmapFont;
	ArrayList<Trace>traces;
	Config config;

	Population population;
	MyStage stage;
	Robot actor;
	Texture map;
	@Override
	public void create () {

		stage = new MyStage(this);
		/*初始化lua*/
		map = new Texture(Gdx.files.internal("path.png"));
		config = new Config();
		traces = new ArrayList<>();
		bitmapFont = new BitmapFont();
		dialog = new Dialog(new Texture(Gdx.files.internal("dialog.png")));
		lines = new ArrayList<>();
		start = new Vector2();
		end = new Vector2();
		points = new ArrayList<>();
		editBoard = new EditBoard(new Texture(Gdx.files.internal("editBoard.png")));
		interval = Gdx.app.getGraphics().getDeltaTime();
		interval = Gdx.app.getGraphics().getDeltaTime();
		myButton = new MyButton(new Texture(Gdx.files.internal("button.png")), "Stop");
		myButton1 = new MyButton(new Texture(Gdx.files.internal("button.png")), "Edit");
		myButton2 = new MyButton(new Texture(Gdx.files.internal("button.png")), "Save");
		myButton3 = new MyButton(new Texture(Gdx.files.internal("button.png")), "Load");
		myButton4 = new MyButton(new Texture(Gdx.files.internal("button.png")), "Output");
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		debug = new Box2DDebugRenderer();
		world = new World(new Vector2(0, 0), true); // 一般标准重力场
		Gdx.input.setInputProcessor(this);;
		Gdx.input.setInputProcessor(stage);
		population = new Population(world);

		actor = new Robot(20, 145, world);
		if (config.initMap) {
			String mapData = FileHelper.read(Config.mapName);
			lines = MyUtil.gerateMap(world,mapData);
		}
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Body body1 = contact.getFixtureA().getBody();
				Body body2 = contact.getFixtureB().getBody();
				BodyInfo bodyInfo1 = (BodyInfo) body1.getUserData();
				BodyInfo bodyInfo2 = (BodyInfo) body2.getUserData();
				if ("wall".equals(""+bodyInfo1.name) &&"robot".equals(""+bodyInfo2.name))
				{

					Robot r = (Robot) bodyInfo2.get("robot");
					r.isAlive = false;
				}
				if ("robot".equals(""+bodyInfo1.name) &&"wall".equals(""+bodyInfo2.name))
				{
					Robot r = (Robot) bodyInfo1.get("robot");
					r.isAlive = false;
				}
			}

			@Override
			public void endContact(Contact contact) {

			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {

			}
		});
		console = new Console(stage);
	}
	@Override
	public void render () {
//		if (count %recordInterval==0)
//		{
//			if (isRecord)
//			{
//				if (Gdx.input.isKeyPressed(Input.Keys.UP) ||Gdx.input.isKeyPressed(Input.Keys.DOWN))
//				{
//				//NOTHING TO DO
//				}else {
//					Trace trace = new Trace();
//					trace.setSource(new double[]{
//									robot.dist[4],
//									robot.dist[2],
//									robot.dist[0],
//									robot.dist[1],
//									robot.dist[3]},
//							new double[]{
//									0, 0
//							}
//
//					);
//					traces.add(trace);
//					recordSize++;
//				}
//			}
//				//TODO:
//			count = 0;
//		}
		count++;
		if (MyUtil.PAUSE)
			world.step(0, 3, 3);
		else
			world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);

		gl.glClearColor(55/255.0f, 83/255.0f, 131/255.0f, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(map, 0, 0);
//		batch.end();
		/*绘制机器人的视线*/
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(new Color(0,1,1,1));
		population.run(lines,shapeRenderer);
		float speed_x = (float) Math.cos(actor.getBody().getAngle()) * 10;
		float speed_y = (float) Math.sin(actor.getBody().getAngle()) * 10;
		actor.getBody().setLinearVelocity(new Vector2(speed_x, speed_y));
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			actor.getBody().setTransform(actor.getBody().getPosition(), (float) (actor.getBody().getAngle() + Math.toRadians(2)));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		{
			actor.getBody().setTransform(actor.getBody().getPosition(), (float) (actor.getBody().getAngle() + Math.toRadians(-2)));
		}
		shapeRenderer.end();
		/*所有物体集合*/
		Array<Body> bodies = new Array<>();
		world.getBodies(bodies);
		for (Body body:bodies) {

			BodyInfo mes = (BodyInfo) body.getUserData();

			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.setColor(new Color(1,1,1,1));
			if ("robot".equals(mes))
			{
				Robot r = (Robot) mes.get("robot");
				if (!r.isAlive)
					continue;
			}
			MyUtil.render(body, shapeRenderer);
			shapeRenderer.end();
		}
		for (Line line: lines) {
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.setColor(new Color(1,1,1,1));
			shapeRenderer.line(line.getStart(),line.getEnd());
			shapeRenderer.end();
		}
		if (addCount >0)
			Drawer.drawAssistLine(shapeRenderer, start, end);
		myButton.draw(batch);
		myButton1.draw(batch);
		myButton2.draw(batch);
		myButton3.draw(batch);
		myButton4.draw(batch);
		editBoard.draw(batch);
		dialog.draw(batch);

		batch.begin();
		bitmapFont.draw(batch, "isRecord: "+isRecord, 10, MyUtil.WINDOW_HEIGHT-10);
		bitmapFont.draw(batch, "recordSize:"+recordSize, 10, MyUtil.WINDOW_HEIGHT-25);
		bitmapFont.draw(batch, "Alive"+population.getAlive(), 10, MyUtil.WINDOW_HEIGHT-40);
		bitmapFont.draw(batch, "best Fitness:"+population.bestFitness, 10, MyUtil.WINDOW_HEIGHT-55);
		bitmapFont.draw(batch, "rr:"+ NN.rr, 10, MyUtil.WINDOW_HEIGHT-70);
		bitmapFont.draw(batch, "mr:"+NN.mr, 10, MyUtil.WINDOW_HEIGHT-85);
		bitmapFont.draw(batch, "mouse_x:"+mouse_x+"mouse_y:"+mouse_y, 10, MyUtil.WINDOW_HEIGHT-100);
		console.draw(batch);
		batch.end();
		stage.act();
		stage.draw();
		camera.update();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {

		super.resize(width, height);
		this.width = width;
		this.height = height;
		camera = new OrthographicCamera(width/P2M, height/P2M);
		camera.position.set(0, 0, 0);
	}

	@Override
	public boolean keyDown(int keycode) {
		console.onKeyDown(keycode);
		/*`按键被按下时*/
		if (keycode == Input.Keys.SPACE)
		{
			isRecord = !isRecord;
		}
		if (keycode == Input.Keys.ESCAPE)
			Gdx.app.exit();
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		System.out.println(button);
		if (button == 1)
		{
			getBodyAtMouse(screenX, (height - screenY),2);
		}
		if (button == 0)
		{
			getBodyAtMouse(screenX, (height - screenY),1);
			try{
				myButton.click(screenX, (height - screenY), new ClickListener() {
					@Override
					public void onClick() throws Exception {
						MyUtil.PAUSE =  !MyUtil.PAUSE;
						if (MyUtil.PAUSE)
							myButton.setText("Play");
						else
							myButton.setText("Stop");
					}
				});
				myButton1.click(screenX, (height - screenY), new ClickListener() {
					@Override
					public void onClick() throws Exception {
						MyUtil.PAUSE = true;
						editBoard.isVisable = !editBoard.isVisable;
//						toolBar.isVisable = !toolBar.isVisable;
					}
				});
				myButton2.click(screenX, (height - screenY), new ClickListener() {
					@Override
					public void onClick() throws Exception {
						dialog.isVisable = !dialog.isVisable;
						MyUtil.saveMap(lines);
					}
				});
				myButton3.click(screenX, (height - screenY), new ClickListener() {
					@Override
					public void onClick() throws Exception {
						String mapData = FileHelper.read("map");
						MyUtil.gerateMap(world,mapData);
					}
				});
				myButton4.click(screenX, (height - screenY), new ClickListener() {
					@Override
					public void onClick() throws Exception {
						dialog.isVisable = !dialog.isVisable;
						MyUtil.saveTrace(traces);

					}
				});
				editBoard.click(screenX,(height - screenY));
				dialog.click(screenX,(height - screenY));

				/*模式 1*/
				if (MyUtil.mode == 1)
				{
					//TODO

				}else if(MyUtil.mode == 2)
				{
					if (addCount == 0)
						start.set(screenX,height - screenY);
					else
					{
						end.set(screenX,height - screenY);
						Line line = new Line(new Vector2(start.x,start.y), new Vector2(end.x,end.y));
						MyUtil.createLine(world, line);
						lines.add(line);
					}

					addCount++;
					addCount%=2;
				}else if(MyUtil.mode == 3)
				{
					if (addCount == 0)
						start.set(screenX,height - screenY);
					else
					{
						end.set(screenX,height - screenY);
						Line line = new Line(new Vector2(start.x,start.y), new Vector2(end.x,end.y));
						for (int i = 0; i < lines.size(); i++) {
							Line wallLine = lines.get(i);
							Vector2 point =  wallLine.getCrossPoint(line);
							if (point !=null)
							{
								lines.remove(wallLine);
								break;
							}
						}
					}
					addCount++;
					addCount%=2;
				} else if (MyUtil.mode == 4) {

					if (addCount == 0)
						start.set(screenX,height - screenY);
					else
					{
						end.set(screenX,height - screenY);
						Line line = new Line(new Vector2(start.x,start.y), new Vector2(end.x,end.y));
						Population.setRobotTrasform(line.getStart(), (float) Math.atan(line.k));
						MyUtil.mode = 1;
					}
					addCount++;
					addCount%=2;
				}
			}catch (Exception e){
			}

		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mouse_x = screenX;
		mouse_y = MyUtil.WINDOW_HEIGHT - screenY;
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void input(String text) {

    }
	@Override
	public void canceled() {

	}
	public void  getBodyAtMouse(float x, float y, final int flag) {
		x /= P2M;
		y /= P2M;
		world.QueryAABB(new QueryCallback() {
			@Override
			public boolean reportFixture(Fixture fixture) {
				Body body = fixture.getBody();
				BodyInfo bodyInfo = (BodyInfo) body.getUserData();
				if ("robot".equals(bodyInfo.name))
                {
                	if (flag == 1)
					{
						Robot robot = (Robot) bodyInfo.get("robot");
						for (int i = 0; i < Config.popSize; i++) {
							Population.robots[i].isSeleted = false;
							break;

						}
						robot.isSeleted = true;
						selectGene = robot.getBrain();
					}
                	if (flag == 2)
					{
						Robot robot = (Robot) bodyInfo.get("robot");
						robot.isAlive = false;

					}
                }
				return false;
			}
		},x-1,y-1,x+1,y+1);
	}
}
