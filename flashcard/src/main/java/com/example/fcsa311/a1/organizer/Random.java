package com.example.fcsa311.a1.organizer;

import com.example.fcsa311.a1.card.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Simple random organizer: shuffles the card list. */
public class Random implements CardOrganizer {
  @Override
  public List<Card> organize(List<Card> cards) {
    List<Card> copy = new ArrayList<>(cards);
    Collections.shuffle(copy);
    return copy;
  }
}
