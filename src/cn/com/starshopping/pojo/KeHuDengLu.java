package cn.com.starshopping.pojo;

import java.sql.Timestamp;

public class KeHuDengLu {
    private int id;
    private String yonghuming;
    private String shoujihaoma;
    private String youxiang;
    private String mima;
    private int denglucishu;
    private Timestamp zuihouyicidenglushijian;
    private int cuowucishu;
    private int zhuangtai;
    private Timestamp zhuceshijian;
    private String uuid;
    private Timestamp dongjieshijian;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYongHuMing() {
        return yonghuming;
    }

    public void setYongHuMing(String yonghuming) {
        this.yonghuming = yonghuming;
    }

    public String getShouJiHaoMa() {
        return shoujihaoma;
    }

    public void setShouJiHaoMa(String shoujihaoma) {
        this.shoujihaoma = shoujihaoma;
    }

    public String getYouXiang() {
        return youxiang;
    }

    public void setYouXiang(String youxiang) {
        this.youxiang = youxiang;
    }

    public String getMiMa() {
        return mima;
    }

    public void setMiMa(String mima) {
        this.mima = mima;
    }

    public int getDengLuCiShu() {
        return denglucishu;
    }

    public void setDengLuCiShu(int denglucishu) {
        this.denglucishu = denglucishu;
    }

    public Timestamp getZuiHouYiCiDengLuShiJian() {
        return zuihouyicidenglushijian;
    }

    public void setZuiHouYiCiDengLuShiJian(Timestamp zuihouyicidenglushijian) {
        this.zuihouyicidenglushijian = zuihouyicidenglushijian;
    }

    public int getCuoWuCiShu() {
        return cuowucishu;
    }

    public void setCuoWuCiShu(int cuowucishu) {
        this.cuowucishu = cuowucishu;
    }

    public int getZhuangTai() {
        return zhuangtai;
    }

    public void setZhuangTai(int zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public Timestamp getZhuCeShiJian() {
        return zhuceshijian;
    }

    public void setZhuCeShiJian(Timestamp zhuceshijian) {
        this.zhuceshijian = zhuceshijian;
    }

    public String getUuId() {
        return uuid;
    }

    public void setUuId(String uuid) {
        this.uuid = uuid;
    }

    public Timestamp getDongJieShiJian() {
        return dongjieshijian;
    }

    public void setDongJieShiJian(Timestamp dongjieshijian) {
        this.dongjieshijian = dongjieshijian;
    }
}
