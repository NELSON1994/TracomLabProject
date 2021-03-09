
package com.tracom.atlas.wrapper;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

public class RepairWrapper {

    @Expose
    private List<Long> ids;
    @Expose
    private RepairModel repairModel;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public RepairModel getRepairModel() {
        return repairModel;
    }

    public void setRepairModel(RepairModel repairModel) {
        this.repairModel = repairModel;
    }

}
