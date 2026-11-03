package com.apexbank.controller;

import com.apexbank.model.Account;
import com.apexbank.model.ATMCard;
import com.apexbank.model.Customer;
import com.apexbank.service.ChatbotService;
import com.apexbank.service.ServiceFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "ChatbotServlet", urlPatterns = {"/api/chatbot", "/atm/chat", "/chat"})
public class ChatbotServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final ChatbotService chatbotService = ServiceFactory.getChatbotService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/api/chatbot".equals(path)) {
            // Return initial welcome greeting JSON
            Customer customer = getSessionCustomer(req);
            Account account = getSessionAccount(req);
            ATMCard card = getSessionCard(req);
            ChatbotService.ChatResponse reply = chatbotService.processMessage("hi", customer, account, card);
            sendJson(resp, reply);
            return;
        }

        // Full screen chat page
        forward(req, resp, "atm/chat");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userInput = req.getParameter("message");

        if (userInput == null || userInput.trim().isEmpty()) {
            try {
                StringBuilder sb = new StringBuilder();
                String line;
                try (BufferedReader reader = req.getReader()) {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                }
                String body = sb.toString().trim();
                if (!body.isEmpty()) {
                    if (body.startsWith("{")) {
                        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
                        if (json.has("message")) {
                            userInput = json.get("message").getAsString();
                        }
                    } else {
                        userInput = body;
                    }
                }
            } catch (Exception e) {
                System.err.println("Chatbot body parse note: " + e.getMessage());
            }
        }

        Customer customer = getSessionCustomer(req);
        Account account = getSessionAccount(req);
        ATMCard card = getSessionCard(req);

        ChatbotService.ChatResponse response = chatbotService.processMessage(userInput, customer, account, card);
        sendJson(resp, response);
    }
}
