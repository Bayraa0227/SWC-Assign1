package com.example.fcsa311.a1;

import com.example.fcsa311.a1.card.Card;
import com.example.fcsa311.a1.card.CardLoader;
import com.example.fcsa311.a1.card.CardStatus;
import com.example.fcsa311.a1.organizer.CardOrganizer;
import com.example.fcsa311.a1.organizer.MostMistakesFirstSorter;
import com.example.fcsa311.a1.organizer.Random;
import com.example.fcsa311.a1.organizer.RecentMistakesFirstSorter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.apache.commons.cli.*;

public class Flashcard {
  public static void main(String[] args) {

    Options options = new Options();
    options.addOption("h", "help", false, "Тусламжийн мэдээлэл харуулах");
    options.addOption(
        Option.builder()
            .option("o")
            .longOpt("order")
            .hasArg()
            .argName("order")
            .desc(
                "Эрэмбэлэгч (default: random). Сонголтууд:\n"
                    + "[random, worst-first, recent-mistakes-first]")
            .build());
    options.addOption(
        Option.builder()
            .option("r")
            .longOpt("repetitions")
            .hasArg()
            .argName("num")
            .desc(
                "Нэг картыг хэдэн удаа зөв хариулахыг шаардлага болгож тохируулна.\n"
                    + "Хэрэв тодорхойлохгүй бол зөвхөн нэг удаа асууна.")
            .build());
    options.addOption(null, "invert", false, "Картын асуулт, хариултыг сольж харуулах");

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

    boolean invert = cmd.hasOption("invert");
    String orderOption = cmd.getOptionValue("order", "random");
    int repetitions = 1;
    if (cmd.hasOption("repetitions")) {
      try {
        repetitions = Integer.parseInt(cmd.getOptionValue("repetitions"));
        if (repetitions < 1) {
          throw new NumberFormatException();
        }
      } catch (NumberFormatException e) {
        System.out.println("--repetitions тохиргооны аргумент эерэг бүхэл тоо байх ёстой.");
        System.exit(1);
      }
    }

    Scanner scanner = new Scanner(System.in);
    while (true) {
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

      if (invert) {
        cards = cards.stream().map(Card::invert).collect(Collectors.toList());
      }

      CardOrganizer organizer;
      switch (orderOption) {
        case "random":
          organizer = new Random();
          break;
        case "worst-first":
          organizer = new MostMistakesFirstSorter();
          break;
        case "recent-mistakes-first":
          organizer = new RecentMistakesFirstSorter();
          break;
        default:
          System.out.println("Эрэмбэлэгч байхгүй: " + orderOption);
          return;
      }
      cards = organizer.organize(cards);

      Map<Card, Integer> needed = new LinkedHashMap<>();
      for (Card card : cards) {
        needed.put(card, repetitions);
      }

      int totalCorrect = 0;
      int totalQuestionAsked = 0;

      while (!needed.isEmpty()) {
        List<Card> roundCards = organizer.organize(new ArrayList<>(needed.keySet()));

        for (Card card : roundCards) {
          int rem = needed.get(card);
          System.out.println("Асуулт: " + card.getQuestion());
          System.out.print("Таны хариулт: ");
          boolean correct = card.attempt(scanner.nextLine());
          totalQuestionAsked++;
          if (correct) {
            totalCorrect++;
            rem--;
            System.out.println("Зөв байна!\n");
            if (rem <= 0) {
              needed.remove(card);
            } else {
              needed.put(card, rem);
            }
          } else {
            System.out.println("Буруу байна. Зөв хариулт: " + card.getAnswer() + "\n");
          }
          if (needed.isEmpty()) {
            break;
          }
        }
      }

      System.out.printf(
          "Бүх асуултанд хариуллаа. %d-с %d зөв хариулсан.%n", totalQuestionAsked, totalCorrect);

      UserDataManager.saveStatuses(filePath, cards);

      System.out.print("Дахиж эхлэх үү? (y/N): ");
      String resp = scanner.nextLine().trim().toLowerCase();
      if (!resp.equals("y") && !resp.equals("yes")) {
        scanner.close();
        break;
      }
    }
  }
}
