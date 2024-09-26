package net.weesli.rfriend.modal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
@Getter@Setter
public class UserInvite {

    private UUID receiver;
    private List<UUID> senders;

    public UserInvite(UUID receiver, List<UUID> senders) {
        this.senders = senders;
        this.receiver = receiver;
    }
}
