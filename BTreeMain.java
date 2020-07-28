import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main Application.
 * You do not need to change this class.
 */
public class BTreeMain {

    public static void main(String[] args) {

        /* Read the input file -- input.txt */
        Scanner scan = null;
        try {
            scan = new Scanner(new File("src/input.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

        /* Read the minimum degree of B+Tree first */
        int degree = scan.nextInt();
        BTree bTree = new BTree(degree);

        /* Reading the database student.csv into B+Tree Node*/
        List<Student> studentsDB = getStudents();
        for (Student s : studentsDB) {
            bTree.insert(s);
        }


        /* Start reading the operations now from input file*/
        try {
            while (scan.hasNextLine()) {
                Scanner s2 = new Scanner(scan.nextLine());

                while (s2.hasNext()) {
                    String operation = s2.next();
                    switch (operation) {
                        case "insert": {
                            long studentId = Long.parseLong(s2.next());
                            String studentName = s2.next() + " " + s2.next();
                            String major = s2.next();
                            String level = s2.next();
                            int age = Integer.parseInt(s2.next());
                            long recordID = Long.parseLong(s2.next());

                            Student s = new Student(studentId, age, studentName, major, level, recordID);
                            bTree.insert(s);
                            bTree.insert_to_csv(s);
                            break;
                        }
                        case "delete": {
                            long studentId = Long.parseLong(s2.next());
                            boolean result = bTree.delete(studentId);
                            if (result)
                                System.out.println("Student deleted successfully.");
                            else
                                System.out.println("Student deletion failed.");
                            break;
                        }
                        case "search": {
                            long studentId = Long.parseLong(s2.next());
                            long recordID = bTree.search(studentId);
                            if (recordID != -1)
                                System.out.println("Student exists in the database at " + recordID);
                            else
                                System.out.println("Student does not exist.");
                            break;
                        }
                        case "print": {
                            List<Long> listOfRecordID = new ArrayList<>();
                            listOfRecordID = bTree.print();
                            System.out.println("List of recordIDs in B+Tree " + listOfRecordID.toString());
                        }
                        default:
                            System.out.println("Wrong Operation");
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Student> getStudents() {

        /*
         * Extract the students information from "Students.csv"
         * return the list<Students>
         */
        List<Student> studentList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Student.csv"));
            String line = null;
            while((line=reader.readLine())!=null){
                String[] item = line.split(","); // get each students' data by ","
                long studentId = Long.parseLong(item[0]);
                String studentName = item[1];
                String major = item[2];
                String level = item[3];
                int age = Integer.parseInt(item[4]);
                long recordId = Long.parseLong(item[5]);
                studentList.add(new Student(studentId, age, studentName, major, level, recordId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return studentList;
    }
}
