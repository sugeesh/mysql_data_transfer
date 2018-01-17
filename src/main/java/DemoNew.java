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
public class DemoNew {

    public static void main(String[] args) {
        Connection connectionDatabase1 = null;
        Connection connectionDatabase2 = null;
        try {
            connectionDatabase1 = DBConnection.getDBConnection("jdbc:mysql://localhost/test1?useUnicode=yes&characterEncoding=UTF-8", "root", "").getConnection();
            connectionDatabase2 = DBConnection.getDBConnection("jdbc:mysql://localhost/new_test?useUnicode=yes&characterEncoding=UTF-8", "root", "").getConnection();

        } catch (ClassNotFoundException e) {
            printErrorOutput("Error in connecting databases", e);
        } catch (SQLException e) {
            printErrorOutput("Error in connecting databases", e);
        }


        ResultSet resultSet = null;
        try {
            resultSet = DBHandle.getData(connectionDatabase1, "SELECT * FROM wp_posts");
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
                String post_parent = resultSet.getString("post_parent");
                String guid = resultSet.getString("guid");
                String post_type = resultSet.getString("post_type");

                DateFormat dateFormatReference = new SimpleDateFormat("yyyy-MMMM-dd");
                String post_date_string = dateFormatReference.format(post_date) + "-news-gov-" + id;

                if ("post".equals(post_type) && "publish".equals(post_status)) {

                    String sqlL = "SELECT p.ID , p.guid, t.name as office_or_sector_name, t.slug as office_or_sector_slug, t.term_id\n" +
                            "FROM wp_posts p\n" +
                            "LEFT JOIN wp_term_relationships rel ON rel.object_id = p.ID\n" +
                            "LEFT JOIN wp_term_taxonomy tax ON tax.term_taxonomy_id = rel.term_taxonomy_id\n" +
                            "LEFT JOIN wp_terms t ON t.term_id = tax.term_id\n" +
                            "WHERE p.ID = " + id;

                    ResultSet resultSetL = DBHandle.getData(connectionDatabase1, sqlL);

                    int sector = 0;

                    while (resultSetL.next()) {
                        int term_id = resultSetL.getInt("term_id");

                        if (term_id == 6) {
                            sector = 2;
                            break;
                        } else if (term_id == 7) {
                            sector = 3;
                            break;
                        } else if (term_id == 8) {
                            sector = 5;
                            break;
                        } else if (term_id == 9) {
                            sector = 4;
                            break;
                        } else if (term_id == 10) {
                            sector = 3;
                            break;
                        } else if (term_id == 11) {
                            sector = 9;
                            break;
                        } else if (term_id == 13) {
                            sector = 6;
                            break;
                        } else if (term_id == 14) {
                            sector = 20;
                            break;
                        } else if (term_id == 15) {
                            sector = 19;
                            break;
                        } else if (term_id == 16) {
                            sector = 19;
                            break;
                        } else if (term_id == 17) {
                            sector = 7;
                            break;
                        } else if (term_id == 18) {
                            sector = 6;
                            break;
                        } else if (term_id == 19) {
                            sector = 11;
                            break;
                        } else if (term_id == 20) {
                            sector = 14;
                            break;
                        } else if (term_id == 21) {
                            sector = 8;
                            break;
                        } else if (term_id == 24) {
                            sector = 13;
                            break;
                        } else if (term_id == 26) {
                            sector = 18;
                            break;
                        }

                    }

                    if (sector != 0) {

                        if (post_title.toCharArray().length > 180) {
                            PreparedStatement stmt1 = connectionDatabase2.prepareStatement(
                                    "INSERT INTO news (title, refNo, preface, description, categoryId, sectorId, linkId, publishStatusId, lan, cnt, created_at, updated_at) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
                            stmt1.setString(1, post_title);//1 specifies the first parameter in the query
                            stmt1.setString(2, post_date_string);
                            stmt1.setString(3, post_title);
                            stmt1.setString(4, post_content);
                            stmt1.setInt(5, 1);
                            stmt1.setInt(6, sector);
                            stmt1.setInt(7, 1);
                            stmt1.setInt(8, 1);
                            stmt1.setInt(9, 0);
                            stmt1.setString(10, "news");

                            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            java.util.Date date1 = new java.util.Date();
                            stmt1.setString(11, dateFormat1.format(date1));
                            stmt1.setString(12, dateFormat1.format(date1));
                            stmt1.executeUpdate();
                        } else {


                            PreparedStatement stmt = connectionDatabase2.prepareStatement(
                                    "INSERT INTO news (title, titleL, refNo, preface, description, categoryId, sectorId, linkId, publishStatusId, lan, cnt, created_at, updated_at) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
                            stmt.setString(1, post_title);//1 specifies the first parameter in the query
                            stmt.setString(2, post_title);
                            stmt.setString(3, post_date_string);
                            stmt.setString(4, post_title);
                            stmt.setString(5, post_content);
                            stmt.setInt(6, 1);
                            stmt.setInt(7, sector);
                            stmt.setInt(8, 0);
                            stmt.setInt(9, 1);
                            stmt.setInt(10, 0);
                            stmt.setString(11, "news");

                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            java.util.Date date = new java.util.Date();
                            stmt.setString(12, dateFormat.format(date));
                            stmt.setString(13, dateFormat.format(date));
                            stmt.executeUpdate();
                        }
                    }
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



