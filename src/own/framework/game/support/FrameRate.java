package own.framework.game.support;

/**
 * Created by 挨踢狗 on 2017/6/23.
 */
public class FrameRate {
    private long now;
    private long last;
    private long period;
    private int count;
    private int rate;

    public void initialize(){
        now = System.currentTimeMillis();
        last = now;
        count = 0;
        period = 0;
        rate = 0;
    }

    public void calculate(){
        now = System.currentTimeMillis();
        period += now - last;
        last = now;
        count++;
        if (period >= 1000){
            period -= 1000;
            rate = count;
            count = 0;
        }
    }

    public int fps(){
        return rate;
    }

}
