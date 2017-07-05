package own.framework.game.framework;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created by 挨踢狗 on 2017/6/23.
 */
public class WindowFramework extends GameFramework {

    private Canvas canvas;

    public WindowFramework(){
        launchApp(this);
    }

    @Override
    protected void createFramework() {
        canvas = new Canvas();
        canvas.setBackground(appBackground);
        canvas.setIgnoreRepaint(true);
        getContentPane().add(canvas);
        setLocationByPlatform(true);
        if (appMaintainratio){
            getContentPane().setBackground(appBorder);
            setSize(appWidth, appHeight);
            setLayout(null);
            getContentPane().addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    onComponentResized(e);
                }
            });
        }else {
            canvas.setSize(appWidth, appHeight);
            pack();
        }
        setTitle(title);
        setInput(canvas);
        setVisible(true);
        createBufferStrategy(canvas, 2);
        canvas.requestFocus();
    }

    protected Canvas getCanvas(){
        return canvas;
    }

    protected void onComponentResized(ComponentEvent e){
        Dimension size = getContentPane().getSize();
        setupViewport(size.width, size.height);
        canvas.setLocation(vx, vy);
        canvas.setSize(vw, vh);
    }

    @Override
    protected void renderFrame(Graphics g) {
        g.clearRect(0, 0, getScreenWidth(), getScreenHeight());
        render(g);
    }

    @Override
    public int getScreenWidth() {
        return canvas.getWidth();
    }

    @Override
    public int getScreenHeight() {
        return canvas.getHeight();
    }
}
