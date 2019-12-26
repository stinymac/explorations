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

package org.mac.explorations.corejava.io.local;

import java.nio.Buffer;
import java.nio.CharBuffer;

/**
 * @auther mac
 * @date 2019-12-21
 */
public class BufferSample {
    public static void main(String[] args) {
        CharBuffer charBuffer = CharBuffer.allocate(8);
        for(char c : "hello".toCharArray()) {
            charBuffer.put(c);
        }
        displayBufferMetadata(charBuffer);
        charBuffer.flip();
        displayBufferData(charBuffer);
        displayBufferMetadata(charBuffer);
        charBuffer.rewind();
        displayBufferMetadata(charBuffer);
        charBuffer.clear();
        displayBufferMetadata(charBuffer);
        displayBufferData(charBuffer);
        System.out.println("-------------------------");
    }

    private static void displayBufferData(CharBuffer charBuffer) {
        while (charBuffer.hasRemaining()) {
            System.out.println(charBuffer.get());
        }
    }

    private static void displayBufferMetadata(Buffer charBuffer) {
        System.out.println("position:" +charBuffer.position() + " limit:" + charBuffer.limit() + " capacity:"+ charBuffer.capacity());
    }
}
