package own.framework.game.framework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created by 挨踢狗 on 2017/6/23.
 */
public class SwingFramework extends GameFramework {

    protected Canvas canvas;
    private JPanel mainPanel;
    private JPanel centerPanel;

    protected  JPanel getMainPanel(){
        if (mainPanel == null){
            mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(getCenterPanel());
        }
        return mainPanel;
    }

    private JPanel getCenterPanel(){
        if (centerPanel == null){
            centerPanel = new JPanel();
            centerPanel.setBackground(appBorder);
            centerPanel.setLayout(null);
            centerPanel.add(canvas);
        }
        return centerPanel;
    }

    private Canvas getCanvas(){
        if (canvas == null){
            canvas = new Canvas();
            canvas.setBackground(appBackground);
        }
        return canvas;
    }

    private void setUpLookAndFeel(){
        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void onCreateAndShowGUI(){

    }

    @Override
    protected void createFramework() {
        setUpLookAndFeel();
        getContentPane().add(getMainPanel());
        setLocationByPlatform(true);
        setSize(appWidth, appHeight);
        setTitle(title);
        getContentPane().setBackground(appBorder);
        setSize(appWidth, appHeight);
        getContentPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                onComponentResized(e);
            }
        });
        setInput(getCanvas());
        onCreateAndShowGUI();
        setVisible(true);
        createBufferStrategy(getCanvas(), 2);
        getCanvas().requestFocus();
    }

    protected void onComponentResized(ComponentEvent e){
        Dimension size = getCenterPanel().getSize();
        setupViewport(size.width, size.height);
        getCanvas().setLocation(vx, vy);
        getCanvas().setSize(vw, vh);
        getCanvas().repaint();
    }

    @Override
    protected void renderFrame(Graphics g) {
        g.clearRect(0, 0, getScreenWidth(), getScreenHeight());
        render(g);
    }

    @Override
    public int getScreenWidth() {
        return getCanvas().getWidth();
    }

    @Override
    public int getScreenHeight() {
        return getCanvas().getHeight();
    }
}
