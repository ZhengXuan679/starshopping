package cn.com.starshopping.service.zx;

import cn.com.starshopping.dao.zx.FenLeiChaXunDao;
import cn.com.starshopping.pojo.PageInfo;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class FenLeiChaXunService_zheng {
    public  JSONObject  FenLeiChaXunService_zheng(HttpServletRequest request){
        String code = request.getParameter("code");//代表分类的名称
        String currentPage1 = request.getParameter("currentPage");//代表当前为第几页
        String pageSize1 = request.getParameter("pageSize");//每页显示条数 默认为6条
        int currentPage=0;
        int pageSize=0;
        int total=0;
        if(currentPage1!=null && !currentPage1.equals("")){
             currentPage = Integer.parseInt(currentPage1);
        }
        if(pageSize1!=null && !pageSize1.equals("")){
             pageSize = Integer.parseInt(pageSize1);
        }
        PageInfo pageInfo = new PageInfo(currentPage,pageSize,total);
        if(code.equals("服饰") || code.equals("鞋") || code.equals("洗护用品")|| code.equals("化妆品") || code.equals("家电数码") || code.equals("其他")){
            FenLeiChaXunDao fenLeiChaXunDao = new FenLeiChaXunDao();
            ArrayList arrayList = fenLeiChaXunDao.selectFuShi(code);//查询大分类中有哪些小分类 并且组装成一个list集合
            PageInfo pageInfo1 = fenLeiChaXunDao.selectTotalDaLei(code, pageInfo);//查询该分类中的商品总数
            JSONObject jsonObject = fenLeiChaXunDao.selectfenleishangpin_fenye(arrayList, pageInfo1);//查询商品信息 并且组装成一个jsonObject
            return jsonObject;
        }else{
            FenLeiChaXunDao fenLeiChaXunDao = new FenLeiChaXunDao();
            PageInfo pageInfo1 = fenLeiChaXunDao.selectTotal(code, pageInfo);//查询小分类中的总条数
            JSONObject selectxiaofenleishangping = fenLeiChaXunDao.selectxianshixinxi(code,pageInfo1);//查询商品信息 组装JSONObject
            return selectxiaofenleishangping;
        }
    }
}
