package course;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Course - a course shell in the StudentScholar website that owns a list ofAssessment objects.
 *   Member 1 - Bastian Koralalage Dineth Praveena Rodrigo (4117907):
 *              base of createNewAssessment (create + store only when valid),
 *              query helpers, Conditions 1-4, and the Assessment constructor
 *   Member 2 - Mohammad Ahmad (4200097): Conditions 5-8
 *   Member 3 - Sehanndu Umeth Peduru Merengna (4128553): Conditions 9-11
 */
public class Course {

   private static final int MIN_DESCRIPTION_WORDS = 3;       // Condition 3
   private static final int MAX_POINTS_PER_ASSESSMENT = 50;  // Condition 5
   private static final int MAX_TOTAL_POINTS = 100;          // Condition 6
   private static final List<String> ALLOWED_SUBMISSION_TYPES =
         List.of("pdf", "zip", "doc");                        // Condition 7

   // Condition 10
   // (2-digit day, 2-digit month, 4-digit year, 2-digit hour, 2-digit minute).
   private static final String DATE_TIME_PATTERN_REGEX =
         "\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}";

   private static final DateTimeFormatter DATE_TIME_FORMATTER =
         DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm")
                          .withResolverStyle(ResolverStyle.STRICT);

   private List<Assessment> assessments;

   public Course() {
      this.assessments = new ArrayList<>();
   }


   public boolean createNewAssessment(String name, String description, int points,
                                      String submissionType, boolean group, String groupingName,
                                      String publishDateTime, String dueDateTime, String hideDateTime) {
      boolean valid = true;

      if (!isNameNotBlank(name) || !isNameUnique(name)) {
         valid = false;
      }

      if (!hasMinimumWords(description)) {
         valid = false;
      }

      if (!isPointsAboveZero(points) || !isPointsWithinMaximum(points)
            || !isTotalPointsWithinLimit(points)) {
         valid = false;
      }

      if (!isSubmissionTypeAllowed(submissionType)) {
         valid = false;
      }

      if (!isGroupingNameProvidedWhenRequired(group, groupingName)
            || !isGroupingNameUnique(groupingName)) {
         valid = false;
      }

      LocalDateTime publish = parseStrictDateTime(publishDateTime);
      LocalDateTime due = parseStrictDateTime(dueDateTime);
      LocalDateTime hide = parseStrictDateTime(hideDateTime);
      if (publish == null || due == null || hide == null) {
         valid = false;
      }

      else if (!isDateOrderCorrect(publish, due, hide)) {
         valid = false;
      }

      if (valid) {
         Assessment newAssessment = new Assessment(name, description, points,
               submissionType, group, groupingName,
               publishDateTime, dueDateTime, hideDateTime);
         assessments.add(newAssessment);
      }

      return valid;
   }

   private boolean isNameNotBlank(String name) {
      return name != null && !name.trim().isEmpty();
   }


   private boolean isNameUnique(String name) {
      return findAssessmentByName(name) == null;
   }

   private boolean hasMinimumWords(String description) {
      if (description == null || description.trim().isEmpty()) {
         return false; // no words at all
      }

      String[] words = description.trim().split("\\s+");
      return words.length >= MIN_DESCRIPTION_WORDS;
   }

   private boolean isPointsAboveZero(int points) {
      return points > 0;
   }

   private boolean isPointsWithinMaximum(int points) {
      return points <= MAX_POINTS_PER_ASSESSMENT;
   }

   private boolean isTotalPointsWithinLimit(int newPoints) {
      return getTotalPoints() + newPoints <= MAX_TOTAL_POINTS;
   }

   private boolean isSubmissionTypeAllowed(String submissionType) {
      return submissionType != null && ALLOWED_SUBMISSION_TYPES.contains(submissionType);
   }


   private boolean isGroupingNameProvidedWhenRequired(boolean group, String groupingName) {
      if (!group) {
         return true; 
      }
      return groupingName != null && !groupingName.trim().isEmpty();
   }

   private boolean isGroupingNameUnique(String groupingName) {
      if (groupingName == null || groupingName.trim().isEmpty()) {
         return true;
      }
      for (Assessment existing : assessments) {
         if (groupingName.equals(existing.getGroupingName())) {
            return false;
         }
      }
      return true;
   }

   private LocalDateTime parseStrictDateTime(String dateTime) {
      if (dateTime == null || !dateTime.matches(DATE_TIME_PATTERN_REGEX)) {
         return null;
      }
      try {
         return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
      } catch (DateTimeParseException e) {
         return null; // impossible calendar date or time
      }
   }

   private boolean isDateOrderCorrect(LocalDateTime publish, LocalDateTime due, LocalDateTime hide) {
      return due.isAfter(publish) && hide.isAfter(due);
   }

   public List<Assessment> getAssessments() {
      return Collections.unmodifiableList(assessments);
   }

   public Assessment findAssessmentByName(String name) {
      for (Assessment existing : assessments) {
         if (existing.getName().equals(name)) {
            return existing;
         }
      }
      return null;
   }

   public int getTotalPoints() {
      int total = 0;
      for (Assessment existing : assessments) {
         total += existing.getPoints();
      }
      return total;
   }
}
