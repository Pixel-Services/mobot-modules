package com.pixelservices.cache;

import net.dv8tion.jda.api.entities.Role;

public class RoleCache {
    private Role supporter;

    public RoleCache(Role supporter){
        this.supporter = supporter;
    }

    public Role getSupporter() {
        return supporter;
    }

    public void setSupporter(Role supporter) {
        this.supporter = supporter;
    }
}
