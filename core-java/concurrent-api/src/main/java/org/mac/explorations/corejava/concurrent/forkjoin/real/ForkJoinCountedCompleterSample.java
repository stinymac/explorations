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

package org.mac.explorations.corejava.concurrent.forkjoin.real;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicReference;

/**
 * CountedCompleter： 在任务完成执行后会触发执行一个自定义的钩子函数
 *
 * @auther mac
 * @date 2019-12-05
 */
public class ForkJoinCountedCompleterSample {

    private static final String J_LETTER = "J";
    private static final String NOT_EXISTENT_LETTER = "2";
    private static final String[] ALPHABET = {"A", "B", "C", "D", "E", "F", "G", "H", "I", J_LETTER};
    private static final Configuration DEFAULT_CONFIG = new Configuration(false, false, false);


    public static void main(String[] args) {
        Searcher<String> searcher =
                new Searcher<>(null, ALPHABET, new AtomicReference<>(), 0, ALPHABET.length, J_LETTER, DEFAULT_CONFIG);
        searcher.setPendingCount(1);

        ForkJoinPool pool = new ForkJoinPool();
        String word = pool.invoke(searcher);
        System.out.println("searcher:"+word);
    }

    static class Searcher<E> extends CountedCompleter<E> {

        private final E[] items;
        private final E expected;
        private final AtomicReference<E> result;
        private final int startIndex, endIndex;
        private final Configuration configuration;

        Searcher(CountedCompleter<?> completer, E[] items, AtomicReference<E> result, int startIndex, int endIndex, E expected, Configuration configuration) {
            super(completer);
            this.items = items;
            this.result = result;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.expected = expected;
            this.configuration = configuration;
        }

        @Override
        public E getRawResult() {
            return result.get();
        }

        @Override
        public void compute() {
            int localStartIndex = startIndex,  localEndIndex = endIndex;
            while (result.get() == null && localEndIndex >= localStartIndex) {

                if (localEndIndex - localStartIndex >= 2) {
                    int mid = localStartIndex + ((localEndIndex - localStartIndex) >>> 1);

                    if (!configuration.canSkipIncrement()) {
                        addToPendingCount(1);
                    }
                    new Searcher(this, items, result, mid, localEndIndex, expected, configuration).fork();
                    localEndIndex = mid;
                } else {
                    E item = items[localStartIndex];
                    if (matches(item) && result.compareAndSet(null, item)) {
                        if (!configuration.canSkipCompleteCall()) {
                            quietlyCompleteRoot();
                        }
                    }
                    break;
                }
            }
            if (!configuration.canSkipTryCompleteCall()) {
                tryComplete();
            }
        }

        boolean matches(E e) {
            return e.equals(expected);
        }

        @Override
        public String toString() {
            return "Searcher {"+startIndex+"-"+endIndex+"}";
        }
    }

    static class Configuration {

        private boolean skipIncrement;

        private boolean skipCompleteCall;

        private boolean skipTryCompleteCall;

        Configuration(boolean skipIncrement, boolean skipCompleteCall, boolean skipTryCompleteCall) {
            this.skipIncrement = skipIncrement;
            this.skipCompleteCall = skipCompleteCall;
            this.skipTryCompleteCall = skipTryCompleteCall;
        }

        public boolean canSkipIncrement() {
            return skipIncrement;
        }

        public boolean canSkipCompleteCall() {
            return skipCompleteCall;
        }

        public boolean canSkipTryCompleteCall() {
            return skipTryCompleteCall;
        }

    }
}
