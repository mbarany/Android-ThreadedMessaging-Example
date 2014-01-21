package com.michaelbarany.examples.messaging.api;

public class Message {
    public static class Content {
        public String body;
    }
    public int id;
    public int threadId;
    public User sender;
    public User receiver;
    public Content content;

    @Override
    public String toString() {
        return content.body;
    }
}
