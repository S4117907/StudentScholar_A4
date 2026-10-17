package course;

/**
 * Assessment - represents a single assessment item that belongs to a Course in the StudentScholar website.
 * 
 * Member 1 - Bastian Koralalage Dineth Praveena Rodrigo (4117907)
 */
public class Assessment {
   private String name;             
   private String description;       // at least 3 words
   private int points;               // 1..50 points
   private String submissionType;    // "pdf", "zip" or "doc"
   private boolean group;            // true = group assessment, false = individual
   private String groupingName;      // required (and unique) for group assessments
   private String publishDateTime;   // "DD/MM/YYYY hh:mm" (24-hour clock)
   private String dueDateTime;       // must be after publishDateTime
   private String hideDateTime;      // must be after dueDateTime

  
   public Assessment(String name, String description, int points,
                     String submissionType, boolean group, String groupingName,
                     String publishDateTime, String dueDateTime, String hideDateTime) {
      this.name = name;
      this.description = description;
      this.points = points;
      this.submissionType = submissionType;
      this.group = group;
      this.groupingName = groupingName;
      this.publishDateTime = publishDateTime;
      this.dueDateTime = dueDateTime;
      this.hideDateTime = hideDateTime;
   }

   public String getName() { return name; }

   public String getDescription() { return description; }

   public int getPoints() { return points; }

   public String getSubmissionType() { return submissionType; }

   public boolean isGroup() { return group; }

   public String getGroupingName() { return groupingName; }

   public String getPublishDateTime() { return publishDateTime; }

   public String getDueDateTime() { return dueDateTime; }

   public String getHideDateTime() { return hideDateTime; }
}
