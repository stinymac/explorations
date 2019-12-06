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
 * 隐式的this逸出
 *
 * @auther mac
 * @date 2019-11-18
 */
public class ImplicitThisEscape {

    private int states;
    /**
     * 不正确的构造
     * ImplicitThisEscape 随着EventListener的发布而逸出
     *
     * @param source
     */
    public ImplicitThisEscape(EventSource source){

        //ImplicitThisEscape ct = this;
        source.registerListener(new EventListener(){
            @Override
            public void onEvent(Event event){
                doSomething();
            }
        });

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        states = 10;
    }

    public void doSomething() {
        System.out.println("states:"+states);
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

    public static void main(String[] args) {
        EventSource eventSource = new EventSource();

        new Thread(() -> new ImplicitThisEscape(eventSource)).start();
        Set<EventListener> listeners = eventSource.getListeners();

        while (true) {
            for (EventListener listener : listeners) {
                listener.onEvent(new Event());
            }
        }
    }
}
