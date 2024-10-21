package com.capecoders.coop.chat.sendmessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequest {
    private Long senderId;
    private String message;
    private Long chatId;
}
