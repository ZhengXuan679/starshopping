package cn.com.starshopping.dao;

import cn.com.starshopping.pojo.KeFuDengLu;
import cn.com.starshopping.util.Mydb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: yanmin
 * Date: 2019/01/14
 * Description:
 * Version: V1.0
 */
public class HouTaiDengLuDao {

    /**
     * 客服登录方法
     * 通过传入的手机号码或者身份证号码和密码进行匹配查询
     *
     * @param keFuDengLu 客服登录pojo对象
     * @return 登录成功返回客服登录pojo对象，登录失败返回null
     */
    public KeFuDengLu dengLu(KeFuDengLu keFuDengLu) {
        KeFuDengLu returnKeFuDengLu = null;  // 返回对象
        // 查询客服姓名，使用手机号码+密码或者身份证号码+密码的方式
        String sql = "select XingMing,ShenFenZheng from kefudenglu where (ShouJiHaoMa=? or ShenFenZheng=?) and MiMa=?;";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            pre.setString(1, keFuDengLu.getShouJiHaoMa());  // 手机号码
            pre.setString(2, keFuDengLu.getShenFenZheng());  // 身份证
            pre.setString(3, keFuDengLu.getMiMa());   // 密码
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {   // 查询结果只有一条
                keFuDengLu.setXingMing(rs.getString(1));   /// 得到姓名，存入pojo对象
                keFuDengLu.setShenFenZheng(rs.getString(2));  // 得到身份证
                returnKeFuDengLu = keFuDengLu;    // 对象赋值给返回对象
            }
            rs.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnKeFuDengLu;
    }

    /**
     * 修改状态方法
     * 此方法可以用来冻结和解冻客服账号
     * 此方法通过用户的手机号码或者身份证号码来进行冻结账户
     *
     * @param keFuDengLu 客户登录pojo对象
     */
    public void dongJie(KeFuDengLu keFuDengLu) {
        // 查询客服姓名，使用手机号码+密码或者身份证号码+密码的方式
        String sql = "update kefudenglu set ZhuangTai=?  where ShouJiHaoMa=? or ShenFenZheng=?;";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            pre.setInt(1, keFuDengLu.getZhuangTai());  // 状态值
            pre.setString(2, keFuDengLu.getShouJiHaoMa());  // 手机号码
            pre.setString(3, keFuDengLu.getShenFenZheng());  // 身份证
            pre.executeUpdate();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询状态情况
     * 通过手机号码或者身份证号码来查询账户的状态
     *
     * @param keFuDengLu 客服登录pojo对象
     * @return 成功返回查询结果pojo对象，失败返回null
     */
    public KeFuDengLu chaXunZhuangTai(KeFuDengLu keFuDengLu) {
        KeFuDengLu returnKeFuDengLu = null;  // 返回对象
        // 查询客服姓名，使用手机号码+密码或者身份证号码+密码的方式
        String sql = "select ZhuangTai from kefudenglu where ShouJiHaoMa=? or ShenFenZheng=?;";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            pre.setString(1, keFuDengLu.getShouJiHaoMa());  // 手机号码
            pre.setString(2, keFuDengLu.getShenFenZheng());  // 身份证
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {   // 查询结果只有一条
                keFuDengLu.setZhuangTai(rs.getInt(1));   // 得到状态存入pojo对象
                returnKeFuDengLu = keFuDengLu;    // 对象赋值给返回对象
            }
            rs.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnKeFuDengLu;
    }

    /**
     * 错误次数修改
     *
     * @param keFuDengLu 客服登录pojo
     */
    public void cuoWu(KeFuDengLu keFuDengLu) {
        // 查询客服姓名，使用手机号码+密码或者身份证号码+密码的方式
        String sql = "update kefudenglu set CuoWuCiShu=?  where ShouJiHaoMa=? or ShenFenZheng=?;";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            pre.setInt(1, keFuDengLu.getCuoWuCiShu());  // 错误次数
            pre.setString(2, keFuDengLu.getShouJiHaoMa());  // 手机号码
            pre.setString(3, keFuDengLu.getShenFenZheng());  // 身份证
            pre.executeUpdate();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 错误次数查询
     * 通过客服输入的手机号码或者身份证号码进行查询
     *
     * @param keFuDengLu
     * @return 成功返回查询结果pojo对象，失败返回null
     */
    public KeFuDengLu chaXunCuoWu(KeFuDengLu keFuDengLu) {
        KeFuDengLu returnKeFuDengLu = null;  // 返回对象
        // 查询客服姓名，使用手机号码+密码或者身份证号码+密码的方式
        String sql = "select CuoWuCiShu from kefudenglu where ShouJiHaoMa=? or ShenFenZheng=?;";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            pre.setString(1, keFuDengLu.getShouJiHaoMa());  // 手机号码
            pre.setString(2, keFuDengLu.getShenFenZheng());  // 身份证
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {   // 查询结果只有一条
                keFuDengLu.setCuoWuCiShu(rs.getInt(1));   // 得到错误次数存入pojo对象
                returnKeFuDengLu = keFuDengLu;    // 对象赋值给返回对象
            }
            rs.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnKeFuDengLu;
    }

    /**
     * 密码修改
     *
     * @param keFuDengLu 客服登录pojo对象
     * @return 修改成功返回true，失败返回false
     */
    public boolean xiuGaiMiMa(KeFuDengLu keFuDengLu) {
        boolean action = false;
        // 查询客服姓名，使用手机号码+密码或者身份证号码+密码的方式
        String sql = "update kefudenglu set MiMa=?  where ShouJiHaoMa=? or ShenFenZheng=?;";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            pre.setString(1, keFuDengLu.getMiMa());  // 错误次数
            pre.setString(2, keFuDengLu.getShouJiHaoMa());  // 手机号码
            pre.setString(3, keFuDengLu.getShenFenZheng());  // 身份证
            pre.executeUpdate();
            pre.close();
            action = true;   // 返回true
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return action;
    }

    /**
     * 密码查询
     *
     * @param keFuDengLu 客服登录pojo对象
     * @return 成功返回查询结果密码字符串，服务器出错返回字符串“服务器出错”，查询没有结果返回null
     */
    public String miMaChaXun(KeFuDengLu keFuDengLu) {
        String action = null;  // 返回对象
        // 查询客服姓名，使用手机号码+密码或者身份证号码+密码的方式
        String sql = "select MiMa from kefudenglu where ShouJiHaoMa=? or ShenFenZheng=?;";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            pre.setString(1, keFuDengLu.getShouJiHaoMa());  // 手机号码
            pre.setString(2, keFuDengLu.getShenFenZheng());  // 身份证
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {   // 查询结果只有一条
                action = rs.getString(1);   // 得到密码存入pojo对象
            }
            rs.close();
            pre.close();
        } catch (SQLException e) {
            action = "服务器出错";
            e.printStackTrace();
        }
        return action;
    }

    /**
     * 修改登录时间
     * @param keFuDengLu 客服登录pojo对象
     */
    public void xiuGaiShiJian(KeFuDengLu keFuDengLu) {
        // 查询客服姓名，使用手机号码+密码或者身份证号码+密码的方式
        String sql = "update kefudenglu set ZuiHouDengLu=now()  where ShouJiHaoMa=? or ShenFenZheng=?;";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            pre.setString(1, keFuDengLu.getShouJiHaoMa());  // 手机号码
            pre.setString(2, keFuDengLu.getShenFenZheng());  // 身份证
            pre.executeUpdate();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
