package com.concurrent.explore;

import org.junit.Test;

import com.concurrent.model.UserBean;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/7/15
 * Time: 9:04
 */
public class ObjectEqual {


	@Test
    public  void testEqualAndHashImpl() {

        UserBean userBean = new UserBean();
        UserBean userBean1 = new UserBean();

        userBean.setAge(12);
        userBean.setName("jingui");
        userBean.setEmail("krisibm@163.com");

        userBean1.setAge(12);
        userBean1.setName("jingui");
        userBean1.setEmail("krisibm@163.com");

        int code = userBean.hashCode();
        int code1 = userBean1.hashCode();

        System.out.println(code + "=" + code1);

        boolean equal = userBean.equals(userBean1);
        System.out.println(equal);
        /**
         * 2016-12-01测试通过，原因为：UserBean 的hashCode被重写了，name.hash()+age.hash()。<br>
         * -1159927848=-1159927848
         * true
         */
    }
	
	
	@Test
    public  void testHashImpl() {

        UserBean userBean = new UserBean();
        UserBean userBean1 = new UserBean();

        userBean.setAge(12);
        userBean.setName("jingui");
        userBean.setEmail("krisibm@163.com");

        userBean1.setAge(12);
        userBean1.setName("jingui");
        userBean1.setEmail("krisibm@163.com");

        int code = userBean.hashCode();
        int code1 = userBean1.hashCode();

        System.out.println(code + "=" + code1);

        boolean equal = userBean.equals(userBean1);
        System.out.println(equal);
        /**
         * 背景把UserBean的equal函数去掉了<br>
         * -1159927848=-1159927848  hashCode()相同。
         * false
         */
    }
}
