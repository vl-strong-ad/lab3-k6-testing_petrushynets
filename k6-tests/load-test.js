import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '10s', target: 5 },
    { duration: '10s', target: 10 },
    { duration: '30s', target: 10 },
    { duration: '10s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<2000'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

function login(username, password) {
  const jar = http.cookieJar();
  const loginPage = http.get(`${BASE_URL}/login`, { jar });

  const csrfMatch = loginPage.body.match(/name="_csrf"\s+value="([^"]+)"/);
  const csrf = csrfMatch ? csrfMatch[1] : '';

  http.post(`${BASE_URL}/login`, { username, password, _csrf: csrf }, { jar });

  return jar;
}

export default function () {
  const jar = login('user', 'pass');

  const res = http.get(`${BASE_URL}/shops`, { jar });

  check(res, {
    'status 200': (r) => r.status === 200,
  });

  sleep(1);
}