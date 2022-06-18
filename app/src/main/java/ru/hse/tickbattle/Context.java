package ru.hse.tickbattle;

public class Context {
    private static String login;
    private static String serverAddress = "192.168.1.4";
    private static int roomPort = 8080;

    public static String getServerAddress() {
        return serverAddress;
    }

    public static int getRoomPort() {
        return roomPort;
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        Context.login = login;
    }
}
