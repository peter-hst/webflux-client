package lab.togo.webfluxclient.interfaces;

import lab.togo.webfluxclient.bean.MethodInfo;
import lab.togo.webfluxclient.bean.ServerInfo;

/**
 * rest请求调用handler
 */
public interface RestHandler {

//    初始化服务器信息
    void init(ServerInfo serverInfo);

    /**
     * 调用rest请求, 返回接口
     * @param serverInfo
     * @param methodInfo
     * @return
     */
    Object invokeRest(ServerInfo serverInfo, MethodInfo methodInfo);
}
