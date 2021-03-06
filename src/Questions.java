import java.io.Serializable;

/**
 * Questions
 *
 * Creates a constructor containing the information for one question
 *
 * @author Ian Fienberg, L15
 *
 * @version 4/11/2022
 *
 */
public class Questions implements Serializable {
    public String question;
    public String option1;
    public String option2;
    public String option3;
    public String option4;
    public String answer; //meant to be a, b, c, or d
    public int points;

    public Questions(String question, String option1, String option2, String option3, String option4, String answer,
                     int points) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.points = points;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
