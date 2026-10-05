ollama pull tev1:4b
https://ollama.com/blog/ollama-now-supports-jev-style-decision-models


curl http://localhost:11434/v1/systemone -d '{
"model": "[nimble](tev1:4b)",
"state": "Design a sharded database schema for a payments ledger.",
"questions": {
"model": {
"type": "choice",
"instructions": "Which model should answer this prompt?",
"criteria": {"gemma4": "Small model", "kimi-k3": "Large model"}
}
}
}'
  

curl http://localhost:11434/v1/systemone -d '{
"model": "tev1:4b",
"state": "We would like to go to lunch",
"questions": {
"label": {
"type": "choice",
"instructions": "Where should we go for lunch. It should be tasty and maybe not to unhealthy. The doener has pizza, fallafel and kebap. The schwarma has no pizza, but schwarma, fallafel, humus, baba ganough",
"criteria": {"doener": "tasty", "schawarma": "tasty and healthy"}
}
}
}' | jq .

