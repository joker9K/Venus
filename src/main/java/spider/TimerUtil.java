package spider;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangwt n 2017/11/20.
 */
public class TimerUtil{
    Timer timer;

    public TimerUtil(){
        Date time = getTime();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, time);
    }

    public Date getTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 38);
        calendar.set(Calendar.SECOND, 0);
        Date time = calendar.getTime();
        return time;
    }


    public static void main(String[] args) {
        new TimerUtil();
    }
}

