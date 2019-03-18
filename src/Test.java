import cn.com.starshopping.util.JDBC;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jxhun
 * Date: 2019/01/06
 * Description:
 * Version: V1.0
 */
public class Test implements Cloneable {
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public static void main(String[] args) {
        Test test = new Test();
        try {
            System.out.println(test.clone() == test);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
