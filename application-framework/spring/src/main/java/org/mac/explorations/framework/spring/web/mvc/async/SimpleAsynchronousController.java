/*
 *          (          (
 *          )\ )  (    )\   )  )     (
 *  (  (   (()/( ))\( ((_| /( /((   ))\
 *  )\ )\   ((_))((_)\ _ )(_)|_))\ /((_)
 * ((_|(_)  _| (_))((_) ((_)__)((_|_))
 * / _/ _ \/ _` / -_|_-< / _` \ V // -_)
 * \__\___/\__,_\___/__/_\__,_|\_/ \___|
 *
 * 东隅已逝，桑榆非晚。(The time has passed,it is not too late.)
 * 虽不能至，心向往之。(Although I can't, my heart is longing for it.)
 *
 */

package org.mac.explorations.framework.spring.web.mvc.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

/**
 * @auther mac
 * @date 2020-01-17
 */
@Controller
public class SimpleAsynchronousController {
    /**
     * Spring MVC 异步处理流程
     *
     * 1、控制器返回Callable
     * 2、Spring异步处理，将Callable 提交到 TaskExecutor 使用一个隔离的线程执行
     * 3、DispatcherServlet和所有的Filter退出web容器的线程，但是response 保持打开状态；
     * 4、Callable返回结果，SpringMVC将请求重新派发给容器，恢复之前的处理；
     * 5、根据Callable返回的结果。SpringMVC继续进行视图渲染流程等。
     *
     * 因此会两次进入非异步的HandlerInterceptor
     *
     * 异步的拦截器可以使用:
     * 1)、原生API的AsyncListener
     * 2)、SpringMVC：实现AsyncHandlerInterceptor；
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/mvcAsync")
    public Callable<String> callableAsync(){
        System.out.println("request start at:"+System.currentTimeMillis());
        Callable<String> callable = () -> {
            System.out.println("process start at:"+System.currentTimeMillis());
            Thread.sleep(1000);
            System.out.println("process   end at:"+System.currentTimeMillis());
            return "callableAsync";
        };
        System.out.println("request   end at:"+System.currentTimeMillis());
        return callable;
    }

    @Autowired
    private DeferredResultProcessLoop deferredResultProcessLoop;

    @ResponseBody
    @RequestMapping("/submit")
    public DeferredResult<Object> createOrder(){
        DeferredResult<Object> deferredResult = new DeferredResult<>((long)3000, "create fail...");
        deferredResultProcessLoop.addDeferredResult(deferredResult);
        return deferredResult;
    }
}
