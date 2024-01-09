package com.xw.jvm;

/**
*  @author  liuxiaowei
*  @description
*  @date 2022/6/28
*/public class JavaMethodAreaOOM {

    /**
     * 下面的代码清单是HotSpot Client VM生成的一段String.hashCode()方法的本地代码，
     * 可以看到在 0x026eb7a9 处的call指令有OopMap记录，
     * 它指明了EBX寄存器和栈中偏移量为16的内存区域中各有一个普通对象指针（Ordinary Object Pointer）的引用，
     * 有效范围为从call指令开始直到 0x026eb730（指令流的起始位置）+142（OopMap记录的偏移量）= 0x026eb7be，
     * 即 hlt 指令为止。
     *
     *
     * [Verified Entry Point]
     *   0x026eb730:mov%eax，-0x8000（%esp）
     *   ……
     *   ；ImplicitNullCheckStub slow case
     *   0x026eb7a9:call 0x026e83e0
     *   ；OopMap{ebx=Oop[16]=Oop off=142}
     *   ；*caload
     *   ；-java.lang.String:hashCode@48（line 1489）
     *   ；{runtime_call}
     *   0x026eb7ae:push$0x83c5c18
     *   ；{external_word}
     *   0x026eb7b3:call 0x026eb7b8
     *   0x026eb7b8:pusha
     *   0x026eb7b9:call 0x0822bec0；{runtime_call}
     *   0x026eb7be:hlt”
     */

    /**
     *  下面代码清单的test指令是HotSpot生成的轮询指令，
     *  当需要暂停线程时，虚拟机把 0x160100 的内存页设置为不可读，线程执行到test指令时就会产生一个自陷异常信号，
     *  在预先注册的异常处理器中暂停线程实现等待，这样一条汇编指令便完成安全点轮询和触发线程中断。
     *
     *
     *  0x01b6d627:call 0x01b2b210；OopMap{[60]=Oop off=460}
     *   ；*invokeinterface size
     *   ；-Client1:main@113（line 23）
     *   ；{virtual_call}
     *   0x01b6d62c:nop
     *   ；OopMap{[60]=Oop off=461}
     *   ；*if_icmplt
     *   ；-Client1:main@118（line 23）
     *   0x01b6d62d:test%eax，0x160100；{poll}
     *   0x01b6d633:mov 0x50（%esp），%esi
     *   0x01b6d637:cmp%eax，%esi
     */
}
