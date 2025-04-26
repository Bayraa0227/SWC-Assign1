package com.example.fcsa311.a1;

import com.example.fcsa311.a1.card.Card;
import com.example.fcsa311.a1.card.CardLoader;
import com.example.fcsa311.a1.card.CardStatus;
import com.example.fcsa311.a1.organizer.CardOrganizer;
import com.example.fcsa311.a1.organizer.Random;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.cli.*;

public class Flashcard {
  public static void main(String[] args) {

    Options options = new Options();
    options.addOption("h", "help", false, "Тусламжийн мэдээлэл харуулах");

    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd;
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      System.out.println("Error parsing arguments: " + e.getMessage());
      formatter.printHelp("java -jar <jar> <cards-file>", options);
      return;
    }

    if (cmd.hasOption("help")) {
      formatter.printHelp("java -jar <jar> <cards-file>", options);
      return;
    }

    List<String> positional = cmd.getArgList();
    if (positional.isEmpty()) {
      System.out.println("Файлын байршил оруулна уу.");
      formatter.printHelp("java -jar <jar> <cards-file>", options);
      System.exit(1);
    }
    String filePath = positional.get(0);

    Scanner scanner = new Scanner(System.in);

    do {
      List<Card> cards;
      try {
        cards = CardLoader.loadFromFile(filePath);
      } catch (IOException e) {
        System.out.println("Файлыг уншихад алдаа гарлаа: " + e.getMessage());
        scanner.close();
        return;
      }

      Map<String, CardStatus> saved = UserDataManager.loadStatuses(filePath);
      for (Card c : cards) {
        String key = c.getQuestion() + "|" + c.getAnswer();
        CardStatus st = saved.get(key);
        if (st != null) {
          if (st.wasLastAttemptCorrect()) {
            c.getStatus().recordAttempt(true);
          } else {
            c.getStatus().recordAttempt(false);
          }
        }
      }

      if (cards.isEmpty()) {
        System.out.println("Файлд flashcard олдсонгүй.");
        break;
      }

      // TODO: order argumentaar random-s uur sorter songoh
      CardOrganizer organizer = new Random();
      cards = organizer.organize(cards);

      int correctCount = 0;
      for (Card card : cards) {
        System.out.println("Асуулт: " + card.getQuestion());
        System.out.print("Таны хариулт: ");
        boolean correct = card.attempt(scanner.nextLine());
        if (correct) {
          System.out.println("Зөв байна!\n");
          correctCount++;
        } else {
          System.out.println("Буруу байна. Зөв хариулт: " + card.getAnswer() + "\n");
        }
      }

      System.out.printf("Бүх асуултанд хариулла: %d-с %d зөв.%n", cards.size(), correctCount);

      UserDataManager.saveStatuses(filePath, cards);

      System.out.print("Дахиж эхлэх үү? (y/N): ");
      String resp = scanner.nextLine().trim().toLowerCase();
      if (!resp.equals("y") && !resp.equals("yes")) {
        scanner.close();
        break;
      }
    } while (true);
  }
}
