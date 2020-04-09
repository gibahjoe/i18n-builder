package com.devappliance.processor.src;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 */
public class Pojo {
    private java.lang.String name;

    private java.lang.String email;

    public Pojo(java.lang.String name, java.lang.String email) {
        this.name = name;
        this.email = email;
    }

    public java.lang.String getName() {
        return name;
    }

    public com.devappliance.processor.src.Pojo setName(java.lang.String name) {
        if (org.apache.commons.lang3.StringUtils.isBlank(name)) {
            throw new com.devappliance.processor.src.ErrorException("TTTTTT");
        }
        this.name = name;
        return this;
    }

    public java.lang.String getEmail() {
        java.lang.String name = "NAMEMM";
        if (org.apache.commons.lang3.StringUtils.isBlank(email)) {
            throw new com.devappliance.processor.src.ErrorException(name);
        }
        return email;
    }

    public com.devappliance.processor.src.Pojo setEmail(java.lang.String email) {
        this.email = email;
        return this;
    }
}
