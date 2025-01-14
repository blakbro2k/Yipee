package asg.games.yipee.aspects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TracedObject {
    private String name;
    private String firstname;
    private String lastname;
    private String pw;

    public TracedObject() {

    }

    @Untraced
    public String getUntraced() {
        return getPw();
    }

    @SensitiveTraceReturn
    public String getPw() {
        return pw;
    }
}
