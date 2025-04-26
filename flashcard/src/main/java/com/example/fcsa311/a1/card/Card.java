package com.example.fcsa311.a1.card;

public class Card {
  private final String question;
  private final String answer;
  private final CardStatus status = new CardStatus();

  public Card(String question, String answer) {
    this.question = question.trim();
    this.answer = answer.trim();
  }

  public boolean attempt(String userAnswer) {
    boolean correct = answer.equalsIgnoreCase(userAnswer.trim());
    status.recordAttempt(correct);
    return correct;
  }

  public String getQuestion() {
    return question;
  }

  public String getAnswer() {
    return answer;
  }

  public CardStatus getStatus() {
    return status;
  }

  public Card invert() {
    return new Card(answer, question);
  }

  public boolean hasLastAttempt() {
    return status.hasAttempted();
  }

  public boolean wasLastAttemptCorrect() {
    return status.wasLastAttemptCorrect();
  }
}
