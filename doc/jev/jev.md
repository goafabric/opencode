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
