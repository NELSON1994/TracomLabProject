package com.tracom.atlas.service;

import com.tracom.atlas.config.Constants;
import com.tracom.atlas.repository.CustomersRepository;
import com.tracom.atlas.repository.DeliveryRepository;
import com.tracom.atlas.repository.DevicesRepository;
import com.tracom.atlas.repository.RepairRepository;
import com.tracom.atlas.wrapper.DashBoardWrapper;
import com.tracom.atlas.wrapper.Extra;
import com.tracom.atlas.wrapper.GraphWrapper;
import com.tracom.atlas.wrapper.ListGraphWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DashboardService {

    private final DevicesRepository devicesRepository;
    private final RepairRepository repairRepository;
    private final DeliveryRepository deliveryRepository;
    private final CustomersRepository customersRepository;

    public DashboardService(DevicesRepository devicesRepository, RepairRepository repairRepository, DeliveryRepository deliveryRepository, CustomersRepository customersRepository) {
        this.devicesRepository = devicesRepository;
        this.repairRepository = repairRepository;
        this.deliveryRepository = deliveryRepository;
        this.customersRepository = customersRepository;
    }

    public List<DashBoardWrapper> getRepairCars() {
        List<DashBoardWrapper> wrapperList = new ArrayList<>();
        wrapperList.add(new DashBoardWrapper("Onboarderd Devices","",getTotalOnboaredDevices()));
        wrapperList.add(new DashBoardWrapper("Received Devices","",getTotalReceivedDevices()));
        wrapperList.add(new DashBoardWrapper("Shipped Devices","",getTotalShippedDevices()));
        wrapperList.add(new DashBoardWrapper("Repaired Devices","",getTotalRepairedDevices()));
        wrapperList.add(new DashBoardWrapper("Delivered Devices","",getTotalDeliveredDevices()));

        return wrapperList;
    }

    public List<GraphWrapper> getRepairLevels() {
        List<GraphWrapper> graphWrapperList = new ArrayList<>();
        List<String> levels = Arrays.asList(Constants.LEVEL_1,Constants.LEVEL_2,Constants.LEVEL_3,Constants.LEVEL_4,Constants.TRACOM_INGENICO);
        levels.parallelStream().forEach(level -> {
            GraphWrapper graphWrapper = new GraphWrapper();
            if (level.equalsIgnoreCase(Constants.TRACOM_INGENICO)) {
                graphWrapper.setName(level);
                graphWrapper.setValue(repairRepository.countAllByLevelsAndRepairCentre(level,Constants.TRACOM_INGENICO));
                Extra extra = new Extra();
                extra.setCode(level);
                graphWrapper.setExtra(extra);
            } else {
                graphWrapper.setName(level);
                graphWrapper.setValue(repairRepository.countAllByLevels(level));
                Extra extra = new Extra();
                extra.setCode(level);
                graphWrapper.setExtra(extra);
            }
            graphWrapperList.add(graphWrapper);
        });

        return graphWrapperList;
    }

    public List<ListGraphWrapper> getRepairPerCustomers() {
        List<ListGraphWrapper> graphWrapperList = new ArrayList<>();
        List<String> levels = Arrays.asList(Constants.LEVEL_1,Constants.LEVEL_2,Constants.LEVEL_3,Constants.LEVEL_4,Constants.TRACOM_INGENICO);
        customersRepository.findAll().parallelStream().forEach(customers -> {
            List<GraphWrapper> graphWrappers = new ArrayList<>();
            ListGraphWrapper graphWrapper = new ListGraphWrapper();
            graphWrapper.setName(customers.getName());
            levels.parallelStream().forEach(level -> {
                GraphWrapper wrapper = new GraphWrapper();

                if (level.equalsIgnoreCase(Constants.TRACOM_INGENICO)) {
                    wrapper.setName(level);
                    wrapper.setValue(repairRepository.countAllByLevelsAndRepairCentreAndCustomers(level,Constants.TRACOM_INGENICO,customers.getName()));
                    Extra extra = new Extra();
                    extra.setCode(level);
                    wrapper.setExtra(extra);
                } else {
                    wrapper.setName(level);
                    wrapper.setValue(repairRepository.countAllByCustomersAndLevels(customers.getName(),level));
                    Extra extra = new Extra();
                    extra.setCode(level);
                    wrapper.setExtra(extra);
                }

                graphWrappers.add(wrapper);
                graphWrapper.setSeries(graphWrappers);
            });
            graphWrapperList.add(graphWrapper);
        });
        return graphWrapperList;
    }

    public Long getTotalOnboaredDevices(){
        return devicesRepository.count();
    }

    public Long getTotalReceivedDevices(){
        return repairRepository.count();
    }

    public Long getTotalShippedDevices(){
        return repairRepository.countAllByRepairCentre(Constants.TRACOM_INGENICO);
    }

    public Long getTotalRepairedDevices(){
        return repairRepository.countAllByRepairStatus(Constants.STATUS_REPAIRED);
    }

    public Long getTotalDeliveredDevices(){
        return deliveryRepository.count();
    }
}
