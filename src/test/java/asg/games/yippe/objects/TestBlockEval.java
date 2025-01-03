package asg.games.yippe.objects;

import asg.games.yipee.objects.YipeeBlock;
import asg.games.yipee.objects.YipeeBlockEval;
import asg.games.yipee.objects.YipeeObject;
import asg.games.yipee.tools.Util;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class TestBlockEval {
    AtomicInteger atomicId = new AtomicInteger(0);

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    public void printBlockLabel(int block){
        System.out.println(YipeeBlockEval.getPowerLabel(block) + " : " + YipeeBlockEval.getPowerUseDescriptionLabel(block));
    }

    @Test
    public void descriptionTest() throws Exception {
        System.out.println(YipeeBlockEval.getPowerUseDescriptionLabel(YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.OFFENSIVE_MINOR)) + "]Y_BLOCK-(OFFENSIVE_MINOR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.OFFENSIVE_MINOR));
        System.out.println(YipeeBlockEval.getPowerUseDescriptionLabel(YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.OFFENSIVE_REGULAR)) + "]Y_BLOCK-(OFFENSIVE_REGULAR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.OFFENSIVE_REGULAR));
        System.out.println(YipeeBlockEval.getPowerUseDescriptionLabel(YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.OFFENSIVE_MEGA)) + "]Y_BLOCK-(OFFENSIVE_MEGA)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.OFFENSIVE_MEGA));
        System.out.println(YipeeBlockEval.getPowerUseDescriptionLabel(YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.DEFENSIVE_MINOR)) + "]Y_BLOCK-(DEFENSIVE_MINOR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.DEFENSIVE_MINOR));
        System.out.println(YipeeBlockEval.getPowerUseDescriptionLabel(YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.DEFENSIVE_REGULAR)) + "]Y_BLOCK-(DEFENSIVE_REGULAR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.DEFENSIVE_REGULAR));
        System.out.println(YipeeBlockEval.getPowerUseDescriptionLabel(YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.DEFENSIVE_MEGA)) + "]Y_BLOCK-(DEFENSIVE_MEGA)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.DEFENSIVE_MEGA));

        System.out.println("Offensive Y minor=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.OFFENSIVE_MINOR));

        System.out.println("O_BLOCK-(OFFENSIVE_MINOR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.A_BLOCK, YipeeBlock.OFFENSIVE_MINOR));
        System.out.println("O_BLOCK-(OFFENSIVE_REGULAR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.A_BLOCK, YipeeBlock.OFFENSIVE_REGULAR));
        System.out.println("O_BLOCK-(OFFENSIVE_MEGA)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.A_BLOCK, YipeeBlock.OFFENSIVE_MEGA));
        System.out.println("O_BLOCK-(DEFENSIVE_MINOR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.A_BLOCK, YipeeBlock.DEFENSIVE_MINOR));
        System.out.println("O_BLOCK-(DEFENSIVE_REGULAR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.A_BLOCK, YipeeBlock.DEFENSIVE_REGULAR));
        System.out.println("O_BLOCK-(DEFENSIVE_MEGA)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.A_BLOCK, YipeeBlock.DEFENSIVE_MEGA));

        System.out.println("K_BLOCK-(OFFENSIVE_MINOR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.H_BLOCK, YipeeBlock.OFFENSIVE_MINOR));
        System.out.println("K_BLOCK-(OFFENSIVE_REGULAR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.H_BLOCK, YipeeBlock.OFFENSIVE_REGULAR));
        System.out.println("K_BLOCK-(OFFENSIVE_MEGA)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.H_BLOCK, YipeeBlock.OFFENSIVE_MEGA));
        System.out.println("K_BLOCK-(DEFENSIVE_MINOR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.H_BLOCK, YipeeBlock.DEFENSIVE_MINOR));
        System.out.println("K_BLOCK-(DEFENSIVE_REGULAR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.H_BLOCK, YipeeBlock.DEFENSIVE_REGULAR));
        System.out.println("K_BLOCK-(DEFENSIVE_MEGA)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.H_BLOCK, YipeeBlock.DEFENSIVE_MEGA));

        System.out.println("E_BLOCK-(OFFENSIVE_MINOR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Op_BLOCK, YipeeBlock.OFFENSIVE_MINOR));
        System.out.println("E_BLOCK-(OFFENSIVE_REGULAR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Op_BLOCK, YipeeBlock.OFFENSIVE_REGULAR));
        System.out.println("E_BLOCK-(OFFENSIVE_MEGA)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Op_BLOCK, YipeeBlock.OFFENSIVE_MEGA));
        System.out.println("E_BLOCK-(DEFENSIVE_MINOR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Op_BLOCK, YipeeBlock.DEFENSIVE_MINOR));
        System.out.println("E_BLOCK-(DEFENSIVE_REGULAR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Op_BLOCK, YipeeBlock.DEFENSIVE_REGULAR));
        System.out.println("E_BLOCK-(DEFENSIVE_MEGA)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Op_BLOCK, YipeeBlock.DEFENSIVE_MEGA));

        System.out.println("L_BLOCK-(OFFENSIVE_MINOR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Oy_BLOCK, YipeeBlock.OFFENSIVE_MINOR));
        System.out.println("L_BLOCK-(OFFENSIVE_REGULAR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Oy_BLOCK, YipeeBlock.OFFENSIVE_REGULAR));
        System.out.println("L_BLOCK-(OFFENSIVE_MEGA)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Oy_BLOCK, YipeeBlock.OFFENSIVE_MEGA));
        System.out.println("L_BLOCK-(DEFENSIVE_MINOR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Oy_BLOCK, YipeeBlock.DEFENSIVE_MINOR));
        System.out.println("L_BLOCK-(DEFENSIVE_REGULAR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Oy_BLOCK, YipeeBlock.DEFENSIVE_REGULAR));
        System.out.println("L_BLOCK-(DEFENSIVE_MEGA)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.Oy_BLOCK, YipeeBlock.DEFENSIVE_MEGA));

        System.out.println("EX_BLOCK-(OFFENSIVE_MINOR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.EX_BLOCK, YipeeBlock.OFFENSIVE_MINOR));
        System.out.println("EX_BLOCK-(OFFENSIVE_REGULAR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.EX_BLOCK, YipeeBlock.OFFENSIVE_REGULAR));
        System.out.println("EX_BLOCK-(OFFENSIVE_MEGA)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.EX_BLOCK, YipeeBlock.OFFENSIVE_MEGA));
        System.out.println("EX_BLOCK-(DEFENSIVE_MINOR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.EX_BLOCK, YipeeBlock.DEFENSIVE_MINOR));
        System.out.println("EX_BLOCK-(DEFENSIVE_REGULAR)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.EX_BLOCK, YipeeBlock.DEFENSIVE_REGULAR));
        System.out.println("EX_BLOCK-(DEFENSIVE_MEGA)=" + YipeeBlockEval.setPowerFlag(YipeeBlock.EX_BLOCK, YipeeBlock.DEFENSIVE_MEGA));
    }

    private void testBlockPowers(int block, int power){
        System.out.println("block: " + block);
        System.out.println("power: " + power);
        System.out.println("blockk: " + block);

        final int originalBlock = block;
        int powerLevel;
        String powerText;

        switch (power){
            case YipeeBlock.OFFENSIVE_MINOR:
                powerText = "OFFENSIVE_MINOR: ";
                powerLevel = 1;
                break;
            case YipeeBlock.OFFENSIVE_REGULAR:
                powerText = "OFFENSIVE_REGULAR: ";
                powerLevel = 2;
                break;
            case YipeeBlock.OFFENSIVE_MEGA:
                powerText = "OFFENSIVE_MEGA: ";
                powerLevel = 3;
                break;
            case YipeeBlock.DEFENSIVE_MINOR:
                powerText = "DEFENSIVE_MINOR: ";
                powerLevel = 1;
                break;
            case YipeeBlock.DEFENSIVE_REGULAR:
                powerText = "DEFENSIVE_REGULAR: ";
                powerLevel = 2;
                break;
            case YipeeBlock.DEFENSIVE_MEGA:
            default:
                powerText = "DEFENSIVE_MEGA: ";
                powerLevel = 3;
                break;
        }

        block = YipeeBlockEval.setPowerFlag(block, power);
        System.out.println(powerText + block);
        System.out.println("CELL: " + YipeeBlockEval.getCellFlag(block));
        Assert.assertEquals(originalBlock, YipeeBlockEval.getCellFlag(block));

        //test add power flag
        block = YipeeBlockEval.addPowerBlockFlag(block);
        System.out.println(powerText + block);
        System.out.println("CELL: " + YipeeBlockEval.getCellFlag(block));
        Assert.assertEquals(originalBlock, YipeeBlockEval.getCellFlag(block));
        printBlockLabel(block);

        Assert.assertTrue(YipeeBlockEval.hasPowerBlockFlag(block));
        Assert.assertEquals(powerLevel, YipeeBlockEval.getPowerLevel(block));
        Assert.assertEquals(originalBlock, YipeeBlockEval.getCellFlag(block));
    }

    @Test
    public void YipeeBlock_Y_Test() throws Exception {
        System.out.println("Start YipeeBlock_Y_Test()");
        int y_block = YipeeBlock.Y_BLOCK;
        int block = y_block;
        Assert.assertEquals(YipeeBlock.Y_BLOCK, block);

        System.out.println("Y Block: " + YipeeBlock.Y_BLOCK);
        printBlockLabel(block);

        //OFFENSIVE_MINOR = 3;
        block = YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.OFFENSIVE_MINOR);
        printBlockLabel(block);
        block = YipeeBlockEval.addPowerBlockFlag(block);
        printBlockLabel(block);

        //OFFENSIVE_MINOR = 3;
        testBlockPowers(YipeeBlock.Y_BLOCK, YipeeBlock.OFFENSIVE_MINOR);

        //OFFENSIVE_REGULAR = 5;
        testBlockPowers(YipeeBlock.Y_BLOCK, YipeeBlock.OFFENSIVE_REGULAR);

        //OFFENSIVE_MEGA = 7;
        testBlockPowers(YipeeBlock.Y_BLOCK, YipeeBlock.OFFENSIVE_MEGA);

        //DEFENSIVE_MINOR = 2;
        testBlockPowers(YipeeBlock.Y_BLOCK, YipeeBlock.DEFENSIVE_MINOR);

        //DEFENSIVE_REGULAR = 4;
        testBlockPowers(YipeeBlock.Y_BLOCK, YipeeBlock.DEFENSIVE_REGULAR);

        //DEFENSIVE_MEGA = 6;
        testBlockPowers(YipeeBlock.Y_BLOCK, YipeeBlock.DEFENSIVE_MEGA);

        Assert.assertFalse(YipeeBlockEval.hasBrokenFlag(y_block));
        y_block = YipeeBlockEval.addBrokenFlag(y_block);
        Assert.assertTrue(YipeeBlockEval.hasBrokenFlag(y_block));
        y_block = YipeeBlockEval.removeBrokenFlag(y_block);
        Assert.assertFalse(YipeeBlockEval.hasBrokenFlag(y_block));

        System.out.println("End YipeeBlock_Y_Test()");
    }

    @Test
    public void YipeeBlock_O_Test() throws Exception {
        System.out.println("Start YipeeBlock_O_Test()");
        int o_block = YipeeBlock.A_BLOCK;
        int block = o_block;
        Assert.assertEquals(YipeeBlock.A_BLOCK, block);

        System.out.println("O Block: " + YipeeBlock.A_BLOCK);
        printBlockLabel(block);

        //OFFENSIVE_MINOR = 3;
        testBlockPowers(YipeeBlock.A_BLOCK, YipeeBlock.OFFENSIVE_MINOR);

        //OFFENSIVE_REGULAR = 5;
        testBlockPowers(YipeeBlock.A_BLOCK, YipeeBlock.OFFENSIVE_REGULAR);

        //OFFENSIVE_MEGA = 7;
        testBlockPowers(YipeeBlock.A_BLOCK, YipeeBlock.OFFENSIVE_MEGA);

        //DEFENSIVE_MINOR = 2;
        testBlockPowers(YipeeBlock.A_BLOCK, YipeeBlock.DEFENSIVE_MINOR);

        //DEFENSIVE_REGULAR = 4;
        testBlockPowers(YipeeBlock.A_BLOCK, YipeeBlock.DEFENSIVE_REGULAR);

        //DEFENSIVE_MEGA = 6;
        testBlockPowers(YipeeBlock.A_BLOCK, YipeeBlock.DEFENSIVE_MEGA);

        Assert.assertFalse(YipeeBlockEval.hasBrokenFlag(o_block));
        o_block = YipeeBlockEval.addBrokenFlag(o_block);
        Assert.assertTrue(YipeeBlockEval.hasBrokenFlag(o_block));
        o_block = YipeeBlockEval.removeBrokenFlag(o_block);
        Assert.assertFalse(YipeeBlockEval.hasBrokenFlag(o_block));

        System.out.println("End YipeeBlock_O_Test()");
    }

    @Test
    public void YipeeBlock_K_Test() throws Exception {
        System.out.println("Start YipeeBlock_K_Test()");
        int k_block = YipeeBlock.H_BLOCK;
        int block = k_block;
        Assert.assertEquals(YipeeBlock.H_BLOCK, block);

        System.out.println("K Block: " + YipeeBlock.H_BLOCK);
        //OFFENSIVE_MINOR = 3;
        block = YipeeBlockEval.setPowerFlag(YipeeBlock.H_BLOCK, YipeeBlock.OFFENSIVE_MINOR);
        System.out.println("OFFENSIVE_MINOR: " + block);
        printBlockLabel(block);

        //test add power flag
        block = YipeeBlockEval.addPowerBlockFlag(block);
        System.out.println("OFFENSIVE_MINOR: " + block);
        System.out.println("CELL: " + YipeeBlockEval.getCellFlag(block));
        Assert.assertEquals(YipeeBlock.H_BLOCK, YipeeBlockEval.getCellFlag(block));
        printBlockLabel(block);

        Assert.assertTrue(YipeeBlockEval.hasPowerBlockFlag(block));
        Assert.assertEquals(1, YipeeBlockEval.getPowerLevel(block));
        Assert.assertEquals(YipeeBlock.H_BLOCK, YipeeBlockEval.getCellFlag(block));

        //OFFENSIVE_MINOR = 3;
        testBlockPowers(YipeeBlock.H_BLOCK, YipeeBlock.OFFENSIVE_MINOR);

        //OFFENSIVE_REGULAR = 5;
        testBlockPowers(YipeeBlock.H_BLOCK, YipeeBlock.OFFENSIVE_REGULAR);

        //OFFENSIVE_MEGA = 7;
        testBlockPowers(YipeeBlock.H_BLOCK, YipeeBlock.OFFENSIVE_MEGA);

        //DEFENSIVE_MINOR = 2;
        testBlockPowers(YipeeBlock.H_BLOCK, YipeeBlock.DEFENSIVE_MINOR);

        //DEFENSIVE_REGULAR = 4;
        testBlockPowers(YipeeBlock.H_BLOCK, YipeeBlock.DEFENSIVE_REGULAR);

        //DEFENSIVE_MEGA = 6;
        testBlockPowers(YipeeBlock.H_BLOCK, YipeeBlock.DEFENSIVE_MEGA);;

        Assert.assertFalse(YipeeBlockEval.hasBrokenFlag(k_block));
        k_block = YipeeBlockEval.addBrokenFlag(k_block);
        Assert.assertTrue(YipeeBlockEval.hasBrokenFlag(k_block));
        k_block = YipeeBlockEval.removeBrokenFlag(k_block);
        Assert.assertFalse(YipeeBlockEval.hasBrokenFlag(k_block));
        System.out.println("End YipeeBlock_K_Test()");
    }

    @Test
    public void YipeeBlock_E_Test() throws Exception {
        System.out.println("Start YipeeBlock_E_Test()");
        int e_block = YipeeBlock.Op_BLOCK;
        int block = e_block;
        Assert.assertEquals(YipeeBlock.Op_BLOCK, block);

        System.out.println("E Block: " + YipeeBlock.Op_BLOCK);

        //OFFENSIVE_MINOR = 3;
        testBlockPowers(YipeeBlock.Op_BLOCK, YipeeBlock.OFFENSIVE_MINOR);

        //OFFENSIVE_REGULAR = 5;
        testBlockPowers(YipeeBlock.Op_BLOCK, YipeeBlock.OFFENSIVE_REGULAR);

        //OFFENSIVE_MEGA = 7;
        testBlockPowers(YipeeBlock.Op_BLOCK, YipeeBlock.OFFENSIVE_MEGA);

        //DEFENSIVE_MINOR = 2;
        testBlockPowers(YipeeBlock.Op_BLOCK, YipeeBlock.DEFENSIVE_MINOR);

        //DEFENSIVE_REGULAR = 4;
        testBlockPowers(YipeeBlock.Op_BLOCK, YipeeBlock.DEFENSIVE_REGULAR);

        //DEFENSIVE_MEGA = 6;
        testBlockPowers(YipeeBlock.Op_BLOCK, YipeeBlock.DEFENSIVE_MEGA);

        Assert.assertFalse(YipeeBlockEval.hasBrokenFlag(e_block));
        e_block = YipeeBlockEval.addBrokenFlag(e_block);
        Assert.assertTrue(YipeeBlockEval.hasBrokenFlag(e_block));
        e_block = YipeeBlockEval.removeBrokenFlag(e_block);
        Assert.assertFalse(YipeeBlockEval.hasBrokenFlag(e_block));
        System.out.println("End YipeeBlock_E_Test()");
    }

    @Test
    public void YipeeBlock_L_Test() throws Exception {
        System.out.println("Start YipeeBlock_L_Test()");
        int l_block = YipeeBlock.Oy_BLOCK;
        int block = l_block;
        Assert.assertEquals(YipeeBlock.Oy_BLOCK, block);

        System.out.println("L Block: " + YipeeBlock.Oy_BLOCK);
        //OFFENSIVE_MINOR = 3;
        testBlockPowers(YipeeBlock.Oy_BLOCK, YipeeBlock.OFFENSIVE_MINOR);

        //OFFENSIVE_REGULAR = 5;
        testBlockPowers(YipeeBlock.Oy_BLOCK, YipeeBlock.OFFENSIVE_REGULAR);

        //OFFENSIVE_MEGA = 7;
        testBlockPowers(YipeeBlock.Oy_BLOCK, YipeeBlock.OFFENSIVE_MEGA);

        //DEFENSIVE_MINOR = 2;
        testBlockPowers(YipeeBlock.Oy_BLOCK, YipeeBlock.DEFENSIVE_MINOR);

        //DEFENSIVE_REGULAR = 4;
        testBlockPowers(YipeeBlock.Oy_BLOCK, YipeeBlock.DEFENSIVE_REGULAR);

        //DEFENSIVE_MEGA = 6;
        testBlockPowers(YipeeBlock.Oy_BLOCK, YipeeBlock.DEFENSIVE_MEGA);

        Assert.assertFalse(YipeeBlockEval.hasBrokenFlag(l_block));
        l_block = YipeeBlockEval.addBrokenFlag(l_block);
        Assert.assertTrue(YipeeBlockEval.hasBrokenFlag(l_block));
        l_block = YipeeBlockEval.removeBrokenFlag(l_block);
        Assert.assertFalse(YipeeBlockEval.hasBrokenFlag(l_block));
        System.out.println("End YipeeBlock_L_Test()");
    }

    @Test
    public void YipeeBlock_EX_Test() {
        System.out.println("Start YipeeBlock_EX_Test()");
        int ex_block = YipeeBlock.EX_BLOCK;
        int block = ex_block;
        Assert.assertEquals(YipeeBlock.EX_BLOCK, block);

        System.out.println("EX Block: " + YipeeBlock.EX_BLOCK);
        //OFFENSIVE_MINOR = 3;
        testBlockPowers(YipeeBlock.EX_BLOCK, YipeeBlock.OFFENSIVE_MINOR);

        //OFFENSIVE_REGULAR = 5;
        testBlockPowers(YipeeBlock.EX_BLOCK, YipeeBlock.OFFENSIVE_REGULAR);

        //OFFENSIVE_MEGA = 7;
        testBlockPowers(YipeeBlock.EX_BLOCK, YipeeBlock.OFFENSIVE_MEGA);

        //DEFENSIVE_MINOR = 2;
        testBlockPowers(YipeeBlock.EX_BLOCK, YipeeBlock.DEFENSIVE_MINOR);

        //DEFENSIVE_REGULAR = 4;
        testBlockPowers(YipeeBlock.EX_BLOCK, YipeeBlock.DEFENSIVE_REGULAR);

        //DEFENSIVE_MEGA = 6;
        testBlockPowers(YipeeBlock.EX_BLOCK, YipeeBlock.DEFENSIVE_MEGA);

        Assert.assertFalse(YipeeBlockEval.hasBrokenFlag(ex_block));
        ex_block = YipeeBlockEval.addBrokenFlag(ex_block);
        Assert.assertTrue(YipeeBlockEval.hasBrokenFlag(ex_block));
        ex_block = YipeeBlockEval.removeBrokenFlag(ex_block);
        Assert.assertFalse(YipeeBlockEval.hasBrokenFlag(ex_block));
        System.out.println("End YipeeBlock_EX_Test()");
    }

    @Test(dataProvider = "yokel_blocks_with_ids")
    public void testGetCellFlag(YipeeBlock block, int blockType) throws Exception {
        System.out.println("Start testGetCellFlag()=" + block);
        int cellBlock = blockType;
        int cellFlag = YipeeBlockEval.setValueFlag(cellBlock, blockType);
        System.out.println("cellBlock=" + cellBlock);
        System.out.println("cellFlag=" + cellFlag);
        Assert.assertEquals(cellFlag, YipeeBlockEval.setValueFlag(cellBlock, blockType));
        Assert.assertEquals(cellBlock, YipeeBlockEval.getCellFlag(cellFlag));
        System.out.println("End testGetCellFlag()");
    }
/*
    @Test
    public void testSetValueFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testHasAddedByYahooFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testAddArtificialFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testHasBrokenFlags() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testAddBrokenFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testRemoveBrokenFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }
*/
    @Test
    public void testHasPartnerBreakFlags() {
        System.out.println("Start testHasPartnerBreakFlags()");
        int block;
        int actual;

        //Normal Blocks
        block = YipeeBlock.Y_BLOCK;
        actual = YipeeBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));

        //block = YipeeBlockEval.addPowerBlockFlag(block, YipeeBlockEval.OFFENSIVE_MINOR);

        block = YipeeBlock.A_BLOCK;
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));

        block = YipeeBlock.H_BLOCK;
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));

        block = YipeeBlock.Op_BLOCK;
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));

        block = YipeeBlock.Oy_BLOCK;
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));

        block = YipeeBlock.EX_BLOCK;
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));

        //Offensive Blocks
        block = YipeeBlockEval.addPowerBlockFlag(YipeeBlock.Y_BLOCK);
        actual = YipeeBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));

        block = YipeeBlock.A_BLOCK;
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));

        block = YipeeBlock.H_BLOCK;
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));

        block = YipeeBlock.Op_BLOCK;
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));

        block = YipeeBlock.Oy_BLOCK;
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));

        block = YipeeBlock.EX_BLOCK;
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.addPartnerBreakFlag(block);
        Assert.assertTrue(YipeeBlockEval.hasPartnerBreakFlag(actual));
        actual = YipeeBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YipeeBlockEval.hasPartnerBreakFlag(actual));
        System.out.println("End testHasPartnerBreakFlags()");
    }
/*
    @Test
    public void testAddPartnerBreakFlag() throws Exception {
        System.out.println("Start testAddPartnerBreakFlag()");
        System.out.println("End testAddPartnerBreakFlag()");
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testRemovePartnerBreakFlag() throws Exception {
        System.out.println("Start testRemovePartnerBreakFlag()");
        System.out.println("End testRemovePartnerBreakFlag()");
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testGetID() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testGetIDFlag() throws Exception {
        System.out.println("Start testPartnerBreakFlag()");
        System.out.println("End testPartnerBreakFlag()");
        throw new Exception("Test not implemented.");
    }
*/
    @Test
    public void testSetIDFlag() throws Exception {
        System.out.println("Start testSetIDFlag()");
        int v0 = YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.OFFENSIVE_MEGA));
        System.out.println("powwah-V0-" + v0);
        int v1= YipeeBlockEval.setIDFlag(v0, 1);
        System.out.println("-V1-" + v1);
        int v2 = YipeeBlockEval.getID(v1);
        System.out.println("-V2-" + v2);
        int v3 = YipeeBlockEval.setValueFlag(v2, v2);
        System.out.println("-V3-" + v3);
        System.out.println("ttm-" + ((v1 & ~0x7f000) | 1 >> 12));

        int v10 = YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, YipeeBlock.DEFENSIVE_MINOR));
        System.out.println("powwah-V0-" + v10);
        int v11= YipeeBlockEval.setIDFlag(v10, 2);
        System.out.println("-V1-" + v11);
        int v12 = YipeeBlockEval.getID(v11);
        System.out.println("-V2-" + v12);
        int v13 = YipeeBlockEval.setValueFlag(v11, v12);
        System.out.println("-V3-" + v13);
        System.out.println("End testSetIDFlag()");
    }
/*
    @Test
    public void testGetPowerFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }
*/
    @Test
    public void testSetPowerFlag() throws Exception {
        System.out.println("power 8" + YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, 8));
        System.out.println("power 8" + YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, 1));
        int block = YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.Y_BLOCK, 4));
        Assert.assertEquals(YipeeBlock.Y_BLOCK, YipeeBlockEval.getCellFlag(block));
        System.out.println("power 8" + YipeeBlockEval.isOffensive(block));
        System.out.println("power level = " + YipeeBlockEval.getPowerLevel(block));

    }
/*
    @Test
    public void testHasSpecialFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testAddSpecialFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }
*/
    @Test
    public void testHasPowerBlockFlag() throws Exception {
        int block = YipeeBlock.Y_BLOCK;
        int actual = YipeeBlockEval.addPowerBlockFlag(block);

        Assert.assertTrue(YipeeBlockEval.hasPowerBlockFlag(actual));
        Assert.assertFalse(YipeeBlockEval.hasPowerBlockFlag(block));
    }

    @Test
    public void testAddPowerBlockFlag() throws Exception {
        System.out.println("Start testAddPowerBlockFlag()");

        int block;
        int actual;

        //Normal Blocks
        block = YipeeBlock.Y_BLOCK;
        actual = YipeeBlockEval.addPowerBlockFlag(block);

        System.out.println("BLOCK: " + block);
        System.out.println("POWER_BLOCK: " + actual);
        System.out.println("HAS Block Flag: " + YipeeBlockEval.hasPowerBlockFlag(actual));
        System.out.println("BLOCK: " + YipeeBlock.A_BLOCK);
        System.out.println("POWER_BLOCK: " + YipeeBlockEval.addPowerBlockFlag(YipeeBlock.A_BLOCK));

        System.out.println("End testAddPowerBlockFlag()");

        Assert.assertTrue(YipeeBlockEval.hasPowerBlockFlag(actual));
        Assert.assertFalse(YipeeBlockEval.hasPowerBlockFlag(block));
    }
/*
    @Test
    public void testIsOffensive() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testGetPowerLevel() throws Exception {
        throw new Exception("Test not implemented.");
    }
*/
    @DataProvider(name = "yokel_objects")
    public Object[][] provideYokelObjects() {
        YipeeBlock yokelBlockY = new YipeeBlock(0,0,0);
        YipeeBlock yokelBlockA = new YipeeBlock(0,0,1);
        YipeeBlock yokelBlockH = new YipeeBlock(0,0,2);
        YipeeBlock yokelBlockOp = new YipeeBlock(0,0,3);
        YipeeBlock yokelBlockOy = new YipeeBlock(0,0,4);
        YipeeBlock yokelBlockBsh = new YipeeBlock(0,0,5);
        YipeeBlock yokelBlockClear = new YipeeBlock(0,0,6);
        YipeeBlock yokelBlockStone = new YipeeBlock(0,0,7);
        YipeeBlock yokelBlockMedusa = new YipeeBlock(0,0,9);

        return new Object[][]{
                {yokelBlockY, yokelBlockY.getBlockType()},
                {yokelBlockA, yokelBlockA.getBlockType()},
                {yokelBlockH, yokelBlockH.getBlockType()},
                {yokelBlockOp, yokelBlockOp.getBlockType()},
                {yokelBlockOy, yokelBlockOy.getBlockType()},
                {yokelBlockBsh, yokelBlockBsh.getBlockType()},
                {yokelBlockClear, yokelBlockClear.getBlockType()},
                {yokelBlockStone, yokelBlockStone.getBlockType()},
                {yokelBlockMedusa, yokelBlockMedusa.getBlockType()}
        };
    }
/*
    @Test
    public void testGetNormalLabel() throws Exception {
        System.out.println("Start testGetNormalLabel()");
        System.out.println("End testGetNormalLabel()");
        throw new Exception("Test not implemented.");
    }
*/
    @DataProvider(name = "yokel_blocks_with_ids")
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