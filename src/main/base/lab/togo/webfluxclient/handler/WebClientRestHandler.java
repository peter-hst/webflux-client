package lab.togo.webfluxclient.handler;

import lab.togo.webfluxclient.bean.MethodInfo;
import lab.togo.webfluxclient.bean.ServerInfo;
import lab.togo.webfluxclient.interfaces.RestHandler;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientRestHandler implements RestHandler {
    private WebClient client;

    /**
     * 初始化WebClient
     *
     * @param serverInfo
     */
    @Override
    public void init(ServerInfo serverInfo) {
        this.client = WebClient.create(serverInfo.getUrl());
    }

    /**
     * 处理rest请求
     *
     * @param serverInfo
     * @param methodInfo
     * @return
     */
    @Override
    public Object invokeRest(ServerInfo serverInfo, MethodInfo methodInfo) {
        Object result = null;
        WebClient.ResponseSpec request = this.client
//                请求方法
                .method(methodInfo.getMethod())
//                请求url
                .uri(methodInfo.getUrl(),methodInfo.getParams())
//                数据类型
                .accept(MediaType.APPLICATION_JSON_UTF8)
//                发出请求
                .retrieve();
//                处理body
        if (methodInfo.isReturnFlux()) {
            result = request.bodyToFlux(methodInfo.getReturnElementType());
        } else {
            result = request.bodyToMono(methodInfo.getReturnElementType());
        }

        return result;
    }
}
