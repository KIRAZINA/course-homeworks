package app.dto;

import java.time.Instant;

public class HistoryItemResponse {
    private String expression;
    private String result;
    private Instant timestamp;

    public HistoryItemResponse() {
    }

    public HistoryItemResponse(String expression, String result, Instant timestamp) {
        this.expression = expression;
        this.result = result;
        this.timestamp = timestamp;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
