package Test1;
import java.security.Key;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class ATM {
    // key value store by cardID since it is always unique
    Map<String, Account> account = new HashMap<>();
    Scanner input = new Scanner(System.in);

    String loginCardId = "";

    Account loginAcc = new Account();  // to store the account after login


    public void welcomePage(){
        while (true) {
            System.out.println("Welcome to our ATM system!");
            System.out.println("There are two services: ");
            System.out.println("1. Open an account");
            System.out.println("2. Login to your account");
            System.out.println("3. Exit the program");
            System.out.println("Please select your choice: ");
            int choice = input.nextInt();
            switch (choice){
                case 1:
                    // Open an account
                    openAccount();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("Program exited successfully!!!");
                    return;
                default:
                    System.out.println("Please choose 1, 2 or 3 as your selected serbice number does not exist!!!");
                    break;
            }
        }
    }

    private void login() {
        if (account.isEmpty()){
            System.out.println("There is no account in the system yet!!!");
            return;
        }

        // verify account
        System.out.println("Welcome to login page!");
//        String cardIdToPass = "";
        while (true) {
            System.out.println("Please enter your card number: ");
            String cardId = input.next();
            Account acc = account.get(cardId);
            if (acc == null){
                System.out.println("The card number does not exist in the system!");
            }
            else {
                // The card ID matches in the system
                System.out.println("Please enter your password: ");
                String password = input.next();
                if (password.equals(acc.getPassword())){
                    // Need password to complete login
                    loginCardId = cardId;
                    loginAcc = acc;
//                    cardIdToPass = cardId;
                    System.out.println("Login successfully! Your card number is: " + cardId);
                    break;
                }
                else {
                    // if password is wrong, try again
                    System.out.println("The password is incorrect!");
                    System.out.println("Would you like to try again? Y/N");
                    String command = input.next().toUpperCase();
                    if(!command.equals("Y")){
                        return;  // if the user does not want to continue, return to welcome page
                    }
                }
            }
        }  // after complete login...
        loginCommand();

    }

    private void loginCommand() {  // this method is used after login has completed successfully
        while (true) {
            System.out.println("Welcome to service page!");
            System.out.println("You can choose the following services: ");
            System.out.println("1. View Account Details");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. Change Password");
            System.out.println("6. Exit");
            System.out.println("7. Cancel Account");
            System.out.println("Please choose your services: ");
            int command = input.nextInt();
            switch (command){
                case 1:
                    // view details
                    viewAccount();
                    break;
                case 2:
                    // deposit money
                    deposit();
                    break;
                case 3:
                    // withdraw money
                    withdraw();
                    break;
                case 4:
                    // transfer money
                    transfer();
                    break;
                case 5:
                    // change password
                    if (changePassword()){return;}
                    break;
                case 6:
                    // exit
                    loginAcc = null;  // set login account to null when log out!
                    System.out.println("Exit program successfully!");
                    return;
                case 7:
                    // cancel account
                    if (cancelAccount()){return;}
                    break;
                default:
                    System.out.println("The service you chose does not exist, please select again!");
                    break;

            }
        }

    }

    private boolean cancelAccount() {
        System.out.println("Welcome to cancel account page!");
        if (loginAcc.getBalance() != 0){
            System.out.println("You cannot cancel your account when there is money in it!");
            return false;
        }
        // no money in the account
        System.out.println("Are you sure about cancel your account? Y/N");
        String command1 = input.next().toUpperCase();
        if (!command1.equals("Y")){
            return false;
        }

        // user want to cancel account
        while (true) {
            System.out.println("Please enter your password to confirm: ");
            String password = input.next();
            if (password.equals(loginAcc.getPassword())){  // valid password
                account.remove(loginCardId);
                System.out.println("Account canceled!");
                return true;
            }
            else {  // invalid password
                System.out.println("Password incorrect!");
                System.out.println("Would you like to try again? Y/N");
                String command2 = input.next().toUpperCase();
                if (!command2.equals("Y")){
                    return false;
                }
            }
        }


    }

    private boolean changePassword() {
        System.out.println("Welcome to change password page!");
        System.out.println("Are you sure about changing your password? Y/N");
        String command = input.next().toUpperCase();
        if (!command.equals("Y")){
            // user does not actually want to change password
            return false;
        }
        // User want to change password
        // Verify current password
        while (true) {
            System.out.println("Please enter your current password to proceed!");
            String currentPassword = input.next();
            if (currentPassword.equals(loginAcc.getPassword())){
                // password matches
                break;
            }
            else {
                // password does not match
                System.out.println("The password does not match the account number!");
                System.out.println("Would you like to try again? Y/N");
                String command2 = input.next().toUpperCase();
                if (!command2.equals("Y")){
                    // do not want to try again
                    return false;
                }
            }
        }
        // continue when password matches

        while (true) {
            System.out.println("Please enter your new password: ");
            String password = input.next();
            System.out.println("Please enter password again to confirm: ");
            String confirmPassword = input.next();
            if (confirmPassword.equals(password)){
                loginAcc.setPassword(confirmPassword);
                System.out.println("Password updated!");
                System.out.println("Please login again for other services!");
                return true;
            }
            else {
                System.out.println("Password do not match!");
                System.out.println("Would you like to try again? Y/N");
                String command3 = input.next().toUpperCase();
                if (!command3.equals("Y")){return false;}
            }
        }

    }

    private void transfer() {
        if (account.size() < 2){
            System.out.println("There are no other account that you can transfer to!");
            return;
        }

        if (loginAcc.getBalance() <= 0){
            System.out.println("You do not have enough money to transfer!!!");
            return;
        }
        System.out.println("Welcome to transaction page!");

        // verify the other person's account!
        while (true) {
            System.out.println("Please enter the card number of the account that you wanted to transfer to: ");
            String cardId = input.next();
            Account acc = account.get(cardId);
            if (acc == null) {
                // account does not exist in the system
                System.out.println("The card number you entered does not exist in the system!");
                System.out.println("Would you like to try again? Y/N");
                String command1 = input.next().toUpperCase();
                if (!command1.equals("Y")) {
                    return;  // user does not want to continue
                }
            } else {
                // it exists in the system
                System.out.println("The account you want to transfer to is: " + cardId);
                System.out.println("Please confirm to continue! Y/N");
                String command2 = input.next().toUpperCase();
                if (!command2.equals("Y")) {
                    // discontinue!
                    System.out.println("You have selected no, program ends!");
                    System.out.println("You have returned to command page!");
                    return;
                }
            }
            // continue
            System.out.println("Please enter the amount to transfer, your current balance is: R" + loginAcc.getBalance());
            System.out.println("Please enter the amount you want to transfer: ");
            double transferAmount = input.nextDouble();
            if (transferAmount > loginAcc.getBalance()){
                System.out.println("You do not have enough money to transfer!");
                System.out.println("Would you like to try again? Y/N");
                String command3 = input.next().toUpperCase();
                if (!command3.equals("Y")){
                    // do not want to transfer again!
                    return;
                }
            }
            else {
                loginAcc.setBalance(loginAcc.getBalance() - transferAmount);
                acc.setBalance(acc.getBalance() + transferAmount);
                System.out.println("Transaction completed!");
                System.out.println("Your current balance after transfer is: R" + loginAcc.getBalance());
                return;

            }
        }







    }

    private void withdraw() {
        if (loginAcc.getBalance() <= 0){
            // if there is no money in the acc
            System.out.println("There is no money in your account!");
            return;
        }

        System.out.println("Welcome to withdrawal page!");

        while (true) {
            // there is money
            System.out.println("The minimum amount to withdraw is R100!");
            System.out.println("Please enter the amount you want to withdraw: ");
            double takeOut = input.nextDouble();
            if (takeOut < 100){
                System.out.println("The minimum amount to withdraw is R100!");
                System.out.println("Would you like to try again? Y/N");
                String command3 = input.next().toUpperCase();
                if (!command3.equals("Y")){
                    return;
                }
            }
            else {
                if (takeOut <= loginAcc.getBalance()){
                    // if there is enough money to take out
                    if (takeOut <= loginAcc.getLimit()){
                        // the withdrawal amount does not exceed the withdrawal limit
                        loginAcc.setBalance(loginAcc.getBalance() - takeOut);
                        System.out.println("You have withdrawal successfully! The balance in your account is: R" + loginAcc.getBalance());
                        return;
                    }
                    else {
                        // the withdrawal amount exceed the withdrawal limit
                        System.out.println("Sorry, the withdrawal amount exceeds the withdrawal limit!");
                        System.out.println("Your withdrawal limit is: R" + loginAcc.getLimit());
                        System.out.println("Would you like to try again? Y/N");
                        String command1 = input.next().toUpperCase();
                        if (!command1.equals("Y")){
                            return;  // if user does not want to continue to withdraw
                        }

                    }
                }
                else {
                    // if there is no enough money
                    System.out.println("Sorry, you do not have enough balance in your account! " +
                            "The maximum amount that you can withdraw is: " + loginAcc.getBalance());
                    System.out.println("Would you want to try again? Y/N");
                    String command2 = input.next().toUpperCase();
                    if (!command2.equals("Y")){
                        return;  // if user does not want to continue to withdraw
                    }
                }

            }

        }

    }

    private void deposit() {
        System.out.println("Welcome to deposit page!");

        while (true) {
            System.out.println("The minimum amount to deposit is R100!");
            System.out.println("Please enter the amount you want to deposit: ");
            double depositAmount = input.nextDouble();
            if (depositAmount >= 100){
                loginAcc.setBalance(depositAmount);
                System.out.println("You have deposited successfully!!! The balance of your account is: " + loginAcc.getBalance());
                return;
            }
            else {
                System.out.println("The minimum amount to deposit is R100!!!");
                System.out.println("Would you like to continue? (Y/N)");
                char command = input.next().toUpperCase().charAt(0);
                if(command != 'Y'){
                    return;  // if user does not want to continue, return to command page
                }
            }
        }
    }

    private void viewAccount() {
        System.out.println("Welcome to account detail page!");
        System.out.println("The name of the account holder is: " + loginAcc.getFirstName() + " " + loginAcc.getSurname());
        System.out.println("The gender is: " + loginAcc.getGender());
        System.out.println("The deposit limit is: R" + loginAcc.getLimit());
        System.out.println("The balance of the account is: R" + loginAcc.getBalance());

    }

    private void openAccount() {
        System.out.println("Welcome to open account page!");
        System.out.println("Please enter your surname: ");
        Account acc = new Account();
        String surname = input.next();
        acc.setSurname(surname);

        System.out.println("Please enter your first name: ");
        String firstName = input.next();
        acc.setFirstName(firstName);

        while (true) {
            System.out.println("Please enter your gender (M/F): ");
            char gender = input.next().toUpperCase().charAt(0);
            if (gender == 'M' || gender == 'F'){
                acc.setGender(gender);
                break;
            }
            else{
                System.out.println("The gender does not exist, it can only be M(male) or F(female)!!!");
            }
        }

        System.out.println("Please set your withdraw limit: ");
        double limit = input.nextDouble();
        acc.setLimit(limit);

        while (true) {
            System.out.println("Please enter your password: ");
            String password = input.next();
            System.out.println("Please enter your password again: ");
            String confirmPassword = input.next();
            if (password.equals(confirmPassword)){
                acc.setPassword(password);
                break;
            }
            else{
                System.out.println("The password you entered do not match!");
            }
        }

        String cardId = generateCardId();
        account.put(cardId, acc);
        System.out.println("Congratulation, you have opened an account successfully!"
                + "\n" + " Your card number is: " + cardId);
    }

    private String generateCardId() {
        while (true) {
            String cardId = "";
            Random random = new Random();
            for (int i = 0; i < 8; i++) {
                cardId += random.nextInt(10);
            }
            if (account.get(cardId) == null){
                return cardId;
            }
        }


    }
}
