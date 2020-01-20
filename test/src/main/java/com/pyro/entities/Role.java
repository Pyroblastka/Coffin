package com.pyro.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Role extends AbstractEntity {
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
