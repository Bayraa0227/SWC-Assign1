package com.example.fcsa311.a1.achievement;

import java.util.stream.Stream;

public enum AchievementType implements Achievement {
  FAST_RESPONDER {
    @Override
    public String getName() {
      return "Хурдан хариулагч";
    }

    @Override
    public boolean isEarned(SessionResult result) {
      return result.getAverageTimeSeconds() < 5.0;
    }
  },

  CORRECT {
    @Override
    public String getName() {
      return "Зөв хариулагч";
    }

    @Override
    public boolean isEarned(SessionResult result) {
      return result.getCardResults().stream().allMatch(CardResult::isCorrect);
    }
  },

  REPEAT {
    @Override
    public String getName() {
      return "Давтагч";
    }

    @Override
    public boolean isEarned(SessionResult result) {
      return result.getCardResults().stream()
          .mapToInt(CardResult::getAttempts)
          .anyMatch(a -> a > 5);
    }
  },

  CONFIDENT {
    @Override
    public String getName() {
      return "Итгэлтэй хариулагч";
    }

    @Override
    public boolean isEarned(SessionResult result) {
      return result.getCardResults().stream()
          .mapToInt(CardResult::getCorrectStreak)
          .anyMatch(s -> s >= 3);
    }
  };

  public static Stream<AchievementType> earned(SessionResult result) {
    return Stream.of(values()).filter(a -> a.isEarned(result));
  }
}
