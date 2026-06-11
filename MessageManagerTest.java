/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Bohlale Madigoe
 */
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageManagerTest {
    private MessageManager manager;

    @Before
    public void setUp() {
        manager = new MessageManager();
        manager.loadTestData();   
    }

    // Test 1: Sent Messages array correctly populated
    @Test
    public void testSentMessagesArray() {
        // Expected texts from test data: message 1 and message 4
        assertEquals(2, manager.getSentMessages().size());
        String text1 = manager.getSentMessages().get(0).getMessageText();
        String text2 = manager.getSentMessages().get(1).getMessageText();
        assertTrue(text1.equals("Did you get the cake?") || text1.equals("It is dinner time!"));
        assertTrue(text2.equals("Did you get the cake?") || text2.equals("It is dinner time!"));
        assertNotEquals(text1, text2);
    }

    // Test 2: Display the longest message (from all stored messages)
    @Test
    public void testLongestStoredMessage() {
        String longest = manager.getLongestStoredMessage();
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest);
    }

    // Test 3: Search for messageID of message 4 (the developer's sent message)
    @Test
    public void testSearchByMessageID() {
        // Find the message that has recipient "0838884567" and text "It is dinner time!"
        String targetID = null;
        for (Message m : manager.getSentMessages()) {
            if (m.getRecipient().equals("0838884567") && m.getMessageText().equals("It is dinner time!")) {
                targetID = m.getMessageID();
                break;
            }
        }
        assertNotNull("Message 4 was not found", targetID);
        String result = manager.searchByMessageID(targetID);
        assertTrue(result.contains("Recipient: 0838884567"));
        assertTrue(result.contains("It is dinner time!"));
    }

    // Test 4: Search all stored messages for recipient +27838884567
    @Test
    public void testSearchByRecipient() {
        // Should return two messages: message 2 and message 5
        java.util.List<String> found = manager.searchStoredMessagesByRecipient("+27838884567");
        assertEquals(2, found.size());
        assertTrue(found.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(found.contains("Ok, I am leaving without you."));
    }

    // Test 5: Delete a message using its hash (Test Message 2)
    @Test
    public void testDeleteByHash() {
        // Obtain hash of the stored message "Where are you?..."
        String hashToDelete = null;
        for (Message m : manager.getAllStoredMessages()) {
            if (m.getMessageText().equals("Where are you? You are late! I have asked you to be on time.")) {
                hashToDelete = m.getMessageHash();
                break;
            }
        }
        assertNotNull("Test message 2 not found", hashToDelete);
        boolean deleted = manager.deleteStoredMessageByHash(hashToDelete);
        assertTrue(deleted);
        // Verify it's gone
        boolean stillPresent = manager.getAllStoredMessages().stream()
                .anyMatch(m -> m.getMessageHash().equals(hashToDelete));
        assertFalse(stillPresent);
        // Also verify the hash list and ID list are updated
        assertFalse(manager.getMessageHashes().contains(hashToDelete));
    }

    // Test 6: Display report 
    @Test
    public void testDisplaySentMessagesReport() {
        String report = manager.getSentMessagesReport();
        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient:"));
        assertTrue(report.contains("Message:"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time!"));
    }
}
    