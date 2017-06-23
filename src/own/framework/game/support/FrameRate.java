package own.framework.game.support;

/**
 * Created by 挨踢狗 on 2017/6/23.
 */
public class FrameRate {
    private long now;
    private long last;
    private int count;
    private int rate;

    public void initialize(){
        now = System.nanoTime();
        last = now;
        count = 0;
        rate = 0;
    }

    public void caculate(){
        now = System.nanoTime();
        count += now - last;
        last = now;
        rate++;
        if (count >= 1000){
            count -= 1000;
            rate = 0;
        }
    }

    public int fps(){
        return rate;
    }

}
