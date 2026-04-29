package app.model;

import java.time.Instant;

public class CalculationHistoryItem {
    private final String expression;
    private final String result;
    private final Instant timestamp;

    public CalculationHistoryItem(String expression, String result, Instant timestamp) {
        this.expression = expression;
        this.result = result;
        this.timestamp = timestamp;
    }

    public String getExpression() {
        return expression;
    }

    public String getResult() {
        return result;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
