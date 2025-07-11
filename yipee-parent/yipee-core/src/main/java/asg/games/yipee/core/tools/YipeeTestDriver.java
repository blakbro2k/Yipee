/**
 * Copyright 2024 See AUTHORS file.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asg.games.yipee.core.tools;

import asg.games.yipee.core.game.YipeeGameBoard;

import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class YipeeTestDriver {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ExecutorService inputExecutor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws Exception {
        long seed = 1;//System.currentTimeMillis();
        YipeeGameBoard player = new YipeeGameBoard(seed);
        YipeeGameBoard partner = new YipeeGameBoard(seed);
        player.setPartnerBoard(partner, true);
        partner.setPartnerBoard(player, false);
        player.begin();

        boolean running = true;

        while (running) {
            player.update(1 / 60f); // simulate frame
            clearScreen();
            YipeePrinter.printYipeeBoard(player);

            System.out.print("Input (l=left, r=right, d=down, s=stop, c=cycle, q=quit): ");
            String input = waitForInput(12); // wait 12 seconds for input

            if (input != null) {
                switch (input.toLowerCase()) {
                    case "l":
                        player.movePieceLeft();
                        break;
                    case "r":
                        player.movePieceRight();
                        break;
                    case "d":
                        player.startMoveDown();
                        break;
                    case "s":
                        player.stopMoveDown();
                        break;
                    case "c":
                        player.cycleDown();
                        break;
                    case "q":
                        running = false;
                        break;
                    default:
                        System.out.println("Unknown command.");
                        break;
                }
            } else {
                System.out.println("(No input received, continuing game...)");
            }

            Thread.sleep(100); // throttle to ~10 FPS
        }

        player.end();
        partner.end();
        inputExecutor.shutdownNow();
        System.out.println("Game test ended.");
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static String waitForInput(int timeoutSeconds) {
        Callable<String> inputTask = () -> {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            }
            return null;
        };

        Future<String> future = inputExecutor.submit(inputTask);
        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
