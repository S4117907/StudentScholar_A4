package course;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * CourseTest - JUnit 5 unit tests for Course.createNewAssessment(...)
 *
 * 12 test cases / 16 test data in total:
 *   - Member 1 : Bastian Koralalage Dineth Praveena Rodrigo (4117907)
 *                TC01-TC04 (Conditions 1, 2, 3, 4)          - 4 test data
 *                + the shared setUp() and helper methods
 *   - Member 2 : Mohammad Ahmad (4200097)
 *                TC05-TC08 (Conditions 5, 6, 7, 8)          - 4 test data
 *   - Member 3 : Sehanndu Umeth Peduru Merengna (4128553)
 *                TC09-TC12 (Conditions 9, 10, 11 + valid)   - 1 + 2 + 2 + 3 test data
 */
class CourseTest {

   private static final String VALID_DESCRIPTION = "software testing assignment report";
   private static final String VALID_PUBLISH = "01/11/2026 09:00";
   private static final String VALID_DUE = "15/11/2026 23:59";
   private static final String VALID_HIDE = "30/11/2026 23:59";

   private Course course;

   @BeforeEach
   void setUp() {
      course = new Course();
      assertEquals(0, course.getAssessments().size(), "Precondition: course should start empty");
      assertEquals(0, course.getTotalPoints(), "Precondition: course should start with 0 points");
   }

   private void seedAssessment(String name, int points, boolean group, String groupingName) {
      boolean seeded = course.createNewAssessment(name, VALID_DESCRIPTION, points, "pdf",
            group, groupingName, VALID_PUBLISH, VALID_DUE, VALID_HIDE);
      assertTrue(seeded, "Precondition: setup assessment '" + name + "' should be valid");
   }

   private void assertNothingStored(int expectedSize, int expectedTotalPoints, String rejectedName) {
      assertEquals(expectedSize, course.getAssessments().size(),
            "No assessment should be added to the list when validation fails");
      assertEquals(expectedTotalPoints, course.getTotalPoints(),
            "Total points should not change when validation fails");
      if (rejectedName != null) {
         assertNull(course.findAssessmentByName(rejectedName),
               "The rejected assessment must not be stored");
      }
   }

   private void assertStoredWithAttributes(String name, String description, int points,
                                           String submissionType, boolean group, String groupingName,
                                           String publish, String due, String hide) {
      Assessment stored = course.findAssessmentByName(name);
      assertNotNull(stored, "The new assessment should be stored in the assessments list");
      assertEquals(name, stored.getName());
      assertEquals(description, stored.getDescription());
      assertEquals(points, stored.getPoints());
      assertEquals(submissionType, stored.getSubmissionType());
      assertEquals(group, stored.isGroup());
      assertEquals(groupingName, stored.getGroupingName());
      assertEquals(publish, stored.getPublishDateTime());
      assertEquals(due, stored.getDueDateTime());
      assertEquals(hide, stored.getHideDateTime());
   }

   @Test
   @DisplayName("TC01 (C1) Duplicate assessment name 'assignment 1' is rejected")
   void tc01_duplicateName_returnsFalse() {
      seedAssessment("assignment 1", 20, false, "");
      assertEquals(1, course.getAssessments().size());
      assertStoredWithAttributes("assignment 1", VALID_DESCRIPTION, 20, "pdf",
            false, "", VALID_PUBLISH, VALID_DUE, VALID_HIDE);

      boolean result = course.createNewAssessment("assignment 1", "weekly unit testing quiz", 10,
            "pdf", false, "", VALID_PUBLISH, VALID_DUE, VALID_HIDE);

      assertFalse(result);
      assertNothingStored(1, 20, null);
      Assessment original = course.findAssessmentByName("assignment 1");
      assertEquals(VALID_DESCRIPTION, original.getDescription(), "Original description must not be overwritten");
      assertEquals(20, original.getPoints(), "Original points must not be overwritten");
   }

   @Test
   @DisplayName("TC02 (C2) Blank assessment name \"\" is rejected")
   void tc02_blankName_returnsFalse() {
      boolean result = course.createNewAssessment("", VALID_DESCRIPTION, 20, "pdf",
            false, "", VALID_PUBLISH, VALID_DUE, VALID_HIDE);

      assertFalse(result);
      assertNothingStored(0, 0, "");
   }

   @Test
   @DisplayName("TC03 (C3) Two-word description 'testing report' is rejected")
   void tc03_descriptionWithTwoWords_returnsFalse() {
      boolean result = course.createNewAssessment("assignment 1", "testing report", 20, "pdf",
            false, "", VALID_PUBLISH, VALID_DUE, VALID_HIDE);
      assertFalse(result);
      assertNothingStored(0, 0, "assignment 1");
   }

   @Test
   @DisplayName("TC04 (C4) Assessment worth 0 points is rejected")
   void tc04_zeroPoints_returnsFalse() {
      boolean result = course.createNewAssessment("assignment 1", VALID_DESCRIPTION, 0, "pdf",
            false, "", VALID_PUBLISH, VALID_DUE, VALID_HIDE);

      assertFalse(result);
      assertNothingStored(0, 0, "assignment 1");
   }

   @Test
   @DisplayName("TC05 (C5) Assessment worth 51 points is rejected")
   void tc05_fiftyOnePoints_returnsFalse() {
      boolean result = course.createNewAssessment("assignment 1", VALID_DESCRIPTION, 51, "pdf",
            false, "", VALID_PUBLISH, VALID_DUE, VALID_HIDE);

      assertFalse(result);
      assertNothingStored(0, 0, "assignment 1");
   }

   @Test
   @DisplayName("TC06 (C6) New 41-point assessment making course total 101 is rejected")
   void tc06_totalPointsWouldBe101_returnsFalse() {
      seedAssessment("assignment 1", 50, false, "");
      seedAssessment("assignment 2", 10, false, "");
      assertEquals(2, course.getAssessments().size());
      assertEquals(60, course.getTotalPoints());

      boolean result = course.createNewAssessment("assignment 3", VALID_DESCRIPTION, 41, "pdf",
            false, "", VALID_PUBLISH, VALID_DUE, VALID_HIDE);

      assertFalse(result);
      assertNothingStored(2, 60, "assignment 3");
   }

   @Test
   @DisplayName("TC07 (C7) Submission type 'docx' is rejected")
   void tc07_invalidSubmissionType_returnsFalse() {
      boolean result = course.createNewAssessment("assignment 1", VALID_DESCRIPTION, 20, "docx",
            false, "", VALID_PUBLISH, VALID_DUE, VALID_HIDE);

      assertFalse(result);
      assertNothingStored(0, 0, "assignment 1");
   }

   @Test
   @DisplayName("TC08 (C8) Group assessment with blank grouping name is rejected")
   void tc08_groupAssessmentWithoutGroupingName_returnsFalse() {
      boolean result = course.createNewAssessment("assignment 1", VALID_DESCRIPTION, 20, "zip",
            true, "", VALID_PUBLISH, VALID_DUE, VALID_HIDE);

      assertFalse(result);
      assertNothingStored(0, 0, "assignment 1");
   }

   @Test
   @DisplayName("TC09 (C9) Reusing grouping name 'team alpha' is rejected")
   void tc09_duplicateGroupingName_returnsFalse() {
      seedAssessment("assignment 1", 20, true, "team alpha");
      assertEquals("team alpha", course.findAssessmentByName("assignment 1").getGroupingName());

      boolean result = course.createNewAssessment("assignment 2", VALID_DESCRIPTION, 20, "zip",
            true, "team alpha", VALID_PUBLISH, VALID_DUE, VALID_HIDE);

      assertFalse(result);
      assertNothingStored(1, 20, "assignment 2");
   }

   @Test
   @DisplayName("TC10 (C10) Invalid date '29/02/2027' and wrong format '1/11/2026' are rejected")
   void tc10_invalidDateTimes_returnFalse() {

      boolean result1 = course.createNewAssessment("assignment 1", VALID_DESCRIPTION, 20, "pdf",
            false, "", "01/02/2027 09:00", "29/02/2027 23:59", "15/03/2027 23:59");
      assertFalse(result1);
      assertNothingStored(0, 0, "assignment 1");

      boolean result2 = course.createNewAssessment("assignment 2", VALID_DESCRIPTION, 20, "pdf",
            false, "", "1/11/2026 09:00", VALID_DUE, VALID_HIDE);
      assertFalse(result2);
      assertNothingStored(0, 0, "assignment 2");
   }

   @Test
   @DisplayName("TC11 (C11) Due equal to publish, and hide equal to due, are rejected")
   void tc11_dateTimesNotInOrder_returnFalse() {
      boolean result1 = course.createNewAssessment("assignment 1", VALID_DESCRIPTION, 20, "pdf",
            false, "", "01/11/2026 09:00", "01/11/2026 09:00", VALID_HIDE);
      assertFalse(result1);
      assertNothingStored(0, 0, "assignment 1");

      boolean result2 = course.createNewAssessment("assignment 2", VALID_DESCRIPTION, 20, "pdf",
            false, "", VALID_PUBLISH, "15/11/2026 23:59", "15/11/2026 23:59");
      assertFalse(result2);
      assertNothingStored(0, 0, "assignment 2");
   }

   @Test
   @DisplayName("TC12 (All valid) Three valid assessments are created and stored correctly")
   void tc12_allValidInputs_returnTrueAndStoreAssessment() {

      boolean result1 = course.createNewAssessment("quiz 1", "weekly testing quiz", 1, "pdf",
            false, "", "01/11/2026 09:00", "01/11/2026 09:01", "01/11/2026 09:02");
      assertTrue(result1);
      assertEquals(1, course.getAssessments().size());
      assertEquals(1, course.getTotalPoints());
      assertStoredWithAttributes("quiz 1", "weekly testing quiz", 1, "pdf",
            false, "", "01/11/2026 09:00", "01/11/2026 09:01", "01/11/2026 09:02");

      boolean result2 = course.createNewAssessment("assignment 2", "group software project report", 50, "zip",
            true, "team alpha", "01/02/2028 09:00", "29/02/2028 23:59", "01/03/2028 00:00");
      assertTrue(result2);
      assertEquals(2, course.getAssessments().size());
      assertEquals(51, course.getTotalPoints());
      assertStoredWithAttributes("assignment 2", "group software project report", 50, "zip",
            true, "team alpha", "01/02/2028 09:00", "29/02/2028 23:59", "01/03/2028 00:00");

      boolean result3 = course.createNewAssessment("final exam", "end of semester exam", 49, "doc",
            false, "", "01/12/2026 08:00", "10/12/2026 12:00", "31/12/2026 23:59");
      assertTrue(result3);
      assertEquals(3, course.getAssessments().size());
      assertEquals(100, course.getTotalPoints());
      assertStoredWithAttributes("final exam", "end of semester exam", 49, "doc",
            false, "", "01/12/2026 08:00", "10/12/2026 12:00", "31/12/2026 23:59");
   }
}
