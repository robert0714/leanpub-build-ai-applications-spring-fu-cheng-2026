package dev.danvega.hub;

import java.io.IOException;
import java.util.List;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
@RestController
public class ImageAnalysisController {
    /**
     * https://docs.spring.io/spring-ai/reference/api/chat/openai-chat.html<p>
     * https://github.com/spring-projects/spring-ai/blob/c9a3e66f90187ce7eae7eb78c462ec622685de6c/models/spring-ai-openai/src/test/java/org/springframework/ai/openai/chat/OpenAiChatModelIT.java#L293
     * **/
    private final OpenAiChatModel chatModel;

    public ImageAnalysisController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }
    /***
     * curl -X POST "http://localhost:8063/analyze" -H "Accept: text/plain" -F "file=@C:\path\to\image.png;type=image/png"
     * 
     * **/
    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String analyzeImage(@Parameter(description = "Select the file to upload") 
            @RequestPart("file") MultipartFile file) throws IOException {        
                // 1. Wrap the image data into a Media object
        Media imageData = new Media(MimeTypeUtils.IMAGE_PNG, file.getResource());

        // 2. Create the user message with text and image
        UserMessage userMessage = UserMessage.builder()
                          .text("ocr image in traditional Chinese")
                          .media(imageData).build();

        // 3. Create prompt with the message
        Prompt prompt = new Prompt(List.of(userMessage));

        // 4. Call the model
        ChatResponse response = chatModel.call(prompt);
        return response.getResult().getOutput().getText();
    }

    @PostMapping(value = "/chat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String chat(@Parameter(description = "Select the file to upload") 
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "message", required = false, defaultValue = "") String message
        
        ) throws IOException {   
            
        UserMessage.Builder builder = UserMessage.builder();

        if (message != null && !message.isBlank()) {
            builder.text(message);
        }    
        // 1. Wrap the image data into a Media object
        if (file != null && !file.isEmpty()) {
            Media imageData = new Media(MimeTypeUtils.IMAGE_PNG, file.getResource());
            builder.media(imageData);
        }

        // 2. Create the user message with text and image
        UserMessage userMessage = builder.build(); 

        // 3. Create prompt with the message
        Prompt prompt = new Prompt(List.of(userMessage));

        // 4. Call the model
        ChatResponse response = chatModel.call(prompt);
        return response.getResult().getOutput().getText();
    }
}