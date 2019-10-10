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

package org.mac.explorations.corejava.base.enumeration;

/**
 * 枚举
 *
 *
 *
 * @auther mac
 * @date 2019-10-03
 */

public enum Seasons {

    SPRING(0) {
        @Override
        String toFullName() {
            return null;
        }
    },
    SUMMER(1){
        @Override
        String toFullName() {
            return null;
        }
    },
    AUTUMN(2){
        @Override
        String toFullName() {
            return null;
        }
    },
    WINTER(3){
        @Override
        String toFullName() {
            return null;
        }
    };

    private int value;

    Seasons(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    /***
     *
     * @see {@link java.util.concurrent.TimeUnit}
     *
     * @return
     */
    abstract String toFullName();
}
