# StudentScholar – Assessment Task 4 
Group ISYS1118_Fri_08:30_G001 – implementation and JUnit 5 tests for `Course.createNewAssessment(...)`.

## Structure (one source folder, opens directly in Eclipse)
```
src/course/Assessment.java   constructor initialises all 9 attributes + getters
src/course/Course.java       createNewAssessment(...) + one validator method per condition
src/course/CourseTest.java   12 JUnit 5 test cases / 16 test data, one section per member
```

## Run
Eclipse → File → Import → General → Existing Projects into Workspace → select this folder.
Right-click `CourseTest.java` → Run As → JUnit Test → expect 12/12 passing.

## Ownership
| Member & ID | Code | Tests |

| 1 – Bastian Koralalage Dineth Praveena Rodrigo - S4117907 | Assessment constructor, base of createNewAssessment (create + store), helpers, C1–C4 | TC01–TC04, setUp + helpers |
| 2 – Mohammad Ahmad - S4200097 | C5–C8 | TC05–TC08 |
| 3 – Sehanndu Umeth Peduru Merengna - S4128553 | C9–C11 | TC09–TC12 |
