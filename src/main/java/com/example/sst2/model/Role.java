package com.example.sst2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

import static com.example.sst2.model.Permission.*;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN(Set.of(UPDATE, DELETE, READ, WRITE)),
    WORKER(Set.of(UPDATE, READ, WRITE));

    private final Set<Permission> permissions;
    }
