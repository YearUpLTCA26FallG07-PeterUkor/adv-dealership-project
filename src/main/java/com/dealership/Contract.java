package com.dealership;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;

public abstract class Contract {
    private LocalDate contractDate;
    private String contractCustomerName;
    private String contractCustomerEmail;
    private Vehicle contractVehicleSold;

    public Contract(LocalDate contractDate, String contractCustomerName, String contractCustomerEmail,
                    Vehicle contractVehicleSold) {
        this.contractDate = contractDate;
        this.contractCustomerName = contractCustomerName;
        this.contractCustomerEmail = contractCustomerEmail;
        this.contractVehicleSold = contractVehicleSold;
    }

    public LocalDate getDate() {
        return contractDate;
    }

    public void setDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    public String getCustomerName() {
        return contractCustomerName;
    }

    public void setCustomerName(String contractCustomerName) {
        this.contractCustomerName = contractCustomerName;
    }

    public String getCustomerEmail() {
        return contractCustomerEmail;
    }

    public void setCustomerEmail(String contractCustomerEmail) {
        this.contractCustomerEmail = contractCustomerEmail;
    }

    public Vehicle getVehicleSold() {
        return contractVehicleSold;
    }

    public void setVehicleSold(Vehicle contractVehicleSold) {
        this.contractVehicleSold = contractVehicleSold;
    }

    // total price of the contract, each contract type calculates its own
    public abstract BigDecimal getTotalPrice();

    // monthly payment of the contract, each contract type calculates its own
    public abstract BigDecimal getMonthlyPayment();

    // monthly loan payment = loanAmount * r * (1 + r)^n / ((1 + r)^n - 1)
    // r is the monthly rate and n is the number of months
    protected BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal annualRate, int numberOfMonths) {
        BigDecimal monthlyRate = annualRate.divide(new BigDecimal("12"), MathContext.DECIMAL64);
        BigDecimal rateGrowth = BigDecimal.ONE.add(monthlyRate).pow(numberOfMonths, MathContext.DECIMAL64);

        BigDecimal monthlyPayment = loanAmount.multiply(monthlyRate).multiply(rateGrowth)
                .divide(rateGrowth.subtract(BigDecimal.ONE), MathContext.DECIMAL64);

        return monthlyPayment.setScale(2, RoundingMode.HALF_UP);
    }
}
