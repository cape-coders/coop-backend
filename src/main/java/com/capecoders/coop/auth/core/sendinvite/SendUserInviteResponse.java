package com.capecoders.coop.auth.core.sendinvite;

import java.time.OffsetDateTime;

public record SendUserInviteResponse(
    String email,
    String code,
    OffsetDateTime emailSentTime
) {
}
