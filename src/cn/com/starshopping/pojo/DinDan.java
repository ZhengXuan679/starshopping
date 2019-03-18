package cn.com.starshopping.pojo;

import java.sql.Timestamp;
import java.util.List;

public class DinDan {
    private int id = -1;                 //主键，自增，设置初始值，便于判断用户是否传入id
    private List<String> dingDanHao;  //订单编号，不能为空
    private String yongHuMing;      //用户名，关联客户登录表YongHuMing
    private String shangPinXinXi;   //商品信息，存入一个JSONArray中，JSONArray格式为[{商品编号：bianhao，商品数量:int,商品单价：￥，商品总价，￥}]
    private String wuLiuDanHao;     //物流单号
    private double dingDanZongE = -1;       //订单总额，本订单总共价格，设置一个默认值，如果用户不输入避免默认为0
    private String dingDanShiJian;//客户支付成功后的时间
    private int dingDanZhuangTai;   //默认为0，用户点击删除订单，状态改为1
    private String shouHuoDiZhi;    // 收货地址
    private int zongTiaoShu;           // 查询满足条件的订单总条数，非数据库字段
    private String dingDanBianHao;          // 这个用来传递单个订单号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getDingDanHao() {
        return dingDanHao;
    }

    public void setDingDanHao(List<String> dingDanHao) {
        this.dingDanHao = dingDanHao;
    }

    public String getYongHuMing() {
        return yongHuMing;
    }

    public void setYongHuMing(String yongHuMing) {
        this.yongHuMing = yongHuMing;
    }

    public String getShangPinXinXi() {
        return shangPinXinXi;
    }

    public void setShangPinXinXi(String shangPinXinXi) {
        this.shangPinXinXi = shangPinXinXi;
    }

    public String getWuLiuDanHao() {
        return wuLiuDanHao;
    }

    public void setWuLiuDanHao(String wuLiuDanHao) {
        this.wuLiuDanHao = wuLiuDanHao;
    }

    public double getDingDanZongE() {
        return dingDanZongE;
    }

    public void setDingDanZongE(double dingDanZongE) {
        this.dingDanZongE = dingDanZongE;
    }

    public String getDingDanShiJian() {
        return dingDanShiJian;
    }

    public void setDingDanShiJian(String dingDanShiJian) {
        this.dingDanShiJian = dingDanShiJian;
    }

    public int getDingDanZhuangTai() {
        return dingDanZhuangTai;
    }

    public void setDingDanZhuangTai(int dingDanZhuangTai) {
        this.dingDanZhuangTai = dingDanZhuangTai;
    }

    public String getShouHuoDiZhi() {
        return shouHuoDiZhi;
    }

    public void setShouHuoDiZhi(String shouHuoDiZhi) {
        this.shouHuoDiZhi = shouHuoDiZhi;
    }

    public int getZongTiaoShu() {
        return zongTiaoShu;
    }

    public void setZongTiaoShu(int zongTiaoShu) {
        this.zongTiaoShu = zongTiaoShu;
    }

    public String getDingDanBianHao() {
        return dingDanBianHao;
    }

    public void setDingDanBianHao(String dingDanBianHao) {
        this.dingDanBianHao = dingDanBianHao;
    }
}
