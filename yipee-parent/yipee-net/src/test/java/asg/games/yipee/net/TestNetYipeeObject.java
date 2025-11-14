package asg.games.yipee.net;

import asg.games.yipee.common.dto.NetYipeeObject;
import lombok.Data;

@Data
public class TestNetYipeeObject implements NetYipeeObject {
    String id;
    String name;
    long created;
    long modified;

    TestNetYipeeObject() {
    }
}
