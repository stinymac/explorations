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

package org.mac.explorations.corejava.collections.performance;

import java.util.List;

/**
 * 容器测试器
 *
 * @auther mac
 * @date 2019-10-18
 */
public class Tester<C> {

    public static int FIELD_WIDTH = 8;

    public static Measuring.Parameter[] DEFAULT_PARAMETERS = new Measuring.Parameter[] {

        new Measuring.Parameter(10,5000),
        new Measuring.Parameter(100,5000),
        new Measuring.Parameter(1000,5000),
        new Measuring.Parameter(10000,500)
    };

    protected C container;

    protected C initialize(int size) {
        return container;
    }

    private String headline = "";
    private List<Measuring<C>> totalMeasurement;

    private static final int SIZE_WIDTH = 5;
    private static final String SIZE_FIELD = "%" + SIZE_WIDTH + "s";

    private Measuring.Parameter[] parameters = DEFAULT_PARAMETERS;

    public void setParameters(Measuring.Parameter[] parameters) {
        this.parameters = parameters;
    }

    public Tester(C container, List<Measuring<C>> totalMeasurement) {
        this.container = container;
        this.totalMeasurement = totalMeasurement;
        if (container != null)
            headline = container.getClass().getSimpleName();
    }

    public Tester(C container, List<Measuring<C>> totalMeasurement, Measuring.Parameter[] parameters) {
        this(container,totalMeasurement);
        this.parameters = parameters;
    }

    public void setHeadline(String newHeadline) {
        this.headline = newHeadline;
    }

    public static <C> void run(C container, List<Measuring<C>> tests){
        new Tester<>(container, tests).timedMeasure();
    }
    public static <C> void run(C container, List<Measuring<C>> tests, Measuring.Parameter[] paramList) {
        new Tester<>(container, tests, paramList).timedMeasure();
    }

    private void displayHeader() {
        int width = FIELD_WIDTH * totalMeasurement.size() + SIZE_WIDTH;
        int dashLength = width - headline.length() - 1;

        StringBuilder head = new StringBuilder(width);
        int halfSize = dashLength / 2;

        displayHalfHeader(halfSize, head);
        head.append(' ');
        head.append(headline);
        head.append(' ');
        displayHalfHeader(halfSize, head);

        System.out.println(head);
        System.out.format(SIZE_FIELD,"size");
        for (Measuring<C> measuring : totalMeasurement) {
            System.out.format(stringField(),measuring.getName());
        }
        System.out.println();
    }

    private static String stringField() {
        return "%" + FIELD_WIDTH + "s";
    }

    private static String numberField() {
        return "%" + FIELD_WIDTH + "d";
    }

    private void displayHalfHeader(int dashHalfLength, StringBuilder head) {
        for (int i = 0; i < dashHalfLength; i++) {
            head.append('-');
        }
    }

    public void timedMeasure () {
        displayHeader();
        for (Measuring.Parameter parameter : parameters) {
            System.out.format(SIZE_FIELD,parameter.size);
            for (Measuring<C> measuring : totalMeasurement) {
                C container = initialize(parameter.size);
                long startTimestamp = System.nanoTime();
                int reps = measuring.execute(container,parameter);
                long duration = System.nanoTime() -  startTimestamp;
                long timePerRep = duration / reps;

                System.out.format(numberField(),timePerRep);
            }
            System.out.println();
        }
    }
}
