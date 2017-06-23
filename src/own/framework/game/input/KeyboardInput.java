package own.framework.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

/**
 * Created by 挨踢狗 on 2017/6/23.
 */
public class KeyboardInput implements KeyListener{

    enum EventType {
        PRESSED,
        RELEASED,
        TYPED
    }

    class Event {
        KeyEvent event;
        EventType eventType;

        public Event(KeyEvent event, EventType eventType){
            this.event = event;
            this.eventType = eventType;
        }
    }

    private LinkedList<Event> gameThread = new LinkedList<>();
    private LinkedList<Event> eventThread = new LinkedList<>();
    private Event event = null;
    private int[] polled;

    public KeyboardInput(){
        polled = new int[256];
    }

    public boolean keyDown(int keyCode){
        return (keyCode == event.event.getKeyCode() && polled[keyCode] > 0);
    }

    public boolean keyDownOnce(int keyCode){
        return (keyCode == event.event.getKeyCode() && polled[keyCode] == 1);
    }

    public boolean processEvent(){
        event = gameThread.poll();
        if (event != null){
            int keyCode = event.event.getKeyCode();
            if (keyCode > 0 && keyCode < polled.length){
                if (event.eventType == EventType.PRESSED){
                    polled[keyCode] += 1;
                }else if (event.eventType == EventType.RELEASED){
                    polled[keyCode] = 0;
                }
            }
        }
        return event != null;
    }

    public Character getKeyTyped(){
        if (event.eventType != EventType.TYPED){
            return null;
        }else {
            return event.event.getKeyChar();
        }
    }

    public synchronized void poll(){
        LinkedList<Event> swap = eventThread;
        eventThread = gameThread;
        gameThread = swap;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        eventThread.add(new Event(e, EventType.TYPED));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        eventThread.add(new Event(e, EventType.PRESSED));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        eventThread.add(new Event(e, EventType.RELEASED));
    }
}
