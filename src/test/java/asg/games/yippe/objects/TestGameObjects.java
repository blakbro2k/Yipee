package asg.games.yippe.objects;

import asg.games.yipee.objects.YipeeGameBoard;
import asg.games.yipee.objects.*;
import asg.games.yipee.tools.Input;
import asg.games.yipee.tools.RandomUtil;
import asg.games.yipee.tools.TimeUtils;
import asg.games.yipee.tools.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class TestGameObjects {
    AtomicInteger atomicId = new AtomicInteger(0);

    @Test()
    public void testRandomArray() {
        YipeeGameBoard.TestRandomBlockArray testNextBlocks = new YipeeGameBoard.TestRandomBlockArray(2048, -1, 6);
        RandomUtil.RandomNumberArray nextBlocks = new RandomUtil.RandomNumberArray(2048, 1, 6);

        System.out.println("testArray=" + Arrays.toString(randomToArray(testNextBlocks)));
        System.out.println("realArray=" + Arrays.toString(randomToArray(nextBlocks)));
    }

    private int[] randomToArray(RandomUtil.RandomNumberArray blocksArray) {
        int[] testReturn = new int[2048];
        for (int i = 0; i < 2048; i++) {
            testReturn[i] = blocksArray.getRandomNumberAt(i);
        }
        return testReturn;
    }

    @Test(dataProvider = "yokel_objects")
    public void testAbstractEqualsAndHashNullIds(AbstractYipeeObject y1, AbstractYipeeObject y2, AbstractYipeeObject x1) {
        Assert.assertEquals(y1, y2);
        Assert.assertNotEquals(y1, x1);
        Assert.assertNotEquals(y2, x1);

        //HashSet uses hashes to determine uniqueness.
        Set<YipeeObject> set = new HashSet<>();
        set.add(y1);
        set.add(y2);
        set.add(x1);
        Assert.assertEquals(set.size(), 2);
    }

    @Test(dataProvider = "yokel_objects_ids")
    public void testAbstractEqualsAndHash(AbstractYipeeObject y1, AbstractYipeeObject y2, AbstractYipeeObject x1) throws JsonProcessingException {
        Assert.assertNotEquals(y1, y2);
        Assert.assertNotEquals(y1, x1);
        Assert.assertNotEquals(y2, x1);
        Assert.assertNotEquals(Objects.hash(y1.getId()), x1.hashCode());
        Assert.assertNotEquals(Objects.hash(y2.getId()), x1.hashCode());
        Assert.assertNotEquals(Objects.hash(y1.getId()), y2.hashCode());
        System.out.println("y1: " + y1);
        System.out.println("y1: " + y1.getClass());
        System.out.println("serialized y1: " + Util.getJsonString(y1));

        YipeeObject copy = Util.getObjectFromJsonString(y1.getClass(), Util.getJsonString(y1));
        Assert.assertEquals(copy, y1);
        Assert.assertEquals(copy.getId(), y1.getId());
        Assert.assertEquals(copy.hashCode(), y1.hashCode());
    }

    @Test(dataProvider = "yokel_objects_ids")
    public void testAbstractYokeObjectDeepCopy(AbstractYipeeObject y1, AbstractYipeeObject y2, AbstractYipeeObject x1) {
        //test Deep Copy
        if (y1 instanceof Copyable<?>) {
            Object copy1 = ((Copyable<?>) y1).deepCopy();
            Assert.assertEquals(copy1, y1);
        }

        if (y2 instanceof Copyable<?>) {
            Object copy1 = ((Copyable<?>) y2).deepCopy();
            Assert.assertEquals(copy1, y2);
        }

        if (x1 instanceof Copyable<?>) {
            Object copy1 = ((Copyable<?>) x1).deepCopy();
            Assert.assertEquals(copy1, x1);
        }
    }

    @Test(dataProvider = "yokel_objects_ids")
    public void testJSONConversion(AbstractYipeeObject y1, AbstractYipeeObject y2, AbstractYipeeObject x1) throws JsonProcessingException {
        //Test Json
        String jsonStringBlock1 = Util.getJsonString(y1);
        String jsonStringYblock2 = Util.getJsonString(y2);
        String jsonStringXblock1 = Util.getJsonString(x1);
        System.out.println(y1);
        System.out.println(jsonStringBlock1);
        System.out.println(y2);
        System.out.println(jsonStringYblock2);
        System.out.println(x1);
        System.out.println(jsonStringXblock1);

        YipeeObject readBlock1 = Util.getObjectFromJsonString(y1.getClass(), jsonStringBlock1);
        YipeeObject readYBlock1 = Util.getObjectFromJsonString(y2.getClass(), jsonStringYblock2);
        YipeeObject readYBlock2 = Util.getObjectFromJsonString(x1.getClass(), jsonStringXblock1);
        Assert.assertEquals(jsonStringBlock1, Util.getJsonString(y1));
        Assert.assertEquals(jsonStringYblock2, Util.getJsonString(y2));
        Assert.assertEquals(jsonStringXblock1, Util.getJsonString(x1));
        Assert.assertEquals(readBlock1, y1);
        Assert.assertEquals(readYBlock1, y2);
        Assert.assertEquals(readYBlock2, x1);
    }

    @Test
    public void testYokelBlock() throws JsonProcessingException {
        YipeeBlock block1 = new YipeeBlock(1, 1);
        YipeeBlock yblock1 = new YipeeBlock(1, 1, YipeeBlock.Y_BLOCK);
        setIdAndName(block1, yblock1);

        //getBlockType() {
        Assert.assertEquals(yblock1.getBlockType(), YipeeBlock.Y_BLOCK);
        //public void setBlockType(int blockType) {
        yblock1.setBlockType(YipeeBlock.OFFENSIVE_BASH_BLOCK_MEGA);
        Assert.assertEquals(yblock1.getBlockType(), YipeeBlock.OFFENSIVE_BASH_BLOCK_MEGA);

        //public void reset() {
        block1.dispose();
        Assert.assertEquals(block1.getBlockType(), YipeeBlock.CLEAR_BLOCK);

        //public void setPowerIntensity(int intensity) {
        //public int getPowerIntensity() {
        block1.setPowerIntensity(1);
        Assert.assertEquals(block1.getPowerIntensity(), 3);
        block1.setPowerIntensity(10);
        Assert.assertEquals(block1.getPowerIntensity(), 7);

        Assert.assertTrue(block1.hasPower());
        block1.setPowerIntensity(0);
        Assert.assertFalse(block1.hasPower());

        //Test Json
        block1.setPowerIntensity(4);
        String json = Util.getJsonString(block1);
        System.out.println("json: " + json);
        YipeeBlock readYokelBlock = Util.getObjectFromJsonString(YipeeBlock.class, json);
        Assert.assertEquals(json, Util.getJsonString(block1));
        System.out.println("Expected: " + Util.getJsonString(block1));
        System.out.println("Actual: " + Util.getJsonString(readYokelBlock));
        Assert.assertEquals(block1, readYokelBlock);
    }

    @Test
    public void testYokelClock() throws JsonProcessingException {
        YipeeClock clock = new YipeeClock();
        setIdAndName(clock);

        //Test Json
        String jsonStringClock1 = Util.getJsonString(clock);
        System.out.println("clock: " + jsonStringClock1);

        //Test methods
        YipeeClock readClock1 = Util.getObjectFromJsonString(YipeeClock.class, jsonStringClock1);
        Assert.assertEquals(jsonStringClock1, Util.getJsonString(clock));
        Assert.assertEquals(readClock1, clock);

        Assert.assertFalse(clock.isRunning());
        Assert.assertEquals(clock.getSeconds(), -1);
        Assert.assertEquals(clock.getMinutes(), -1);

        clock.start();
        Assert.assertEquals(clock.getStart(), TimeUtils.millis());
        Assert.assertTrue(clock.isRunning());
        Assert.assertEquals(clock.getSeconds(), 0);
        Assert.assertEquals(clock.getMinutes(), 0);

        //Test Json
        String json = Util.getJsonString(clock);
        YipeeClock readYokelClock = Util.getObjectFromJsonString(YipeeClock.class, json);

        Assert.assertEquals(json, Util.getJsonString(clock));
        System.out.println("Expected: " + Util.getJsonString(clock));
        System.out.println("Actual: " + Util.getJsonString(readYokelClock));
        Assert.assertEquals(clock, readYokelClock);
    }

    @Test
    public void testYokelBoardPair() throws JsonProcessingException {
        YipeeGameBoard board1 = new YipeeGameBoard();
        YipeeGameBoard board2 = new YipeeGameBoard(14);
        YipeeBoardPair pair = new YipeeBoardPair(board1, board2);
        setIdAndName(board1, board2, pair);

        //Test methods
        String jsonStringBoard1 = Util.getJsonString(board1);
        String jsonStringBoard2 = Util.getJsonString(board2);
        String jsonStringPair = Util.getJsonString(pair);
        System.out.println("board1: " + jsonStringBoard1);
        System.out.println("board2: " + jsonStringBoard2);
        System.out.println("pair: " + jsonStringPair);

        //Json Test
        YipeeGameBoard readStringBoard1 = Util.getObjectFromJsonString(YipeeGameBoard.class, jsonStringBoard1);
        YipeeGameBoard readStringBoard2 = Util.getObjectFromJsonString(YipeeGameBoard.class, jsonStringBoard2);
        YipeeBoardPair readStringPair = Util.getObjectFromJsonString(YipeeBoardPair.class, jsonStringPair);
        Assert.assertEquals(jsonStringBoard1, Util.getJsonString(board1));
        Assert.assertEquals(jsonStringBoard2, Util.getJsonString(board2));
        Assert.assertEquals(jsonStringPair, Util.getJsonString(pair));
        Assert.assertEquals(readStringBoard1, board1);
        Assert.assertEquals(readStringBoard2, board2);
        Assert.assertEquals(readStringPair, pair);

        Assert.assertEquals(pair.getLeftBoard(), board1);
        Assert.assertEquals(pair.getRightBoard(), board2);
    }

    @Test
    public void testYokelBlockMove() throws JsonProcessingException {
        //TODO: UPDATE TEST
        YipeeBlockMove blockMove = new YipeeBlockMove(5, 5, 1, 2, 4);
        setIdAndName(blockMove);
        System.out.println(" blockMove: " + blockMove);


        String json = Util.getJsonString(blockMove);
        System.out.println(" blockMove: " + json);
        YipeeBlockMove readBlockMove = Util.getObjectFromJsonString(YipeeBlockMove.class, json);
        Assert.assertEquals(json, Util.getJsonString(blockMove));
        Assert.assertEquals(blockMove, readBlockMove);

        Assert.assertEquals(blockMove.getCol(), 1);
        Assert.assertEquals(blockMove.getRow(), 2);
        Assert.assertEquals(blockMove.getTargetRow(), 4);
        Assert.assertEquals(json, Util.getJsonString(blockMove));
        Assert.assertEquals(blockMove, Util.getObjectFromJsonString(YipeeBlockMove.class, json));
    }

    @Test
    public void testYokelBrokenBlock() throws JsonProcessingException {
        YipeeBlockMove blockMove = new YipeeBlockMove(1, 1, 2, 4, 5);
        setIdAndName(blockMove);
        System.out.println(" blockMove: " + blockMove);

        String json = Util.getJsonString(blockMove);
        YipeeBlockMove readBlockMove = Util.getObjectFromJsonString(YipeeBlockMove.class, json);
        Assert.assertEquals(json, Util.getJsonString(blockMove));
        Assert.assertEquals(blockMove, readBlockMove);

        Assert.assertEquals(blockMove.getRow(), 4);
        Assert.assertEquals(blockMove.getCol(), 2);
        Assert.assertEquals(blockMove.getTargetRow(), 5);
        Assert.assertEquals(json, Util.getJsonString(blockMove));
        Assert.assertEquals(blockMove, Util.getObjectFromJsonString(YipeeBlockMove.class, json));
    }

    @Test
    public void testYokelPiece() throws JsonProcessingException {
        int[] expected = new int[3];
        int expectedIndex = 1;
        expected[0] = 4;
        expected[2] = 2;

        YipeePiece yokelPiece = new YipeePiece(1, 2, 0, 4);
        setIdAndName(yokelPiece);
        System.out.println("yokelPiece: " + yokelPiece);

        yokelPiece.setPosition(2, 3);
        Assert.assertEquals(yokelPiece.row, 2);
        Assert.assertEquals(yokelPiece.column, 3);

        Assert.assertThrows(RuntimeException.class, () -> yokelPiece.setPosition(-30, 4));
        Assert.assertThrows(RuntimeException.class, () -> yokelPiece.setPosition(6, -4));

        Assert.assertEquals(yokelPiece.getValueAt(1), expected[1]);
        Assert.assertEquals(yokelPiece.getValueAt(2), expected[2]);
        Assert.assertEquals(yokelPiece.getValueAt(0), expected[0]);

        Assert.assertEquals(yokelPiece.getIndex(), expectedIndex);
        Assert.assertEquals(yokelPiece.getBlock1(), expected[2]);
        Assert.assertEquals(yokelPiece.getBlock2(), expected[1]);
        Assert.assertEquals(yokelPiece.getBlock3(), expected[0]);
        Assert.assertEquals(Arrays.toString(yokelPiece.getCells()), Arrays.toString(expected));

        //Cycle Down
        yokelPiece.cycleDown();
        Assert.assertEquals(yokelPiece.getBlock1(), expected[0]);
        Assert.assertEquals(yokelPiece.getBlock2(), expected[2]);
        Assert.assertEquals(yokelPiece.getBlock3(), expected[1]);

        //Cycle Down
        yokelPiece.cycleUp();
        Assert.assertEquals(yokelPiece.getBlock1(), expected[2]);
        Assert.assertEquals(yokelPiece.getBlock2(), expected[1]);
        Assert.assertEquals(yokelPiece.getBlock3(), expected[0]);

        //Test Json
        String json = Util.getJsonString(yokelPiece);
        YipeePiece readBlockMove = Util.getObjectFromJsonString(YipeePiece.class, json);
        Assert.assertEquals(json, Util.getJsonString(yokelPiece));
        System.out.println("Expected: " + Util.getJsonString(yokelPiece));
        System.out.println("Actual: " + Util.getJsonString(readBlockMove));
        Assert.assertEquals(yokelPiece, readBlockMove);
    }

    @Test
    public void testYokelPlayer() throws JsonProcessingException {
        YipeePlayer yokelPlayer = new YipeePlayer("TestUser1", 500, 4);
        YipeePlayer yokelPlayer2 = new YipeePlayer("TestUser2", 2500);
        YipeePlayer yokelPlayer3 = new YipeePlayer("TestUser3");
        yokelPlayer3.setIcon(12);
        yokelPlayer3.increaseRating(10);
        yokelPlayer3.decreaseRating(5);

        Assert.assertEquals(yokelPlayer.getName(), "TestUser1");
        Assert.assertEquals(yokelPlayer2.getName(), "TestUser2");
        Assert.assertEquals(yokelPlayer3.getName(), "TestUser3");

        setIdAndName(yokelPlayer, yokelPlayer2, yokelPlayer3);
        System.out.println("yokelPlayer: " + yokelPlayer);
        System.out.println("yokelPlayer2: " + yokelPlayer2);
        System.out.println("yokelPlayer3: " + yokelPlayer3);

        Assert.assertEquals(yokelPlayer.getIcon(), 4);
        Assert.assertEquals(yokelPlayer.getRating(), 500);

        Assert.assertEquals(yokelPlayer2.getIcon(), 1);
        Assert.assertEquals(yokelPlayer2.getRating(), 2500);

        Assert.assertEquals(yokelPlayer3.getIcon(), 12);
        Assert.assertEquals(yokelPlayer3.getRating(), 1505);

        //Test Json
        String json = Util.getJsonString(yokelPlayer3);
        System.out.println("json: " + json);
        YipeePlayer readYokelPlayer3 = Util.getObjectFromJsonString(YipeePlayer.class, json);
        Assert.assertEquals(json, Util.getJsonString(yokelPlayer3));
        Assert.assertEquals(yokelPlayer3, readYokelPlayer3);

        YipeePlayer copy = new YipeePlayer(YipeePlayer.class, json);
        System.out.println("Expected: " + Util.getJsonString(readYokelPlayer3));
        System.out.println("Actual: " + Util.getJsonString(copy));
        Assert.assertEquals(copy, readYokelPlayer3);
    }

    @Test
    public void testYokelSeat() throws JsonProcessingException {
        Assert.assertThrows(RuntimeException.class, () -> new YipeeSeat("12", 8));
        Assert.assertThrows(RuntimeException.class, () -> new YipeeSeat("12", -1));
        YipeePlayer yokelPlayer = new YipeePlayer("TestUser1", 500, 4);
        YipeePlayer yokelPlayer2 = new YipeePlayer("TestUser2", 2500);
        YipeePlayer yokelPlayer3 = new YipeePlayer("TestUser3");
        setIdAndName(yokelPlayer, yokelPlayer2, yokelPlayer3);

        YipeeSeat yokelSeat = new YipeeSeat("sim:table:1#1", 0);
        Assert.assertEquals(yokelSeat.getSeatNumber(), 0);
        Assert.assertEquals(yokelSeat.getTableId(), "sim:table:1#1");

        Assert.assertNull(yokelSeat.getSeatedPlayer());
        Assert.assertFalse(yokelSeat.isOccupied());

        Assert.assertTrue(yokelSeat.sitDown(yokelPlayer));
        Assert.assertFalse(yokelSeat.sitDown(yokelPlayer));
        Assert.assertEquals(yokelSeat.getSeatedPlayer(), yokelPlayer);
        Assert.assertTrue(yokelSeat.isOccupied());
        YipeePlayer standUp = yokelSeat.standUp();
        Assert.assertFalse(yokelSeat.isOccupied());
        Assert.assertEquals(standUp, yokelPlayer);

        yokelSeat.sitDown(yokelPlayer2);
        Assert.assertEquals(yokelSeat.getSeatedPlayer(), yokelPlayer2);
        Assert.assertTrue(yokelSeat.isOccupied());
        yokelSeat.dispose();
        Assert.assertFalse(yokelSeat.isOccupied());

        //Test Json
        yokelSeat.sitDown(yokelPlayer2);
        String json = Util.getJsonString(yokelSeat);
        System.out.println("json: " + json);
        YipeeSeat readYokelSeat = Util.getObjectFromJsonString(YipeeSeat.class, json);
        Assert.assertEquals(json, Util.getJsonString(yokelSeat));
        System.out.println("Expected: " + Util.getJsonString(readYokelSeat));
        System.out.println("Actual: " + Util.getJsonString(yokelSeat));
        Assert.assertEquals(yokelSeat, readYokelSeat);
    }

    @Test
    public void testYokelRoom() throws JsonProcessingException {
        YipeeRoom yokelRoom1 = new YipeeRoom("Eiffel Tower", YipeeRoom.ADVANCED_LOUNGE);
        YipeePlayer yokelPlayer = new YipeePlayer("TestUser1", 500, 4);
        YipeePlayer yokelPlayer2 = new YipeePlayer("TestUser2", 2500);
        YipeePlayer yokelPlayer3 = new YipeePlayer("TestUser3");
        setIdAndName(yokelRoom1, yokelPlayer, yokelPlayer2, yokelPlayer3);
        yokelRoom1.setName("Eiffel Tower");

        Assert.assertEquals(yokelRoom1.getName(), "Eiffel Tower");
        Assert.assertEquals(yokelRoom1.getLoungeName(), YipeeRoom.ADVANCED_LOUNGE);

        System.out.println("yokelRoom1: " + yokelRoom1);
        List<YipeePlayer> expectedPlayers = new ArrayList<>();
        Map<Integer, YipeeTable> expectedTables = new HashMap<>();

        Assert.assertEquals(yokelRoom1.getAllTableIndexes(), Util.getMapKeys(expectedTables));
        Assert.assertEquals(yokelRoom1.getAllTables(), Util.getMapValues(expectedTables));
        Assert.assertEquals(yokelRoom1.getAllPlayers(), expectedPlayers);

        //Test players watch list
        yokelRoom1.joinRoom(yokelPlayer);
        yokelRoom1.joinRoom(yokelPlayer);
        yokelRoom1.joinRoom(yokelPlayer2);
        yokelRoom1.joinRoom(yokelPlayer3);
        yokelRoom1.leaveRoom(yokelPlayer3);
        yokelRoom1.leaveRoom(yokelPlayer3);
        expectedPlayers.add(yokelPlayer);
        expectedPlayers.add(yokelPlayer2);
        Assert.assertEquals(yokelRoom1.getAllPlayers(), expectedPlayers);

        YipeeRoom room = new YipeeRoom("NewRoom");
        room.dispose();

        //Test table list
        yokelRoom1.addTable();

        Map<String, Object> arguments = new HashMap<>();
        arguments.put(YipeeTable.ARG_TYPE, YipeeTable.ENUM_VALUE_PRIVATE);
        arguments.put(YipeeTable.ARG_RATED, true);
        yokelRoom1.addTable(arguments);

        arguments.put(YipeeTable.ARG_TYPE, YipeeTable.ENUM_VALUE_PROTECTED);
        arguments.put(YipeeTable.ARG_RATED, false);
        yokelRoom1.addTable(arguments);

        yokelRoom1.addTable();
        Assert.assertEquals(Util.size(yokelRoom1.getAllTables()), 4);
        yokelRoom1.removeTable(2);
        Assert.assertEquals(Util.size(yokelRoom1.getAllTables()), 3);

        Collection<YipeeTable> tables = yokelRoom1.getAllTables();
        for (YipeeTable table : tables) {
            table.setId(Util.IDGenerator.getID());
        }

        Assert.assertNotNull(yokelRoom1.getTable(4));
        Assert.assertNull(yokelRoom1.getTable(2));
        yokelRoom1.addTable();
        Assert.assertNotNull(yokelRoom1.getTable(2));

        //Test Json
        String json = Util.getJsonString(yokelRoom1);
        System.out.println("json: " + json);
        YipeeRoom readYokelRoom = Util.getObjectFromJsonString(YipeeRoom.class, json);
        Assert.assertEquals(json, Util.getJsonString(yokelRoom1));
        System.out.println("Expected: " + Util.getJsonString(yokelRoom1));
        System.out.println("Actual  : " + Util.getJsonString(readYokelRoom));
        Assert.assertEquals(readYokelRoom, yokelRoom1);
    }

    @Test
    public void testYokelKeyMap() {
        YipeeKeyMap blockKeyMap = new YipeeKeyMap();
        System.out.println("blockKeyMap: " + blockKeyMap);

        Assert.assertEquals(blockKeyMap.getRightKey(), Input.Keys.RIGHT);
        blockKeyMap.setRightKey(Input.Keys.LEFT);
        Assert.assertEquals(blockKeyMap.getRightKey(), Input.Keys.LEFT);

        Assert.assertEquals(blockKeyMap.getLeftKey(), Input.Keys.LEFT);
        blockKeyMap.setLeftKey(Input.Keys.UP);
        Assert.assertEquals(blockKeyMap.getLeftKey(), Input.Keys.UP);

        Assert.assertEquals(blockKeyMap.getCycleDownKey(), Input.Keys.UP);
        blockKeyMap.setCycleDownKey(Input.Keys.DOWN);
        Assert.assertEquals(blockKeyMap.getCycleDownKey(), Input.Keys.DOWN);

        Assert.assertEquals(blockKeyMap.getDownKey(), Input.Keys.DOWN);
        blockKeyMap.setDownKey(Input.Keys.LEFT);
        Assert.assertEquals(blockKeyMap.getDownKey(), Input.Keys.LEFT);

        Assert.assertEquals(blockKeyMap.getCycleUpKey(), Input.Keys.P);
        blockKeyMap.setCycleUpKey(Input.Keys.DOWN);
        Assert.assertEquals(blockKeyMap.getCycleUpKey(), Input.Keys.DOWN);

        Assert.assertEquals(blockKeyMap.getTarget1(), Input.Keys.NUM_1);
        blockKeyMap.setTarget1(Input.Keys.DOWN);
        Assert.assertEquals(blockKeyMap.getTarget1(), Input.Keys.DOWN);

        Assert.assertEquals(blockKeyMap.getTarget2(), Input.Keys.NUM_2);
        blockKeyMap.setTarget2(Input.Keys.DOWN);
        Assert.assertEquals(blockKeyMap.getTarget2(), Input.Keys.DOWN);

        Assert.assertEquals(blockKeyMap.getTarget3(), Input.Keys.NUM_3);
        blockKeyMap.setTarget3(Input.Keys.DOWN);
        Assert.assertEquals(blockKeyMap.getTarget3(), Input.Keys.DOWN);

        Assert.assertEquals(blockKeyMap.getTarget4(), Input.Keys.NUM_4);
        blockKeyMap.setTarget4(Input.Keys.DOWN);
        Assert.assertEquals(blockKeyMap.getTarget4(), Input.Keys.DOWN);

        Assert.assertEquals(blockKeyMap.getTarget5(), Input.Keys.NUM_5);
        blockKeyMap.setTarget5(Input.Keys.DOWN);
        Assert.assertEquals(blockKeyMap.getTarget5(), Input.Keys.DOWN);

        Assert.assertEquals(blockKeyMap.getTarget6(), Input.Keys.NUM_6);
        blockKeyMap.setTarget6(Input.Keys.DOWN);
        Assert.assertEquals(blockKeyMap.getTarget6(), Input.Keys.DOWN);

        Assert.assertEquals(blockKeyMap.getTarget7(), Input.Keys.NUM_7);
        blockKeyMap.setTarget7(Input.Keys.DOWN);
        Assert.assertEquals(blockKeyMap.getTarget7(), Input.Keys.DOWN);

        Assert.assertEquals(blockKeyMap.getTarget8(), Input.Keys.NUM_8);
        blockKeyMap.setTarget8(Input.Keys.DOWN);
        Assert.assertEquals(blockKeyMap.getTarget8(), Input.Keys.DOWN);

        Assert.assertEquals(blockKeyMap.getRandomAttackKey(), Input.Keys.SPACE);
        blockKeyMap.setRandomAttackKey(Input.Keys.DOWN);
        Assert.assertEquals(blockKeyMap.getRandomAttackKey(), Input.Keys.DOWN);
    }

    @Test
    public void testYokelTable() throws JsonProcessingException {
        YipeeTable yokelTable = new YipeeTable("sim:room1", 1);
        YipeeTable yokelTable2 = new YipeeTable("sim:room2", 2);

        YipeePlayer yokelPlayer = new YipeePlayer("TestUser1", 500, 4);
        YipeePlayer yokelPlayer2 = new YipeePlayer("TestUser2", 2500);

        Map<String, Object> arguments = new HashMap<>();
        arguments.put(YipeeTable.ARG_RATED, true);
        arguments.put(YipeeTable.ARG_TYPE, YipeeTable.ENUM_VALUE_PRIVATE);
        YipeeTable yokelTable3 = new YipeeTable("sim:room3", 1, arguments);
        setIdAndName(yokelTable, yokelTable2, yokelTable3, yokelPlayer, yokelPlayer2);
        yokelTable.setName("sim:room1");
        yokelTable.setTableName(1);
        yokelTable2.setName("sim:room2");
        yokelTable2.setTableName(2);
        yokelTable3.setName("sim:room3");
        yokelTable3.setTableName(1);

        yokelTable2.setAccessType(YipeeTable.ACCESS_TYPE.PROTECTED);
        Assert.assertEquals(yokelTable.getAccessType(), YipeeTable.ACCESS_TYPE.PUBLIC);
        Assert.assertEquals(yokelTable2.getAccessType().getValue(), YipeeTable.ACCESS_TYPE.PROTECTED.toString());
        Assert.assertEquals(yokelTable3.getAccessType(), YipeeTable.ACCESS_TYPE.PRIVATE);

        Assert.assertEquals(yokelTable.getTableNumber(), 1);
        Assert.assertEquals(yokelTable2.getTableNumber(), 2);
        Assert.assertEquals(yokelTable3.getTableNumber(), 1);

        Assert.assertFalse(yokelTable.isRated());
        Assert.assertFalse(yokelTable2.isRated());
        Assert.assertTrue(yokelTable3.isRated());

        yokelTable.addWatcher(yokelPlayer);
        yokelTable.addWatcher(yokelPlayer2);
        Assert.assertEquals(Util.size(yokelTable.getWatchers()), 2);
        yokelTable.removeWatcher(yokelPlayer2);
        Assert.assertEquals(Util.size(yokelTable.getWatchers()), 1);

        Assert.assertFalse(yokelTable.isGroupReady(-3));
        Assert.assertFalse(yokelTable.isGroupReady(23));

        for (int i = 0; i < 9; i++) {
            Assert.assertFalse(yokelTable.isGroupReady(i));
        }

        //Test Json
        String json = Util.getJsonString(yokelTable);
        System.out.println("json: " + json);
        YipeeTable readYokelTable = Util.getObjectFromJsonString(YipeeTable.class, json);
        Assert.assertEquals(json,  Util.getJsonString(yokelTable));
        System.out.println("Expected: " + Util.getJsonString(yokelTable));
        System.out.println("Actual  : " + Util.getJsonString(readYokelTable));
        Assert.assertEquals(readYokelTable, yokelTable);
    }

    @DataProvider(name = "yokel_objects")
    public Object[][] provideYokelObjects() {
        YipeeBlock yokelBlock1 = new YipeeBlock();
        YipeeBlock yokelBlock2 = new YipeeBlock();
        YipeeBlock yokelBlock3 = new YipeeBlock();
        yokelBlock3.setModified(TimeUtils.millis() + 1);

        YipeeClock yokelClock1 = new YipeeClock();
        YipeeClock yokelClock2 = new YipeeClock();
        YipeeClock yokelClock3 = new YipeeClock();
        yokelClock3.setModified(TimeUtils.millis() + 1);

        YipeePlayer player1 = new YipeePlayer();
        YipeePlayer player2 = new YipeePlayer();
        YipeePlayer player3 = new YipeePlayer();
        player3.setModified(TimeUtils.millis() + 1);

        YipeePiece piece1 = new YipeePiece();
        YipeePiece piece2 = new YipeePiece();
        YipeePiece piece3 = new YipeePiece();
        piece3.setModified(TimeUtils.millis() + 1);

        YipeeBlockMove yokelBlockMove1 = new YipeeBlockMove();
        YipeeBlockMove yokelBlockMove2 = new YipeeBlockMove();
        YipeeBlockMove yokelBlockMove3 = new YipeeBlockMove();
        yokelBlockMove3.setModified(TimeUtils.millis() + 1);

        YipeeBoardPair yokelBoardPair1 = new YipeeBoardPair();
        YipeeBoardPair yokelBoardPair2 = new YipeeBoardPair();
        YipeeBoardPair yokelBoardPair3 = new YipeeBoardPair();
        yokelBoardPair3.setModified(TimeUtils.millis() + 1);

        YipeeRoom yokelRoom1 = new YipeeRoom();
        YipeeRoom yokelRoom2 = new YipeeRoom();
        YipeeRoom yokelRoom3 = new YipeeRoom();
        yokelRoom3.setModified(TimeUtils.millis() + 1);

        YipeeSeat yokelSeat1 = new YipeeSeat();
        YipeeSeat yokelSeat2 = new YipeeSeat();
        YipeeSeat yokelSeat3 = new YipeeSeat();
        yokelSeat3.setModified(TimeUtils.millis() + 1);

        YipeeTable yokelTablet1 = new YipeeTable();
        YipeeTable yokelTablet2 = new YipeeTable();
        YipeeTable yokelTablet3 = new YipeeTable();
        yokelTablet3.setModified(TimeUtils.millis() + 1);

        YipeeBrokenBlock YokelBrokenBlock1 = new YipeeBrokenBlock();
        YipeeBrokenBlock YokelBrokenBlock2 = new YipeeBrokenBlock();
        YipeeBrokenBlock YokelBrokenBlock3 = new YipeeBrokenBlock();
        YokelBrokenBlock3.setModified(TimeUtils.millis() + 1);

        return new Object[][]{
                {yokelBlock1, yokelBlock2, yokelBlock3},
                {yokelClock1, yokelClock2, yokelClock3},
                {piece1, piece2, piece3},
                {yokelBlockMove1, yokelBlockMove2, yokelBlockMove3},
                {yokelBoardPair1, yokelBoardPair2, yokelBoardPair3},
                {yokelRoom1, yokelRoom2, yokelRoom3},
                {yokelSeat1, yokelSeat2, yokelSeat3},
                {yokelTablet1, yokelTablet2, yokelTablet3},
                {YokelBrokenBlock1, YokelBrokenBlock2, YokelBrokenBlock3},
                {player1, player2, player3}
        };
    }

    @DataProvider(name = "yokel_objects_ids")
    public Object[][] provideYokelObjectsWithIds() {
        Object[][] objectsWithIds = provideYokelObjects();
        // let's loop through array to populate id and name
        for (Object[] objectsWithId : objectsWithIds) {
            for (Object object : objectsWithId) {
                if (object instanceof YipeeObject) {
                    setIdAndName((YipeeObject) object);
                }
            }
        }
        return objectsWithIds;
    }

    private void setIdAndName(YipeeObject... yipeeObjects) {
        for (YipeeObject yipeeObject : Util.safeIterableArray(yipeeObjects)) {
            if (yipeeObject != null) {
                int id = atomicId.getAndIncrement();
                yipeeObject.setId(Util.IDGenerator.getID());
                yipeeObject.setName(id + "-" + yipeeObject.getClass().getSimpleName());
            }
        }
    }
}