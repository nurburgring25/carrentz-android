package dev.burikk.carrentz.api.merchant.endpoint.rent.item;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MerchantRentItem implements Serializable {
    private Long id;
    private MerchantRentUserItem user;
    private MerchantRentVehicleItem vehicle;
    private MerchantRentStoreItem store;
    private String number;
    private String status;
    private LocalDate start;
    private LocalDate until;
    private Integer duration;
    private BigDecimal costPerDay;
    private BigDecimal total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MerchantRentUserItem getUser() {
        return user;
    }

    public void setUser(MerchantRentUserItem user) {
        this.user = user;
    }

    public MerchantRentVehicleItem getVehicle() {
        return vehicle;
    }

    public void setVehicle(MerchantRentVehicleItem vehicle) {
        this.vehicle = vehicle;
    }

    public MerchantRentStoreItem getStore() {
        return store;
    }

    public void setStore(MerchantRentStoreItem store) {
        this.store = store;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getUntil() {
        return until;
    }

    public void setUntil(LocalDate until) {
        this.until = until;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public BigDecimal getCostPerDay() {
        return costPerDay;
    }

    public void setCostPerDay(BigDecimal costPerDay) {
        this.costPerDay = costPerDay;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}