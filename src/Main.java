/***********************************
 * Names: Zach Coolidge/Jose Blanco*
 ****** Class:CS490*****************
 ****** Term:Spring 2023************
 ****** Assignment: Java Team Project*******
 ********************************/

import javax.swing.*;
import java.util.Scanner;
import java.sql.*;
public class Main {
    public static void main(String[] args) throws SQLException {
        Connection con = DbConnection.connect();
        Scanner in = new Scanner(System.in);
        char input = ' ';
        DbConnection.connect();
        while(input != 'q'){
            println(print_menu());
            print("Choose Option: ");
            input = in.next().charAt(0);
        switch (input){
            case 'a':
                JTextField cust_id = new JTextField();
                JTextField first_name = new JTextField();
                JTextField last_name = new JTextField();
                JTextField email = new JTextField();
                JTextField date = new JTextField();
                JTextField phone = new JTextField();
                JTextField zipcode = new JTextField();
                Object [] fields = {"ID", cust_id,"First Name", first_name,"Last Name", last_name, "Email", email, "Date", date, "Phone", phone, "Zipcode", zipcode};

                JFrame jf = new JFrame();
                jf.setAlwaysOnTop(true); // makes sure the frame pops to the top of screen

                JOptionPane.showConfirmDialog(jf, fields, "Insert Record", JOptionPane.OK_CANCEL_OPTION); // cancels when you click the button ok

                addCustData(con, Integer.parseInt(cust_id.getText()), first_name.getText(), last_name.getText(),email.getText(), date.getText(), phone.getText(),Integer.parseInt(zipcode.getText()));// getting the text from the input line 18-21
                // error catching if someone puts a ! instead of id number
                break;
            case 'd':
                JTextField remove_id = new JTextField();

                Object [] removeField = {"ID", remove_id};

                JFrame jf2 = new JFrame();
                jf2.setAlwaysOnTop(true); // makes sure the frame pops to the top of screen

                JOptionPane.showConfirmDialog(jf2, removeField, "Insert ID", JOptionPane.OK_CANCEL_OPTION); // cancels when you click the button ok

                removeCustData(Integer.parseInt(remove_id.getText()), con);// getting the text from the input line 18-21
                // error catching if someone puts a ! instead of id number
                break;
            case 'm':
                int choice;
                do {
                    println("MENU");
                    println("1 - Modify first name");
                    println("2 - Modify last name");
                    println("3 - Modify email");
                    println("4 - Modify customer date");
                    println("5 - Modify phone number");
                    println("6 - Modify zip code");
                    println("7 - Modify customer ID");
                    println("8 - Return to main menu");
                    print("Choose option: ");
                    choice = in.nextInt();
                    if(choice!=8) {
                        cust_id = new JTextField();
                        fields = new Object[]{"ID", cust_id};

                        jf = new JFrame();
                        jf.setAlwaysOnTop(true); // makes sure the frame pops to the top of screen

                        JOptionPane.showConfirmDialog(jf, fields, "Insert Record", JOptionPane.OK_CANCEL_OPTION); // cancels when you click the button ok
                        modCust(choice, con, Integer.parseInt(cust_id.getText()));
                    }
                }while(choice!=8);
                break;
            case 'f':
                JTextField cust_name = new JTextField();
                fields = new Object[]{"Name", cust_name};

                jf = new JFrame();
                jf.setAlwaysOnTop(true);
                JOptionPane.showConfirmDialog(jf, fields, "Insert Record", JOptionPane.OK_CANCEL_OPTION);
                println(findCustomer(cust_name.getText(),con));
                break;
            case 'p':
                println(printCustData(con));
                break;
            case 'n':
                println("You have "+ customerCount(con)+ " customers");
            default: println("Invalid entry. Try again.");
        }


        }
        con.close();
        println("Program terminated.");
    }

    public static class DbConnection { //sets a connection up with DB browser and opens a specific file
        private static final String data_base_location = "jdbc:sqlite:C:\\Users\\zachc\\Software Development\\sql\\customer_info.db"; //java database connection
        //C:\Users\Jose0\Documents\customer_info.db
        public static Connection connect(){
            Connection con = null; //creating a connection object con and setting it to null
            try {
                con = DriverManager.getConnection(data_base_location);// goes out to the file and open a connection to the file
                println("Connected");
            } catch(SQLException e){println(e+""); //e is a generic error message from SQLException
        }
            return con;
    }

        }
    public static String print_menu(){
        String menu_output = " ";
        menu_output += " \n MENU\na - Add new customer\nd - Delete customer\nm - Modify Customer\np - Print customer information\nf - Find customer\nn - Number of customers\nq - Quit\n\n";
        return menu_output;
    }
    public static void addCustData(Connection con, int custid, String fname, String lname, String email, String date, String phone, int zip){
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO customer_info(ID, 'First Name', 'Last Name', Email, Date, Phone, Zipcode) VALUES(?,?,?,?,?,?,?)"; // commands that it is sending to sql
            ps = con.prepareStatement(sql);

            ps.setInt(1,custid);
            ps.setString(2,fname);
            ps.setString(3, lname);
            ps.setString(4, email);// make sure the numbers are the exact values of columns in database, this will be put in column 4
            ps.setString(5,date);
            ps.setString(6, phone);
            ps.setInt(7,zip);
            ps.execute();
            println("Data inserted");
        }catch (SQLException e){println(e+"");
    } finally {
            try {
                assert ps != null; // confirms the ps is not null
                ps.close();
            } catch (SQLException e){
                println(e+"");
            }
        }
    }
    public static void removeCustData(int custId, Connection con){
        PreparedStatement ps = null;
        try {
            String sql = ("DELETE FROM customer_info WHERE ID="+custId);// commands that it is sending to sql
            ps = con.prepareStatement(sql);
            ps.execute();
            println("Data deleted");
        }catch (SQLException e){println(e+"");
        } finally {
            try {
                assert ps != null; // confirms the ps is not null
                ps.close();
            } catch (SQLException e){
                println(e+"");
            }
        }
    }
    public static String printCustData(Connection con){
        StringBuilder menuDist= new StringBuilder();
        String sql = ("SELECT * FROM customer_info");
        Statement stm;
        try{
            stm=con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                menuDist.append(rs.getInt("ID")).append(" | ").append(rs.getString("First Name")).append(" | ").append(rs.getString("Last Name")).append(" | ").append(rs.getString("Email")).append(" | ").append(rs.getString("Date")).append(" | ").append(rs.getString("Phone")).append(" | ").append(rs.getInt("Zipcode")).append("\n");
            }

        }catch (SQLException e){println(e+"");
        }
        return menuDist.toString();

    }
    public static int customerCount(Connection con){
        int count=0;
        String sql = ("SELECT * FROM customer_info");
        Statement stmnt;
        try{
            stmnt=con.createStatement();
            ResultSet rs = stmnt.executeQuery(sql);
            while(rs.next()){
                count++  ;
            }

        }catch (SQLException e){println(e+"");
        }
        return count;
    }
    public static String findCustomer(String name, Connection con){
        StringBuilder customer= new StringBuilder();
        String sql = ("SELECT * FROM customer_info");

        Statement stm= null;
        try{
            stm=con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()) {
                if(rs.getString("First Name").equals(name)){
                    customer.append(rs.getInt("ID")).append(" | ").append(rs.getString("First Name")).append
                            (" | ").append(rs.getString("Last Name")).append(" | ").append(rs.getString("Email")).append(" | ").append(rs.getString("Date")).append(" | ").append(rs.getString("Phone")).append(" | ").append(rs.getInt("Zipcode")).append("\n");
                }
                if(rs.getString("Last Name").equals(name)&&!(rs.getString("First Name").equals(name))){
                    customer.append(rs.getInt("ID")).append(" | ").append(rs.getString("First Name")).append
                            (" | ").append(rs.getString("Last Name")).append(" | ").append(rs.getString("Email")).append(" | ").append(rs.getString("Date")).append(" | ").append(rs.getString("Phone")).append(" | ").append(rs.getInt("Zipcode")).append("\n");
                }
                if((rs.getString("First Name")+" "+rs.getString("Last Name")).equals(name)
                &&!(rs.getString("First Name").equals(name)) &&!(rs.getString("Last Name").equals(name))){
                    customer.append(rs.getInt("ID")).append(" | ").append(rs.getString("First Name")).append
                            (" | ").append(rs.getString("Last Name")).append(" | ").append(rs.getString("Email")).append(" | ").append(rs.getString("Date")).append(" | ").append(rs.getString("Phone")).append(" | ").append(rs.getInt("Zipcode")).append("\n");
                }
            }

        }catch (SQLException e){println(e+"");
        } finally {
            try {
                assert stm != null; // confirms the stm is not null
                stm.close();
            } catch (SQLException e){
                println(e+"");
            }
        }

        return customer.toString();

    }

    public static void modCust(int choice,Connection conn, int cust_id){
        PreparedStatement ps;
        String sql;
        switch (choice) {
            case 1 -> {
                try {
                    JTextField new_fname = new JTextField();
                    Object[] fields = new Object[]{"New Name", new_fname};

                    JFrame jf = new JFrame();
                    jf.setAlwaysOnTop(true); // makes sure the frame pops to the top of screen

                    JOptionPane.showConfirmDialog(jf, fields, "Insert Record", JOptionPane.OK_CANCEL_OPTION);
                    sql = "UPDATE customer_info SET `First Name`='" + new_fname.getText() + "' WHERE ID=" + cust_id;

                    ps = conn.prepareStatement(sql);
                    ps.execute();
                    println("First name edited.");
                } catch (SQLException e) {
                    println("" + e);
                }
            }
            case 2 -> {
                try {
                    JTextField new_lname = new JTextField();
                    Object[] fields = new Object[]{"New Name", new_lname};

                    JFrame jf = new JFrame();
                    jf.setAlwaysOnTop(true); // makes sure the frame pops to the top of screen

                    JOptionPane.showConfirmDialog(jf, fields, "Insert Record", JOptionPane.OK_CANCEL_OPTION);
                    sql = "UPDATE customer_info SET `Last Name`='" + new_lname.getText() + "' WHERE ID=" + cust_id;

                    ps = conn.prepareStatement(sql);
                    ps.execute();
                    println("Last name edited.");
                } catch (SQLException e) {
                    println("" + e);
                }
            }
            case 3 -> {
                try {
                    JTextField new_email = new JTextField();
                    Object[] fields = new Object[]{"New Email", new_email};

                    JFrame jf = new JFrame();
                    jf.setAlwaysOnTop(true); // makes sure the frame pops to the top of screen

                    JOptionPane.showConfirmDialog(jf, fields, "Insert Record", JOptionPane.OK_CANCEL_OPTION);
                    sql = "UPDATE customer_info SET `Email`='" + new_email.getText() + "' WHERE ID=" + cust_id;

                    ps = conn.prepareStatement(sql);
                    ps.execute();
                    println("Email edited.");
                } catch (SQLException e) {
                    println("" + e);
                }
            }
            case 4 -> {
                try {
                    JTextField new_date = new JTextField();
                    Object[] fields = new Object[]{"New Date", new_date};

                    JFrame jf = new JFrame();
                    jf.setAlwaysOnTop(true); // makes sure the frame pops to the top of screen

                    JOptionPane.showConfirmDialog(jf, fields, "Insert Record", JOptionPane.OK_CANCEL_OPTION);
                    sql = "UPDATE customer_info SET `Date`='" + new_date.getText() + "' WHERE ID=" + cust_id;

                    ps = conn.prepareStatement(sql);
                    ps.execute();
                    println("Email edited.");
                } catch (SQLException e) {
                    println("" + e);
                }
            }
            case 5 -> {
                try {
                    JTextField new_phone = new JTextField();
                    Object[] fields = new Object[]{"New Phone Number", new_phone};

                    JFrame jf = new JFrame();
                    jf.setAlwaysOnTop(true); // makes sure the frame pops to the top of screen

                    JOptionPane.showConfirmDialog(jf, fields, "Insert Record", JOptionPane.OK_CANCEL_OPTION);
                    sql = "UPDATE customer_info SET `Phone`='" + new_phone.getText() + "' WHERE ID=" + cust_id;

                    ps = conn.prepareStatement(sql);
                    ps.execute();
                    println("Phone Number edited.");
                } catch (SQLException e) {
                    println("" + e);
                }
            }
            case 6 -> {
                try {
                    JTextField new_zip = new JTextField();
                    Object[] fields = new Object[]{"New Zip code", new_zip};

                    JFrame jf = new JFrame();
                    jf.setAlwaysOnTop(true); // makes sure the frame pops to the top of screen

                    JOptionPane.showConfirmDialog(jf, fields, "Insert Record", JOptionPane.OK_CANCEL_OPTION);
                    sql = "UPDATE customer_info SET `Zipcode`='" + Integer.parseInt(new_zip.getText()) + "' WHERE ID=" + cust_id;

                    ps = conn.prepareStatement(sql);
                    ps.execute();
                    println("Zip code edited.");
                } catch (SQLException e) {
                    println("" + e);
                }
            }
            case 7 -> {
                try {
                    JTextField new_ID = new JTextField();
                    Object[] fields = new Object[]{"New Customer ID", new_ID};

                    JFrame jf = new JFrame();
                    jf.setAlwaysOnTop(true); // makes sure the frame pops to the top of screen

                    JOptionPane.showConfirmDialog(jf, fields, "Insert Record", JOptionPane.OK_CANCEL_OPTION);
                    sql = "UPDATE customer_info SET `ID`='" + Integer.parseInt(new_ID.getText()) + "' WHERE ID=" + cust_id;

                    ps = conn.prepareStatement(sql);
                    ps.execute();
                    println("Zip code edited.");
                } catch (SQLException e) {
                    println("" + e);
                }
                }
            default -> println("Invalid entry. Try again.");
        }

    }
    public static <E> void print(E item){
        System.out.print(item);
    }
    public static <E> void println(E item){
        System.out.println(item);
    }

}//test
