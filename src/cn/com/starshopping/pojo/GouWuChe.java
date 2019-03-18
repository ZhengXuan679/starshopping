package cn.com.starshopping.pojo;


public class GouWuChe {
    private int id;                     //主键，自增
    private String yonghuming;          //用户名，关联客户登录表YongHuMing
    private String shangpinbianhao;     //商品编号，不能为空，关联商品信息表
    private int shangpinshuliang;       //商品数量，默认为1，不能为空
    private  String  shangpinmiaoshu;
    private  String  shangpinguige1;
    private  String  shangpinguige2;
    private  String  ShangPinJiaGe;

    public String getShangPinMiaoShu() {
        return shangpinmiaoshu;
    }

    public void setShangPinMiaoShu(String shangpinmiaoshu) {
        this.shangpinmiaoshu = shangpinmiaoshu;
    }

    public String getShangPinGuiGe1() {
        return shangpinguige1;
    }

    public void setShangPinGuiGe1(String shangpinguige1) {
        this.shangpinguige1 = shangpinguige1;
    }

    public String getShangPinGuiGe2() {
        return shangpinguige2;
    }

    public void setShangPinGuiGe2(String shangpinguige2) {
        this.shangpinguige2 = shangpinguige2;
    }

    public String getShangPinJiaGe() {
        return ShangPinJiaGe;
    }

    public void setShangPinJiaGe(String shangPinJiaGe) {
        ShangPinJiaGe = shangPinJiaGe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYongHuMing()
    {
        return yonghuming;
    }


    public void setYongHuMing(String yonghuming) {
        this.yonghuming = yonghuming;
    }

    public String getShangPinBianHao() {
        return shangpinbianhao;
    }

    public void setShangPinBianHao(String shangpinbianhao) {
        this.shangpinbianhao = shangpinbianhao;
    }

    public int getShangPinShuLiang() {
        return shangpinshuliang;
    }

    public void setShangPinShuLiang(int shangpinshuliang) {
        this.shangpinshuliang = shangpinshuliang;
    }
}
