/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.messageApp;

/**
 *
 * @author Bohlale Madigoe
 */
import java.util.List;
import java.util.Scanner;

public class MessageApp {
    private static final MessageManager Manager = new MessageManager();
    private static final Scanner Scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Load test data 
        Manager.loadTestData();
        System.out.println("Welcome to the Messaging System");

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1:
                    sendNewMessage();
                    break;
                case 2:
                    viewStatistics();
                    break;
                case 3:
                    storedMessagesMenu();
                    break;
                case 4:
                    running = false;
                    System.out.println("Exiting... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        Scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. Send New Message");
        System.out.println("2. View Message Statistics");
        System.out.println("3. Stored Messages (fourth option)");
        System.out.println("4. Exit");
    }

    private static void sendNewMessage() {
        System.out.println("\n--- Send New Message ---");
        System.out.print("Sender: ");
        String sender = Scanner.nextLine();
        System.out.print("Recipient: ");
        String recipient = Scanner.nextLine();
        System.out.print("Message: ");
        String text = Scanner.nextLine();
        System.out.print("Flag (Sent/Stored/Disregard): ");
        String flag = Scanner.nextLine();

        Message Message= new Message(sender, recipient, text, flag);
        Manager.addMessage(Message);
        System.out.println("Message added successfully.");
        if (flag.equalsIgnoreCase("Stored")) {
            System.out.println("Message was saved to stored_messages.json");
        }
    }

    private static void viewStatistics() {
        System.out.println("\n--- Message Statistics ---");
        System.out.println("Sent messages: " + Manager.getSentMessages().Size());
        System.out.println("Disregarded messages: " + Manager.getDisregardedMessages().size());
        System.out.println("Stored messages: " + Manager.getAllStoredMessages().size());
        System.out.println("Unique message IDs stored: " + Manager.getMessageIDs().size());
        System.out.println("Unique message hashes stored: " + Manager.getMessageHashes().size());
    }

    private static void storedMessagesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== STORED MESSAGES MENU =====");
            System.out.println("a. Display sender and recipient of all stored messages");
            System.out.println("b. Display the longest stored message");
            System.out.println("c. Search for a message ID");
            System.out.println("d. Search for all stored messages by recipient");
            System.out.println("e. Delete a stored message using its hash");
            System.out.println("f. Display full report of all stored messages");
            System.out.println("g. Back to Main Menu");
            System.out.print("Your choice: ");
            String option = Scanner.nextLine().toLowerCase();

            switch (option) {
                case "a":
                    List<String> pairs = Manager.getStoredSendersAndRecipients();
                    if (pairs.isEmpty()) System.out.println("No stored messages.");
                    else pairs.forEach(System.out::println);
                    break;
                case "b":
                    String longest = Manager.getLongestStoredMessage();
                    if (longest == null) System.out.println("No stored messages.");
                    else System.out.println("Longest stored message: " + longest);
                    break;
                case "c":
                    System.out.print("Enter Message ID: ");
                    String id = Scanner.nextLine();
                    String result = Manager.searchByMessageID(id);
                    if (result == null) System.out.println("Message ID not found.");
                    else System.out.println(result);
                    break;
                case "d":
                    System.out.print("Enter recipient: ");
                    String recipient = Scanner.nextLine();
                    List<String> messages = Manager.searchStoredMessagesByRecipient(recipient);
                    if (messages.isEmpty()) System.out.println("No stored messages for that recipient.");
                    else messages.forEach(System.out::println);
                    break;
                case "e":
                    System.out.print("Enter message hash: ");
                    String hash = Scanner.nextLine();
                    boolean deleted = Manager.deleteStoredMessageByHash(hash);
                    if (deleted) System.out.println("Message successfully deleted.");
                    else System.out.println("Hash not found.");
                    break;
                case "f":
                    System.out.println(Manager.getFullStoredMessagesReport());
                    break;
                case "g":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (!Scanner.hasNextInt()) {
            System.out.print("Invalid number. " + prompt);
            Scanner.next();
        }
        int num = Scanner.nextInt();
        Scanner.nextLine(); 
        return num;
    }

    private static class MessageManager {

        public MessageManager() {
        }

        private void loadTestData() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private Object getSentMessages() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    }
}
