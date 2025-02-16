/**
 * Copyright 2024 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asg.games.yippe.objects;

import asg.games.yipee.game.YipeeGameBoard;
import asg.games.yipee.net.ConnectionRequest;
import asg.games.yipee.net.PacketRegistrar;
import asg.games.yipee.objects.YipeePlayer;
import asg.games.yipee.tools.RandomUtil;
import asg.games.yipee.tools.TimeUtils;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class TestUtil {
    AtomicInteger atomicId = new AtomicInteger(0);
    Server server = null;
    Client client = null;

    @AfterTest
    public void shutDown() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

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

    @Test()
    public void testClientServer() throws ParserConfigurationException, IOException, SAXException {
        server = new Server();
        server.start(); // Start the server
        PacketRegistrar.reloadConfiguration("U:\\YipeeWebServer\\src\\main\\resources\\packets.xml");
        // Register all necessary packet classes for serialization
        PacketRegistrar.registerPackets(server.getKryo());

        server.bind(8081, 55005); // Bind the server to the given ports

        client = new Client();
        client.start();
        PacketRegistrar.registerPackets(client.getKryo());

        client.connect(5000, "localhost", 8081, 55005);
        client.updateReturnTripTime(); // Sync UDP latency

        ConnectionRequest request = new ConnectionRequest();
        request.setClientId("1");
        request.setSessionId("noew");
        request.setPlayer(new YipeePlayer("blakbro"));
        request.setTimeStamp(TimeUtils.millis());
        client.sendTCP(request);
    }
}