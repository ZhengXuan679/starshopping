package cn.com.starshopping.service.fzy;

public interface CaoZuoJiLuInterface {

    /**
     *  对参数进行判断,然后调用并传参Dao层的chaRuCaoZuoJiLu（）方法
     * @param option  switch 对应的操作请求参数
     * @param yonghuming 执行此操作用户对应的用户名
     */
    void zhiXingChaRu(String option, String yonghuming);
}
