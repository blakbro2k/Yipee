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
package asg.games.yipee.net.errors;

/**
 * Base class for all Yipee game-related runtime exceptions.
 * <p>
 * These exceptions represent expected failure conditions within the
 * game, session, or networking domain and are intended to be thrown
 * from request handlers or game logic.
 * <p>
 * All {@code YipeeException} instances are intercepted at the networking
 * boundary and translated into protocol-level {@code ErrorResponse}
 * objects using {@link ErrorMapper}.
 * <p>
 * This class is intentionally unchecked to avoid polluting game and
 * packet-handling code with excessive catch blocks while still allowing
 * deterministic error reporting to clients.
 */
public class YipeeException extends RuntimeException {

    /**
     * Constructs a new {@code YipeeException} with a descriptive message.
     *
     * @param message human-readable explanation of the failure
     */
    public YipeeException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code YipeeException} with a descriptive message
     * and an underlying cause.
     *
     * @param message human-readable explanation of the failure
     * @param cause   the underlying cause of this exception
     */
    public YipeeException(String message, Throwable cause) {
        super(message, cause);
    }
}
