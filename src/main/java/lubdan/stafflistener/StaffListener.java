package lubdan.stafflistener;

import lubdan.stafflistener.events.*;
import lubdan.stafflistener.util.Type;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.sqlite.JDBC;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.*;


public final class StaffListener extends JavaPlugin {
    private Connection DBconnection;
    private int LogNumber;
    static String path = "/plugins/StaffListener/Staff.db";

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new JDBC());
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File("");
        initializeDatabase(file.getAbsolutePath() + path);
        setLogNumber(file.getAbsolutePath() + path);
        this.getServer().getPluginManager().registerEvents(new Chat(this),this);
        this.getServer().getPluginManager().registerEvents(new Command(this),this);
        this.getServer().getPluginManager().registerEvents(new Teleport(this),this);
        this.getServer().getPluginManager().registerEvents(new GameMode(this),this);
        this.getServer().getPluginManager().registerEvents(new DropItem(this),this);
        this.getServer().getPluginManager().registerEvents(new Join(this),this);
        this.getServer().getPluginManager().registerEvents(new Quit(this),this);
        this.getServer().getPluginManager().registerEvents(new BreakBlock(this),this);
        getCommand("StaffListener").setExecutor(new lubdan.stafflistener.Commands.StaffListener(this));

    }

    public boolean dumpPlayer(String player){
        String sql = "SELECT * FROM STAFF WHERE NAME = ?";
        try{
            PreparedStatement stm = this.DBconnection.prepareStatement(sql);
            stm.setString(1,player);
            ResultSet rs = stm.executeQuery();
            StringBuilder sb = new StringBuilder();
            while(rs.next()){
                sb.append("Type: " + rs.getString("Type") + " | Name: " + rs.getString("Name") + " | Details: " + rs.getString("Content") + " | UUID: " + rs.getString("UUID") + " | X Coordinate: " + rs.getDouble("X_coordinate") + " | Y Coordinate: " + rs.getDouble("Y_coordinate") + " | Z Coordinate: " + rs.getDouble("Z_coordinate") + " | Time: " + rs.getString("Time"));
                sb.append("\n ------------------------ \n");
            }
            if(sb.length() == 0){
                throw new IllegalArgumentException();
            }
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("plugins/StaffListener/Report-%name%.txt".replace("%name%",player)));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            return true;

        }
        catch (Exception ex){
            System.out.println("The following error typically means you requested a report for a player with no logs, capitalization matters. Make sure they have the permission StaffListener.log");
            ex.printStackTrace();
            return false;
        }

    }

    public boolean dumpDatabase(){
        String sql = "SELECT * FROM STAFF";
        try{
            Statement stm = this.DBconnection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            StringBuilder sb = new StringBuilder();
            while(rs.next()){
                sb.append("Type: " + rs.getString("Type") + " | Name: " + rs.getString("Name") + " | Details: " + rs.getString("Content") + " | UUID: " + rs.getString("UUID") + " | X Coordinate: " + rs.getDouble("X_coordinate") + " | Y Coordinate: " + rs.getDouble("Y_coordinate") + " | Z Coordinate: " + rs.getDouble("Z_coordinate") + " | Time: " + rs.getString("Time"));
                sb.append("\n ------------------------ \n");
            }
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("plugins/StaffListener/Report-All.txt"));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            return true;

        }
        catch (Exception ex){
            System.out.println("The following error typically means you need to fix write permissions in the /plugins/StaffListener directory.");
            ex.printStackTrace();
            return false;
        }

    }

    public void writeLog(Type type, String Content, String UUID, String Name, double x, double y, double z, String Time){

        String sql = "INSERT INTO Staff(LogNumber,Type,Content,UUID,Name,X_coordinate,Y_coordinate,Z_Coordinate,Time) VALUES (?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement stm = this.DBconnection.prepareStatement(sql);
            stm.setInt(1,this.LogNumber);
            stm.setString(2,type.toString());
            stm.setString(3,Content);
            stm.setString(4,UUID);
            stm.setString(5,Name);
            stm.setDouble(6,x);
            stm.setDouble(7,y);
            stm.setDouble(8,z);
            stm.setString(9,Time);
            stm.executeUpdate();
            this.LogNumber++;

            System.out.println("Successfully wrote update to database");

        }
        catch (Exception ex){
            System.out.println("Failed to write update to DB");
        }



    }

    private void setLogNumber(String path){
        try{
            String url = "jdbc:sqlite:"+path;
                String sql = "SELECT * FROM Staff";
                Statement stm = this.DBconnection.createStatement();
                ResultSet rs = stm.executeQuery(sql);
                int counter = 0;
                while(rs.next()){
                    counter++;
                }
                this.LogNumber = counter;
        }
        catch (Exception e){
            System.out.println("Staff Listener hit a fatal error..Plugin will not function as intended.");
            e.printStackTrace();
        }
    }

    private void initializeDatabase(String path){
        try{
            String url = "jdbc:sqlite:"+path;
            this.DBconnection = DriverManager.getConnection(url);
                String sql = "CREATE TABLE IF NOT EXISTS Staff(\n"
                        + "LogNumber integer PRIMARYKEY, \n"
                        + "Type text NOT NULL, \n"
                        + "Content text NOT NULL, \n"
                        + "UUID text NOT NULL, \n"
                        + "Name text NOT NULL, \n"
                        + "X_coordinate double NOT NULL, \n"
                        + "Y_coordinate double NOT NULL, \n"
                        + "Z_coordinate double NOT NULL, \n"
                        + "Time text NOT NULL \n"
                        + ");";
                Statement statement = DBconnection.createStatement();
                statement.execute(sql);
        }
        catch (Exception e){
            System.out.println("Staff Listener hit a fatal error..Plugin will not function as intended.");
            e.printStackTrace();
        }
    }


}
