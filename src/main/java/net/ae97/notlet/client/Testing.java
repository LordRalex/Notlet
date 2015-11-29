package net.ae97.notlet.client;

/**
 *
 * @author Adam
 */
/*
 import org.lwjgl.LWJGLException;
 import org.lwjgl.opengl.Display;
 import org.lwjgl.opengl.DisplayMode;

 import org.lwjgl.LWJGLException;
 import org.lwjgl.input.Keyboard;
 import org.lwjgl.input.Mouse;

 import static org.lwjgl.opengl.GL11.*;

 import org.lwjgl.LWJGLException;
 import org.lwjgl.opengl.Display;
 import org.lwjgl.opengl.DisplayMode;
 import org.lwjgl.opengl.GL11;

 import java.io.IOException;

 import org.newdawn.slick.Color;
 import org.newdawn.slick.opengl.Texture;
 import org.newdawn.slick.opengl.TextureLoader;
 import org.newdawn.slick.util.ResourceLoader;



 public class Testing {

 // The texture that will hold the image details //
 private Texture texture;
 //private Texture dirt_Texture;
    
 int x = 0;
 int y = 0;
     
 ///
 // Start the example
 //
 public void start() {
 initGL(800,600);
 init();

 while (true) {
 GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
 render();
             
 Display.update();
 Display.sync(100);
 
 if (Display.isCloseRequested()) {
 Display.destroy();
 System.exit(0);
 }
 }
 }

 //
 // Initialize the GL display
 //
 // @param width The width of the display
 // @param height The height of the display
 //
 private void initGL(int width, int height) {
 try {
 Display.setDisplayMode(new DisplayMode(width,height));
 Display.create();
 Display.setVSyncEnabled(true);
 } catch (LWJGLException e) {
 e.printStackTrace();
 System.exit(0);
 }

 GL11.glEnable(GL11.GL_TEXTURE_2D);
        

 GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

 // enable alpha blending
 GL11.glEnable(GL11.GL_BLEND);
 GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
         
 GL11.glViewport(0,0,width,height);
 GL11.glMatrixMode(GL11.GL_MODELVIEW);

 GL11.glMatrixMode(GL11.GL_PROJECTION);
 GL11.glLoadIdentity();
 GL11.glOrtho(0, width, height, 0, 1, -1);
 GL11.glMatrixMode(GL11.GL_MODELVIEW);
 }
     
 //
 // Initialize resources
 //
 public void init() {
         
 try {
 // load texture from PNG file
 texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("image.png"));
 //dirt_Texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("dirt.png"));
         
 System.out.println("Texture loaded: "+texture);
 System.out.println(">> Image width: "+texture.getImageWidth());
 System.out.println(">> Image height: "+texture.getImageHeight());
 System.out.println(">> Texture width: "+texture.getTextureWidth());
 System.out.println(">> Texture height: "+texture.getTextureHeight());
 System.out.println(">> Texture ID: "+texture.getTextureID());
                      
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 
 //
 // draw a quad with the image on it
 //
 public void render() {
 Color.white.bind();
 texture.bind(); // or GL11.glBind(texture.getTextureID());
 //dirt_Texture.bind();
 while (!Display.isCloseRequested()) {

 // render OpenGL here
 // Clear the screen and depth buffer
 GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
 GL11.glBegin(GL11.GL_QUADS);
 GL11.glTexCoord2f(0,0);
 GL11.glVertex2f(x,y);
 GL11.glTexCoord2f(1,0);
 GL11.glVertex2f(x+texture.getTextureWidth(),y);
 GL11.glTexCoord2f(1,1);
 GL11.glVertex2f(x+texture.getTextureWidth(),y+texture.getTextureHeight());
 GL11.glTexCoord2f(0,1);
 GL11.glVertex2f(x,y+texture.getTextureHeight());
 GL11.glEnd();
                
 pollInput();
 Display.update();

 }
 }
 public void pollInput() {

 if (Mouse.isButtonDown(0)) {
 int a = Mouse.getX();
 int b = Mouse.getY();
             
 System.out.println("MOUSE DOWN @ X: " + a + " Y: " + b);
 }
 while (Keyboard.next()) {
 if(Keyboard.getEventKeyState()) {
 if (Keyboard.getEventKey() == Keyboard.KEY_A) {
 x = x-16;
 System.out.println("A Key Pressed");
 }
 if(Keyboard.getEventKey() == Keyboard.KEY_S) {
 y = y+16;
 System.out.println("S Key Pressed");
 }
 if (Keyboard.getEventKey() == Keyboard.KEY_D) {
 x = x+16;
 System.out.println("D Key Pressed");
 }
 if (Keyboard.getEventKey() == Keyboard.KEY_W) {
 y = y-16;
 System.out.println("W Key Pressed");
 }
 if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
 //x = x+20;
 //y = y+20;
 System.out.println("SPACE Key Pressed");
 }
 } else {
 if (Keyboard.getEventKey() == Keyboard.KEY_A) {
 System.out.println("A Key Released");
 }
 if (Keyboard.getEventKey() == Keyboard.KEY_S) {
 System.out.println("S Key Released");
 }
 if (Keyboard.getEventKey() == Keyboard.KEY_D) {
 System.out.println("D Key Released");
 }
 if (Keyboard.getEventKey() == Keyboard.KEY_W) {
 System.out.println("W Key Released");
 }
 if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
 System.out.println("Space Key Released");
 }
 }
 }
 }
    
    

 //
 // Main Class
 //
 public static void main(String[] argv) {
 Testing textureExample = new Testing();
 textureExample.start();
 }
 }
 */
///////////////////////////////////////////////////////////////////////////////////////////////////////
/*
 import org.lwjgl.LWJGLException;
 import org.lwjgl.Sys;
 import org.lwjgl.input.Keyboard;
 import org.lwjgl.opengl.Display;
 import org.lwjgl.opengl.DisplayMode;
 import org.lwjgl.opengl.GL11;

 public class Testing {
 
 // position of quad //
 float x = 0, y = 600;
 
 public void start() {
 try {
 Display.setDisplayMode(new DisplayMode(800, 600));
 Display.create();
 } catch (LWJGLException e) {
 e.printStackTrace();
 System.exit(0);
 }

 initGL(); // init OpenGL
 
 while (!Display.isCloseRequested()) {
 //int delta = getDelta();
 
 update();
 renderGL();

 Display.update();
 Display.sync(60); // cap fps to 60fps
 }
 
 Display.destroy();
 }

 public void update() {
 // rotate quad
 //rotation += 0.15f * delta;
 
 if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) x -= 16;
 if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) x += 16;
 
 if (Keyboard.isKeyDown(Keyboard.KEY_UP)) y += 16;
 if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) y -= 16;
 
 // keep quad on the screen
 if (x < 0) x = 0;
 if (x > 800) x = 800;
 if (y < 0) y = 0;
 if (y > 600) y = 600;
 }
 
 //
 // Get the accurate system time
 //
 // @return The system time in milliseconds
 //
 public long getTime() {
 return (Sys.getTime() * 1000) / Sys.getTimerResolution();
 }
 
 public void initGL() {
 GL11.glMatrixMode(GL11.GL_PROJECTION);
 GL11.glLoadIdentity();
 GL11.glOrtho(0, 800, 0, 600, 1, -1);
 GL11.glMatrixMode(GL11.GL_MODELVIEW);
 }

 public void renderGL() {
 // Clear The Screen And The Depth Buffer
 GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
 
 // R,G,B,A Set The Color To Blue One Time Only
 GL11.glColor3f(0.5f, 0.5f, 1.0f);

 // draw quad
 GL11.glPushMatrix();
 GL11.glTranslatef(x, y, 0);
 //GL11.glRotatef(rotation, 0f, 0f, 1f);
 GL11.glTranslatef(-x, -y, 0);
 
 GL11.glBegin(GL11.GL_QUADS);
 GL11.glVertex2f(x - 50, y - 50);
 GL11.glVertex2f(x + 50, y - 50);
 GL11.glVertex2f(x + 50, y + 50);
 GL11.glVertex2f(x - 50, y + 50);
 GL11.glEnd();
 GL11.glPopMatrix();
 }
 
 public static void main(String[] argv) {
 Testing timerExample = new Testing();
 timerExample.start();
 }
 }
 */
/////////////////////////////////////////////////////////////////////////////////
import java.io.IOException;
import net.ae97.notlet.client.network.ServerConnection;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Testing {

    // The texture that will hold the image details //
    private Texture backg;
    private Texture wall;
    private Texture dirt;
    private Texture texture;
    private Texture textureU;
    private Texture textureD;
    private Texture textureL;
    private Texture textureR;
    private Texture currentPlayer;
    private ServerConnection connection;

    //private Texture dirt_Texture;
    final int SpriteScaleFactor = 1;

    int x = 0;
    int y = 0;

    ///
    // Start the example
    //
    public void start() throws LWJGLException, IOException {
        init();

        render();
    }

    //
    // Initialize the GL display
    //
    // @param width The width of the display
    // @param height The height of the display
    //
    private void initGL(int width, int height) throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(width, height));
        Display.create();
        Display.setVSyncEnabled(true);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // enable alpha blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glViewport(0, 0, width, height);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    //
    // Initialize resources
    //
    public void init() throws IOException {

        // load texture from PNG file
        backg = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("wall.png"));
        wall = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("wall.png"));
        dirt = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("dirt.png"));
        currentPlayer = texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("rangerD.png"));
        textureU = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("rangerU.png"));
        textureD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("rangerD.png"));
        textureL = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("rangerL.png"));
        textureR = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("rangerR.png"));
        //dirt_Texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("dirt.png"));

        System.out.println("Texture loaded: " + texture);
        System.out.println(">> Image width: " + texture.getImageWidth() * SpriteScaleFactor);
        System.out.println(">> Image height: " + texture.getImageHeight() * SpriteScaleFactor);
        System.out.println(">> Texture width: " + texture.getTextureWidth() * SpriteScaleFactor);
        System.out.println(">> Texture height: " + texture.getTextureHeight() * SpriteScaleFactor);
        System.out.println(">> Texture ID: " + texture.getTextureID());
    }

    //
    // draw a quad with the image on it
    //
    public void render() {
        Color.white.bind();
        Display.sync(60);
        //texture.bind();
        //dirt_Texture.bind();
        while (!Display.isCloseRequested()) {

            // render OpenGL here
            // Clear the screen and depth buffer
            renderMap();
            pollInput();
            renderGL();

            Display.update();

        }

        connection.close();
    }

    public void pollInput() {

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            x -= 16;
            currentPlayer = textureL;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            x += 16;
            currentPlayer = textureR;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            y -= 16;
            currentPlayer = textureU;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            y += 16;
            currentPlayer = textureD;
        }

        // keep quad on the screen
        if (x < 0) {
            x = 0;
        }
        if (x > 766) {
            x = 766;
        }
        if (y < 0) {
            y = 0;
        }
        if (y > 562) {
            y = 562;
        }

    }

    public void renderMap() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        if (GameInstance.getMap() == null) {
            return;
        }
        boolean[][] map = GameInstance.getMap();
        dirt.bind();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j]) {
                    renderFG(i * 32, j * 32);
                }
            }
        }

        wall.bind();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (!map[i][j]) {
                    renderFG(i * 32, j * 32);
                }
            }
        }

    }

    public void renderFG(int x, int y) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(x + 32, y);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(x + 32, y + 32);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(x, y + 32);
        GL11.glEnd();
    }

    public void renderGL() {
        // Clear The Screen And The Depth Buffer
        currentPlayer.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(x + texture.getTextureWidth() * SpriteScaleFactor, y);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(x + texture.getTextureWidth() * SpriteScaleFactor, y + texture.getTextureHeight() * SpriteScaleFactor);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(x, y + texture.getTextureHeight() * SpriteScaleFactor);
        GL11.glEnd();
        //GL11.glPopMatrix();

    }

    //
    // Main Class
    //
    public static void display(ServerConnection connection) throws LWJGLException, IOException {
        Testing testing = new Testing();
        testing.connection = connection;
        testing.start();
    }
}
