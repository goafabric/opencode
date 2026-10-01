ollama pull tev1:4b
https://ollama.com/blog/ollama-now-supports-jev-style-decision-models


curl http://localhost:11434/v1/systemone -d '{
"model": "tev1",
"state": "Our checkout has returned 500 errors since 9am.",
"questions": {
"label": {
"type": "choice",
"instructions": "Which label fits this ticket?",
"criteria": {"billing": null, "bug": null, "account": null}
}
}
}'


curl http://localhost:11434/v1/systemone -d '{
"model": "tev1",
"state": "We would like to go to lunch",
"questions": {
"label": {
"type": "choice",
"instructions": "Where should we go for lunch. It should be tasty and maybe not to unhealthy. The doener has pizza, fallafel and kebap. The schwarma has no pizza, but schwarma, fallafel, humus, baba ganough",
"criteria": {"doener": "tasty", "schawarma": "tasty and healthy"}
}
}
}'

