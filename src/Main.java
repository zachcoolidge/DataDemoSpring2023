import javax.swing.*;
import java.util.Scanner;
import java.sql.*;
public class Main {
    public static void main(String[] args) {//meow meow 
        Scanner sc = new Scanner(System.in);
        char input = ' ';
        DbConnection.connect();
        while(input != 'q'){
            System.out.println(print_menu());
            System.out.print("Choose Option: ");
            input = sc.next().charAt(0); //meow meow

        if((input != 'a'&&input != 'd'&&input != 'q'&&input != 'p'&&input != 'f'&&input != 'n'&&input != 'm'))
            System.out.println("Invalid Entry");
        switch (input){
            case 'a':
                JTextField jstudent_id = new JTextField();
                JTextField jfirst_name = new JTextField();
                JTextField jlast_name = new JTextField();
                JTextField jemail = new JTextField();

                Object [] fields = {"ID", jstudent_id,"First", jfirst_name,"Last", jlast_name, "Email", jemail};

                JFrame jf = new JFrame();
                jf.setAlwaysOnTop(true); // makes sure the frame pops to the top of screen

                JOptionPane.showConfirmDialog(jf, fields, "Insert Record", JOptionPane.OK_CANCEL_OPTION); // cancels when you click the button ok

                add_data(Integer.parseInt(jstudent_id.getText()), jfirst_name.getText(), jlast_name.getText(),jemail.getText());// getting the text from the input line 18-21
                // error catching if someone puts a ! instead of id number
                break;
            case 'd':
                break;
            case 'm':
                break;
            case 'q':
                input = 'q';
                break;
            case 'p':
                break;
        }


        }
        System.out.println("Program terminated.");
    }

    public static class DbConnection { //sets a connection up with DB browser and opens a specific file
        private static final String data_base_location = "jdbc:sqlite:C:\\Users\\zachc\\Software Development\\sql\\example1db.db"; //java database connection
        public static Connection connect(){
            Connection con = null; //creating a connection object con and setting it to null
            try {
                con = DriverManager.getConnection(data_base_location);// goes out to the file and open a connection to the file
                System.out.println("Connected");
            } catch(SQLException e){System.out.println(e+""); //e is a generic error message from SQLException
        }
            return con;
    }

        }
    public static String print_menu(){
        String menu_output = " ";
        menu_output += " \n MENU\nc - Create Database\nt - Create table\na - Add new row\nd - Delete row\nm - Modify Row\np - Print Table\nf - Find Row\nn - Number of Rows\nq - Quit\n\n";
        return menu_output;
    }
    public static void add_data(int studentid, String fname, String lname, String email){
        Connection con = DbConnection.connect();
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO table1(First, Last, Email, ID) VALUES(?,?,?,?)"; // commands that it is sending to sql
            ps = con.prepareStatement(sql);

            ps.setString(1,fname);
            ps.setString(2,lname);
            ps.setString(3, email);
            ps.setInt(4, studentid);// make sure the numbers are the exact values of columns in database, this will be put in column 4
            ps.execute();
            System.out.println("Data inserted");
        }catch (SQLException e){System.out.println(e+"");
    } finally {
            try {
                assert ps != null; // confirms the ps is not null
                ps.close();
                con.close();
            } catch (SQLException e){
                System.out.println(e+"");
            }
        }
    }

}
