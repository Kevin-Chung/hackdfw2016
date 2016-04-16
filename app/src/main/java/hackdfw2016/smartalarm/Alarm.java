package hackdfw2016.smartalarm;

/**
 * Created by Kevin on 4/16/2016.
 */
public class Alarm {
    public String alarmName;
    public String wakeUpTime;
    public String days;

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getWakeUpTime() {
        return wakeUpTime;
    }

    public void setWakeUpTime(String wakeUpTime) {
        this.wakeUpTime = wakeUpTime;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public Alarm(String alarmName, String wakeUpTime, String days) {

        this.alarmName = alarmName;
        this.wakeUpTime = wakeUpTime;
        this.days = days;
    }
}
