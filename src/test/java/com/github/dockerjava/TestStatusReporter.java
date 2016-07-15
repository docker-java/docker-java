package com.github.dockerjava;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestStatusReporter implements ITestListener {
    private int successes, failures, errors, skipped;
    private long startTime;

    @Override
    public void onTestStart(ITestResult result) {
        reset();
        System.out.printf("Running %s#%s%n", result.getTestClass().getName(), result.getMethod().getMethodName());
    }

    private void reset() {
        this.startTime = System.currentTimeMillis();
        this.successes = 0;
        this.failures = 0;
        this.errors = 0;
        this.skipped = 0;
    }

    private void printReport() {
        System.out.printf("Tests run: %d, Failures: %d, Errors: %d, Skipped: %d, Time elapsed: %.3f sec", getNumberRun(), failures, errors, skipped, (System.currentTimeMillis() - startTime) / 1000.0);
        if (failures > 0) {
            System.out.println("  <<< FAILURE!");
        }
        System.out.println();
    }

    private int getNumberRun() {
        return successes + errors + failures;
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        successes++;
        printReport();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failures++;
        printReport();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skipped++;
        printReport();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        failures++;
        printReport();
    }

    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
    }
}
