package br.com.wtd.analisedelive.service;

import br.com.wtd.analisedelive.model.CommentsInfo;
import br.com.wtd.analisedelive.model.Interaction;
import br.com.wtd.analisedelive.model.Sentiment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class OpenAIAnalysis {

    private final Dotenv dotenv = Dotenv.load();
    private final String OPENAI_API_KEY = dotenv.get("OPENAI_API_KEY");

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String SYSTEM_MESSAGE = """
    Você é um analista de comentários de lives. Sua tarefa é classificar cada comentário com dois números:

      - O primeiro número representa o sentimento:
        0 = Negativo
        1 = Neutro
        2 = Positivo

      - O segundo número representa o tipo de interação:
        3 = Pergunta
        4 = Elogio
        5 = Crítica
        6 = Sugestão
        7 = Meme / Piada
        8 = Reclamação
        9 = Reação emocional

      Cada comentário deve ser seguido por sua classificação no formato,
      Siga estritamente o padrão abaixo, não adicione mais nenhuma informação :
      "<comentário>" → <sentimento> <tipo>
      Caso não se encaixe exatamente em nenhuma categoria classifique com a categoria mais próxima e sempre siga o padrão fornecido.
    """;

    public void analyzeCommentsBatch(List<CommentsInfo> comments) {
        List<List<CommentsInfo>> batches = partition(comments, 30);

        for (List<CommentsInfo> batch : batches) {
            try {
                String prompt = buildPrompt(batch);
                String responseJson = callOpenAI(prompt);
                parseAndApplyResponse(responseJson, batch);
            } catch (Exception e) {
                System.err.println("Erro ao analisar lote de comentários: " + e.getMessage());
            }
        }
    }

    private String buildPrompt(List<CommentsInfo> comments) {
        return comments.stream()
                .map(c -> "\"" + c.getCommentsDetailsData() + "\"")
                .collect(Collectors.joining("\n"));
    }


    private String callOpenAI(String prompt) throws IOException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String body = mapper.writeValueAsString(Map.of(
                "model", "gpt-4",
                "messages", List.of(
                        Map.of("role", "system", "content", SYSTEM_MESSAGE),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        ));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_URL))
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    private void parseAndApplyResponse(String json, List<CommentsInfo> batch) throws JsonProcessingException {
        JsonNode root = mapper.readTree(json);
        String content = root.path("choices").get(0).path("message").path("content").asText();

        Pattern pattern = Pattern.compile("^\"?(.*?)\"?\\s*→\\s*(\\d)\\s+(\\d)$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String commentText = matcher.group(1).trim();
            int sentimentCode = Integer.parseInt(matcher.group(2));
            int interactionCode = Integer.parseInt(matcher.group(3));

            CommentsInfo match = batch.stream()
                    .filter(c -> {
                        String cText = c.getCommentsDetailsData().commentContent();
                        return normalize(cText).contains(normalize(commentText)) ||
                                normalize(commentText).contains(normalize(cText));
                    })
                    .findFirst()
                    .orElse(null);


            if (match != null) {
                match.setSentiment(Sentiment.fromCode(sentimentCode));
                match.setInteraction(Interaction.fromCode(interactionCode));
            }
        }
    }

    private <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }

    private String normalize(String text) {
        return text.toLowerCase().replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}\\s]", "").trim();
    }
}
