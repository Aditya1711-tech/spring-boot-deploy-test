package com.tiffin.foodDelivery.entities.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NameEntity extends BaseEntity {

    private String name;

    public NameEntity(String name) {
        this();
        setName(name);
    }

    public NameEntity(String name, String id) {
        this(name);
        setId(id);
    }
}
