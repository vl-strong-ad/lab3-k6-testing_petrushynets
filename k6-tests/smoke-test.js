import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 1,
  duration: '30s',
  thresholds: {
    http_req_duration: ['p(95)<1000'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

function login(username, password) {
  const jar = http.cookieJar();

  const loginPage = http.get(`${BASE_URL}/login`, { jar });

  const csrfMatch = loginPage.body.match(/name="_csrf"\s+value="([^"]+)"/);
  const csrf = csrfMatch ? csrfMatch[1] : '';

  http.post(
    `${BASE_URL}/login`,
    { username, password, _csrf: csrf },
    { jar, redirects: 5 }
  );

  return jar;
}

export default function () {
  const loginPage = http.get(`${BASE_URL}/login`);

  check(loginPage, {
    'login page is 200': (r) => r.status === 200,
  });

  const jar = login('user', 'pass');

  const shopsRes = http.get(`${BASE_URL}/shops`, { jar });

  check(shopsRes, {
    'shops page is 200': (r) => r.status === 200,
  });

  sleep(1);
}