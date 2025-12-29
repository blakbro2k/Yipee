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

import asg.games.yipee.net.packets.ErrorResponse;

/**
 * Standardized error codes returned by the server in {@link ErrorResponse}
 * packets to indicate the reason a request failed.
 *
 * <p>This enum is intentionally compact so that it can be serialized efficiently
 * over KryoNet, GWT/WebSocket clients, and any future network transport.
 *
 * <p>Client implementations should treat these as stable API surface; new values
 * may be added, but existing meanings should not be changed.
 */
public enum ErrorCode {

    // ========================================================================
    // Authentication & Session Errors (00xx)
    // ========================================================================

    /**
     * Authentication failed (bad authToken, bad clientId, signature mismatch).
     */
    AUTH_FAILED,

    /** The provided sessionKey is invalid for this connection. */
    INVALID_SESSION,

    /** The sessionKey has expired and must be renewed via handshake. */
    SESSION_EXPIRED,

    /** The player is authenticated but not authorized for the requested action. */
    UNAUTHORIZED,


    // ========================================================================
    // Game Sync & Logic Errors (10xx)
    // ========================================================================

    /** Client actions arrived with an invalid or unexpected tick index. */
    INVALID_TICK,

    /** Client is too far out-of-sync with the authoritative game state. */
    OUT_OF_SYNC,

    /** Action was rejected due to game rules or illegal state transition. */
    ACTION_REJECTED,

    /** Duplicate PlayerAction detected for the same tick. */
    DUPLICATE_ACTION,

    /** Action is not allowed for the player, board, or phase. */
    ILLEGAL_ACTION,


    // ========================================================================
    // Connection, Compatibility, and Capacity Errors (20xx)
    // ========================================================================

    /** Connection attempt exceeded timeout threshold. */
    CONNECTION_TIMEOUT,

    /** Server cannot process the request due to load or resource limits. */
    SERVER_OVERLOADED,

    /** Client version is incompatible with required protocol version. */
    CLIENT_VERSION_MISMATCH,


    // ========================================================================
    // General Request / Processing Errors (30xx)
    // ========================================================================

    /** Client sent incomplete, invalid, or malformed data. */
    BAD_REQUEST,

    /** Unexpected exception or unhandled error occurred during processing. */
    INTERNAL_ERROR,

    /** The requested operation is valid but not supported by this server build. */
    UNSUPPORTED_OPERATION
}
