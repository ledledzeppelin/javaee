package edu.whu.framework;

public class Application {
    public static void main(String[] args) {
        try {
            BootstrapService.start("/myapp.properties");
        } catch (BootstrapException e) {
            System.out.println(e.getErrorType()+":"+e.getMessage());
        }
    }

}