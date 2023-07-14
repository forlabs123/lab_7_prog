package org.example.ServerMoudle.commands;

public class ManagerResult {
    private static StringBuilder stringBuilder = new StringBuilder();
    public static void write(String s){
        if (!stringBuilder.toString().contains(s)) {
            stringBuilder.append(s + System.lineSeparator());
        }
    }
    public static String getResult(){
        return stringBuilder.toString();
    }
    public static void clear(){
        stringBuilder.setLength(0);
    }
}
