# BTree :deciduous_tree:
This is a implementation of b-tree on database queries 

## Features
- :heavy_check_mark:  Create a B+-tree index file for the Student table on the StudentId field. The leaf node contains <StudentID, recordID>.
- :heavy_check_mark:  Search for an existing student given a studentId, return recordId if found. Otherwise print out a message that the given studentId has not been found in the table.
- :heavy_check_mark:  Insert a new student with a new studentId. Use a random generator to generate recordId for this student. Update both the B+ tree index and the Student table.
-   Delete an existing student given a studentId. Return true if deletion is complete successfully and return false otherwise.
- :heavy_check_mark:  Print the leaf nodes of B+Tree from left to right using the pointer. Return the list of recordIDs.

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
