package own.framework.game.framework;

import own.framework.game.input.KeyboardInput;
import own.framework.game.support.FrameRate;
import own.framework.game.support.Matrix3x3f;
import own.framework.game.support.Utility;
import own.framework.game.support.Vector2f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

/**
 * Created by 挨踢狗 on 2017/6/23.
 */
public abstract class GameFramework extends JFrame implements Runnable {
    private BufferStrategy bs;
    private volatile boolean running;
    private Thread gameThread;
    protected int vx;
    protected int vy;
    protected int vw;
    protected int vh;
    protected FrameRate frameRate;
    //    protected RelativeMouseInput mouse;
    protected KeyboardInput keyboard;
    protected Color appBackground = Color.BLACK;
    protected Color appBorder = Color.LIGHT_GRAY;
    protected Color appFPSColor = Color.GREEN;
    protected Font appFont = new Font("Courier New", Font.PLAIN, 14);
    protected String title = "TBD-Title";
    protected float appBorderScale = 0.8f;
    protected int appWidth = 640;
    protected int appHeight = 640;
    protected float appWorldWidth = 2.0f;
    protected float appWorldHeight = 2.0f;
    protected long appSleep = 10L;
    protected boolean appMaintainratio = false;
    protected boolean appDisableCursor = false;
    protected int textPos = 0;

    /**
     * constructor
     */
    public GameFramework() {

    }

    protected abstract void createFramework();

    protected abstract void renderFrame(Graphics g);

    public abstract int getScreenWidth();

    public abstract int getScreenHeight();

    /**
     * do start thread and some init of create GUI
     */

    protected void createAndShowGUI() {
        createFramework();
        if (appDisableCursor) {
            disableCursor();
        }
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * add mouse and keyboard listener to component like canvas
     *
     * @param component
     */

    protected void setInput(Component component) {
        keyboard = new KeyboardInput();
        component.addKeyListener(keyboard);
        // not mouse
    }

    protected void createBufferStrategy(Canvas component, int bufferPools) {
        component.createBufferStrategy(bufferPools);
        bs = component.getBufferStrategy();
    }

    protected void createBufferStrategy(Window window, int bufferPools) {
        window.createBufferStrategy(bufferPools);
        bs = window.getBufferStrategy();
    }

    protected void setupViewport(int sw, int sh) {
        int w = (int) (sw * appBorderScale);
        int h = (int) (sh * appBorderScale);
        int x = (sw - w) / 2;
        int y = (sh - h) / 2;
        vw = w;
        vh = (int) (w * appWorldHeight / appWorldWidth);
        if (vh > h) {
            vw = (int) (h * appWorldWidth / appWorldHeight);
            vh = h;
        }
        vx = x + (w - vw) / 2;
        vy = y + (h - vh) / 2;
    }

    protected Matrix3x3f getViewportTransform() {
        return Utility.createViewport(appWorldWidth, appWorldHeight, getScreenWidth(), getScreenHeight());
    }

    protected Matrix3x3f getReverseViewportATransform() {
        return Utility.createReverseViewport(appWorldWidth, appWorldHeight, getScreenWidth(), getScreenHeight());
    }

    protected Vector2f getWorldMousePosition() {
        Matrix3x3f screentToWorld = getReverseViewportATransform();
        //
    }

    @Override
    public void run() {
        running = true;
        initialize();
        long curTime = System.nanoTime();
        long lastTime = curTime;
        double nsPerFrame;
        while (running){
            curTime = System.nanoTime();
            nsPerFrame = curTime - lastTime;
            gameLoop((float)(nsPerFrame / 1.0E9));
            lastTime = curTime;
        }
        terminate();
    }

    protected void initialize(){
        frameRate = new FrameRate();
        frameRate.initialize();
    }

    protected void terminate(){

    }

    private void gameLoop(float delta){
        processInput(delta);
        updateObject(delta);
        renderFrame();
        sleep(appSleep);
    }

    private void renderFrame(){
        do {
            do {
                Graphics g = null;
                try{
                    g = bs.getDrawGraphics();
                    renderFrame(g);
                }finally {
                    if (g != null){
                        g.dispose();
                    }
                }
            }while (bs.contentsRestored());
            bs.show();
        }while (bs.contentsLost());
    }

    private void sleep(long sleep){
        try{
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void processInput(float delta){
        keyboard.poll();
    }

    protected void updateObject(float delta){

    }

    protected void render(Graphics g){
        g.setFont(appFont);
        g.setColor(appFPSColor);
        frameRate.caculate();
        textPos = Utility.drawString(g, 20, 0, String.valueOf(frameRate.fps()));
    }

    private void disableCursor(){
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.createImage("");
        Point point = new Point(0,0);
        String name = "CanBeAnything";
        Cursor cursor = tk.createCustomCursor(image, point, name);
        setCursor(cursor);
    }

    protected void shutDown(){
        if (Thread.currentThread() != gameThread){
            try{
                running = false;
                gameThread.join();
                onShutDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    shutDown();
                }
            });
        }
    }

    protected void onShutDown(){

    }

    protected static void launchApp(final GameFramework app){
        app.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                app.shutDown();
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                app.createAndShowGUI();
            }
        });
    }
}
