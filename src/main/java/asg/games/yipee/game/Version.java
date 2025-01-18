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
package asg.games.yipee.game;

public class Version {
    private static final String release = "1";
    private static final String major = "10";
    private static final String minor = "1";
    private static final String patch = "0";
    private static final String releaseSeparator = ".";
    private static final String patchSeparator = "p";

    public static String printVersion() {
        return release + releaseSeparator + major + releaseSeparator + minor + patchSeparator + patch;
    }
}
