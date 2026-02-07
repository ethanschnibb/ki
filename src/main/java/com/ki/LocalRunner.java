package com.ki;

import com.ki.models.Payment;
import com.ki.models.ShareOrder;
import com.ki.services.PaymentProcessor;
import com.ki.services.ShareEngine;
import com.opencsv.CSVWriter;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LocalRunner {

    public static String simulatePlatform(String csvPath, String source, BigDecimal sharePrice) {

        String[] fieldNames = new String[]{
                "customer_id",
                "shares",
        };

        PaymentProcessor paymentProcessor = new PaymentProcessor();
        Payment[] payments = paymentProcessor.getPayments(csvPath, source);
        Payment[] filtered = paymentProcessor.verifyPayments(payments);

        ShareEngine shareEngine = new ShareEngine();
        ShareOrder[] shareOrders = shareEngine.generateShareOrders(sharePrice, filtered);

        List<String[]> data = new ArrayList<>();

        for (ShareOrder shareOrder : shareOrders) {
            data.add(new String[]{
                    String.valueOf(shareOrder.getCustomerId()),
                    String.valueOf(shareOrder.getShares()),
            });
        }

        return generateCsv(fieldNames, data);
    }

    public static void main(String[] args) {

        ArgumentParser parser = ArgumentParsers.newFor("LocalRunner").build();
        parser.addArgument("csv_path").help("Path to the payments CSV file");
        parser.addArgument("source").help("The source of the payment, currently only 'card' is supported");
        parser.addArgument("share_price").help("Share price to generate share orders for e.g. '1.30'");
        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        String output = simulatePlatform(ns.getString("csv_path"), ns.getString("source"), new BigDecimal(ns.getString("share_price")));
        System.out.println(output);
    }

    private static String generateCsv(String[] fieldNames, List<String[]> data) {
        StringWriter output = new StringWriter();
        CSVWriter writer = new CSVWriter(output);
        writer.writeNext(fieldNames);
        writer.writeAll(data);
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }


}
