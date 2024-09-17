package com.capecoders.coop.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAddedEvent {

    private final Long userId;
    private final String userName;
}
