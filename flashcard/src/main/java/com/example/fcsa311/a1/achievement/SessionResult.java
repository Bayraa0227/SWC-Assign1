package com.example.fcsa311.a1.achievement;

import java.util.List;

public class SessionResult {
  private final List<CardResult> cardResults;

  public SessionResult(List<CardResult> cardResults) {
    this.cardResults = cardResults;
  }

  public List<CardResult> getCardResults() {
    return cardResults;
  }

  public double getAverageTimeSeconds() {
    return cardResults.stream().mapToLong(CardResult::getTimeMillis).average().orElse(0) / 1000.0;
  }
}
