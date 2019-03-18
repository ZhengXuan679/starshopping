package cn.com.starshopping.pojo;

import java.sql.Timestamp;

public class KeFuDengLu {
    private int id;             //主键，自增
    private String xingMing;    //客服姓名，不能为空
    private String shouJiHaoMa; //客服手机号码，不能为空，客服可以通过手机号码登录平台
    private String miMa;        //密码，默认为6个1，第一次登录后强制跳转修改密码
    private int qianDaoCiShu;   //签到次数
    private Timestamp zuiHouQianDao;    //最后签到时间
    private String qianDaoShiJian;      //签到时间，存储为一个JSONArray，存储格式[datetime,datetime,…]
    private String shenFenZheng; // 身份证号码
    private int zhuangTai;  // 状态
    private int cuoWuCiShu;  // 错误次数
    private int zongTiaoShu;   // 总条数，不属于数据库，只是用来查询出总条数传递数据

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getXingMing() {
        return xingMing;
    }

    public void setXingMing(String xingMing) {
        this.xingMing = xingMing;
    }

    public String getShouJiHaoMa() {
        return shouJiHaoMa;
    }

    public void setShouJiHaoMa(String shouJiHaoMa) {
        this.shouJiHaoMa = shouJiHaoMa;
    }

    public String getMiMa() {
        return miMa;
    }

    public void setMiMa(String miMa) {
        this.miMa = miMa;
    }

    public int getQianDaoCiShu() {
        return qianDaoCiShu;
    }

    public void setQianDaoCiShu(int qianDaoCiShu) {
        this.qianDaoCiShu = qianDaoCiShu;
    }

    public Timestamp getZuiHouQianDao() {
        return zuiHouQianDao;
    }

    public void setZuiHouQianDao(Timestamp zuiHouQianDao) {
        this.zuiHouQianDao = zuiHouQianDao;
    }

    public String getQianDaoShiJian() {
        return qianDaoShiJian;
    }

    public void setQianDaoShiJian(String qianDaoShiJian) {
        this.qianDaoShiJian = qianDaoShiJian;
    }

    public String getShenFenZheng() {
        return shenFenZheng;
    }

    public void setShenFenZheng(String shenFenZheng) {
        this.shenFenZheng = shenFenZheng;
    }

    public int getZhuangTai() {
        return zhuangTai;
    }

    public void setZhuangTai(int zhuangTai) {
        this.zhuangTai = zhuangTai;
    }

    public int getCuoWuCiShu() {
        return cuoWuCiShu;
    }

    public void setCuoWuCiShu(int cuoWuCiShu) {
        this.cuoWuCiShu = cuoWuCiShu;
    }

    public int getZongTiaoShu() {
        return zongTiaoShu;
    }

    public void setZongTiaoShu(int zongTiaoShu) {
        this.zongTiaoShu = zongTiaoShu;
    }
}
