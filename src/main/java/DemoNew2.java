/**
 * @author Sugeesh Chandraweera
 */
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Sugeesh Chandraweera
 */
@SuppressWarnings("ALL")
public class DemoNew2 {

    public static void main(String[] args) {
        Connection connectionDatabase1 = null;
        Connection connectionDatabase2 = null;
        try {
            connectionDatabase1 = DBConnection.getDBConnection("jdbc:mysql://localhost/president_test1?useUnicode=yes&characterEncoding=UTF-8", "root", "").getConnection();
            connectionDatabase2 = DBConnection.getDBConnection("jdbc:mysql://localhost/president_new?useUnicode=yes&characterEncoding=UTF-8", "root", "").getConnection();

//            DBHandle.setData(connectionDatabase1,"INSERT INTO Persons(PersonID,LastName) VALUES('5','සිංහල පංතිය')");
        } catch (ClassNotFoundException e) {
            printErrorOutput("Error in connecting databases", e);
        } catch (SQLException e) {
            printErrorOutput("Error in connecting databases", e);
        }


        ResultSet resultSet = null;
        try {
            resultSet = DBHandle.getData(connectionDatabase1, "SELECT * FROM wp_posts limit 50");
        } catch (SQLException e) {
            printErrorOutput("Error in getting data from database", e);
        }

        try {
            while (resultSet.next()) {

                // Logic here
                Integer id = resultSet.getInt("ID");
                String post_title = resultSet.getString("post_title");
                String post_content = resultSet.getString("post_content");
                Date post_date = resultSet.getDate("post_date");
                String post_excerpt = resultSet.getString("post_excerpt");
                String post_status = resultSet.getString("post_status");
                String comment_status = resultSet.getString("comment_status");
                Integer post_parent = resultSet.getInt("post_parent");
                String guid = resultSet.getString("guid");
                String post_type = resultSet.getString("post_type");

                DateFormat dateFormatReference = new SimpleDateFormat("yyyy-MMMM-dd");
                String post_date_string = dateFormatReference.format(post_date)+"-news-gov-"+id;

                if("attachment".equals(post_type)) {

                    PreparedStatement stmt = connectionDatabase2.prepareStatement("UPDATE news SET featureImageUrl='"+guid+"' WHERE id='"+post_parent+"'");

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    java.util.Date date = new java.util.Date();
                    stmt.setString(12, dateFormat.format(date));
                    stmt.setString(13, dateFormat.format(date));
                    stmt.executeUpdate();
                }

            }
            System.out.println("Successfully Updated.");

        } catch (SQLException e) {
            printErrorOutput("Error in Updating", e);
        }
    }


    static void printErrorOutput(String msg, Exception e) {
        System.out.println(msg);
        e.printStackTrace();
        System.out.println("====================================================================");
    }

}



