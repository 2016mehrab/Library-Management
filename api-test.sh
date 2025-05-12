for i in {1..30}; do
  curl -s -o /dev/null -w "%{http_code}\n" \
    -X POST http://localhost:8080/auth/authenticate \
    -H "Content-Type: application/json" \
    -d '{"username":"admin", "password":"admin"}'
done