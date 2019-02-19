package lab.togo.webfluxclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TestController {

    //    直接注入定义的接口
    @Autowired
    IUserApi userApi;

    @GetMapping("/")
    public void test() {
//        测试信息提取
//        不订阅不会实际发出请求,但会进入我们的代理类,可以做个测试
        userApi.getAllUser();
        userApi.getUserById("111111");
        userApi.deleteUserById("222222222");
        userApi.createUser(Mono.just(User.builder().name("Peter").age(23).build()));


//        直接调用实现调用rest接口的效果
//        Flux<User> users = userApi.getAllUser();
//        users.subscribe(System.out::print);
    }
}
