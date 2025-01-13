### instruction how to run

Firstly, deploy ollama gpt. To do it run:
```
docker compose -f docker-compose.ollama.yaml up -d --build
```
Then you have to download gpt model:
```
docker exec -it ollama ollama pull llama3.2
```

To run all project services, execute:
```
docker compose up -d --build
```
