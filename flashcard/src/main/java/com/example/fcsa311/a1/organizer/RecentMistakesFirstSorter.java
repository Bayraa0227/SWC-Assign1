package com.example.fcsa311.a1.organizer;

import com.example.fcsa311.a1.card.Card;
import java.util.ArrayList;
import java.util.List;

public class RecentMistakesFirstSorter implements CardOrganizer {
  @Override
  public List<Card> organize(List<Card> cards) {
    List<Card> mistakes = new ArrayList<>();
    List<Card> others = new ArrayList<>();
    for (Card c : cards) {
      if (c.hasLastAttempt() && !c.wasLastAttemptCorrect()) {
        mistakes.add(c);
      } else {
        others.add(c);
      }
    }
    List<Card> ordered = new ArrayList<>(cards.size());
    ordered.addAll(mistakes);
    ordered.addAll(others);
    return ordered;
  }
}
