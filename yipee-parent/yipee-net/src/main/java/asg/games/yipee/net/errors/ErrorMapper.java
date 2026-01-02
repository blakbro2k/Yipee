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
 * Maps internal server-side exceptions to externally visible {@link ErrorCode}s.
 * <p>
 * This class provides a single, centralized translation layer between
 * Java exceptions thrown during request handling and the protocol-level
 * error codes sent back to clients.
 * </p>
 *
 * <h2>Design Goals</h2>
 * <ul>
 *   <li>Prevent leaking internal exception details to clients</li>
 *   <li>Ensure consistent error semantics across transports (WebSocket, Kryo, REST)</li>
 *   <li>Decouple gameplay/auth/session logic from wire-level error handling</li>
 * </ul>
 *
 * <h3>Usage</h3>
 * Typically invoked by packet handlers or transport layers when an exception
 * escapes normal request processing:
 *
 * <pre>{@code
 * catch (Throwable t) {
 *     ErrorCode code = ErrorMapper.toCode(t);
 *     return new ErrorResponse(code, t.getMessage());
 * }
 * }</pre>
 *
 * <h3>Error Categories</h3>
 * <ul>
 *   <li>{@link YipeeAuthException} → {@link ErrorCode#UNAUTHORIZED}</li>
 *   <li>{@link YipeeSessionException} → {@link ErrorCode#INVALID_SESSION}</li>
 *   <li>{@link YipeeSyncException} → {@link ErrorCode#OUT_OF_SYNC}</li>
 *   <li>{@link YipeeActionException} → {@link ErrorCode#ILLEGAL_ACTION}</li>
 *   <li>{@link YipeeBadRequestException} → {@link ErrorCode#BAD_REQUEST}</li>
 *   <li>All other exceptions → {@link ErrorCode#INTERNAL_ERROR}</li>
 * </ul>
 *
 * <h3>MVP / Alpha Notes</h3>
 * <ul>
 *   <li>The mapping is intentionally coarse-grained for alpha</li>
 *   <li>Future versions may introduce finer-grained error codes
 *       or structured error metadata</li>
 * </ul>
 *
 * <h3>Extensibility</h3>
 * When introducing new exception types that should be exposed to clients,
 * add an explicit mapping here rather than relying on
 * {@link ErrorCode#INTERNAL_ERROR}.
 */
public final class ErrorMapper {

    private ErrorMapper() {
        // Utility class; prevent instantiation
    }

    /**
     * Converts a thrown {@link Throwable} into a protocol-level {@link ErrorCode}.
     *
     * @param t the exception to map
     * @return the corresponding {@link ErrorCode} suitable for client responses
     */
    public static ErrorCode toCode(Throwable t) {
        if (t instanceof YipeeAuthException) {
            return ErrorCode.UNAUTHORIZED;
        }
        if (t instanceof YipeeSessionException) {
            return ErrorCode.INVALID_SESSION;
        }
        if (t instanceof YipeeSyncException) {
            return ErrorCode.OUT_OF_SYNC;
        }
        if (t instanceof YipeeActionException) {
            return ErrorCode.ILLEGAL_ACTION;
        }
        if (t instanceof YipeeBadRequestException) {
            return ErrorCode.BAD_REQUEST;
        }

        return ErrorCode.INTERNAL_ERROR;
    }
}
