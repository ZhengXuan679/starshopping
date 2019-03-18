package cn.com.starshopping.pojo;

import java.util.Date;

public class YongHu {
    private int id;             //主键，自增
    private String YongHuMing;  //用户名，唯一，关联客户登录表YongHuMing
    private String NiCheng;     //昵称
    private String XingMing;    //姓名
    private String XingBie;     //性别
    private Date ShengRi;       //生日
    private String ChuShengRiQi;       //生日
    private String XingZuo;     //星座
    private String JuZhuDi;     //居住地
    private String TouXiang;    //头像地址
    private String JiaXiang;    //家乡

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYongHuMing() {
        return YongHuMing;
    }

    public void setYongHuMing(String yongHuMing) {
        YongHuMing = yongHuMing;
    }

    public String getNiCheng() {
        return NiCheng;
    }

    public void setNiCheng(String niCheng) {
        NiCheng = niCheng;
    }

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public String getXingBie() {
        return XingBie;
    }

    public void setXingBie(String xingBie) {
        XingBie = xingBie;
    }

    public Date getShengRi() {
        return ShengRi;
    }

    public void setShengRi(Date shengRi) {
        ShengRi = shengRi;
    }

    public String getXingZuo() {
        return XingZuo;
    }

    public void setXingZuo(String xingZuo) {
        XingZuo = xingZuo;
    }

    public String getJuZhuDi() {
        return JuZhuDi;
    }

    public void setJuZhuDi(String juZhuDi) {
        JuZhuDi = juZhuDi;
    }

    public String getTouXiang() {
        return TouXiang;
    }

    public void setTouXiang(String touXiang) {
        TouXiang = touXiang;
    }

    public String getJiaXiang() {
        return JiaXiang;
    }

    public void setJiaXiang(String jiaXiang) {
        JiaXiang = jiaXiang;
    }

    public String getChuShengRiQi() {
        return ChuShengRiQi;
    }

    public void setChuShengRiQi(String chuShengRiQi) {
        ChuShengRiQi = chuShengRiQi;
    }
}
