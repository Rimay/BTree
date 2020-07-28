# BTree
This is a implementation of b tree data structure on database queries

## Features
- create a B+-tree index file for the Student table on the StudentId field. The leaf node contains <StudentID, recordID>.
- Search for an existing student given a studentId, return recordId if found. Otherwise, print out a message that the given studentId has not been found in the table.
- insert a new student with a new studentId. Use a random generator to generate recordId for this student. Update both the B+ tree index and the Student table.
- delete an existing student given a studentId. Return true if deletion is complete successfully. Return false otherwise.
- Print the leaf nodes of B+Tree from left to right using the pointer. Return the list of recordIDs.

Assuming that all the values being indexed are unique. The structure of the Student table is as follow:

| Attribute | Type |
| ---------- | :-----------: |
| StudentId | bigint |
| StudentName | varchar(255) |
| Major | varchar(255) |
| Level | char(2) |
| Age   | int |
| RecordId | bigint |



RecordId represents the address of each row in the table. The data for the table is posted under src/student.csv file. 
