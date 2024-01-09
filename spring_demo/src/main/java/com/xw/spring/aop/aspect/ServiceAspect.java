package com.xw.spring.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liuxiaowei
 * @description 切面类
 *  Spring AOP 实现原理
 *      Spring基于代理模式实现功能动态扩展
 *      1、目标类有接口，通过JDK动态代理实现功能扩展
 *      2、目标类没有接口，通过CGlib组件实现功能扩展
 *
 * @date 2023/12/5
 */
@Aspect
@Component  // 标记当前类为组件类
public class ServiceAspect {

    /**
     * 切面方法 用于扩展额外功能
     * @param joinPoint 连接点 可以获取目标类/方法的信息
     */
    public void printExecutionTime(JoinPoint joinPoint) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        String now = format.format(new Date());
        // 获取目标类名称
        String className = joinPoint.getTarget().getClass().getName();
        // 获取目标方法名称
        String methodName = joinPoint.getSignature().getName();
        System.out.println(now + ": " + className + ": " + methodName);
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            System.out.println(arg);
        }
    }

    /**
     * 后置通知
     * 无法获取运行过程中产生的返回值和内部抛出的异常
     * @param joinPoint
     */
    public void doAfter(JoinPoint joinPoint) {
        System.out.println("这里是后置通知");
    }

    /**
     * 后置返回通知
     * 用于接收目标方法产生的返回值
     * @param joinPoint
     * @param o
     */
    public void doAfterReturning(JoinPoint joinPoint, Object o) {
        System.out.println("这里是后置返回通知，返回参数为：" + o);
    }

    /**
     * 异常通知
     * 用于接收目标方法内部抛出的异常
     * @param joinPoint
     * @param t
     */
    public void doAfterThrowing(JoinPoint joinPoint, Throwable t) {
        System.out.println("这里是异常通知，返回参数为：" + t);
    }

    /**
     * 环绕通知
     * ProceedingJoinPoint是JoinPoint的升级版，在原有功能外，还可以控制目标方法是否执行
     * @param joinPoint
     * @return
     */
    @Around("execution(public * com.xw..*.*(..))")
    public Object countExecutionTime(ProceedingJoinPoint joinPoint) {
        // 记录开始时间
        long begin = System.currentTimeMillis();
        // 执行目标 service
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        // 记录结束时间
        long end = System.currentTimeMillis();
        long takeTime = end - begin;
        if (takeTime > 3000) {
            // 获取目标类名称
            String className = joinPoint.getTarget().getClass().getName();
            // 获取目标方法名称
            String methodName = joinPoint.getSignature().getName();
            System.out.println(className + ": " + methodName + " cost " + takeTime + "ms");
        }
        return result;
    }
}
