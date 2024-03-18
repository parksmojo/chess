package ui;

abstract class UIHelper {

    static boolean hasGoodParams(int actual, int expected){
        expected++;
        if(actual == expected) {
            return true;
        } else if (actual < expected){
            System.out.println("Missing arguments");
            return false;
        } else {
            System.out.println("Too many arguments");
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
                System.out.println("Already taken");
                break;
            default:
                System.out.println("Unknown error occurred");
                break;
        }
    }
}
