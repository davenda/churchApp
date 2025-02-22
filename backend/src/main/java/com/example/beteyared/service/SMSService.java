package com.example.beteyared.service;

import com.example.beteyared.config.TwilioConfig;
import com.example.beteyared.model.User;
import com.example.beteyared.service.UserService;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SMSService {
    private final TwilioConfig twilioConfig;
    private final UserService userService;

    public SMSService(TwilioConfig twilioConfig, UserService userService) {
        this.twilioConfig = twilioConfig;
        this.userService = userService;
        try {
            Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
            log.info("Twilio initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize Twilio: {}", e.getMessage());
        }
    }

    public void sendMeetingReminder(String cohort, String meetingId, String meetingLink) {
        List<User> users = userService.getUsersByCohort(cohort);

        for (User user : users) {
            try {
                // Format phone number to E.164 format
                String formattedNumber = formatPhoneNumber(user.getPhoneNumber());

                Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(formattedNumber),    // to
                        new com.twilio.type.PhoneNumber(twilioConfig.getPhoneNumber()),  // from
                        String.format(
                                "Hello %s, \n\nYour BeteYared Bible Study meeting is scheduled for today. " +
                                        "Join using this link: %s\n\nMeeting ID: %s",
                                user.getFirstName(),
                                meetingLink,
                                meetingId
                        )
                ).create();

                log.info("Sent message to {}: {}", formattedNumber, message.getSid());
            } catch (Exception e) {
                log.error("Failed to send message to {}: {}", user.getPhoneNumber(), e.getMessage());
            }
        }
    }

    public void sendMessageToAllUsers(String messageContent) {
        List<User> users = userService.getAllActiveUsers();
        sendBulkMessages(users, messageContent);
    }

    public void sendMessageToCohort(String cohort, String messageContent) {
        List<User> users = userService.getUsersByCohort(cohort);
        sendBulkMessages(users, messageContent);
    }

    public void sendMessageToUser(Long userId, String messageContent) {
        User user = userService.getUserById(userId);
        sendSingleMessage(user, messageContent);
    }

    private void sendBulkMessages(List<User> users, String messageContent) {
        for (User user : users) {
            try {
                sendSingleMessage(user, messageContent);
            } catch (Exception e) {
                log.error("Failed to send message to user {}: {}", user.getEmail(), e.getMessage());
            }
        }
    }

    private void sendSingleMessage(User user, String messageContent) {
        try {
            // Re-initialize Twilio for each message to ensure fresh credentials
            Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());

            String formattedNumber = formatPhoneNumber(user.getPhoneNumber());
            log.debug("Attempting to send message to {} using phone number {}",
                    formattedNumber, twilioConfig.getPhoneNumber());

            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(formattedNumber),
                    new com.twilio.type.PhoneNumber(twilioConfig.getPhoneNumber()),
                    messageContent
            ).create();

            log.info("Message sent successfully. SID: {}", message.getSid());
        } catch (ApiException e) {
            log.error("Twilio API Error - Status: {}, Message: {}, Code: {}",
                    e.getStatusCode(), e.getMessage(), e.getCode());
            throw e;
        } catch (Exception e) {
            log.error("Failed to send message: {}", e.getMessage());
            throw new RuntimeException("Failed to send SMS: " + e.getMessage());
        }
    }

    private String formatPhoneNumber(String phoneNumber) {
        // Remove any non-digit characters
        String digitsOnly = phoneNumber.replaceAll("\\D+", "");

        // Ensure number starts with +1 for US numbers
        if (!digitsOnly.startsWith("1")) {
            digitsOnly = "1" + digitsOnly;
        }

        return "+" + digitsOnly;
    }
}