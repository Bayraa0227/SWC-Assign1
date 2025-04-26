package com.example.fcsa311.a1.organizer;

import com.example.fcsa311.a1.card.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MostMistakesFirstSorter implements CardOrganizer {
  @Override
  public List<Card> organize(List<Card> cards) {
    List<Card> copy = new ArrayList<>(cards);
    // mistake count = attempts - corrects
    Collections.sort(
        copy,
        Comparator.comparingInt(
                (Card c) -> c.getStatus().getTotalAttempts() - c.getStatus().getCorrectAttempts())
            .reversed());
    return copy;
  }
}
