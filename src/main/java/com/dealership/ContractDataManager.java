package com.dealership;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ContractDataManager {
    private static final String FILE_NAME = "contracts.csv";

    // contract dates are saved like 20261119
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    // append a contract to contracts.csv, the line format depends on the contract type
    public void saveContract(Contract contract) {
        String contractLine;

        if (contract instanceof SalesContract) {
            SalesContract salesContract = (SalesContract) contract;
            String financeOption = salesContract.isFinance() ? "YES" : "NO";

            contractLine = "SALE|" + getContractDetails(contract) + "|" +
                    salesContract.getSalesTax() + "|" + salesContract.getRecordingFee() + "|" +
                    salesContract.getProcessingFee() + "|" + salesContract.getTotalPrice() + "|" +
                    financeOption + "|" + salesContract.getMonthlyPayment();

        } else if (contract instanceof LeaseContract) {
            LeaseContract leaseContract = (LeaseContract) contract;

            contractLine = "LEASE|" + getContractDetails(contract) + "|" +
                    leaseContract.getExpectedEndingValue() + "|" + leaseContract.getLeaseFee() + "|" +
                    leaseContract.getTotalPrice() + "|" + leaseContract.getMonthlyPayment();

        } else {
            System.out.println("!!! Unknown contract type - contract not saved !!!");
            return;
        }

        // true = append to the file instead of overwriting it
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bufferedWriter.write(contractLine);
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.out.println("!!! Error saving contracts file !!!");
        }
    }

    // date, customer and vehicle parts that every contract type starts with
    private String getContractDetails(Contract contract) {
        return contract.getDate().format(DATE_FORMATTER) + "|" + contract.getCustomerName() + "|" +
                contract.getCustomerEmail() + "|" + contract.getVehicleSold().toFileString();
    }
}
