package lab.togo.webfluxclient;

import lab.togo.webfluxclient.interfaces.ProxyCreator;
import lab.togo.webfluxclient.proxy.JDKProxyCreator;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebfluxClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxClientApplication.class, args);
    }


    /**
     * 创建JDK代理工具类
     * @return
     */
    @Bean
    ProxyCreator jdkProxyCreator() {
        return new JDKProxyCreator();
    }

    @Bean
    FactoryBean<IUserApi> userApi(ProxyCreator proxyCreater) {
        return new FactoryBean<IUserApi>() {

            /**
             * 返回代理对象
             * @return
             * @throws Exception
             */
            @Override
            public IUserApi getObject() throws Exception {
                return (IUserApi) proxyCreater.createProxy(this.getObjectType());
            }

            @Override
            public Class<?> getObjectType() {
                return IUserApi.class;
            }
        };
    }

}
