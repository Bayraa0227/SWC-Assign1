package com.example.fcsa311.a1.card;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CardLoader {
  public static List<Card> loadFromFile(String filePath) throws IOException {
    List<Card> cards = new ArrayList<>();
    List<String> lines = Files.readAllLines(Path.of(filePath), StandardCharsets.UTF_8);
    for (String line : lines) {
      String trimmed = line.trim();
      if (trimmed.isEmpty()) continue;
      String[] parts = trimmed.split(":::", 2);
      if (parts.length < 2) continue;
      String answer = parts[0].trim();
      String question = parts[1].trim();
      cards.add(new Card(question, answer));
    }
    return cards;
  }
}
