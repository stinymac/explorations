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

package org.mac.explorations.corejava.concurrent.basics.thread.security.escape;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 安全的构造 工厂方法阻止this逸出
 *
 * @auther mac
 * @date 2019-11-18
 */
public class SafeListener {
    private final EventListener listener;

    private SafeListener() {
        listener = new EventListener () {
            public void onEvent(Event e){

            }
        };
    }

    public static SafeListener newInstance(EventSource source) {
        SafeListener safe = new SafeListener();
        source.registerListener(safe.listener);
        return safe;
    }

    private  interface EventListener{
        void onEvent(Event event);
    }

    private static class EventSource {
        private Set<EventListener> listeners = new ConcurrentSkipListSet<>();

        public void registerListener(EventListener eventListener) {
            listeners.add(eventListener);
        }

        public Set<EventListener> getListeners() {
            return listeners;
        }
    }

    private static class Event {
    }
}
