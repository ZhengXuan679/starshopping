package cn.com.starshopping.dao.shangpinguanli;

import cn.com.starshopping.pojo.ShangPinXinXi;
import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Wong
 * Date: 2019/01/09
 * Description:
 * Version: V1.0
 */
public class UpdateDao implements Cloneable {
    /**
     * 该方法获取组编号通过双重查询获取对应的规格名
     *
     * @param shangPinZuBianHao 商品组编号，用于查询对应类型再查询对应规格名
     * @return
     */
    public JSONObject selectGG(String shangPinZuBianHao) {
        String sql = "select GuiGe1,GuiGe2 from leixingguigexinxibiao where LeiXing in (select ShangPinLeiXin from shangpinxinxi where ShangPinZuBianHao=?)";
        JSONObject jsonObject = new JSONObject();
        try {
            PreparedStatement preparedStatement = Mydb.myConnection.prepareStatement(sql);
            preparedStatement.setString(1, shangPinZuBianHao);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            jsonObject.put("GuiGe1", resultSet.getString("GuiGe1"));
            jsonObject.put("GuiGe2", resultSet.getString("GuiGe2"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 该方法获取组编号进商品信息表获取对应的全部信息经过处理装入JSONObject返回给前端
     *
     * @param shangPinZuBianHao 商品组编号，用于查询对应详细信息
     * @return
     */
    public JSONObject selectAll(String shangPinZuBianHao) {
        String sql = "select ShangPinMingChen,ShangPinLeiXin,ShangPinGuiGe1,ShangPinGuiGe2,PinPai,ShangPinJiaGe,ShangPinChengBen,ShangPinMiaoSu,ShangPinZhuangTai from shangpinxinxi where ShangPinZuBianHao = ?";
        JSONObject jsonObject = new JSONObject();
        try {
            PreparedStatement preparedStatement = Mydb.myConnection.prepareStatement(sql);
            preparedStatement.setString(1, shangPinZuBianHao);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            JSONArray jsonArray1 = new JSONArray();
            JSONArray jsonArray2 = new JSONArray();
            jsonObject.put("ShangPinZuBianHao", shangPinZuBianHao);
            jsonObject.put("ShangPinMingChen", resultSet.getString("ShangPinMingChen"));
            jsonObject.put("ShangPinLeiXin", resultSet.getString("ShangPinLeiXin"));
            jsonArray1.put(resultSet.getString("ShangPinGuiGe1"));
            jsonArray2.put(resultSet.getString("ShangPinGuiGe2"));
            jsonObject.put("PinPai", resultSet.getString("PinPai"));
            jsonObject.put("ShangPinJiaGe", resultSet.getString("ShangPinJiaGe"));
            jsonObject.put("ShangPinChengBen", resultSet.getString("ShangPinChengBen"));
            jsonObject.put("ShangPinMiaoSu", resultSet.getString("ShangPinMiaoSu"));
            jsonObject.put("ShangPinZhuangTai", resultSet.getString("ShangPinZhuangTai").equals("1") ? "已上架" : "下架中");
            while (resultSet.next()) {
                jsonArray1.put(resultSet.getString("ShangPinGuiGe1"));
                jsonArray2.put(resultSet.getString("ShangPinGuiGe2"));
            }

            // 对两个JSONArray进行去重操作
            HashSet<String> hashSet = new HashSet<>();
            for (int i = 0; i < jsonArray1.length(); i++) {
                hashSet.add(jsonArray1.getString(i));
            }
            jsonArray1 = new JSONArray();
            Iterator<String> iterator = hashSet.iterator();
            while (iterator.hasNext()) {
                jsonArray1.put(iterator.next());
            }
            hashSet = new HashSet<>();
            for (int i = 0; i < jsonArray2.length(); i++) {
                hashSet.add(jsonArray2.getString(i));
            }
            jsonArray2 = new JSONArray();
            iterator = hashSet.iterator();
            while (iterator.hasNext()) {
                jsonArray2.put(iterator.next());
            }

            // 将存储规格值的两个JSONArray放入JSONObject返回
            jsonObject.put("GuiGe1", jsonArray1);
            jsonObject.put("GuiGe2", jsonArray2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 将前端获得的规格信息集合进行处理，返回一个处理后的集合
     *
     * @param arrayList 不同需求的动态值集合
     * @return
     */
    public ArrayList<ArrayList<String>> createArray(ArrayList<String[]> arrayList) {
        ArrayList<String> arrayList1 = new ArrayList<>();
        ArrayList<String> arrayList2 = new ArrayList<>();
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
        for (int i = 2; i < 4; i++) {
            for (int n = 0; n < arrayList.get(i).length; n++) {
                for (int o = 0; o < arrayList.get(i - 2).length; o++) {
                    if (arrayList.get(i)[n].equals(arrayList.get(i - 2)[o])) {
                        arrayList1.add(arrayList.get(i)[n]);
                        break;
                    } else if (o == arrayList.get(i - 2).length - 1) {
                        arrayList2.add(arrayList.get(i)[n]);
                    }
                }
            }
            arrayLists.add(arrayList1);
            arrayLists.add(arrayList2);
            arrayList1 = new ArrayList<>();
            arrayList2 = new ArrayList<>();
        }
        for (String s : arrayList.get(0)) {
            arrayList1.add(s);
        }
        for (String s : arrayList.get(1)) {
            arrayList2.add(s);
        }
        arrayLists.add(arrayList1);
        arrayLists.add(arrayList2);
        return arrayLists;
    }

    /**
     * 通过前端数据与整合的集合拼装出POJO集合
     *
     * @param arrayLists 动态数据整合的集合
     * @param request    前端传来的静态值
     * @return
     */
    public ArrayList<ShangPinXinXi> createPOJO(ArrayList<ArrayList<String>> arrayLists, HttpServletRequest request) {
        ShangPinXinXi shangPinXinXi = new ShangPinXinXi();
        shangPinXinXi.setShangPinZuBianHao(request.getParameter("ShangPinZuBianHao"));
        shangPinXinXi.setShangPinMingChen(request.getParameter("ShangPinMingChen"));
        shangPinXinXi.setShangPinLeiXin(request.getParameter("ShangPinLeiXin"));
        shangPinXinXi.setPinPai(request.getParameter("PinPai"));
        shangPinXinXi.setShangPinJiaGe(Double.valueOf(request.getParameter("ShangPinJiaGe")));
        shangPinXinXi.setShangPinChengBen(Double.valueOf(request.getParameter("ShangPinChengBen")));
        shangPinXinXi.setShangPinMiaoSu(request.getParameter("ShangPinMiaoSu"));
        shangPinXinXi.setShangPinZhuangTai(request.getParameter("ShangPinZhuangTai").equals("已上架") ? 1 : 2);
        ShangPinXinXi shangPinXinXi1 = null;
        ArrayList<ShangPinXinXi> arrayList = new ArrayList<>();
        // 该循环处理新的规格1与旧的规格2之间的配对
        // 集合1是新的规格1，集合2是旧的规格2，集合4是累计规格1
        for (int i = 0; i < arrayLists.get(1).size(); i++) {
            for (int j = 0; j < arrayLists.get(2).size(); j++) {
                shangPinXinXi1 = (ShangPinXinXi) shangPinXinXi.clone();
                shangPinXinXi1.setShangPinBianHao(shangPinXinXi.getShangPinZuBianHao() + "-A" + (i + arrayLists.get(4).size()) + "B" + j);
                shangPinXinXi1.setShangPinGuiGe1(arrayLists.get(1).get(i));
                shangPinXinXi1.setShangPinGuiGe2(arrayLists.get(2).get(j));
                arrayList.add(shangPinXinXi1);
            }
        }
        // 该循环处理新的规格2与旧的规格1之间的配对
        // 集合3是新的规格2，集合0是旧的规格1，集合5是累计规格2
        for (int i = 0; i < arrayLists.get(3).size(); i++) {
            for (int j = 0; j < arrayLists.get(0).size(); j++) {
                shangPinXinXi1 = (ShangPinXinXi) shangPinXinXi.clone();
                shangPinXinXi1.setShangPinBianHao(shangPinXinXi.getShangPinZuBianHao() + "-A" + j + "B" + (i + arrayLists.get(5).size()));
                shangPinXinXi1.setShangPinGuiGe1(arrayLists.get(0).get(j));
                shangPinXinXi1.setShangPinGuiGe2(arrayLists.get(3).get(i));
                arrayList.add(shangPinXinXi1);
            }
        }
        // 该循环处理新的规格1与新的规格2之间的配对
        // 集合1是新的规格1，集合3是新的规格2，集合4是累计规格1，集合5是累计规格2
        for (int i = 0; i < arrayLists.get(1).size(); i++) {
            for (int j = 0; j < arrayLists.get(3).size(); j++) {
                shangPinXinXi1 = (ShangPinXinXi) shangPinXinXi.clone();
                shangPinXinXi1.setShangPinBianHao(shangPinXinXi.getShangPinZuBianHao() + "-A" + (i + arrayLists.get(4).size()) + "B" + (j + arrayLists.get(5).size()));
                shangPinXinXi1.setShangPinGuiGe1(arrayLists.get(1).get(i));
                shangPinXinXi1.setShangPinGuiGe2(arrayLists.get(3).get(j));
                arrayList.add(shangPinXinXi1);
            }
        }
        return arrayList;
    }

    /**
     * 该方法通过传过来的组编号，字段名与对应字段内容的集合删除数据库中的内容
     *
     * @param request   用于获取组编号
     * @param ziDuan    将要操作的字段名
     * @param arrayList 对应字段的内容，该内容以外的值将被删除
     */
    public void delete(HttpServletRequest request, String ziDuan, ArrayList<String> arrayList) {
        String sql = "where ShangPinZuBianHao = ? and " + ziDuan + " not in (?";
        for (int i = 1; i < arrayList.size(); i++) {
            sql += ",?";
        }
        sql = "delete from shangpinxinxi " + sql + ")";
        try {
            PreparedStatement preparedStatement = Mydb.myConnection.prepareStatement(sql);
            Mydb.myConnection.setAutoCommit(false);
            preparedStatement.setString(1, request.getParameter("ShangPinZuBianHao"));
            for (int i = 2; i < arrayList.size() + 2; i++) {
                preparedStatement.setString(i, arrayList.get(i - 2));
            }
            preparedStatement.execute();
            Mydb.myConnection.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 遍历POJO集合，依次添加进数据库中
     *
     * @param arrayList
     */
    public void insert(ArrayList<ShangPinXinXi> arrayList) {
        String sql = "insert into shangpinxinxi(shangpinbianhao, shangpinzubianhao, shangpinmingchen, shangpinleixin, ShangPinGuiGe1, ShangPinGuiGe2, pinpai, shangpinjiage, shangpinchengben, shangpinmiaosu, caozuoshijian,ShangPinZhuangTai) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = Mydb.myConnection.prepareStatement(sql);
            Mydb.myConnection.setAutoCommit(false);
            for (ShangPinXinXi shangPinXinXi : arrayList) {
                preparedStatement.setString(1, shangPinXinXi.getShangPinBianHao());
                preparedStatement.setString(2, shangPinXinXi.getShangPinZuBianHao());
                preparedStatement.setString(3, shangPinXinXi.getShangPinMingChen());
                preparedStatement.setString(4, shangPinXinXi.getShangPinLeiXin());
                preparedStatement.setString(5, shangPinXinXi.getShangPinGuiGe1());
                preparedStatement.setString(6, shangPinXinXi.getShangPinGuiGe2());
                preparedStatement.setString(7, shangPinXinXi.getPinPai());
                preparedStatement.setString(8, String.valueOf(shangPinXinXi.getShangPinJiaGe()));
                preparedStatement.setString(9, String.valueOf(shangPinXinXi.getShangPinChengBen()));
                preparedStatement.setString(10, shangPinXinXi.getShangPinMiaoSu());
                preparedStatement.setString(11, "" + new Timestamp(System.currentTimeMillis()));
                preparedStatement.setInt(12, shangPinXinXi.getShangPinZhuangTai());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            Mydb.myConnection.commit();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 针对组编号对全部信息的公共字段进行更新
     *
     * @param request
     */
    public void update(HttpServletRequest request) {
        String sql = "update shangpinxinxi set ShangPinMingChen=?,PinPai=?,ShangPinJiaGe=?,ShangPinChengBen=?,ShangPinMiaoSu=?,ShangPinZhuangTai=? where ShangPinZuBianHao=?";
        try {
            PreparedStatement preparedStatement = Mydb.myConnection.prepareStatement(sql);
            preparedStatement.setString(1, request.getParameter("ShangPinMingChen"));
            preparedStatement.setString(2, request.getParameter("PinPai"));
            preparedStatement.setString(3, request.getParameter("ShangPinJiaGe"));
            preparedStatement.setString(4, request.getParameter("ShangPinChengBen"));
            preparedStatement.setString(5, request.getParameter("ShangPinMiaoSu"));
            preparedStatement.setInt(6, request.getParameter("ShangPinZhuangTai").equals("已上架") ? 1 : 2);
            preparedStatement.setString(7, request.getParameter("ShangPinZuBianHao"));
            preparedStatement.execute();
            Mydb.myConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
