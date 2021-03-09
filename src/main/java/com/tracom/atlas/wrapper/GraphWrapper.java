
package com.tracom.atlas.wrapper;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

public class GraphWrapper {

    @Expose
    private Extra extra;
    @Expose
    private String name;
    @Expose
    private Long value;

    public Extra getExtra() {
        return extra;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

}
