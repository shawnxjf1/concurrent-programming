package com.concurrent.objectshare;

/**
 * 1.不要在构造函数中使用this应用逸出
 * 2.使用工程方法来防止this引用在构造函数中逸出<br>
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/8
 * Time: 22:29
 */
/**
 * concept:
 * 发布：发布一个对象是指：使对象在当前作用域之外的代码中使用<br>
 * 逸出：某个不应该被发布的对象被发布.<br>
 * 构造函数逸出的方式有：1.在构造函数中启动了一个线程（最好不要启动线程，线程可能修改this对象的属性）。 <br>
 * 
 *
 */
/**
 * 
 * @author shawn
 *
 */
public class SafePublish {

    private final Publish publish;

    private SafePublish() {
        publish = new Publish();
    }

    /**
     * 私有的构造函数和工厂方法防止逸出
     * @return
     */
    public static SafePublish newInstance() {
        SafePublish safePublish = new SafePublish();
        return safePublish;
    }

}


class Publish {

}
