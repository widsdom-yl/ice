package university.chongqing.model;

public class ICEDataModel {
    public String date;
    public float battery;
    public Integer a1;
    public Integer a2;
    public Integer a3;
    public Integer a4;
    public float temp;
    public float humidity;
    public Integer icethickness;
    public float getA1(){
        float ret = a1-22 > 0 ? a1-22:0;
        return ret*400/1000;
    }
    public float getA2(){
        float ret = a2-22 > 0 ? a2-22:0;
        return ret*400/1000;
    }
    public float getA3(){
        float ret = a3-22 > 0 ? a3-22:0;
        return ret*400/1000;
    }
    public float getA4(){
        float ret = a4-22 > 0 ? a4-22:0;
        return ret*400/1000;
    }
    public float getAllCurrent(){
        return getA1()+getA2()+getA3()+getA4();
    }
    public float getTemp(){
        return temp/10;
    }
    public float getHumidity(){
        return humidity/10;
    }
    public float getBattery(){
        return battery/1000;
    }


}
