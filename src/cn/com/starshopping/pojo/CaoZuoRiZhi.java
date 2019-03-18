package cn.com.starshopping.pojo;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: ZouQinLun
 * Date: 2019/01/15
 * Description:
 * Version: V1.0
 */
public class CaoZuoRiZhi {
    private String yongHuMing;
    private String caoZuoJiLu;
    private Timestamp shiJian;

    public String getYongHuMing() {
        return yongHuMing;
    }

    public void setYongHuMing(String yongHuMing) {
        this.yongHuMing = yongHuMing;
    }

    public String getCaoZuoJiLu() {
        return caoZuoJiLu;
    }

    public void setCaoZuoJiLu(String caoZuoJiLu) {
        this.caoZuoJiLu = caoZuoJiLu;
    }

    public Timestamp getShiJian() {
        return shiJian;
    }

    public void setShiJian(Timestamp shiJian) {
        this.shiJian = shiJian;
    }
}
