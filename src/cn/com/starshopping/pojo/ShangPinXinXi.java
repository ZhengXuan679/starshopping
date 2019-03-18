package cn.com.starshopping.pojo;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: Wong
 * Date: 2019/01/05
 * Description:
 * Version: V1.0
 */
public class ShangPinXinXi implements Cloneable{
    private int id;
    private String shangPinBianHao;
    private String shangPinZuBianHao;
    private String shangPinMingChen;
    private String shangPinLeiXin;
    private String shangPinGuiGe1;
    private String shangPinGuiGe2;
    private String pinPai;
    private double shangPinJiaGe;
    private double shangPinChengBen;
    private String shangPinMiaoSu;
    private int yueXiaoLiang;
    private int zongXiaoLiang;
    private int shangPinZhuangTai;
    private Timestamp caoZuoShiJian;

    public double getShangPinJiaGe() {
        return shangPinJiaGe;
    }

    public void setShangPinJiaGe(double shangPinJiaGe) {
        this.shangPinJiaGe = shangPinJiaGe;
    }

    public double getShangPinChengBen() {
        return shangPinChengBen;
    }

    public void setShangPinChengBen(double shangPinChengBen) {
        this.shangPinChengBen = shangPinChengBen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShangPinBianHao() {
        return shangPinBianHao;
    }

    public void setShangPinBianHao(String shangPinBianHao) {
        this.shangPinBianHao = shangPinBianHao;
    }

    public String getShangPinZuBianHao() {
        return shangPinZuBianHao;
    }

    public void setShangPinZuBianHao(String shangPinZuBianHao) {
        this.shangPinZuBianHao = shangPinZuBianHao;
    }

    public String getShangPinMingChen() {
        return shangPinMingChen;
    }

    public void setShangPinMingChen(String shangPinMingChen) {
        this.shangPinMingChen = shangPinMingChen;
    }

    public String getShangPinLeiXin() {
        return shangPinLeiXin;
    }

    public void setShangPinLeiXin(String shangPinLeiXin) {
        this.shangPinLeiXin = shangPinLeiXin;
    }

    public String getShangPinGuiGe1() {
        return shangPinGuiGe1;
    }

    public void setShangPinGuiGe1(String shangPinGuiGe1) {
        this.shangPinGuiGe1 = shangPinGuiGe1;
    }

    public String getShangPinGuiGe2() {
        return shangPinGuiGe2;
    }

    public void setShangPinGuiGe2(String shangPinGuiGe2) {
        this.shangPinGuiGe2 = shangPinGuiGe2;
    }

    public String getPinPai() {
        return pinPai;
    }

    public void setPinPai(String pinPai) {
        this.pinPai = pinPai;
    }

    public String getShangPinMiaoSu() {
        return shangPinMiaoSu;
    }

    public void setShangPinMiaoSu(String shangPinMiaoSu) {
        this.shangPinMiaoSu = shangPinMiaoSu;
    }

    public int getYueXiaoLiang() {
        return yueXiaoLiang;
    }

    public void setYueXiaoLiang(int yueXiaoLiang) {
        this.yueXiaoLiang = yueXiaoLiang;
    }

    public int getZongXiaoLiang() {
        return zongXiaoLiang;
    }

    public void setZongXiaoLiang(int zongXiaoLiang) {
        this.zongXiaoLiang = zongXiaoLiang;
    }

    public int getShangPinZhuangTai() {
        return shangPinZhuangTai;
    }

    public void setShangPinZhuangTai(int shangPinZhuangTai) {
        this.shangPinZhuangTai = shangPinZhuangTai;
    }

    public Timestamp getCaoZuoShiJian() {
        return caoZuoShiJian;
    }

    public void setCaoZuoShiJian(Timestamp caoZuoShiJian) {
        this.caoZuoShiJian = caoZuoShiJian;
    }

    @Override
    public Object clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {}
        return o;

    }
}
