package ui;

abstract class UIHelper {

    static boolean hasGoodParams(int actual, int expected){
        expected++;
        if(actual == expected) {
            return true;
        } else if (actual < expected){
            System.out.println("Not enough arguments entered");
            return false;
        } else {
            System.out.println("Too many arguments entered");
            return false;
        }
    }

    static void printError(int code){
        switch (code){
            case 400:
                System.out.println("Error: bad request");
                break;
            case 401:
                System.out.println("Error: unauthorized");
                break;
            case 403:
                System.out.println("Username already taken");
                break;
            default:
                System.out.println("Unknown error occurred");
                break;
        }
    }
}
