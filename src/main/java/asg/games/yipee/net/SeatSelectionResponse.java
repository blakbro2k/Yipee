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

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response sent by the server after processing a {@link SeatSelectionRequest}.
 *
 * <p>Indicates whether the client's seat selection was accepted or rejected,
 * and provides context via a human-readable message.</p>
 *
 * <p>Failure may occur due to:</p>
 * <ul>
 *   <li>Seat already occupied</li>
 *   <li>Seat index out of range</li>
 *   <li>Table ID not found</li>
 *   <li>Invalid session or permissions</li>
 * </ul>
 *
 * <p><b>Direction:</b> Server → Client</p>
 *
 * @see SeatSelectionRequest
 */
@Data
@NoArgsConstructor
public class SeatSelectionResponse extends AbstractServerResponse {

    /**
     * Indicates whether the seat selection was accepted.
     */
    private boolean accepted;

    /**
     * A descriptive message explaining the result of the request.
     */
    private String message;

    /**
     * The ID of the table for which this response applies.
     */
    private String tableId;

    /**
     * The index of the seat that was requested.
     */
    private int seatIndex;
}
