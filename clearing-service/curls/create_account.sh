curl -X POST http://localhost:8082/api/accounts \
     -H "Content-Type: application/json" \
     -d '{
       "playerName": "John Doe",
       "initialBalance": 1000.00
     }'
