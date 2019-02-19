package lab.togo.webfluxclient.proxy;

import lab.togo.webfluxclient.ApiServer;
import lab.togo.webfluxclient.bean.MethodInfo;
import lab.togo.webfluxclient.bean.ServerInfo;
import lab.togo.webfluxclient.handler.WebClientRestHandler;
import lab.togo.webfluxclient.interfaces.ProxyCreator;
import lab.togo.webfluxclient.interfaces.RestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

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
        log.info("serverInfo:{}", serverInfo);

//        给每个代理类一个实现
        RestHandler handler = new WebClientRestHandler();

//        初始化服务器信息(初始化web client)
        handler.init(serverInfo);


        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{type}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

//                根据方法和参数得到调用信息
                MethodInfo methodInfo = extractMethodInfo(method, objects);
                log.info("methodInfo:{}", methodInfo);
//                调用rest
                return handler.invokeRest(serverInfo, methodInfo);
            }

            /**
             * 根据方法定义和调用参数得到调用的相关信息
             * @param method
             * @param args
             * @return
             */
            private MethodInfo extractMethodInfo(Method method, Object[] args) {
                MethodInfo methodInfo = MethodInfo.builder().build();
                Annotation[] annotations = method.getAnnotations();

//                得到请求的URL和方法
                extractUrlAndMethod(methodInfo, annotations);

//                得到请求的parm和body
                extractRequestParamAndBody(method, args, methodInfo);

//                提取返回对象信息
                extractReturnInfo(method, methodInfo);
                return methodInfo;
            }

            /**
             * 提取返回对象信息
             * @param method
             * @param methodInfo
             */
            private void extractReturnInfo(Method method, MethodInfo methodInfo) {
//                返回Flux还是Mono
//                isAssignableFrom 判断类型是否是某个的子类  isInstanceOf 判断实例是否是某个的子类
                boolean isFlux = method.getReturnType().isAssignableFrom(Flux.class);
                methodInfo.setReturnFlux(isFlux);

//                得到返回对象的实际类型
                Class<?> elementType = extractElementType(method.getGenericReturnType());
                methodInfo.setReturnElementType(elementType);
            }

//            得到泛型类型的实际类型
            private Class<?> extractElementType(Type genericReturnType) {
                Type[] actualTypeArguments = ((ParameterizedType)genericReturnType).getActualTypeArguments();
                return (Class<?>) actualTypeArguments[0];
            }

            private void extractRequestParamAndBody(Method method, Object[] args, MethodInfo methodInfo) {
                //                 得到调用的参数和body
                Parameter[] parameters = method.getParameters();

//                参数和值对应的map
                Map<String, Object> params = new LinkedHashMap<>();
                methodInfo.setParams(params);
                for (int i = 0; i < parameters.length; i++) {
//                    是否带 @PathVariable
                    PathVariable annoPath = parameters[i].getAnnotation(PathVariable.class);
                    if (null != annoPath) {
                        params.put(annoPath.value(), args[i]);
                    }
//                    是否带 @RequestBody
                    RequestBody annoBody = parameters[i].getAnnotation(RequestBody.class);
                    if (null != annoBody) {
                        methodInfo.setBody((Mono<?>) args[i]);
                    }

                }
            }

            private void extractUrlAndMethod(MethodInfo methodInfo, Annotation[] annotations) {
                for (Annotation annotation : annotations) {
                    if (annotation instanceof GetMapping) {
                        GetMapping a = (GetMapping) annotation;
                        methodInfo.setUrl(a.value()[0]);
                        methodInfo.setMethod(HttpMethod.GET);
                    } else if (annotation instanceof PostMapping) {
                        PostMapping a = (PostMapping) annotation;
                        methodInfo.setUrl(a.value()[0]);
                        methodInfo.setMethod(HttpMethod.POST);
                    } else if (annotation instanceof PutMapping) {
                        PutMapping a = (PutMapping) annotation;
                        methodInfo.setUrl(a.value()[0]);
                        methodInfo.setMethod(HttpMethod.PUT);
                    } else if (annotation instanceof DeleteMapping) {
                        DeleteMapping a = (DeleteMapping) annotation;
                        methodInfo.setUrl(a.value()[0]);
                        methodInfo.setMethod(HttpMethod.DELETE);
                    }
                }
            }
        });
    }

    /**
     * 提取服务器信息
     *
     * @param type
     * @return
     */
    private ServerInfo extractServerInfo(Class<?> type) {
        ApiServer anno = type.getAnnotation(ApiServer.class);
        return ServerInfo.builder().url(anno.value()).build();
    }
}
