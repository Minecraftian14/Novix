package io.anuke.ucore.bots;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/* renamed from: io.anuke.ucore.bots.MessageHandler */
public interface MessageHandler {

    /* renamed from: io.anuke.ucore.bots.MessageHandler$MessageListener */
    public interface MessageListener {
        void onFileRecieved(String str, String str2, String str3);

        void onMessageRecieved(String str, String str2, String str3, String str4, String str5);
    }

    void edit(String str, String str2, String str3);

    String getUserName(String str);

    void send(String str, String str2);

    void sendFile(File file, String str);

    void setMessageListener(MessageListener messageListener);

    /* renamed from: io.anuke.ucore.bots.MessageHandler$TimedMessageHandler */
    public static abstract class TimedMessageHandler implements MessageHandler {
        String lastmessage;
        ConcurrentHashMap<String, String> messages = new ConcurrentHashMap<>();

        public abstract String sendRaw(String str, String str2);

        public void send(String message, String id) {
            if (this.messages.containsKey(id)) {
                this.messages.put(id, String.valueOf(this.messages.get(id)) + "\n" + message);
                return;
            }
            this.messages.put(id, message);
            new Timer().schedule(new TimerTask() {
                public void run() {
                    TimedMessageHandler.this.sendRaw(TimedMessageHandler.this.messages.get(id), id);
                    TimedMessageHandler.this.messages.remove(id);
                }
            }, 50);
        }
    }
}
