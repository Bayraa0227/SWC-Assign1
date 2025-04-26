package com.example.fcsa311.a1;

import com.example.fcsa311.a1.card.Card;
import com.example.fcsa311.a1.card.CardStatus;
import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDataManager {
  private static final String DATA_SUFFIX = ".userdata";

  @SuppressWarnings("unchecked")
  public static Map<String, CardStatus> loadStatuses(String cardsFilePath) {
    Path cardsPath = Path.of(cardsFilePath);
    Path parent = cardsPath.getParent();
    if (parent == null) {
      parent = Path.of(".");
    }
    Path dataPath = parent.resolve(cardsPath.getFileName() + DATA_SUFFIX);
    if (!dataPath.toFile().exists()) {
      return new HashMap<>();
    }
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataPath.toFile()))) {
      return (Map<String, CardStatus>) in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      System.out.println("Warning: unable to load saved data, starting fresh: " + e.getMessage());
      return new HashMap<>();
    }
  }

  public static void saveStatuses(String cardsFilePath, List<Card> cards) {
    Path cardsPath = Path.of(cardsFilePath);
    Path parent = cardsPath.getParent();
    if (parent == null) {
      parent = Path.of(".");
    }
    Map<String, CardStatus> map = new HashMap<>();
    for (Card c : cards) {
      String key = c.getQuestion() + "|" + c.getAnswer();
      map.put(key, c.getStatus());
    }
    Path dataPath = parent.resolve(cardsPath.getFileName() + DATA_SUFFIX);
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataPath.toFile()))) {
      out.writeObject(map);
    } catch (IOException e) {
      System.out.println("Warning: unable to save data: " + e.getMessage());
    }
  }
}
