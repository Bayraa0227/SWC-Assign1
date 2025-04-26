package com.example.fcsa311.a1.achievement;

public class CardResult {
  private final long timeMillis;
  private final boolean correct;
  private final int attempts;
  private final int correctStreak;

  public CardResult(long timeMillis, boolean correct, int attempts, int correctStreak) {
    this.timeMillis = timeMillis;
    this.correct = correct;
    this.attempts = attempts;
    this.correctStreak = correctStreak;
  }

  public long getTimeMillis() {
    return timeMillis;
  }

  public boolean isCorrect() {
    return correct;
  }

  public int getAttempts() {
    return attempts;
  }

  public int getCorrectStreak() {
    return correctStreak;
  }
}
