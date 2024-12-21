package com.example.gpstracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GPTChat extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    Button sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();
    ImageButton goOutOfChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gptchat);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        messageList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.meesage_edit_text); // Fixed typo in ID
        sendButton = findViewById(R.id.send_btn);

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener(v -> {
            String question = messageEditText.getText().toString().trim();
            if (!question.isEmpty()) { // Prevent sending empty messages
                addToChat(question, Message.SEND_BY_ME);
                messageEditText.setText("");
                callApi(question);
                welcomeTextView.setVisibility(View.GONE);
            }
        });

        goOutOfChat = findViewById(R.id.imageBut2);
        goOutOfChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GPTChat.this, Main.class));
            }
        });
    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(() -> {
            messageList.add(new Message(message, sentBy));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }

    void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SEND_BY_BOT);
    }

    void callApi(String question) {
        messageList.add(new Message("Typing...", Message.SEND_BY_BOT));
        JSONObject jsonBody = new JSONObject();
        try {
            // Create a proper message array
            JSONArray messagesArray = new JSONArray();
            for (Message msg : messageList) {
                JSONObject msgObject = new JSONObject();
                msgObject.put("role", msg.getSentBy()); // Assuming getSentBy() returns appropriate role
                msgObject.put("content", msg.getMessage());
                messagesArray.put(msgObject);
            }

            jsonBody.put("messages", messagesArray); // Add the message array
            jsonBody.put("model", "gpt-3.5-turbo");
            // Removed prompt as it's not used in this context for chat-based model
            jsonBody.put("max_tokens", 2000);
            jsonBody.put("temperature", 0.3);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.proxyapi.ru/openai/v1/chat/completions")
                .header("Authorization", "Bearer sk-ciLNtVlL4lPMof3odbGRbeV7Mj0oLGeS")
                .addHeader("x-folder-id", "sk-ciLNtVlL4lPMof3odbGRbeV7Mj0oLGeS")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String str = response.body().string();
                Log.v("OkHttp", str);

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        JSONArray choicesArray = jsonObject.getJSONArray("choices");
                        JSONObject choiceObject = choicesArray.getJSONObject(0);
                        JSONObject messageObject = choiceObject.getJSONObject("message");

                        // Extract the assistant's content
                        String result = messageObject.getString("content");
                        addResponse(result.trim());

                        // Optionally log other relevant details
                        String usageInfo = jsonObject.getJSONObject("usage").toString();
                        Log.d("Usage Info", usageInfo);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        addResponse("Error parsing response: " + e.getMessage());
                    }
                } else {
                    addResponse("Failed to load response due to " + response.body().string());
                }
            }
        });
    }
}