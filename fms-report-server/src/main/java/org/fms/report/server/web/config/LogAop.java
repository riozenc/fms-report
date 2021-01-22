/**
 * Auth:riozenc
 * Date:2019年3月19日 下午4:34:09
 * Title:com.riozenc.cim.web.config.LogAop.java
 **/
package org.fms.report.server.web.config;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.riozenc.titanTool.annotation.AfterAopSupport;

/**
 * 不合理，待完善
 * @author riozenc
 *
 */
@Aspect
@Component
public class LogAop {
    private static final Log logger = LogFactory.getLog(LogAop.class);

    @After("@annotation(com.riozenc.titanTool.annotation.AfterAopSupport)")
    public void after(JoinPoint joinPoint) {
        Object object = joinPoint.getTarget();// 调用对象
        Object[] args = joinPoint.getArgs();// 调用参数
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        AfterAopSupport afterAopSupport = method.getAnnotation(AfterAopSupport.class);
        String afterMethodName = afterAopSupport.method();
        Class<?>[] parameterTypes = afterAopSupport.parameterTypes();
        try {
            Object[] params = new Object[args.length + 1];
            System.arraycopy(args, 0, params, 0, args.length);
            params[args.length] = method.getName();
            Method afterMethod = object.getClass().getDeclaredMethod(afterMethodName, parameterTypes);
            Object value = afterMethod.invoke(object, params);
            logger.info("LogAop: [" + object + "." + afterMethodName + "]=" + value);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.info("LogAop: " + e);
        }
    }
}
