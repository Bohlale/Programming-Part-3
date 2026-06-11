/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Bohlale Madigoe
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class MessageManager {
    // The five required arrays 
    private List<Message> sentMessages;
    private List<Message> disregardedMessages;
    private List<Message> storedMessages;    
    private List<String> messageHashes;
    private List<String> messageIDs;

    private static final String STORED_FILE = "stored_messages.json";
    private final Gson gson;

    public MessageManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        sentMessages = new ArrayList<>();
        disregardedMessages = new ArrayList<>();
        storedMessages = new ArrayList<>();
        messageHashes = new ArrayList<>();
        messageIDs = new ArrayList<>();

        // 
        loadStoredMessagesFromJSON();
    }

    // -----------------------------------------------------------------
    // Core operattions
    // -----------------------------------------------------------------
    public void addMessage(Message msg) {
        // Update the global ID and hash lists
        messageIDs.add(msg.getMessageID());
        messageHashes.add(msg.getMessageHash());

        // Route to the specific flag array
        switch (msg.getFlag()) {
            case "Sent":
                sentMessages.add(msg);
                break;
            case "Disregard":
                disregardedMessages.add(msg);
                break;
            case "Stored":
                storedMessages.add(msg);
                saveStoredMessagesToJSON();   // persist immediately
                break;
            default:
                throw new IllegalArgumentException("Unknown flag: " + msg.getFlag());
        }
    }

    // -----------------------------------------------------------------
    // JSON persistence for stored messages
    // -----------------------------------------------------------------
    private void saveStoredMessagesToJSON() {
        try (Writer writer = new FileWriter(STORED_FILE)) {
            gson.toJson(storedMessages, writer);
        } catch (IOException e) {
            System.err.println("Error saving stored messages: " + e.getMessage());
        }
    }

    private void loadStoredMessagesFromJSON() {
        File file = new File(STORED_FILE);
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Message>>() {}.getType();
            List<Message> loaded = gson.fromJson(reader, listType);
            if (loaded != null) {
                storedMessages.clear();
                storedMessages.addAll(loaded);
                // Also update the global ID/hash lists from these loaded messages
                for (Message m : loaded) {
                    if (!messageIDs.contains(m.getMessageID())) {
                        messageIDs.add(m.getMessageID());
                        messageHashes.add(m.getMessageHash());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading stored messages: " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------
    // Operations required for the "Stored Messages" menu
    // -----------------------------------------------------------------
    public List<Message> getAllStoredMessages() {
        return Collections.unmodifiableList(storedMessages);
    }

    // a. Display sender and recipient of all stored messages
    public List<String> getStoredSendersAndRecipients() {
        List<String> result = new ArrayList<>();
        for (Message m : storedMessages) {
            result.add("Sender: " + m.getSender() + ", Recipient: " + m.getRecipient());
        }
        return result;
    }

    // b. Longest stored message (by length of message text)
    public String getLongestStoredMessage() {
        if (storedMessages.isEmpty()) return null;
        Message longest = storedMessages.stream()
                .max(Comparator.comparingInt(m -> m.getMessageText().length()))
                .orElse(null);
        return longest != null ? longest.getMessageText() : null;
    }

    // c. Search for a message ID and return recipient + message
    public String searchByMessageID(String messageID) {
        // search in all three collections
        Message found = findMessageByIdInList(sentMessages, messageID);
        if (found == null) found = findMessageByIdInList(disregardedMessages, messageID);
        if (found == null) found = findMessageByIdInList(storedMessages, messageID);
        if (found == null) return null;
        return "Recipient: " + found.getRecipient() + ", Message: " + found.getMessageText();
    }

    private Message findMessageByIdInList(List<Message> list, String id) {
        return list.stream().filter(m -> m.getMessageID().equals(id)).findFirst().orElse(null);
    }

    // d. Search all stored messages for a particular recipient
    public List<String> searchStoredMessagesByRecipient(String recipient) {
        return storedMessages.stream()
                .filter(m -> m.getRecipient().equals(recipient))
                .map(Message::getMessageText)
                .collect(Collectors.toList());
    }

    // e. Delete a stored message using its hash (also removes from JSON)
    public boolean deleteStoredMessageByHash(String hash) {
        Message toRemove = null;
        for (Message m : storedMessages) {
            if (m.getMessageHash().equals(hash)) {
                toRemove = m;
                break;
            }
        }
        if (toRemove != null) {
            storedMessages.remove(toRemove);
            // Also remove from global ID/hash lists
            messageIDs.remove(toRemove.getMessageID());
            messageHashes.remove(toRemove.getMessageHash());
            saveStoredMessagesToJSON();
            return true;
        }
        return false;
    }

    // f. Full report of all stored messages (includes all details)
    public String getFullStoredMessagesReport() {
        if (storedMessages.isEmpty()) return "No stored messages.";
        StringBuilder sb = new StringBuilder("===== STORED MESSAGES REPORT =====\n");
        for (Message m : storedMessages) {
            sb.append("Hash: ").append(m.getMessageHash()).append("\n");
            sb.append("ID: ").append(m.getMessageID()).append("\n");
            sb.append("Sender: ").append(m.getSender()).append("\n");
            sb.append("Recipient: ").append(m.getRecipient()).append("\n");
            sb.append("Message: ").append(m.getMessageText()).append("\n");
            sb.append("Flag: ").append(m.getFlag()).append("\n");
            sb.append("-----------------------------------\n");
        }
        return sb.toString();
    }

    // -----------------------------------------------------------------
    // Getters for arrays (used in unit tests and for UI)
    // -----------------------------------------------------------------
    public List<Message> getSentMessages() { return Collections.unmodifiableList(sentMessages); }
    public List<Message> getDisregardedMessages() { return Collections.unmodifiableList(disregardedMessages); }
    public List<String> getMessageHashes() { return Collections.unmodifiableList(messageHashes); }
    public List<String> getMessageIDs() { return Collections.unmodifiableList(messageIDs); }

    // Helper: get all sent messages formatted for the unit test report
    public String getSentMessagesReport() {
        if (sentMessages.isEmpty()) return "No sent messages.";
        StringBuilder sb = new StringBuilder("===== SENT MESSAGES REPORT =====\n");
        for (Message m : sentMessages) {
            sb.append("Message Hash: ").append(m.getMessageHash()).append("\n");
            sb.append("Recipient: ").append(m.getRecipient()).append("\n");
            sb.append("Message: ").append(m.getMessageText()).append("\n");
            sb.append("-----------------------------------\n");
        }
        return sb.toString();
    }

    // Initialize with the given test data (used in main and unit tests)
    public void loadTestData() {
        // Message 1 (Sent)
        addMessage(new Message("+27830000001", "+27834557896", "Did you get the cake?", "Sent"));
        // Message 2 (Stored)
        addMessage(new Message("+27830000001", "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored"));
        // Message 3 (Disregard)
        addMessage(new Message("+27830000001", "+27834484567", "Yohoooo, I am at your gate.", "Disregard"));
        // Message 4 (Sent) – recipient is the developer number
        addMessage(new Message("0838884567", "0838884567", "It is dinner time!", "Sent"));
        // Message 5 (Stored)
        addMessage(new Message("+27830000001", "+27838884567", "Ok, I am leaving without you.", "Stored"));
    }
}