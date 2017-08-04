package com.dxy.platform;

import org.junit.Test;

/**
 * Created by guoguo on 2017/8/5.
 */
public class BaseTest {

    @Test
    public void baseTest(){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement track: stackTrace) {
            System.out.println(String.format("执行测试 -> %s.%s.%s %d", track.getFileName(), track.getClassName(), track.getMethodName(), track.getLineNumber()));
        }

    }

}
