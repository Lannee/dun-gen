package backend.services;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class OllamaService {

    // private static final Logger LOGGER = LoggerFactory.getLogger(MinioService.class);
    private String genUrl;


    public void sendRequestToModel(String model) {
        WebClient.create();
    }

}