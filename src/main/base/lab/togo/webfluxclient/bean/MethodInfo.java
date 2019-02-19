package lab.togo.webfluxclient.bean;

import lombok.*;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 方法调用信息类
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MethodInfo {

    /**
     * 请求URL
     */
    private String url;

    /**
     * 请求方法
     */
    private HttpMethod method;

    /**
     * 请求参数(url)
     */
    private Map<String,Object> params;

    /**
     * 请求body
     */
    private Mono<?> body;

}
