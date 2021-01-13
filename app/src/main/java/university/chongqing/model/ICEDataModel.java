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

    // 0:融冰 1：积冰
    public float getA1(int type){
        if (type == 0){
            float ret = a1-22 > 0 ? a1-22:0;
            return ret*400/1000;
        }
        return  a1;

    }
    public float getA2(int type){
        if (type == 0) {
            float ret = a2 - 22 > 0 ? a2 - 22 : 0;
            return ret * 400 / 1000;
        }
        return  a2;
    }
    public float getA3(int type){
        if (type == 0){
            float ret = a3-22 > 0 ? a3-22:0;
            return ret*400/1000;
        }

        return  a3;

    }
    public float getA4(int type){
         if (type == 0){
                float ret = a4-22 > 0 ? a4-22:0;
                return ret*400/1000;
         }
         return  a4;

    }
    public float getAllCurrent(int type){

        return getA1(type)+getA2(type)+getA3(type)+getA4(type);
    }
    public float getTemp(){
        return (float) temp/10;
    }
    public float getHumidity(){
        return humidity/10;
    }
    public float getBattery(){
        return battery/1000;
    }


}
