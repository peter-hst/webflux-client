package lab.togo.webfluxclient.handler;

import lab.togo.webfluxclient.bean.MethodInfo;
import lab.togo.webfluxclient.bean.ServerInfo;
import lab.togo.webfluxclient.interfaces.RestHandler;

public class WebClientRestHandler implements RestHandler {
    @Override
    public void init(ServerInfo serverInfo) {

    }

    @Override
    public Object invokeRest(ServerInfo serverInfo, MethodInfo methodInfo) {
        return null;
    }
}
