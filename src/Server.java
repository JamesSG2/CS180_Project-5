import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.lang.Runnable;

/**
 * Server
 *
 * Receives, stores and retrieves information for multiple Clients.
 *
 * @author James Gilliam, L15
 *
 * @version 5/2/2022
 *
 */
public class Server implements Runnable, Serializable {
    Socket socket;

    public Server(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {

            BufferedReader readClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writeToClient = new PrintWriter(socket.getOutputStream());

            ObjectOutputStream clientObjectOut = new ObjectOutputStream(socket.getOutputStream());
            //GZIPOutputStream clientObjectOut = new GZIPOutputStream(new ObjectOutputStream(socket.getOutputStream()));
            ObjectInputStream clientObjectIn = new ObjectInputStream(socket.getInputStream());

            // Variables necessary from OptionList for the server to work, edited by Zonglin
            ArrayList<Questions> quiz = null;
            ArrayList<String> studentAnswer = null;
            ArrayList<String> correctAnswer = new ArrayList<String>();
            ArrayList<String> submission = new ArrayList<String>();
            ArrayList<String> sub = new ArrayList<String>();

            String userName = "";
            String password = "";
            Account user = new Account(userName, password);  // Initializes account

            ArrayList<Quizzes> quizzes = new ArrayList<>();  // JUST AS IN THE CLIENT

            boolean start = true;
            String userType = "";
            while (start) {
                String sl = readClient.readLine();
                if (sl.equals("0")) {
                    return;
                }
                if (sl.equals("1")) {
                    userName = readClient.readLine();
                    password = readClient.readLine();
                    if (userName == null || password == null) {
                        writeToClient.close();
                        readClient.close();
                        clientObjectIn.close();
                        clientObjectOut.close();
                        socket.close();
                        System.out.println("A client disconnected!");
                        return;
                    }
                    Boolean type = Boolean.parseBoolean(readClient.readLine());

                    user = new Account(userName, password, type); // Creates a new account

                    boolean create = user.isCreated();
                    writeToClient.println(create);
                    writeToClient.flush();
                }

                userName = readClient.readLine();
                password = readClient.readLine();
                if (userName == null || password == null) {
                    writeToClient.close();
                    readClient.close();
                    clientObjectIn.close();
                    clientObjectOut.close();
                    socket.close();
                    System.out.println("A client disconnected!");
                    return;
                }
                user = new Account(userName, password); // Accesses their account

                boolean valid = user.isValid();
                boolean teach = user.isTeacher();
                boolean stud = user.isStudent();

                if (valid) {
                    if (teach) {
                        userType = "Teacher";
                    } else if (stud) {
                        userType = "Student";
                    }
                    start = false;
                }
                writeToClient.println(valid);
                writeToClient.flush();
                writeToClient.println(teach);
                writeToClient.flush();
                writeToClient.println(stud);
                writeToClient.flush();
            }

            Course usersCourse = null;
            boolean courseInvalid = true;
            while (courseInvalid) {

                String courseTitle = readClient.readLine();
                if (courseTitle == null) {
                    writeToClient.close();
                    readClient.close();
                    clientObjectIn.close();
                    clientObjectOut.close();
                    socket.close();
                    System.out.println("A client disconnected!");
                    return;
                }
                // SYNCHRONIZES CREATION OR REJECTION OF A COURSE INTERNALLY
                usersCourse = new Course(courseTitle, userType);

                writeToClient.println(usersCourse.isNewCourseCreated());
                writeToClient.flush();

                if (userType.equals("Teacher")) {
                    courseInvalid = false;
                } else {
                    if (usersCourse.isNewCourseCreated()) {
                        courseInvalid = true;
                    } else {
                        courseInvalid = false;
                    }
                }
            }


            // FILLS THE QUIZZES ARRAYLIST WITH ALL THE QUIZZES IN THE COURSE.
            ArrayList<String> courseQuizTitles = usersCourse.getCourseQuizTitles();

            for (String quizTitle : courseQuizTitles) {
                ArrayList<String> quizText = usersCourse.getQuiz(quizTitle);

                ArrayList<Questions> tempQuestions = new ArrayList<>();

                for (int i = 1; i < quizText.size(); i++) {
                    String question = quizText.get(i);
                    String option1 = quizText.get(++i);
                    String option2 = quizText.get(++i);
                    String option3 = quizText.get(++i);
                    String option4 = quizText.get(++i);
                    String answer = quizText.get(++i);
                    int points = Integer.parseInt(quizText.get(++i));
                    tempQuestions.add(new Questions(question, option1, option2, option3,
                            option4, answer, points));
                }
                quizzes.add(new Quizzes(tempQuestions, quizTitle));
            }
            clientObjectOut.writeObject(quizzes);
            clientObjectOut.flush();

            boolean teacher = true;
            if (userType.equalsIgnoreCase("Teacher")) {
                while (teacher) {
                    int options = 0;
                    try {
                        options = Integer.parseInt(readClient.readLine());
                    } catch (NumberFormatException e) {
                    }

                    if (options == 1) {
                        teacher = false;
                    } else if (options == 2) {
                        //ADDS QUIZ ARRAYLIST AND QUIZ NAME TO QUIZZES ARRAYLIST. ALSO SAVES IT TO THE COURSE
                        if (!Boolean.parseBoolean(readClient.readLine())) {  // if Number Format error
                            quizzes.add((Quizzes) clientObjectIn.readObject());
                            ArrayList<String> quizText = new ArrayList<>();
                            int quizLength = Integer.parseInt(readClient.readLine());
                            for (int j = 0; j < quizLength; j++) {
                                quizText.add(readClient.readLine());
                            }
                            boolean added = usersCourse.addQuiz(quizText);

                            writeToClient.println(added);
                            writeToClient.flush();
                        }
                    } else if (options == 3) {
                        String quizName = "";
                        if (quizzes.size() != 0) {
                            quizName = readClient.readLine();
                        }
                        if (quizName == null) {
                            writeToClient.close();
                            readClient.close();
                            clientObjectIn.close();
                            clientObjectOut.close();
                            socket.close();
                            System.out.println("A client disconnected!");
                            return;
                        }
                        for (int i = 0; i < quizzes.size(); i++) {
                            if (quizzes.get(i).getName().equalsIgnoreCase(quizName)) {
                                ArrayList<String> quizText = usersCourse.getQuiz(quizName);
                                writeToClient.println(quizText.size());
                                writeToClient.flush();
                                for (String line : quizText) {
                                    writeToClient.println(line);
                                    writeToClient.flush();
                                }

                                boolean numErr = Boolean.parseBoolean(readClient.readLine());
                                if (!numErr) {
                                    usersCourse.deleteQuiz(quizName);
                                    quizText = new ArrayList<String>();
                                    int quizLength = Integer.parseInt(readClient.readLine());
                                    for (int j = 0; j < quizLength; j++) {
                                        quizText.add(readClient.readLine());
                                    }
                                    usersCourse.addQuiz(quizText);

                                    quizzes = (ArrayList<Quizzes>) clientObjectIn.readObject();
                                }
                                break;
                            }
                        }
                    } else if (options == 4) {
                        String quizName2 = "";
                        if (quizzes.size() != 0) {
                            quizName2 = readClient.readLine();
                        }
                        for (int i = 0; i < quizzes.size(); i++) {
                            if (quizzes.get(i).getName().equalsIgnoreCase(quizName2)) {
                                String sure = readClient.readLine();
                                if (!sure.equalsIgnoreCase("no")) {
                                    quizzes.remove(i);
                                    usersCourse.deleteQuiz(quizName2);  // DELETES QUIZ FROM THE COURSE
                                }
                                break;
                            }
                        }
                    } else if (options == 5) {
                        String filename = readClient.readLine();

                        // WILL READ THE TEACHER'S FILE AND ADD THEIR QUIZ TO "quizzes" ARRAYLIST
                        ArrayList<String> quizText = new ArrayList<>();

                        File fi = new File(filename);
                        if (fi.exists()) {
                            BufferedReader buf = new BufferedReader(new FileReader(fi));
                            String p = buf.readLine();
                            String quizName = p;
                            quizText.add(quizName);
                            boolean q = true;
                            while (p != null) {
                                if (p.length() > 0) {
                                    String maybe = "";
                                    ArrayList<Questions> tempQuestions = new ArrayList<>();
                                    for (int i = 0; i < 1; i++) {
                                        String question;
                                        if (q) {
                                            question = buf.readLine();
                                            quizText.add(question);
                                        } else {
                                            question = maybe;
                                        }
                                        String option1 = buf.readLine();
                                        String option2 = buf.readLine();
                                        String option3 = buf.readLine();
                                        String option4 = buf.readLine();
                                        String answer = buf.readLine();
                                        quizText.add(option1);
                                        quizText.add(option2);
                                        quizText.add(option3);
                                        quizText.add(option4);
                                        quizText.add(answer);

                                        int points;
                                        try {
                                            points = Integer.parseInt(buf.readLine());
                                        } catch (NumberFormatException e) {
                                            JOptionPane.showMessageDialog(null,
                                                    "Error! Point value must be an integer.",
                                                    "Upload Quiz", JOptionPane.ERROR_MESSAGE);
                                            break;
                                        }
                                        quizText.add("" + points);
                                        maybe = buf.readLine();
                                        if (!maybe.equals("--------------------------------------------------")) {
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
                            buf.close();
                            boolean added = usersCourse.addQuiz(quizText); // SAVES QUIZ TO THE COURSE
                            writeToClient.println(added);
                            writeToClient.flush();
                            clientObjectOut.writeObject(quizzes);
                            clientObjectOut.flush();
                        }
                    } else if (options == 6) {
                        if (quizzes.size() != 0) {
                            int quizNum1 = Integer.parseInt(readClient.readLine());
                            String name = readClient.readLine();
                            String key = readClient.readLine();
                            boolean isValidSubmission = true;
                            int i = 0;
                            try {
                                i = Integer.parseInt(readClient.readLine());
                            } catch (NumberFormatException e) {
                                isValidSubmission = false;
                            }
                            if (isValidSubmission) {
                                isValidSubmission = user.getSubmission(quizzes.get(quizNum1 - 1)
                                        .getName(), name, key, i) != null;
                            }
                            writeToClient.println(isValidSubmission);
                            writeToClient.flush();

                            if (isValidSubmission) {
                                sub = user.getSubmission(quizzes.get(quizNum1 - 1).getName(), name, key, i);

                                writeToClient.println("Attempt #" + i);
                                writeToClient.flush();
                                writeToClient.println(sub.size());
                                writeToClient.flush();

                                for (String v : sub) {
                                    writeToClient.println(v);
                                    writeToClient.flush();
                                }
                            }
                        }
                    } else if (options == 7) {
                        String newUser = readClient.readLine();
                        String newPass = readClient.readLine();
                        if (newUser != null && newPass != null) {
                            user.editAccount(newUser, newPass);
                        }
                    } else if (options == 8) {
                        user.deleteAccount();
                        teacher = false;
                    }
                }
            }


            boolean student = true;

            if (userType.equalsIgnoreCase("Student")) {

                while (student) {

                    int options = Integer.parseInt(readClient.readLine());
                    //scan.nextLine();

                    //STUDENT CHOOSES TO QUIT
                    if (options == 1) {
                        student = false;
                        //STUDENT CHOOSES TO TAKE A QUIZ
                    } else if (options == 2) {

                        int quizNum = Integer.parseInt(readClient.readLine());
                        boolean cancel = false;
                        if (quizNum > 0 && quizNum <= quizzes.size()) {
                            String longString = "";
                            studentAnswer = new ArrayList<String>();
                            // PRINTS EACH QUESTION AND OPTIONS, THEN STORES STUDENTS ANSWERS IN ARRAYLIST
                            // "STUDENTANSWER"
                            for (int i = 0; i < quizzes.get(quizNum - 1).getQuestions().size(); i++) {
                                boolean askAgain = false;
                                String guess = "";
                                do {
                                    askAgain = false;
                                    guess = readClient.readLine();

                                    if (guess != null && !(guess.equals("a") || guess.equals("b") || guess.equals("c") ||
                                            guess.equals("d") || guess.equals("file") || guess.equals("cancel"))) {
                                        askAgain = true;
                                    } else if (guess.equals("cancel")) {
                                        cancel = true;
                                        break;
                                    }
                                } while (askAgain);

                                // TODO: store the file uploaded to the server
                                if (guess.equalsIgnoreCase("file")) {

                                    String file = readClient.readLine();

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

                            if (!cancel) {
                                //System.out.println("Would you like to submit? (yes/no)");
                                String submit = readClient.readLine();


                                if (submit.equalsIgnoreCase("no") || submit.equalsIgnoreCase("n")) {
                                    //System.out.println("Alright. Your quiz will not be submitted.");
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

                                    user.addSubmission(submission);

                                }
                            }
                        }
                    } else if (options == 3) {

                        if (quizzes.size() != 0) {
                            // System.out.println("Which quiz would you like to see?");
                            // PRINTS LIST OF QUIZZES BY NAME
                            for (int i = 0; i < quizzes.size(); i++) {
                                // System.out.println((i + 1) + ". " + quizzes.get(i).getName());
                            }
                            //int quizNum1 = scan.nextInt();
                            int quizNum1 = Integer.parseInt(readClient.readLine());
                            //scan.nextLine();

                            String name = userName;
                            String key = password;

                            boolean validSubmission = true;
                            int i = 0;
                            try {
                                i = Integer.parseInt(readClient.readLine());
                            } catch (NumberFormatException e) {
                                validSubmission = false;
                            }
                            if (validSubmission) {
                                validSubmission = user.getSubmission(quizzes.get(quizNum1 - 1)
                                        .getName(), name, key, i) != null;
                            }
                            writeToClient.println(validSubmission);
                            writeToClient.flush();
                            if (validSubmission) {

                                sub = user.getSubmission(quizzes.get(quizNum1 - 1).getName(), name, key, i);

                                writeToClient.println("Attempt #" + i);
                                writeToClient.flush();
                                writeToClient.println(sub.size());
                                writeToClient.flush();

                                for (String v : sub) {
                                    writeToClient.println(v);
                                    writeToClient.flush();
                                }
                            }
                        }
                    } else if (options == 4) {

                        String newUser = readClient.readLine();
                        String newPass = readClient.readLine();
                        if (newUser != null && newPass != null) {
                            user.editAccount(newUser, newPass);
                        }
                    } else if (options == 5) {
                        user.deleteAccount();
                        student = false;
                    }
                }
            }
            writeToClient.close();
            readClient.close();
            clientObjectIn.close();
            clientObjectOut.close();
            socket.close();
            System.out.println("A client disconnected!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            // Initialize server socket for port 4242
            ServerSocket serverSocket = new ServerSocket(4242);
            System.out.printf("Waiting for connections on %s\n",
                    serverSocket);
            // Infinite server loop to accept a connection and create thread for that client
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("A client connected!");
                Server server = new Server(socket);
                new Thread(server).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
