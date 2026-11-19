package com.dealership;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class LeaseContract extends Contract {
    private BigDecimal leaseContractExpectedEndingValue;
    private BigDecimal leaseContractLeaseFee;

    public LeaseContract(LocalDate contractDate, String contractCustomerName, String contractCustomerEmail,
                         Vehicle contractVehicleSold) {
        super(contractDate, contractCustomerName, contractCustomerEmail, contractVehicleSold);

        BigDecimal vehiclePrice = contractVehicleSold.getPrice();

        // ending value is 50% of the price and the lease fee is 7% of the price
        this.leaseContractExpectedEndingValue = vehiclePrice.multiply(new BigDecimal("0.50"))
                .setScale(2, RoundingMode.HALF_UP);
        this.leaseContractLeaseFee = vehiclePrice.multiply(new BigDecimal("0.07"))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getExpectedEndingValue() {
        return leaseContractExpectedEndingValue;
    }

    public void setExpectedEndingValue(BigDecimal leaseContractExpectedEndingValue) {
        this.leaseContractExpectedEndingValue = leaseContractExpectedEndingValue;
    }

    public BigDecimal getLeaseFee() {
        return leaseContractLeaseFee;
    }

    public void setLeaseFee(BigDecimal leaseContractLeaseFee) {
        this.leaseContractLeaseFee = leaseContractLeaseFee;
    }

    // expected ending value + lease fee
    @Override
    public BigDecimal getTotalPrice() {
        return leaseContractExpectedEndingValue.add(leaseContractLeaseFee).setScale(2, RoundingMode.HALF_UP);
    }

    // every lease is financed at 4% for 36 months
    @Override
    public BigDecimal getMonthlyPayment() {
        return calculateMonthlyPayment(getTotalPrice(), new BigDecimal("0.04"), 36);
    }
}
