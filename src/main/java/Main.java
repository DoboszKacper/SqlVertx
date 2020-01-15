import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.Scanner;

public class Main{

    private static MySQLConnectOptions connectOptions;
    private static MySQLPool client;
    private static String command;

    public static void Console(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Type your command: \n SelectAll - Shows list of all users \n" +
                " CreateUser - Creates new user \n" +
                " SelectNumber - Shows list of users between given numbers \n" +
                " Delete - Deletes user by name \n Exit - Ends program \n" +
                " Info - Shows list of available commends");
        System.out.print("Command: ");

        do{
            command = scan.nextLine();
            switch (command) {
                case "Info":{
                    System.out.println("Type your command: \n SelectAll - Shows list of all users \n" +
                            " CreateUser - Creates new user \n" +
                            " SelectNumber - Shows list of users between given numbers \n" +
                            " Delete - Deletes user by name \n Exit - Ends program \n" +
                            " Info - Shows list of available commends");
                    System.out.print("Command: ");
                    break;
                }

                case "SelectAll": {
                    SelectAll();
                    try {
                        Thread.sleep(1000);
                        System.out.print("Command: ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                case "CreateUser": {
                    System.out.print("Type name: ");
                    String name = scan.nextLine();
                    System.out.print("Type phone number: ");
                    String phoneNumber = scan.nextLine();
                    CreateUser(name, phoneNumber);
                    try {
                        Thread.sleep(500);
                        System.out.print("Command: ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                case "SelectNumber": {
                    System.out.print("Type min value: ");
                    String minValue = scan.nextLine();
                    System.out.print("Type max value: ");
                    String maxValue =scan.nextLine();
                    SelectNumber(minValue, maxValue);
                    try {
                        Thread.sleep(1000);
                        System.out.print("Command: ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                case "Delete": {
                    System.out.print("Type name to delete: ");
                    String name = scan.nextLine();
                    Delete(name);
                    try {
                        Thread.sleep(1000);
                        System.out.print("Command: ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
                }

                case "Exit": {
                    System.exit(0);
                }

                default: {
                    System.out.println("Wrong command try type again!");
                    System.out.print("Command: ");
                    break;
                }
            }
        }while(true);
    }

    public static void main(String[] args) {
        connectOptions = new MySQLConnectOptions()
                .setPort(3306)
                .setHost("localhost")
                .setDatabase("users")
                .setUser("kacper")
                .setPassword("123");

        //Pool options
        PoolOptions poolOptions = new PoolOptions();

        //Create the client pool
        client = MySQLPool.pool(connectOptions,poolOptions);

        //Execution of all the commends
        Console();
    }
    public static void CreateUser(String name, String phoneNumber){
        Insert(new User(name,phoneNumber));
    }

    //------------------------------------------MySql Pagination----------------------------------------------
    public static void SelectNumber(String lowerLimit, String upperLimit){
        client.query("SELECT * FROM users LIMIT "+lowerLimit+","+upperLimit, res->{
            if(res.succeeded()){
                RowSet<Row> result = res.result();
                for (Row row : result) {
                    System.out.println("User: " + row.getString(1) + " \n Phone number: " + row.getString(2)+"\n");
                }
            }else{
                System.out.println("fail");
                client.close();
            }
        });
    }


    public static void SelectAll() {
        client.query("SELECT * FROM users",res->{
            if(res.succeeded()){
                RowSet<Row> result = res.result();
                for (Row row : result) {
                    System.out.println("User: " + row.getString(1) + " \n Phone number: " + row.getString(2)+"\n");
                }
            }else{
                System.out.println("Failure: " + res.cause().getMessage());
                client.close();
            }
        });
    }

    public static void Delete(String name){
        client.query("DELETE FROM users WHERE name='"+name+"';", res -> {
            if (res.succeeded()) {
                System.out.println("Deleted Successfully");
            } else {
                System.out.println("Failure: " + res.cause().getMessage());
                client.close();
            }
        });
    }

    public static void Insert(User user){
        String name = user.getName();
        String phone_number = user.getPhone_number();
        client.preparedQuery("INSERT INTO users (name, phone_number) VALUES (?, ?)", Tuple.of(name, phone_number), res -> {
            if (res.succeeded()) {
                System.out.println("Inserted Successfully");

            } else {
                System.out.println("Failure: " + res.cause().getMessage());
                client.close();
            }
        });
    }
}
