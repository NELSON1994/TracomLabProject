
package com.tracom.atlas.wrapper;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

public class ListGraphWrapper {

    @Expose
    private String name;
    @Expose
    private List<GraphWrapper> series;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GraphWrapper> getSeries() {
        return series;
    }

    public void setSeries(List<GraphWrapper> series) {
        this.series = series;
    }

}
