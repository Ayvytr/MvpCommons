package com.wanshare.wscomponent.update;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

//@FixMethodOrder(MethodSorters.JVM)//按照JVM编译的顺序，顺序也可能发生变化
//@FixMethodOrder(MethodSorters.DEFAULT)//不可预测的顺序，可以认为是随机的顺序。
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //按照方法的命名的顺序，也就是按照字母的顺序，从a到z。
public class FixMethodOrderTest {

        @Test
        public void testA(){
            System.out.println("A");
        }

        @Test
        public void testB(){
            System.out.println("B");
        }

        @Test
        public void testC() {
            System.out.println("C");
        }
}
