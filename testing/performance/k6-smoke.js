import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  thresholds: {
    http_req_failed: ["rate<0.01"],
    http_req_duration: ["p(95)<750"],
  },
  vus: 5,
  duration: "30s",
};

const baseUrl = __ENV.BASE_URL || "http://localhost:8080";

export default function fluentoSmoke() {
  const status = http.get(`${baseUrl}/api/v1/public/status`);
  check(status, {
    "status is 200": (response) => response.status === 200,
  });

  const courses = http.get(`${baseUrl}/api/v1/catalog/courses`);
  check(courses, {
    "courses available": (response) => response.status === 200,
  });

  sleep(1);
}
