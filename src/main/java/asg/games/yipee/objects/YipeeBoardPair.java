package asg.games.yipee.objects;

import java.util.Objects;

public class YipeeBoardPair extends AbstractYipeeObject {
    YipeeGameBoard leftBoard;
    YipeeGameBoard rightBoard;

    //Empty Contractor required for Json.Serializable
    public YipeeBoardPair() {
        super();
    }

    public YipeeBoardPair(YipeeGameBoard left, YipeeGameBoard right) {
        this();
        setLeftBoard(left);
        setRightBoard(right);
    }

    public void setLeftBoard(YipeeGameBoard leftBoard) {
        this.leftBoard = leftBoard;
    }

    public void setRightBoard(YipeeGameBoard rightBoard){
        this.rightBoard = rightBoard;
    }

    public YipeeGameBoard getLeftBoard() {
        return leftBoard;
    }

    public YipeeGameBoard getRightBoard() {
        return rightBoard;
    }
/*
    @Override
    public void write(Json json) {
        if (json != null) {
            super.write(json);
            if (leftBoard != null) {
                json.writeValue("leftBoard", leftBoard, leftBoard.getClass());
            }
            if (rightBoard != null) {
                json.writeValue("rightBoard", rightBoard, rightBoard.getClass());
            }
        }
    }

    @Override
    public void read(Json json, JsonValue jsonValue) {
        if (json != null) {
            super.read(json, jsonValue);
            leftBoard = json.readValue("leftBoard", YokelGameBoard.class, jsonValue);
            rightBoard = json.readValue("rightBoard", YokelGameBoard.class, jsonValue);
        }
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeeBoardPair that = (YipeeBoardPair) o;
        return Objects.equals(getLeftBoard(), that.getLeftBoard()) && Objects.equals(getRightBoard(), that.getRightBoard());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLeftBoard(), getRightBoard());
    }
}
