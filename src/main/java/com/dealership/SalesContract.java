package com.dealership;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class SalesContract extends Contract {
    private BigDecimal salesContractSalesTax;
    private BigDecimal salesContractRecordingFee;
    private BigDecimal salesContractProcessingFee;
    private boolean salesContractFinance;

    // fees and rates for every sale
    private static final BigDecimal SALES_TAX_RATE = new BigDecimal("0.05");
    private static final BigDecimal RECORDING_FEE = new BigDecimal("100.00");
    private static final BigDecimal LOW_PROCESSING_FEE = new BigDecimal("295.00");
    private static final BigDecimal HIGH_PROCESSING_FEE = new BigDecimal("495.00");
    private static final BigDecimal TEN_THOUSAND = new BigDecimal("10000");

    public SalesContract(LocalDate contractDate, String contractCustomerName, String contractCustomerEmail,
                         Vehicle contractVehicleSold, boolean salesContractFinance) {
        super(contractDate, contractCustomerName, contractCustomerEmail, contractVehicleSold);
        this.salesContractFinance = salesContractFinance;

        BigDecimal vehiclePrice = contractVehicleSold.getPrice();

        // 5% sales tax and a $100 recording fee on every sale
        this.salesContractSalesTax = vehiclePrice.multiply(SALES_TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        this.salesContractRecordingFee = RECORDING_FEE;

        // $295 processing fee under $10,000, $495 for everything else
        if (vehiclePrice.compareTo(TEN_THOUSAND) < 0) {
            this.salesContractProcessingFee = LOW_PROCESSING_FEE;
        } else {
            this.salesContractProcessingFee = HIGH_PROCESSING_FEE;
        }
    }

    public BigDecimal getSalesTax() {
        return salesContractSalesTax;
    }

    public void setSalesTax(BigDecimal salesContractSalesTax) {
        this.salesContractSalesTax = salesContractSalesTax;
    }

    public BigDecimal getRecordingFee() {
        return salesContractRecordingFee;
    }

    public void setRecordingFee(BigDecimal salesContractRecordingFee) {
        this.salesContractRecordingFee = salesContractRecordingFee;
    }

    public BigDecimal getProcessingFee() {
        return salesContractProcessingFee;
    }

    public void setProcessingFee(BigDecimal salesContractProcessingFee) {
        this.salesContractProcessingFee = salesContractProcessingFee;
    }

    public boolean isFinance() {
        return salesContractFinance;
    }

    public void setFinance(boolean salesContractFinance) {
        this.salesContractFinance = salesContractFinance;
    }

    // vehicle price + sales tax + recording fee + processing fee
    @Override
    public BigDecimal getTotalPrice() {
        return getVehicleSold().getPrice()
                .add(salesContractSalesTax)
                .add(salesContractRecordingFee)
                .add(salesContractProcessingFee)
                .setScale(2, RoundingMode.HALF_UP);
    }

    // 0 if not financed, 4.25% for 48 months if $10,000 or more, otherwise 5.25% for 24 months
    @Override
    public BigDecimal getMonthlyPayment() {
        if (!salesContractFinance) {
            return new BigDecimal("0.00");
        }

        if (getVehicleSold().getPrice().compareTo(TEN_THOUSAND) >= 0) {
            return calculateMonthlyPayment(getTotalPrice(), new BigDecimal("0.0425"), 48);
        }
        return calculateMonthlyPayment(getTotalPrice(), new BigDecimal("0.0525"), 24);
    }
}
