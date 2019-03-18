package cn.com.starshopping.pojo;

public class ShouHuoDiZhi {
    private int id;                 //主键，自增
    private String yongHuMing;      //用户名，关联客户登录表YongHuMing
    private String shouHuoRen;      //收货人，不能为空
    private String suoZaiDiQu;      //所在地区，不能为空
    private String xiangXiDiZhi;    //详细地址，不为空
    private String youBian;         //邮编，默认6个0
    private String lianXiDianHua;   //联系电话，不能为空
    private int zhuangTai; //1代表默认地址，0代表其他地址，默认为0不能为空

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYongHuMing() {
        return yongHuMing;
    }

    public void setYongHuMing(String yongHuMing) {
        this.yongHuMing = yongHuMing;
    }

    public String getShouHuoRen() {
        return shouHuoRen;
    }

    public void setShouHuoRen(String shouHuoRen) {
        this.shouHuoRen = shouHuoRen;
    }

    public String getSuoZaiDiQu() {
        return suoZaiDiQu;
    }

    public void setSuoZaiDiQu(String suoZaiDiQu) {
        this.suoZaiDiQu = suoZaiDiQu;
    }

    public String getXiangXiDiZhi() {
        return xiangXiDiZhi;
    }

    public void setXiangXiDiZhi(String xiangXiDiZhi) {
        this.xiangXiDiZhi = xiangXiDiZhi;
    }

    public String getYouBian() {
        return youBian;
    }

    public void setYouBian(String youBian) {
        this.youBian = youBian;
    }

    public String getLianXiDianHua() {
        return lianXiDianHua;
    }

    public void setLianXiDianHua(String lianXiDianHua) {
        this.lianXiDianHua = lianXiDianHua;
    }

    public int getZhuangTai() {
        return zhuangTai;
    }

    public void setZhuangTai(int zhuangtai) {
        this.zhuangTai = zhuangtai;
    }
}
