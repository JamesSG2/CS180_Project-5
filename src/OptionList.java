import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * OptionList
 * <p>
 * Handles the input and output via scanners. Contains main method
 *
 * @author Ian Fienberg, L15
 * @version 4/11/2022
 */


public class OptionList {

    public static void main(String[] args) throws IOException {

        // Ian's main method + updated initialization for ArrayList<Questions> quiz = new ArrayList<>();
        //updated parts:
        Scanner scan = new Scanner(System.in);
        ArrayList<Questions> quiz = null;
        ArrayList<String> studentAnswer = null;
        ArrayList<String> correctAnswer = new ArrayList<String>();
        ArrayList<Quizzes> quizzes = new ArrayList<>();
        String grade = "";
        String user = "";
        //updated by Zonglin:
        ArrayList<String> submission = new ArrayList<String>();
        ArrayList<String> sub = new ArrayList<String>();
        ArrayList<Integer> attemptNum = new ArrayList<Integer>(3);
        attemptNum.add(1);
        attemptNum.add(1);
        attemptNum.add(1);
        attemptNum.ensureCapacity(500);

        for (int i : attemptNum) {
            i = 1;
        }

        //changed by Zonglin:
        String userName = "";
        String password = "";
        Account lo = new Account(userName, password);


        boolean start = true;
        while (start) {
            System.out.println("What would you like to do?\n1. Sign up\n2. Log in");
            int sl = scan.nextInt();
            if (sl == 1) {
                System.out.println("What would like your username to be?");
                scan.nextLine();
                userName = scan.nextLine();
                System.out.println("What would like your password to be?");
                password = scan.nextLine();
                boolean type = false;
                System.out.println("Are you a teacher or student?\n1. Teacher\n2. Student");
                int ts = scan.nextInt();
                if (ts == 1) {
                    type = true;
                }
                new CreateAccount(userName, password, type);
            }
            System.out.println("LOG IN");
            System.out.println("What is your username?");
            scan.nextLine();
            userName = scan.nextLine();
            System.out.println("What is your password?");
            password = scan.nextLine();
            lo = new Account(userName, password);
            boolean valid = lo.isValid();
            boolean teach = lo.isTeacher();
            boolean stud = lo.isStudent();

            if (valid) {
                if (teach) {
                    user = "Teacher";
                } else if (stud) {
                    user = "Student";
                }
                start = false;
            } else {
                System.out.println("That is not a valid account!");
            }
        }


        //WILL READ QUIZINFO.TXT AND ADD PREVIOUS QUIZZES TO "quizzes" ARRAYLIST
        try {
            File fi = new File("QuizInfo.txt");
            fi.createNewFile();
            if (fi.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(fi));
                            /*while ((p = br.readLine()) != null) {
                                log.add(p);
                            }*/
                String p = "";
                while ((p = br.readLine()) != null) {
                    if (p.length() > 0) {
                        String quizName = p;
                        String maybe = "";
                        boolean q = true;
                        ArrayList<Questions> tempQuestions = new ArrayList<>();
                        for (int i = 0; i < 1; i++) {
                            String question;
                            if (q) {
                                question = br.readLine();
                            } else {
                                question = maybe;
                            }
                            String option1 = br.readLine();
                            String option2 = br.readLine();
                            String option3 = br.readLine();
                            String option4 = br.readLine();
                            String answer = br.readLine();
                            int points = Integer.parseInt(br.readLine());
                            maybe = br.readLine();
                            if (maybe.equals("END OF QUIZ")) {
                            } else {
                                q = false;
                                i--;
                            }
                            tempQuestions.add(new Questions(question, option1, option2, option3, option4, answer, points));
                        }
                        quizzes.add(new Quizzes(tempQuestions, quizName));
                    }
                }
                //writer.write("END OF QUIZ\n");
                br.close();
                //writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean teacher = true;
        if (user.equals("Teacher")) {
            System.out.println("Hello teacher");
            while (teacher) {
                System.out.println("What would you like to do?");
                //updated parts:
                System.out.println("1. Log out\n" + "2. Create a new quiz\n" + "3. Edit a quiz\n" + "4. Delete a quiz\n"
                        + "5. Upload a quiz\n" + "6. View submissions");
                int options = scan.nextInt();
                scan.nextLine();

                //assuming option 1 for teachers is to create a quiz
                //updated: added a while loop for user to choose option until they want to quit

                //TEACHER CHOOSES TO QUIT
                if (options == 1) {
                    System.out.println("Goodbye!");
                    teacher = false;
                }
                //while (options != 0) {
                //TEACHER CHOOSES TO CREATE A QUIZ
                else if (options == 2) {
                    quiz = new ArrayList<Questions>();
                    System.out.println("What would you like to name this quiz?");
                    String quizName = scan.nextLine();

                    System.out.println("How many questions will there be in this quiz?");
                    int numOfQuestions = scan.nextInt();

                    for (int i = 1; i <= numOfQuestions; i++) {
                        System.out.println("What is question " + i + "?");
                        scan.nextLine();
                        String question = scan.nextLine();
                        System.out.println("What is option 1?");
                        String option1 = scan.nextLine();
                        System.out.println("What is option 2?");
                        String option2 = scan.nextLine();
                        System.out.println("What is option 3?");
                        String option3 = scan.nextLine();
                        System.out.println("What is option 4?");
                        String option4 = scan.nextLine();
                        System.out.println("Which option is the correct answer");
                        String answer = scan.nextLine();
                        System.out.println("How many points is this question worth?");
                        int points = scan.nextInt();

                        //ADDS QUESTION TO QUIZ ARRAYLIST
                        quiz.add(new Questions(question, option1, option2, option3, option4, answer, points));
                        correctAnswer.add(answer);
                        //just for test:
                        //studentAnswer.add(answer);
                    }
                    //ADDS QUIZ ARRAYLIST AND QUIZ NAME TO QUIZZES ARRAYLIST
                    quizzes.add(new Quizzes(quiz, quizName));
                    try {
                        File f = new File("QuizInfo.txt");
                        f.createNewFile();
                        if (f.exists()) {
                            /*BufferedReader br = new BufferedReader(new FileReader(f));
                            while ((p = br.readLine()) != null) {
                                log.add(p);
                            }*/
                            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("QuizInfo.txt", true)));
                            int quizIndex = 0;
                            for (int i = 0; i < quizzes.size(); i++) {
                                if (quizzes.get(i).getName().equals(quizName)) {
                                    quizIndex = i;
                                    break;
                                }
                            }
                            //WRITES ENTIRE QUIZ TO QUIZINFO.TXT
                            writer.write(quizzes.get(quizIndex).getName() + "\n");
                            for (int i = 0; i < quizzes.get(quizIndex).getQuestions().size(); i++) {
                                writer.write(quizzes.get(quizIndex).getQuestions().get(i).getQuestion() + "\n");
                                writer.write(quizzes.get(quizIndex).getQuestions().get(i).getOption1() + "\n");
                                writer.write(quizzes.get(quizIndex).getQuestions().get(i).getOption2() + "\n");
                                writer.write(quizzes.get(quizIndex).getQuestions().get(i).getOption3() + "\n");
                                writer.write(quizzes.get(quizIndex).getQuestions().get(i).getOption4() + "\n");
                                writer.write(quizzes.get(quizIndex).getQuestions().get(i).getAnswer() + "\n");
                                writer.write(quizzes.get(quizIndex).getQuestions().get(i).getPoints() + "\n");
                            }
                            writer.write("END OF QUIZ\n");
                            //br.close();
                            writer.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Quiz created!");
                }
                //IF A TEACHER WOULD LIKE TO EDIT A QUIZ
                else if (options == 3) {
                    String quizName = "";
                    if (quizzes.size() != 0) {
                        System.out.println("What is the name of the quiz you would like to edit?");
                        quizName = scan.nextLine();
                    }
                    for (int i = 0; i < quizzes.size(); i++) {
                        if (quizzes.get(i).getName().equalsIgnoreCase(quizName)) {
                            System.out.println("What would you like to change?");
                            System.out.println("1. Name\n2. Question");
                            int alter = scan.nextInt();
                            //TO CHANGE THE NAME OF A QUIZ
                            if (alter == 1) {
                                System.out.println("What name would you like to change it to?");
                                scan.nextLine();
                                String newName = scan.nextLine();
                                quizzes.get(i).setName(newName);
                                System.out.println("Name changed!");
                                //System.out.println(quizzes.toArray());
                                //break;
                                //TO CHANGE AN ENTIRE QUESTION ON A QUIZ
                            } else if (alter == 2) {
                                System.out.println("Which question would you like to change?");
                                int qnum = scan.nextInt();
                                System.out.println("What should this question be?");
                                scan.nextLine();
                                String question = scan.nextLine();
                                System.out.println("What is option 1?");
                                String option1 = scan.nextLine();
                                System.out.println("What is option 2?");
                                String option2 = scan.nextLine();
                                System.out.println("What is option 3?");
                                String option3 = scan.nextLine();
                                System.out.println("What is option 4?");
                                String option4 = scan.nextLine();
                                System.out.println("Which option is the correct answer");
                                String answer = scan.nextLine();
                                System.out.println("How many points is this question worth?");
                                int points = scan.nextInt();

                                quizzes.get(i).getQuestions().get(qnum - 1).setQuestion(question);
                                quizzes.get(i).getQuestions().get(qnum - 1).setOption1(option1);
                                quizzes.get(i).getQuestions().get(qnum - 1).setOption2(option2);
                                quizzes.get(i).getQuestions().get(qnum - 1).setOption3(option3);
                                quizzes.get(i).getQuestions().get(qnum - 1).setOption4(option4);
                                quizzes.get(i).getQuestions().get(qnum - 1).setAnswer(answer);
                                quizzes.get(i).getQuestions().get(qnum - 1).setPoints(points);
                                //quiz.set(qnum - 1, new Questions(question, option1, option2, option3, option4, answer, points));
                                //ISNT USED YET
                                //////////////correctAnswer.set(qnum - 1, answer);
                                //just for test:
                                //studentAnswer.set(qnum - 1, answer);
                                //quizzes.add(new Quizzes(quiz, quizName));
                                System.out.println("Quiz edited!");
                                //break;
                            }

                            try {
                                File f = new File("QuizInfo.txt");
                                f.createNewFile();
                                if (f.exists()) {

                                    //REPLACES CURRENT QUIZINFO.TXT WITH A CURRENT VERSION WITH CHANGES
                                    PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("QuizInfo.txt", false)));
                                    for (int j = 0; j < quizzes.size(); j++) {
                                        writer.write(quizzes.get(j).getName() + "\n");
                                        for (int k = 0; k < quizzes.get(j).getQuestions().size(); k++) {
                                            writer.write(quizzes.get(j).getQuestions().get(k).getQuestion() + "\n");
                                            writer.write(quizzes.get(j).getQuestions().get(k).getOption1() + "\n");
                                            writer.write(quizzes.get(j).getQuestions().get(k).getOption2() + "\n");
                                            writer.write(quizzes.get(j).getQuestions().get(k).getOption3() + "\n");
                                            writer.write(quizzes.get(j).getQuestions().get(k).getOption4() + "\n");
                                            writer.write(quizzes.get(j).getQuestions().get(k).getAnswer() + "\n");
                                            writer.write(quizzes.get(j).getQuestions().get(k).getPoints() + "\n");

                                        }
                                        writer.write("END OF QUIZ\n");
                                    }
                                    /*
                                    writer.write(quizzes.get(quizIndex).getName() + "\n");
                                    for (int i = 0; i < quizzes.get(quizIndex).getQuestions().size(); i++) {
                                        writer.write(quizzes.get(quizIndex).getQuestions().get(i).getQuestion() + "\n");
                                        writer.write(quizzes.get(quizIndex).getQuestions().get(i).getOption1() + "\n");
                                        writer.write(quizzes.get(quizIndex).getQuestions().get(i).getOption2() + "\n");
                                        writer.write(quizzes.get(quizIndex).getQuestions().get(i).getOption3() + "\n");
                                        writer.write(quizzes.get(quizIndex).getQuestions().get(i).getOption4() + "\n");
                                        writer.write(quizzes.get(quizIndex).getQuestions().get(i).getAnswer() + "\n");
                                        writer.write(quizzes.get(quizIndex).getQuestions().get(i).getPoints() + "\n");*/
                                    //br.close();
                                    writer.close();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;


                            //IF THERE ARE QUIZZES, BUT THE INPUTTED NAME DOESN'T MATCH ANY
                        } else if (i == quizzes.size() - 1) {
                            System.out.println("That is not a name of a current quiz!");
                        }
                    }
                    //IF THE ARRAYLIST OF QUIZZES IS SIZE 0, PRINT AN ERROR MESSAGE AND TRY AGAIN
                    if (quizzes.size() == 0) {
                        System.out.println("You need to create a quiz before you can edit one!");
                    }

                }
                //IF A TEACHER WOULD LIKE TO DELETE AN ENTIRE QUIZ
                else if (options == 4) {
                    String quizName2 = "";
                    if (quizzes.size() != 0) {
                        System.out.println("What is the name of the quiz you would like to delete?");
                        quizName2 = scan.nextLine();
                    }
                    int p = -1;
                    for (int i = 0; i < quizzes.size(); i++) {
                        if (quizzes.get(i).getName().equalsIgnoreCase(quizName2)) {
                            System.out.println("Are you sure you would like to delete this quiz?");
                            String sure = scan.nextLine();
                            if (!sure.equalsIgnoreCase("no") || !sure.equalsIgnoreCase("n")) {
                                quizzes.remove(i);
                                System.out.println("Quiz deleted!");
                                try {
                                    File f = new File("QuizInfo.txt");
                                    f.createNewFile();
                                    if (f.exists()) {

                                        //REPLACES CURRENT QUIZINFO.TXT WITH A CURRENT VERSION WITH CHANGES
                                        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("QuizInfo.txt", false)));
                                        for (int j = 0; j < quizzes.size(); j++) {
                                            writer.write(quizzes.get(j).getName() + "\n");
                                            for (int k = 0; k < quizzes.get(j).getQuestions().size(); k++) {
                                                writer.write(quizzes.get(j).getQuestions().get(k).getQuestion() + "\n");
                                                writer.write(quizzes.get(j).getQuestions().get(k).getOption1() + "\n");
                                                writer.write(quizzes.get(j).getQuestions().get(k).getOption2() + "\n");
                                                writer.write(quizzes.get(j).getQuestions().get(k).getOption3() + "\n");
                                                writer.write(quizzes.get(j).getQuestions().get(k).getOption4() + "\n");
                                                writer.write(quizzes.get(j).getQuestions().get(k).getAnswer() + "\n");
                                                writer.write(quizzes.get(j).getQuestions().get(k).getPoints() + "\n");

                                            }
                                        }
                                        writer.write("END OF QUIZ\n");
                                        writer.close();
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            } else {
                                System.out.println("Okay. Your quiz will not be deleted.");
                            }
                            p = i;
                            break;
                            //IF THERE ARE QUIZZES, BUT THE INPUTTED NAME DOESN'T MATCH ANY
                        } else if (i == quizzes.size() - 1) {
                            System.out.println("That is not a name of a current quiz!");
                        }
                        p = i;
                    }
                    //IF THE ARRAYLIST OF QUIZZES IS SIZE 0, PRINT AN ERROR MESSAGE AND TRY AGAIN
                    if (quizzes.size() == 0 && p != 0) {
                        System.out.println("You need to create a quiz before you can delete one!");
                    }
                    //IF A TEACHER WOULD LIKE TO UPLOAD A QUIZ FILE
                } else if (options == 5) {
                    System.out.println("What is the name of the quiz file you would like to upload?");
                    //System.out.println(quizzes.get(0).getQuestions().get(0).getQuestion());
                    String fileName = scan.nextLine();
                    //WILL READ QUIZINFO.TXT AND ADD PREVIOUS QUIZZES TO "quizzes" ARRAYLIST
                    try {
                        File fi = new File(fileName);
                        fi.createNewFile();
                        if (fi.exists()) {
                            BufferedReader buf = new BufferedReader(new FileReader(fi));
                            String p = buf.readLine();
                            while (p != null) {
                                if (p.length() > 0) {
                                    String quizName = p;
                                    String maybe = "";
                                    boolean q = true;
                                    ArrayList<Questions> tempQuestions = new ArrayList<>();
                                    for (int i = 0; i < 1; i++) {
                                        String question;
                                        if (q) {
                                            question = buf.readLine();
                                        } else {
                                            question = maybe;
                                        }
                                        String option1 = buf.readLine();
                                        String option2 = buf.readLine();
                                        String option3 = buf.readLine();
                                        String option4 = buf.readLine();
                                        String answer = buf.readLine();
                                        int points = Integer.parseInt(buf.readLine());
                                        maybe = buf.readLine();
                                        if (maybe.equals("END OF QUIZ")) {
                                        } else {
                                            q = false;
                                            i--;
                                        }
                                        tempQuestions.add(new Questions(question, option1, option2, option3, option4, answer, points));
                                    }
                                    quizzes.add(new Quizzes(tempQuestions, quizName));
                                }
                                p = buf.readLine();
                            }
                            //writer.write("END OF QUIZ\n");
                            buf.close();
                            //writer.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //System.out.println(quizzes.get(1).getQuestions().get(1).getQuestion());

                    //WILL WRITE NEW QUIZ TO QUIZINFO.TXT
                    try {
                        File f = new File("QuizInfo.txt");
                        f.createNewFile();
                        if (f.exists()) {

                            //REPLACES CURRENT QUIZINFO.TXT WITH A CURRENT VERSION WITH CHANGES
                            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("QuizInfo.txt", true)));
                            for (int j = 0; j < quizzes.size(); j++) {
                                writer.write(quizzes.get(j).getName() + "\n");
                                for (int k = 0; k < quizzes.get(j).getQuestions().size(); k++) {
                                    writer.write(quizzes.get(j).getQuestions().get(k).getQuestion() + "\n");
                                    writer.write(quizzes.get(j).getQuestions().get(k).getOption1() + "\n");
                                    writer.write(quizzes.get(j).getQuestions().get(k).getOption2() + "\n");
                                    writer.write(quizzes.get(j).getQuestions().get(k).getOption3() + "\n");
                                    writer.write(quizzes.get(j).getQuestions().get(k).getOption4() + "\n");
                                    writer.write(quizzes.get(j).getQuestions().get(k).getAnswer() + "\n");
                                    writer.write(quizzes.get(j).getQuestions().get(k).getPoints() + "\n");
                                }
                                writer.write("END OF QUIZ\n");
                            }
                            writer.close();
                            System.out.println("Quiz added!");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (options == 6) {

                    String quizName3 = "";
                    if (quizzes.size() != 0) {
                        System.out.println("Which quiz would you like to see?");
                        //PRINTS LIST OF QUIZZES BY NAME
                        for (int i = 0; i < quizzes.size(); i++) {
                            System.out.println((i + 1) + ". " + quizzes.get(i).getName());
                        }
                        int quizNum1 = scan.nextInt();
                        scan.nextLine();

                        //System.out.println("attemptNum.get(quizNum1 - 1) test: " + attemptNum.get(quizNum1 - 1));

                        //for (ArrayList<String>() a : lo.getSubmission(quizzes.get(quizNum1 - 1).getName(), userName, password, k)) {

                        System.out.println("Please input the student's name: ");
                        String name = scan.nextLine();
                        System.out.println("Please input the student's password: ");
                        String key = scan.nextLine();
                        System.out.println("Please input the student's attempt number: ");
                        int i = scan.nextInt();
                        if (lo.getSubmission(quizzes.get(quizNum1 - 1).getName(), name, key, i) != null) {

                            sub = lo.getSubmission(quizzes.get(quizNum1 - 1).getName(), name, key, i);

                            System.out.println("Attempt: " + i);

                            for (String v : sub) {
                                System.out.println(v);
                            }

                        } else {
                            System.out.println("ERROR! THE INFORMATION IS INVALID!");
                        }

                        //}


                        //System.out.println("below is the getSubmission");
                        /*for (int k = 0; k < quizzes.get(quizNum1 - 1).getQuestions().size(); k++) {
                            System.out.println(sub.get(k));
                        }
                         */
                        //IF THERE ARE QUIZZES, BUT THE INPUTTED NAME DOESN'T MATCH ANY
                        //}
                    }
                    //IF THE ARRAYLIST OF QUIZZES IS SIZE 0, PRINT AN ERROR MESSAGE AND TRY AGAIN
                    if (quizzes.size() == 0) {
                        System.out.println("You need to create a quiz before you can grade one!");
                    }

                } else {
                    System.out.println("That is not a valid option! Please enter a number 1-6.");
                }

            }
        }
        boolean student = true;

        //int count = 0; //count for attemptNum

        if (user.equals("Student")) {
            System.out.println("Hello student");
            while (student) {
                System.out.println("What would you like to do?");
                System.out.println("1. Log out\n" + "2. Take a quiz\n" + "3. See your submission");
                int options = scan.nextInt();
                scan.nextLine();
                //STUDENT CHOOSES TO QUIT
                if (options == 1) {
                    System.out.println("Goodbye!");
                    student = false;
                    //STUDENT CHOOSES TO TAKE A QUIZ
                } else if (options == 2) {
                    System.out.println("Which quiz would you like to take?");
                    //PRINTS LIST OF QUIZZES BY NAME
                    for (int i = 0; i < quizzes.size(); i++) {
                        System.out.println((i + 1) + ". " + quizzes.get(i).getName());
                    }
                    int quizNum = scan.nextInt();
                    scan.nextLine();


                    if (quizNum > 0 && quizNum <= quizzes.size()) {
                        studentAnswer = new ArrayList<String>();
                        //PRINTS EACH QUESTION AND OPTIONS, THEN STORES STUDENTS ANSWERS IN ARRAYLIST "STUDENTANSWER"
                        for (int i = 0; i < quizzes.get(quizNum - 1).getQuestions().size(); i++) {
                            System.out.println("Question " + (i + 1) + ":");
                            System.out.println(quizzes.get(quizNum - 1).getQuestions().get(i).getQuestion());
                            System.out.println("a) " + quizzes.get(quizNum - 1).getQuestions().get(i).getOption1());
                            System.out.println("b) " + quizzes.get(quizNum - 1).getQuestions().get(i).getOption2());
                            System.out.println("c) " + quizzes.get(quizNum - 1).getQuestions().get(i).getOption3());
                            System.out.println("d) " + quizzes.get(quizNum - 1).getQuestions().get(i).getOption4());
                            String guess = scan.nextLine();
                            studentAnswer.add(guess);
                        }
                        System.out.println("Would you like to submit?");
                        String submit = scan.nextLine();

                        //updated attemptNum in case student makes multiple attempts to the same quiz
                        //System.out.println("count: " + count);

                        attemptNum.set(quizNum - 1, attemptNum.get(quizNum - 1) + 1);


                        if (submit.equalsIgnoreCase("no") || submit.equalsIgnoreCase("n")) {
                            System.out.println("Alright. Your quiz will not be submitted.");
                            //studentAnswers.remove(index);
                            continue;
                        } else {
                            try {
                                File f = new File("StudentQuizInfo.txt");
                                f.createNewFile();
                                if (f.exists()) {

                                    PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("StudentQuizInfo.txt", true)));

                                    writer.write(quizzes.get(quizNum - 1).getName() + "\n");
                                    for (int i = 0; i < studentAnswer.size(); i++) {
                                        writer.write(studentAnswer.get(i) + "\n");
                                    }
                                    //NEED TO PRINT SCORE
                                    writer.write("END OF QUIZ\n");
                                    //br.close();
                                    writer.close();


                                    String quizName3 = "";

                                    ArrayList<String> tempAnswerList = new ArrayList<String>();
                                    ArrayList<Integer> tempPointList = new ArrayList<Integer>();

                                    //System.out.println("quizzes.get(quizNum1 - 1).getQuestions().size() " + quizzes.get(quizNum1 - 1).getQuestions().size());

                                    for (int j = 0; j < quizzes.get(quizNum - 1).getQuestions().size(); j++) {

                                        tempAnswerList.add(quizzes.get(quizNum - 1).getQuestions().get(j).getAnswer());
                                        tempPointList.add(quizzes.get(quizNum - 1).getQuestions().get(j).getPoints());
                                    }

                                    Grading testGrade = new Grading(quizzes.get(quizNum - 1).getQuestions(), tempAnswerList, tempPointList);
                                    submission = testGrade.gradeAnswer("StudentQuizInfo.txt", quizzes.get(quizNum - 1).getName(), userName);
                                    //System.out.println("Testing Grade: " + testGrade.getGrade());

                                    //for (String a : submission) {
                                    //    System.out.println(a);
                                    //}

                                    lo.addSubmission(submission);

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Quiz submitted!");
                        }
                    }
                    if (quizNum < 1 || quizNum > quizzes.size()) {
                        System.out.println("That is not a valid option!");
                    }
                } else if (options == 3) {

                    if (quizzes.size() != 0) {
                        System.out.println("Which quiz would you like to see?");
                        //PRINTS LIST OF QUIZZES BY NAME
                        for (int i = 0; i < quizzes.size(); i++) {
                            System.out.println((i + 1) + ". " + quizzes.get(i).getName());
                        }
                        int quizNum1 = scan.nextInt();
                        scan.nextLine();

                        //System.out.println("attemptNum.get(quizNum1 - 1) test: " + attemptNum.get(quizNum1 - 1));

                        //for (ArrayList<String>() a : lo.getSubmission(quizzes.get(quizNum1 - 1).getName(), userName, password, k)) {


                        String name = userName;
                        //System.out.println("Please input the student's password: ");
                        String key = password;
                        System.out.println("Please input the attempt number: ");
                        int i = scan.nextInt();
                        if (lo.getSubmission(quizzes.get(quizNum1 - 1).getName(), name, key, i) != null) {

                            sub = lo.getSubmission(quizzes.get(quizNum1 - 1).getName(), name, key, i);

                            System.out.println("Attempt: " + i);

                            for (String v : sub) {
                                System.out.println(v);
                            }

                        } else {
                            System.out.println("ERROR! THE INFORMATION IS INVALID!");
                        }
                    }

                } else {
                    System.out.println("That is not a valid option! Please enter a number 1-3");
                }

            }
        }
    }

}
