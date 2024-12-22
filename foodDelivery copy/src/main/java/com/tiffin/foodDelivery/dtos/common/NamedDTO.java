package com.tiffin.foodDelivery.dtos.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class NamedDTO extends BaseDTO {

    @Length(max = 151, message = "Name must be less than or equal to 150 characters.")
    private String name;

}
