/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package payrollControlBreak;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import dao.LineSequential;

/**
 *
 * @author Clarence
 */
public class PayrollControlBreak {

        static String inFile = "e:/yao/classwork/PayrollControlBreak/PayrollMaster.dat";
        static String inFileStreamName = "payrollMaster";
        static String outFile = "e:/yao/classwork/PayrollControlBreak/PayrollListing.dat";
        static String outFileStreamName = "payrollList";
        static SimpleDateFormat dateFormatStandard = new SimpleDateFormat("M/d/YYYY");
        static Date today = new Date();
        static String heading1String = moveSpaces(19) + "Payroll Listing" + moveSpaces(16)
                + dateFormatStandard.format(today) + moveSpaces(22);
        static String heading2String = "EMP. No. ";
        static int [] employeeRecordInMarks = {5, 25, 27,29, 35};
        static int [] employeeRecordOutMarks = {5, 9, 29, 37, 39, 46, 54};
        static String blankRecord = moveSpaces(80);
        static DecimalFormat annualSalaryFormat = new DecimalFormat("###,##0");
        static Employee employee = new Employee();
        static String inputLine;
        static StringBuilder employeeRecordOut;
        
        static int controlBreakTerritoryNumber;
        static int totalAnnualSalary;
        static StringBuilder summaryRecordOut;
        static int [] summaryRecordOutMarks = {24, 41, 42, 44, 54};
        static DecimalFormat totalAnnualSalaryFormat = new DecimalFormat("#, ###, ##0");
        static String summaryString = "Total Territory"; 
        

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        initialization();
        setControlBreakField();
        
        
        while((inputLine = LineSequential.read(inFileStreamName)) != null)
        {
            moveFields();
        }
        performControlBreak();
        
        terminationRoutine();
        
    }

    private static void initialization(){
        LineSequential.open(inFile, inFileStreamName, "input");
        LineSequential.open(outFile, outFileStreamName, "output");
        writeHeadings();
    }
    
    private static void writeHeadings(){
        LineSequential.write(outFileStreamName, blankRecord);
        LineSequential.write(outFileStreamName, heading1String);
        LineSequential.write(outFileStreamName, blankRecord);

    }
    
    static void setControlBreakField(){
        inputLine = LineSequential.read(inFileStreamName); //Primaring Read
        employeeRecordOut = new StringBuilder(blankRecord); // wroutes out inital line) 
        initializeEmployeeFields();                         //( or records
        makeEmployeeRecord();
        LineSequential.write(outFileStreamName, employeeRecordOut.toString());
        controlBreakTerritoryNumber = employee.getTerritoryNumber(); //sets control break voidble to territory number
        totalAnnualSalary = employee.getAnnualSalary(); // summary information of total annual salary.
        
    }
   
    private static void moveFields(){
        employeeRecordOut = new StringBuilder(blankRecord);
        initializeEmployeeFields();
        if(controlBreakTerritoryNumber != employee.getTerritoryNumber()) // If true we have a control a break.
        {
            performControlBreak();
        }
        makeEmployeeRecord();
        LineSequential.write(outFileStreamName, employeeRecordOut.toString());
        totalAnnualSalary += employee.getAnnualSalary();
    }
    
    static void performControlBreak() {
        makeSummaryLine(); //create summarry line
        LineSequential.write(outFileStreamName, summaryRecordOut.toString()); // write it and a blank line to the file.
        LineSequential.write(outFileStreamName, blankRecord);
        controlBreakTerritoryNumber = employee.getTerritoryNumber(); // update control break field
        totalAnnualSalary = 0;  //update total annual salary to zero. 
        
    }
    
    private static void initializeEmployeeFields(){
        employee.setEmployeeNumber(Integer.valueOf(inputLine.substring(0, employeeRecordInMarks[0])));
        employee.setEmployeeName(inputLine.substring(employeeRecordInMarks[0], employeeRecordInMarks[1]));
        employee.setTerritoryNumber(Integer.valueOf(inputLine.substring(employeeRecordInMarks[1], employeeRecordInMarks[2])));
        employee.setAnnualSalary(Integer.valueOf(inputLine.substring(employeeRecordInMarks[3], employeeRecordInMarks[4])));
    }
    
    private static void makeEmployeeRecord(){
        String employeeNumberString = String.valueOf(employee.getEmployeeNumber());
        String employeeName = employee.getEmployeeName();
        String territoryNumberString = String.valueOf(employee.getTerritoryNumber());
        String annualSalaryString = annualSalaryFormat.format(employee.getAnnualSalary());
        employeeRecordOut.replace(employeeRecordOutMarks[0] - employeeNumberString.length(), employeeRecordOutMarks[0], employeeNumberString);
        employeeRecordOut.replace(employeeRecordOutMarks[1], employeeRecordOutMarks[2], employeeName);
        employeeRecordOut.replace(employeeRecordOutMarks[4] - territoryNumberString.length(), employeeRecordOutMarks[4], territoryNumberString);
        employeeRecordOut.replace(employeeRecordOutMarks[5], employeeRecordOutMarks[5] + 1, "$");
        employeeRecordOut.replace(employeeRecordOutMarks[6] - annualSalaryString.length(), employeeRecordOutMarks[6], annualSalaryString);
    }
    
    static void makeSummaryLine() 
    {
        summaryRecordOut = new StringBuilder(blankRecord);
        String terrritoryNumberString = String.valueOf(controlBreakTerritoryNumber); // must use the control break field or else we get the wrong territory number
        String totalAnnualSalaryString = totalAnnualSalaryFormat.format(totalAnnualSalary);
        summaryRecordOut.replace(summaryRecordOutMarks[0], summaryRecordOutMarks[1], summaryString);
        summaryRecordOut.replace(summaryRecordOutMarks[2] - terrritoryNumberString.length(), summaryRecordOutMarks[2], terrritoryNumberString);
        summaryRecordOut.replace(summaryRecordOutMarks[3], summaryRecordOutMarks[3] + 1, "$");
        summaryRecordOut.replace(summaryRecordOutMarks[4] - totalAnnualSalaryString.length(), summaryRecordOutMarks[4], totalAnnualSalaryString);
    }
    
    private static void terminationRoutine(){
        LineSequential.close(inFileStreamName, "input");
        LineSequential.close(outFileStreamName, "output");
        System.out.println("File is complete.");
    }
    
    private static String moveSpaces(int numberOfSpaces){
        StringBuilder sb1 = new StringBuilder(numberOfSpaces);
        for(int i = 0; i < numberOfSpaces; i++)
        {
            sb1.append(" ");
        }
        
        return sb1.toString();
    }    
}
