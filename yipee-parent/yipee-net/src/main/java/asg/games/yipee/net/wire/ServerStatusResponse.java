package asg.games.yipee.net.wire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
public class ServerStatusResponse {
    private String status;
    private String service;
    private String serverId;
    private String timestamp;
    private String version;
    private String motd;
}