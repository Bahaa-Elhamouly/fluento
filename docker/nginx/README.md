# Nginx

Nginx or gateway configuration should terminate public traffic and route:

- `/` to the Fluento web app
- `/api` to the Spring Boot API
- `/swagger-ui` only in non-production or restricted admin environments
