package cn.com.starshopping.service.zql;

import cn.com.starshopping.dao.zql.DingDanDao;
import cn.com.starshopping.pojo.CaoZuoRiZhi;
import cn.com.starshopping.pojo.DingDanSouSuo;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: ZouQinLun
 * Date: 2019/01/05
 * Description:
 * Version: V1.0
 */
public class DingDanService {
    /**
     * 此方法通过传入前台的请求，获取该用户名下，订单状态为0，以时间倒序分页显示的所有订单信息，组装成jsonObject返回
     * @param request
     * @return
     */
    public JSONObject dingDanService(HttpServletRequest request){
        String dangQianYeMa = request.getParameter("dangQianYeMa");     //获取前台传来的当前页码数
        String yongHuMing=(String)request.getSession().getAttribute("yonghuming");//获取登陆后存储在session中的用户名
        int dangQianYeMa01=0;                                              //定义需查询的当前页码数
        int meiYe_tiaoShu01 = 10;                                           //定义每页显示数据数量为10

        dangQianYeMa01=dangQianYeMa(dangQianYeMa);                      //传入当前页码数，调用dangQianYeMa方法，返回需查询的当前页码数



        DingDanDao dingDanDao = new DingDanDao();                           //创建DingDanDao的对象
        int zongTiaoShu = dingDanDao.count(yongHuMing);            //用DingDanDao的对象，传入用户名，调用count方法，返回数据总数
        int zongYeShu = (zongTiaoShu-1)/meiYe_tiaoShu01+1;                  //利用数据总数和每页显示条数求出总页数
        int qiShiXiaBiao = (dangQianYeMa01-1)*meiYe_tiaoShu01;              //利用当前页码数和每页显示条数求出起始下标



        JSONArray jsonArray = dingDanDao.dingDanDao(yongHuMing,meiYe_tiaoShu01,qiShiXiaBiao);//传入用户名，每页条数，起始下标，分页查找该用户名下，订单状态为0的订单详情，并将每单订单以JSONObject形式按时间倒序的顺序封装入JSONArray，返回此JSONArray
        JSONObject jsonObject= creatJSONObject(jsonArray,zongTiaoShu,zongYeShu,dangQianYeMa01); //拼装jsonObject数据

        return jsonObject;                  //返回拼装的jsonObject
    }

    /**
     * 实现前端页面的删除功能，将订单号从前端传递传来，组装成数组，遍历数组传递给dao层修改订单状态实现前端的删除
     * @param request
     * @return
     */
    public boolean shanChu(HttpServletRequest request){
        boolean action = false;
        String s = request.getParameter("data");    //以字符串形式获取前台传来的订单号数组
        String[] dingDanHao =s.split(",");          //逗号拆分字符串返回订单号组成的数组
        DingDanDao dingDanDao = new DingDanDao();       //创建DingDanDao的对象
        for(int i =0;i<dingDanHao.length;i++){          //循环订单号数组，依次在数据库中修改每个订单号的订单状态为1
            action = dingDanDao.shanChu(dingDanHao[i]);
        }
        return action;
    }

    /**
     * 此方法实现搜索功能
     * @param request
     * @return
     */
    public JSONObject souSuo(HttpServletRequest request){
        String dangQianYeMa = request.getParameter("dangQianYeMa"); //获取前台传来的当前页码
        String dingDanHao = request.getParameter("dingDanHao");     //获取前台传来的订单号

        int dangQianYeMa01=0;                                           //定义需要查询的当前页码数
        int meiYe_tiaoShu01=10;                                         //定义每页显示条数为10

        dangQianYeMa01=dangQianYeMa(dangQianYeMa);                  //传入当前页码数，调用dangQianYeMa方法，返回需查询的当前页码数

        String yongHuMing =(String)request.getSession().getAttribute("yonghuming");//获取登陆后存储在session中的用户名

        String qiShiRiQi = request.getParameter("qiShiRiQi");           //  得到起始日期
        String zhongZhiRiQi= request.getParameter("zhongZhiRiQi");      //得到终止日期
        String jiaGeXiaXian = request.getParameter("jiaGeXiaXian");     //得到价格下限
        String jiaGeShangXian =request.getParameter("jiaGeShangXian");  //得到价格上限
        String guanJianCi = request.getParameter("guanJianCi");         //得到关键词
        DingDanSouSuo dingDanSouSuo = new DingDanSouSuo();                 //得到DingDanSouSuo的Pojo对象
        dingDanSouSuo.setQiShiRiQi(qiShiRiQi);                             //将起始日期封装到Pojo对象中
        dingDanSouSuo.setZhongZhiRiQi(zhongZhiRiQi);                       //将终止日期封装到Pojo对象中
        dingDanSouSuo.setJiaGeXiaXian(jiaGeXiaXian);                       //将价格下限封装到Pojo对象中
        dingDanSouSuo.setJiaGeShangXian(jiaGeShangXian);                   //将价格上限封装到Pojo对象中
        dingDanSouSuo.setGuanJianCi(guanJianCi);                           //将关键词封装到Pojo对象中


        DingDanDao dingDanDao = new DingDanDao();                          //得到DingDanDao的对象
        JSONObject jsonObject=new JSONObject();                             //创建新的jsonObject
        JSONArray jsonArray =new JSONArray();                               //创建新的jsonArray
        int zongTiaoShu = -1;                                               //定义总条数为-1
        int zongYeShu=0;                                                    //定义总页数为0
        if ( dingDanHao==null||dingDanHao.equals("") ){                     //如果从前端获取的订单号为null或为空，则调用按时间，价格，关键词搜索的方法搜索订单，返回数据
            zongTiaoShu = dingDanDao.souSuoCount(dingDanSouSuo,yongHuMing);           //调用Dao层的方法得到查询出的数据的总条数
            if (zongTiaoShu != -1){                                         //如果有搜索到订单，则将每条订单信息组装成jsonObject，放入jsonArray返回
                zongYeShu = (zongTiaoShu-1)/meiYe_tiaoShu01+1;
                int qiShiXiaBiao = (dangQianYeMa01-1)*meiYe_tiaoShu01;
                jsonArray = dingDanDao.souSuo(dingDanSouSuo,meiYe_tiaoShu01,qiShiXiaBiao,yongHuMing);
            }
            else{                                                          //如果没有搜索到，则在jsonArray中放入空
                jsonArray.put("");
            }
        }
        else{                                                                //如果从前端获取的订单号不为null和空，则调用查询订单号的方法返回数据
            jsonArray = dingDanDao.dingDanHaoSouSuo(yongHuMing,dingDanHao);
        }

        jsonObject= creatJSONObject(jsonArray,zongTiaoShu,zongYeShu,dangQianYeMa01);//利用jsonArray，总条数，总页数，当前页码数，组装jsonObject
        return jsonObject;                                          //返回jsonObject
    }

    /**
     * 此方法传入从前台获取的当前页码，生成需要查询的当前页码数，并返回
     * @param dangQianYeMa
     * @return
     */
    public int dangQianYeMa(String dangQianYeMa){
        int dangQianYeMa01=0;               //定义需查询的当前页码数为0
        if(dangQianYeMa==null ||dangQianYeMa ==""){ //如果从前台获取的当前页码数为null或为空，则需查询的当前页码数为1
            dangQianYeMa01=1;
        }
        else {                              //否则，需查询的当前页码数即为前台传来的当前页码数
            dangQianYeMa01 =Integer.parseInt(dangQianYeMa);
        }
        return dangQianYeMa01;                //返回需查询的当前页码数
    }

    /**
     * 传入JSONArray，总条数，总页数，当前页码数，拼装一个jsonObject，返回此jsonObject
     * @param jsonArray
     * @param zongTiaoShu
     * @param zongYeShu
     * @param dangQianYeMa01
     * @return
     */
    public JSONObject creatJSONObject(JSONArray jsonArray,int zongTiaoShu,int zongYeShu,int dangQianYeMa01){
        JSONObject jsonObject = new JSONObject();   //创建一个jsonObject
        if (jsonArray!=null &&!jsonArray.equals("")){       //如果传入的jsonArray不为null且不为空，则组装jsonObject
            jsonObject.put("returnCode",200);
            jsonObject.put("code",0);
            jsonObject.put("msg","");
            jsonObject.put("count",zongTiaoShu);
            jsonObject.put("zongYeShu",zongYeShu);
            jsonObject.put("zongTiaoShu",zongTiaoShu);
            jsonObject.put("dangQianYeMa",dangQianYeMa01);
            jsonObject.put("data",jsonArray);
        }
        else{                                                   //否则则直接在jsonObject中放入"returnCode"：500
            jsonObject.put("returnCode",500);
        }
        return jsonObject;                                      //返回jsonObject
    }

    /**
     * 此方法通过接受前台传来的删除请求，组装删除操作的操作日志的jsonObject，插入进数据库中的操作日志表，成功返回true，失败返回false
     * @param request
     * @return
     */
    public boolean shanChuRiZhi(HttpServletRequest request){
        boolean action = false;                         //定义状态为false
        String yongHuMing =(String)request.getSession().getAttribute("yonghuming");//获取登陆后存储在session中的用户名
        String s = request.getParameter("data");    //以字符串形式获取需要删除的订单的订单编号数组
        CaoZuoRiZhi caoZuoRiZhi = new CaoZuoRiZhi();    //创建CaoZuoRiZhi的pojo对象
        caoZuoRiZhi.setYongHuMing(yongHuMing);                 //pojo对象中放入用户名
        caoZuoRiZhi.setCaoZuoJiLu("删除："+s);            //pojo对象中放入操作记录
        caoZuoRiZhi.setShiJian(new Timestamp(System.currentTimeMillis()));//pojo对象放入当前时间，精确到毫秒
        DingDanDao dingDanDao = new DingDanDao();        //创建DingDanDao对象
        int count = dingDanDao.caoZuoRiZhi(caoZuoRiZhi); //利用DingDanDao对象调用caoZuoRiZhi方法，传入pojo对象，返回插入的数据数
        if(count==1){   //如果返回1则表示插入成功，修改状态为true
            action = true;
        }
        return  action; //返回状态
    }

    /**
     * 此方法通过接受前台传来的搜索请求，组装搜索操作的操作日志的jsonObject，插入进数据库中的操作日志表，成功返回true，失败返回false
     * @param request
     * @return
     */
    public boolean souSuoRiZhi(HttpServletRequest request ){
        boolean action =false;                                                     //定义状态机为false
        String dingDanHao =request.getParameter("dingDanHao");                  //获取前台传来的订单号
        String yongHuMing =(String)request.getSession().getAttribute("yonghuming");//获取登陆后存储在session中的用户名
        CaoZuoRiZhi caoZuoRiZhi = new CaoZuoRiZhi();                               //创建CaoZuoRiZhi的pojo对象
        if(dingDanHao == null || dingDanHao.equals("")){                           //如果订单号为null或为空，则是进行的时间，价格，关键词搜索
            String qiShiRiQi = request.getParameter("qiShiRiQi");               //获取起始日期
            String zhongZhiRiQi= request.getParameter("zhongZhiRiQi");          //获取终止日期
            String jiaGeXiaXian = request.getParameter("jiaGeXiaXian");         //获取价格下限
            String jiaGeShangXian =request.getParameter("jiaGeShangXian");      //获取价格上限
            String guanJianCi = request.getParameter("guanJianCi");             //获取价格下限
            caoZuoRiZhi.setYongHuMing(yongHuMing);                                       //将用户名set进pojo对象
            caoZuoRiZhi.setCaoZuoJiLu("搜索 "+"日期："+qiShiRiQi+"——"+zhongZhiRiQi+",价格："+jiaGeXiaXian+"——"+jiaGeShangXian+",关键词："+guanJianCi);//将组装的操作记录放入pojo对象
            caoZuoRiZhi.setShiJian(new Timestamp(System.currentTimeMillis()));      //将当前时间存入pojo对象
        }
        else{                                                       //如果为按订单号搜索，则进行此操作
            caoZuoRiZhi.setYongHuMing(yongHuMing);                         //将用户名存入pojo对象
            caoZuoRiZhi.setCaoZuoJiLu("搜索 " + "订单编号："+dingDanHao);  //将搜索订单号的操作记录存入pojo对象
            caoZuoRiZhi.setShiJian(new Timestamp(System.currentTimeMillis()));//将当前时间存入pojo对象
        }
        DingDanDao dingDanDao = new DingDanDao();                   //创建DingDanDao对象
        int count = dingDanDao.caoZuoRiZhi(caoZuoRiZhi);            //利用DingDanDao对象调用caoZuoRiZhi方法，传入pojo对象，插入数据库
        if(count==1){                                               //数据库插入成功返回1，修改状态机为true；
            action = true;
        }
        return  action;                                         //返回状态机
    }
}
