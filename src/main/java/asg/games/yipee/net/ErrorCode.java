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
package asg.games.yipee.net;

/**
 * Enumerates standard error codes used in {@link ErrorResponse} messages
 * sent from the server to the client.
 * <p>
 * Each code represents a specific type of failure that the client can handle or display.
 */
public enum ErrorCode {

    // === Authentication and Session Errors ===
    AUTH_FAILED,
    INVALID_SESSION,
    SESSION_EXPIRED,
    UNAUTHORIZED,

    // === Game Sync & Logic Errors ===
    INVALID_TICK,
    OUT_OF_SYNC,
    ACTION_REJECTED,
    DUPLICATE_ACTION,
    ILLEGAL_ACTION,

    // === Connection and Compatibility Issues ===
    CONNECTION_TIMEOUT,
    SERVER_OVERLOADED,
    CLIENT_VERSION_MISMATCH,

    // === General or System Errors ===
    BAD_REQUEST,
    INTERNAL_ERROR,
    UNSUPPORTED_OPERATION
}
