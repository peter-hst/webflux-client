package lab.togo.webfluxclient.proxy;

import lab.togo.webfluxclient.bean.MethodInfo;
import lab.togo.webfluxclient.bean.ServerInfo;
import lab.togo.webfluxclient.interfaces.ProxyCreator;
import lab.togo.webfluxclient.interfaces.RestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

import java.lang.reflect.Method;

/**
 * 使用JDK动态代理 实现代理类
 */
@Slf4j
public class JDKProxyCreator implements ProxyCreator {
    @Override
    public Object createProxy(Class<?> type) {
        log.info("createProxy: {}", type);

        //        得到服务器信息
        ServerInfo serverInfo = extractServerInfo(type);
        log.info("serverInfo:{}",serverInfo);

//        给每个代理类一个实现
        RestHandler handler = null;

//        初始化服务器信息(初始化web client)
        handler.init(serverInfo);


        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{type}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

//                根据方法和参数得到调用信息
                MethodInfo methodInfo = extractMethodInfo(method, objects);
                log.info("methodInfo:{}",methodInfo);
//                调用rest
                return handler.invokeRest(serverInfo, methodInfo);
            }

            private MethodInfo extractMethodInfo(Method method, Object[] objects) {
                return null;
            }
        });
    }

    private ServerInfo extractServerInfo(Class<?> type) {
//        type.getAnnotations()
        return null;
    }
}
