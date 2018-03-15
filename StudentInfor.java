/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * class used for tableview
 *
 * @author mingfeng
 */
public class StudentInfor {

    private final StringProperty visitdate;//date
    private final StringProperty ids;//ID for visit time
    private final StringProperty name;//name
    private final StringProperty program;//program
    private final StringProperty gender;//gender
    private final StringProperty reason;//visiting reason

    /**
     * constructor
     *
     * @param visitdate date
     * @param ids visiting label
     * @param name name
     * @param program program
     * @param gender gender
     * @param reason visiting reason
     */
    public StudentInfor(String visitdate, String ids, String name, String program, String gender, String reason) {
        this.visitdate = new SimpleStringProperty(visitdate);
        this.ids = new SimpleStringProperty(ids);
        this.name = new SimpleStringProperty(name);
        this.program = new SimpleStringProperty(program);
        this.gender = new SimpleStringProperty(gender);

        this.reason = new SimpleStringProperty(reason);

    }

    /**
     * getter for date
     *
     * @return String
     */
    public String getVisitdate() {
        return visitdate.get();
    }

    /**
     * getter for label
     *
     * @return String
     */
    public String getIds() {
        return ids.get();
    }

    /**
     * getter for name
     *
     * @return String
     */
    public String getName() {
        return name.get();
    }

    /**
     * getter for program
     *
     * @return String
     */
    public String getProgram() {
        return program.get();
    }

    /**
     * getter for gender
     *
     * @return String
     */
    public String getGender() {
        return gender.get();
    }

    /**
     * getter for reason
     *
     * @return String
     */
    public String getReason() {
        return reason.get();
    }

    /**
     * setter for name
     *
     * @param value String
     */
    public void setName(String value) {
        name.set(value);
    }

    /**
     * setter for date
     *
     * @param value String
     */
    public void setVisitdate(String value) {
        visitdate.set(value);
    }

    /**
     * setter for ID
     *
     * @param value String
     */
    public void setIds(String value) {
        ids.set(value);
    }

    /**
     * setter for program
     *
     * @param value String
     */
    public void setProgram(String value) {
        program.set(value);
    }

    /**
     * setter for gender
     *
     * @param value String
     */
    public void setGender(String value) {
        gender.set(value);
    }

    /**
     * setter for reason
     *
     * @param value String
     */
    public void setReason(String value) {
        reason.set(value);
    }

    /**
     * getter for date
     *
     * @return StringProperty
     */
    public StringProperty visitdateProperty() {
        return visitdate;
    }

    /**
     * getter for ID
     *
     * @return StringProperty
     */
    public StringProperty idsProperty() {
        return ids;
    }

    /**
     * getter for name
     *
     * @return StringProperty
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * getter for program
     *
     * @return StringProperty
     */
    public StringProperty programProperty() {
        return program;
    }

    /**
     * getter for gender
     *
     * @return StringProperty
     */
    public StringProperty genderProperty() {
        return gender;
    }

    /**
     * getter for reason
     *
     * @return StringProperty
     */
    public StringProperty reasonProperty() {
        return reason;
    }
}
