package com.anastasia.smart_service.analysis;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class TechnicalAnalysis {

    public double calculateSimpleMovingAverage(double[] prices, int period) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        stats.setWindowSize(period);
        for (double price : prices) {
            stats.addValue(price);
        }
        return stats.getMean();
    }

    public double calculateVolatility(double[] returns) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (double r : returns) {
            stats.addValue(r);
        }
        return stats.getStandardDeviation();
    }
}
