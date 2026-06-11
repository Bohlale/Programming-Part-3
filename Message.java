/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Bohlale Madigoe
 */
public class Message {
    private String sender;
    private String recipient;
    private String messageText;
    private String flag;          
    private String messageID;
    private String messageHash;

    // Constructor for creating a new message 
    public Message(String sender, String recipient, String messageText, String flag) {
        this.sender = sender;
        this.recipient = recipient;
        this.messageText = messageText;
        this.flag = flag;
        this.messageID = UUID.randomUUID().toString();
        this.messageHash = computeHash();
    }

    // 
    public Message(String sender, String recipient, String messageText, String flag,
                   String messageID, String messageHash) {
        this.sender = sender;
        this.recipient = recipient;
        this.messageText = messageText;
        this.flag = flag;
        this.messageID = messageID;
        this.messageHash = messageHash;
    }

    private String computeHash() {
        String data = sender + recipient + messageText + flag + System.currentTimeMillis();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // fallback
            return Integer.toHexString(data.hashCode());
        }
    }

    // Getters
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getFlag() { return flag; }
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }

    // reports
    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", text='" + messageText + '\'' +
                ", flag='" + flag + '\'' +
                ", id='" + messageID + '\'' +
                ", hash='" + messageHash + '\'' +
                '}';
    }
