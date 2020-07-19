package university.chongqing.Bean;

public class DevisionDetailBean {
    public DevisionDetailBean(String name, String elecCurrent, String lineTemp) {
        this.name = name;
        this.elecCurrent = elecCurrent;
        this.lineTemp = lineTemp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getElecCurrent() {
        return elecCurrent;
    }

    public void setElecCurrent(String elecCurrent) {
        this.elecCurrent = elecCurrent;
    }

    public String getLineTemp() {
        return lineTemp;
    }

    public void setLineTemp(String lineTemp) {
        this.lineTemp = lineTemp;
    }

    String name;
    String elecCurrent;
    String lineTemp;

}
