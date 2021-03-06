import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * OptionList
 * <p>
 * Handles the input and output via scanners. Contains main method
 *
 * @author Ian Fienberg, L15
 *
 * @version 4/11/2022
 *
 */
public class OptionList {

    public static void main(String[] args) throws IOException {

        Scanner scan = new Scanner(System.in);
        ArrayList<Questions> quiz = null;
        ArrayList<String> studentAnswer = null;
        ArrayList<String> correctAnswer = new ArrayList<String>();
        ArrayList<Quizzes> quizzes = new ArrayList<>();
        String grade = "";
        String user = "";
        ArrayList<String> submission = new ArrayList<String>();
        ArrayList<String> sub = new ArrayList<String>();

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

                CreateAccount ca = new CreateAccount(userName, password, type);

                boolean create = ca.isCreated();
                if (!create) {
                    System.out.println("The username is already taken! Account was not created.");
                }


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
                            tempQuestions.add(new Questions(question, option1, option2, option3,
                                    option4, answer, points));
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
        if (user.equalsIgnoreCase("Teacher")) {
            System.out.println("Hello teacher");
            while (teacher) {
                System.out.println("What would you like to do?");
                //updated parts:
                System.out.println("1. Log out\n" + "2. Create a new quiz\n" + "3. Edit a quiz\n"
                        + "4. Delete a quiz\n"
                        + "5. Upload a quiz\n" + "6. View submissions\n"
                        + "7. Edit account\n" + "8. Delete account");
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
                    System.out.println("Enter the course name and what you what to call the quiz. \n" +
                            "(i.e. Course Name: quiz#1) Note: If it's a new course the course will " +
                            "automatically be added");
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

                        //below is changed by Zonglin to prompt the teacher if they want files as submission
                        System.out.println("Which option is the correct answer (a, b, c, d, "
                                + "or otherwise if student should submit a file, please enter 'file')");
                        String answer = scan.nextLine();

                        //if the teacher enter 'file', they should input a String of correct answer allowing AutoGrading
                        if (answer.equals("file")) {
                            System.out.println("/****************");
                            System.out.println("*Please Add '/' for each new line. For example, " +
                                    "the correct answer of:\n*Bright \n*space\n"  +
                                    "*should be written as 'Bright/space/' as there are two lines");
                            System.out.println("/****************");
                            System.out.println("Please enter the correct answer:");
                            answer = scan.nextLine();
                        }

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
                            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
                                    "QuizInfo.txt", true)));
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
                        System.out.println("Enter the course name and quiz title of the quiz you want to edit. \n" +
                                "(i.e. Course Name: quiz#1)");
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
                                System.out.println("Which option is the correct answer (a, b, c, d, "
                                        + "or otherwise if student should submit a file, please enter 'file')");
                                String answer = scan.nextLine();

                                if (answer.equals("file")) {
                                    System.out.println("/****************");
                                    System.out.println("*Please Add '/' for each new line. For example, " +
                                            "the correct answer of:\n*Bright \n*space\n"  +
                                            "*should be written as 'Bright/space/' as there are two lines");
                                    System.out.println("/****************");
                                    System.out.println("Please enter the correct answer:");
                                    answer = scan.nextLine();
                                }
                                System.out.println("How many points is this question worth?");
                                int points = scan.nextInt();

                                quizzes.get(i).getQuestions().get(qnum - 1).setQuestion(question);
                                quizzes.get(i).getQuestions().get(qnum - 1).setOption1(option1);
                                quizzes.get(i).getQuestions().get(qnum - 1).setOption2(option2);
                                quizzes.get(i).getQuestions().get(qnum - 1).setOption3(option3);
                                quizzes.get(i).getQuestions().get(qnum - 1).setOption4(option4);
                                quizzes.get(i).getQuestions().get(qnum - 1).setAnswer(answer);
                                quizzes.get(i).getQuestions().get(qnum - 1).setPoints(points);
                                //quiz.set(qnum - 1, new Questions(question, option1, option2, option3, option4,
                                // answer, points));
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
                                    PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
                                            "QuizInfo.txt", false)));
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
                        System.out.println("Enter the course name and quiz title of the quiz you want to delete. \n" +
                                "(i.e. Course Name: quiz#1)");
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
                                        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
                                                "QuizInfo.txt", false)));
                                        for (int j = 0; j < quizzes.size(); j++) {
                                            writer.write(quizzes.get(j).getName() + "\n");
                                            for (int k = 0; k < quizzes.get(j).getQuestions().size(); k++) {
                                                writer.write(quizzes.get(j).getQuestions().get(k).getQuestion() +
                                                        "\n");
                                                writer.write(quizzes.get(j).getQuestions().get(k).getOption1() +
                                                        "\n");
                                                writer.write(quizzes.get(j).getQuestions().get(k).getOption2() +
                                                        "\n");
                                                writer.write(quizzes.get(j).getQuestions().get(k).getOption3() +
                                                        "\n");
                                                writer.write(quizzes.get(j).getQuestions().get(k).getOption4() +
                                                        "\n");
                                                writer.write(quizzes.get(j).getQuestions().get(k).getAnswer() +
                                                        "\n");
                                                writer.write(quizzes.get(j).getQuestions().get(k).getPoints() +
                                                        "\n");

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
                    System.out.println("Note: the file must follow the format of quiz title first then for each \n" +
                            "question: the question, the 4 choices, the correct answer, then the point value." +
                            "\nAt the end of the quiz:" +
                            "type \"END OF QUIZ\". \nEverything is separated with a new line. See QuizInfo.txt\n" +
                            "to look at past quizzes made by this program. Follow that format.");
                    System.out.println("What is the name of the quiz file you would like to upload?");
                    //System.out.println(quizzes.get(0).getQuestions().get(0).getQuestion());
                    String fileName = scan.nextLine();
                    //WILL READ QUIZINFO.TXT AND ADD PREVIOUS QUIZZES TO "quizzes" ARRAYLIST
                    try {
                        File fi = new File(fileName);
                        if (fi.exists()) {
                            BufferedReader buf = new BufferedReader(new FileReader(fi));
                            String p = buf.readLine();
                            String quizName = p;
                            boolean q = true;
                            while (p != null) {
                                if (p.length() > 0) {
                                    String maybe = "";
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
                                        int points;
                                        try {
                                            points = Integer.parseInt(buf.readLine());
                                        } catch (NumberFormatException e) {
                                            System.out.println("Error! Point value must be an integer.");
                                            break;
                                        }
                                        maybe = buf.readLine();
                                        if (!maybe.equals("END OF QUIZ")) {
                                            q = false;
                                            i--;
                                        }
                                        tempQuestions.add(new Questions(question, option1, option2, option3, option4,
                                                answer, points));
                                    }
                                    quizzes.add(new Quizzes(tempQuestions, quizName));
                                }
                                p = buf.readLine();
                            }
                            //writer.write("END OF QUIZ\n");
                            buf.close();
                            //writer.close();
                        } else {
                            System.out.println("Error! File Not Found.");
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
                            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
                                    "QuizInfo.txt", false)));
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
                    //IF TEACHER CHOOSES TO VIEW SUBMISSIONS
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


                        System.out.println("Please input the student's username: ");
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

                        //IF THERE ARE QUIZZES, BUT THE INPUTTED NAME DOESN'T MATCH ANY
                        //}
                    }
                    //IF THE ARRAYLIST OF QUIZZES IS SIZE 0, PRINT AN ERROR MESSAGE AND TRY AGAIN
                    if (quizzes.size() == 0) {
                        System.out.println("You need to create a quiz before you can see one!");
                    }

                } else if (options == 7) {
                    System.out.println("What would you like your new username to be?");
                    String newUser = scan.nextLine();
                    System.out.println("What would you like your new password to be?");
                    String newPass = scan.nextLine();
                    lo.editAccount(newUser, newPass);
                } else if (options == 8) {
                    lo.deleteAccount();
                    System.out.println("Account Deleted.\nGoodbye!");
                    teacher = false;
                } else {
                    System.out.println("That is not a valid option! Please enter a number 1-7.");
                }

            }
        }
        boolean student = true;


        if (user.equalsIgnoreCase("Student")) {
            System.out.println("Hello student");
            while (student) {
                System.out.println("What would you like to do?");
                System.out.println("1. Log out\n" + "2. Take a quiz\n" + "3. See your submission\n" +
                        "4. Edit account\n" + "5. Delete account");
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
                        String longString = "";
                        studentAnswer = new ArrayList<String>();
                        //PRINTS EACH QUESTION AND OPTIONS, THEN STORES STUDENTS ANSWERS IN ARRAYLIST "STUDENTANSWER"
                        for (int i = 0; i < quizzes.get(quizNum - 1).getQuestions().size(); i++) {
                            boolean askAgain = false;
                            String guess = "";
                            do {
                                askAgain = false;
                                System.out.println("Question " + (i + 1) + ":");
                                System.out.println(quizzes.get(quizNum - 1).getQuestions().get(i).getQuestion());
                                System.out.println("a) "
                                        + quizzes.get(quizNum - 1).getQuestions().get(i).getOption1());
                                System.out.println("b) "
                                        + quizzes.get(quizNum - 1).getQuestions().get(i).getOption2());
                                System.out.println("c) "
                                        + quizzes.get(quizNum - 1).getQuestions().get(i).getOption3());
                                System.out.println("d) "
                                        + quizzes.get(quizNum - 1).getQuestions().get(i).getOption4());

                                System.out.println("If you would like to attach a file as your answer, " +
                                        "enter \"file\", " +
                                        "otherwise, enter a, b, c or d");
                                guess = scan.nextLine();

                                if (!(guess.equals("a") || guess.equals("b") || guess.equals("c") || guess.equals("d")
                                        || guess.equals("file"))) {
                                    System.out.println("Error! Invalid option.");
                                    askAgain = true;
                                }
                            } while (askAgain);

                            if (guess.equalsIgnoreCase("file")) {

                                System.out.println("please input the name of the file");
                                String file = scan.nextLine();

                                ArrayList<String> list = new ArrayList<>();
                                File f = new File(file);

                                try {

                                    FileReader fr = new FileReader(f);
                                    BufferedReader bfr = new BufferedReader(fr);
                                    String line = bfr.readLine();

                                    while (line != null) {
                                        list.add(line);
                                        line = bfr.readLine();
                                    }

                                    bfr.close();

                                } catch (FileNotFoundException e) {
                                    throw e;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                //for each new line, a '/' is added for Grading.java to read
                                //print longString to know the format of file submission of student
                                for (String a : list) {
                                    longString += a + "/";
                                }

                                studentAnswer.add(longString);

                            } else {

                                studentAnswer.add(guess);

                            }
                        }
                        System.out.println("Would you like to submit? (yes/no)");
                        String submit = scan.nextLine();

                        if (submit.equalsIgnoreCase("no") || submit.equalsIgnoreCase("n")) {
                            System.out.println("Alright. Your quiz will not be submitted.");
                            //studentAnswers.remove(index);
                            continue;
                        } else {
                            //Setup input for the quiz to be automatically graded
                            ArrayList<String> correctAnswerList = new ArrayList<String>();
                            ArrayList<Integer> PointList = new ArrayList<Integer>();

                            for (int j = 0; j < quizzes.get(quizNum - 1).getQuestions().size(); j++) {
                                correctAnswerList.add(quizzes.get(quizNum - 1).getQuestions().get(j).getAnswer());
                                PointList.add(quizzes.get(quizNum - 1).getQuestions().get(j).getPoints());
                            }

                            Grading testGrade = new Grading(quizzes.get(quizNum - 1).getQuestions(),
                                    correctAnswerList, PointList);
                            submission = testGrade.autoGrade(studentAnswer,
                                    quizzes.get(quizNum - 1).getName(), userName);

                            lo.addSubmission(submission);

                        }

                        System.out.println("Quiz submitted!");
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

                        String name = userName;
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

                } else if (options == 4) {
                    System.out.println("What would you like your new username to be?");
                    String newUser = scan.nextLine();
                    System.out.println("What would you like your new password to be?");
                    String newPass = scan.nextLine();
                    lo.editAccount(newUser, newPass);
                } else if (options == 5) {
                    lo.deleteAccount();
                    System.out.println("Account Deleted.\nGoodbye!");
                    student = false;
                } else {
                    System.out.println("That is not a valid option! Please enter a number 1-3");
                }
            }
        }
    }
}