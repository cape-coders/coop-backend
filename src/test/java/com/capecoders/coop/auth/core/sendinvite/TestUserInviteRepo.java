package com.capecoders.coop.auth.core.sendinvite;

import com.capecoders.coop.chat.ChatUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestUserInviteRepo implements UserInviteRepo {
    private List<UserInvite> data = new ArrayList<>(){};
    @Override
    public UserInvite getByEmail(String email) {
        return data.stream().filter(x -> x.getEmail().equals(email)).findFirst().orElse(null);
    }

    @Override
    public UserInvite save(UserInvite userInvite) {
        if (userInvite.getId() == null) {
            userInvite.setId((long) data.size() + 1);
        }
        data.add(userInvite);
        return userInvite;
    }

    public Boolean emailExistsOnlyOnce(String email) {
        return (long) data.stream().filter(x -> x.getEmail().equals(email)).toList().size() == 1;
    }
}
