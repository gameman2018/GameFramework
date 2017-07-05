package own.framework.game.examples;

import own.framework.game.framework.GameFramework;
import own.framework.game.framework.WindowFramework;
import own.framework.game.support.FrameRate;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by 挨踢狗 on 2017/6/29.
 */
public class RelativeMouseExample extends WindowFramework {
    private Point point = new Point(0, 0);
    private boolean disableCursor = false;
    private Canvas canvas;


    @Override
    protected void createFramework() {
        super.createFramework();
        this.canvas = getCanvas();
    }

    @Override
    protected void processInput(float delta) {
        super.processInput(delta);
        Point p = mouse.getPosition();

        if (mouse.isRelative()){
            point.x += p.x;
            point.y += p.y;
        }else {
            point.x = p.x;
            point.y = p.y;
        }

        if (point.x + 25 < 0){
            point.x = canvas.getWidth() - 1;
        }else if (point.x > canvas.getWidth() - 1){
            point.x = -25;
        }

        if (point.y + 25 < 0){
            point.y = canvas.getHeight() - 1;
        }else if (point.y > canvas.getHeight() - 1){
            point.y = -25;
        }

        while(keyboard.processEvent())
        {
            if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)){
                mouse.setRelative(!mouse.isRelative());
            }

            if (keyboard.keyDownOnce(KeyEvent.VK_C)){
                disableCursor = !disableCursor;
                if (disableCursor){
                    disableCursor();
                }else {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        }
    }

    @Override
    protected void render(Graphics g) {
        super.render(g);
        g.drawString(mouse.getPosition().toString(), 20, textPos += 15);
        g.drawString("Relative: " + mouse.isRelative(), 20, textPos += 15);
        g.drawString("Press Space to switch mouse modes", 20, textPos += 15);
        g.drawString("Press C to toggle cursor", 20, textPos += 15);
        g.setColor(Color.WHITE);
        g.drawRect(point.x, point.y, 25, 25);
    }

    private void disableCursor(){
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.createImage("");
        Point point = new Point(0, 0);
        String name = "CanBeAnyThing";
        Cursor cursor = tk.createCustomCursor(image, point, name);
        setCursor(cursor);
    }
}
