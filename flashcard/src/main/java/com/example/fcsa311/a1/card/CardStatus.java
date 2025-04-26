package com.example.fcsa311.a1.card;

import java.io.Serializable;

public class CardStatus implements Serializable {
  private int totalAttempts;
  private int correctAttempts;
  private boolean lastAttemptCorrect;

  public void recordAttempt(boolean correct) {
    totalAttempts++;
    lastAttemptCorrect = correct;
    if (correct) {
      correctAttempts++;
    }
  }

  public int getTotalAttempts() {
    return totalAttempts;
  }

  public int getCorrectAttempts() {
    return correctAttempts;
  }

  public boolean wasLastAttemptCorrect() {
    return lastAttemptCorrect;
  }

  public boolean hasAttempted() {
    return totalAttempts > 0;
  }
}
