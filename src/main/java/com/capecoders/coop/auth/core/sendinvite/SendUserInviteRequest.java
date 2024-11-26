package com.capecoders.coop.auth.core.sendinvite;

public record SendUserInviteRequest(String email, String body, String subject, String inviteLink) {
}
