# OWASP ZAP Baseline

Target:

```text
http://localhost:8080
```

Recommended baseline command:

```sh
docker run --rm -t owasp/zap2docker-stable zap-baseline.py -t http://host.docker.internal:8080 -r fluento-zap-report.html
```

Minimum checks before release:

- Authentication endpoints reject malformed requests.
- Authenticated endpoints reject missing or invalid Bearer tokens.
- Admin endpoints reject non-admin users.
- API responses do not expose stack traces.
- Security headers are reviewed for the deployed frontend and backend gateway.
