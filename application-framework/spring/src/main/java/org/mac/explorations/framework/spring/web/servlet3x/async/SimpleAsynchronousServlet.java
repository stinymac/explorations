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

package org.mac.explorations.framework.spring.web.servlet3x.async;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @auther mac
 * @date 2020-01-16
 */
@WebServlet(value = "/async",asyncSupported = true)
public class SimpleAsynchronousServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AsyncContext asyncContext = req.startAsync();
        System.out.println("request start at:"+System.currentTimeMillis());
        System.err.println("resp:"+resp);
        asyncContext.start(() ->{
            System.out.println("Asynchronous process start at:"+System.currentTimeMillis());
            try {
                doService();
                System.err.println("asyncContext.getResponse():"+asyncContext.getResponse());
                asyncContext.getResponse().getWriter().write("Asynchronous process finished!");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            asyncContext.complete();
            System.out.println("Asynchronous process   end at:"+System.currentTimeMillis());
        });
        System.out.println("request   end at:"+System.currentTimeMillis());
    }

    private void doService() throws InterruptedException {
        Thread.sleep(3000);
    }
}
