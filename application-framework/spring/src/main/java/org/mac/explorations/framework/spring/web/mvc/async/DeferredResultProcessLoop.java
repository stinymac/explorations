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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @auther mac
 * @date 2020-01-17
 */
@Component
public class DeferredResultProcessLoop implements InitializingBean {

    private final Queue<DeferredResult> pendingQueue = new ConcurrentLinkedQueue<>();

    private final Executor executor = Executors.newFixedThreadPool(1);

    public boolean addDeferredResult(DeferredResult deferredResult) {
        return pendingQueue.offer(deferredResult);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executor.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DeferredResult deferredResult = pendingQueue.poll();
                if (deferredResult != null) {
                    String id = UUID.randomUUID().toString().toUpperCase();
                    deferredResult.setResult(id);
                }
            }
        });
    }
}
