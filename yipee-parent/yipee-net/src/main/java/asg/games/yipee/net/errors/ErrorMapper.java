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

public class ErrorMapper {
    private ErrorMapper() {
    }

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
