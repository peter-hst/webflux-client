package lab.togo.webfluxclient.bean;

import lombok.*;

/**
 * 服务器信息
 */

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServerInfo {

    /**
     * 服务器URL
     */
    private String url;
}
